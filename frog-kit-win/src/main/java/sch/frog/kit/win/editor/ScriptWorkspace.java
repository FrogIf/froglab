package sch.frog.kit.win.editor;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.reactfx.collection.LiveList;
import sch.frog.kit.lang.LangRunner;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.exception.GrammarException;
import sch.frog.kit.lang.io.StringScriptStream;
import sch.frog.kit.lang.lexical.GeneralTokenStream;
import sch.frog.kit.lang.lexical.LexicalAnalyzer;
import sch.frog.kit.lang.lexical.Token;
import sch.frog.kit.lang.semantic.IExecuteContext;
import sch.frog.kit.lang.semantic.Result;
import sch.frog.kit.lang.util.ExpressionFormatUtil;
import sch.frog.kit.win.ClipboardUtil;
import sch.frog.kit.win.MessageUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptWorkspace extends BorderPane implements IWorkspace{

    private final SplitPane mainContainer = new SplitPane();

    private final CustomCodeArea codeArea = new CustomCodeArea();

    private SearchBox textSearchBox;

    private final LangRunner langRunner;

    private final TextArea logTextArea = new TextArea();
    private final LogWriter writer;

    private final Tab selfTab;

    public ScriptWorkspace(LangRunner langRunner, Tab selfTab) {
        this.langRunner = langRunner;
        writer = new LogWriter(logTextArea);
        initView();
        this.selfTab = selfTab;
    }

    private void initView(){
        // 脚本编辑
        initCodeArea();
        VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);

        HBox editorCon = new HBox();
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        editorCon.getChildren().add(scrollPane);
        textSearchBox = CodeAreaSearchBoxFactory.createSearchBox(this, codeArea);

        VBox controlCon = new VBox();
        editorCon.getChildren().add(controlCon);

        Button runBtn = new Button("▶");
        buttonTooltip(runBtn, "F5");
        buttonStyle(runBtn);
        runBtn.setOnMouseClicked(mouseEvent -> executeCode());
        controlCon.getChildren().add(runBtn);

        Button prettyBtn = new Button("♣");
        buttonStyle(prettyBtn);
        buttonTooltip(prettyBtn, "pretty");
        prettyBtn.setOnMouseClicked(e -> prettyCode());
        controlCon.getChildren().add(prettyBtn);

        Button compressBtn = new Button("♒");
        buttonStyle(compressBtn);
        buttonTooltip(compressBtn, "compress");
        compressBtn.setOnMouseClicked(e -> compressCode());
        controlCon.getChildren().add(compressBtn);

        Button saveBtn = new Button("✍");
        buttonTooltip(saveBtn, "Ctrl+S");
        buttonStyle(saveBtn);
        saveBtn.setOnMouseClicked(e -> saveScript());
        controlCon.getChildren().add(saveBtn);

        // 日志输出
        HBox logBox = new HBox();
        logTextArea.setEditable(false);
        logTextArea.setWrapText(true);
        logBox.getChildren().add(logTextArea);
        HBox.setHgrow(logTextArea, Priority.ALWAYS);

        VBox logControlBox = new VBox();
        HBox.setHgrow(logControlBox, Priority.NEVER);
        logBox.getChildren().add(logControlBox);

        Button clearLog = new Button("C");
        clearLog.setOnMouseClicked(e -> logTextArea.clear());
        buttonStyle(clearLog);
        logControlBox.getChildren().add(clearLog);

        Button wrapBtn = new Button("↙");
        wrapBtn.setOnMouseClicked(e -> logTextArea.setWrapText(!logTextArea.isWrapText()));
        buttonStyle(wrapBtn);
        logControlBox.getChildren().add(wrapBtn);

        // 总体页面
        mainContainer.setOrientation(Orientation.VERTICAL);
        mainContainer.getItems().add(editorCon);
        mainContainer.getItems().add(logBox);
        mainContainer.setDividerPosition(0, 0.9);
        this.setCenter(mainContainer);
    }

    public void buttonStyle(Button btn){
        btn.setPadding(new Insets(2, 5, 2, 5));
        btn.setStyle("-fx-font-family: Consolas; -fx-font-size:12px; -fx-pref-width: 25px; -fx-pref-height: 10px; -fx-cursor: hand;-fx-background-radius:10;-fx-border-radius:10;-fx-background-color: #9b9b9b;");
        VBox.setMargin(btn, new Insets(4, 0, 0, 0));
    }

    public void buttonTooltip(Button btn, String tip){
        Tooltip tooltip = new Tooltip(tip);
        tooltip.setShowDelay(Duration.seconds(1));
        tooltip.setShowDuration(Duration.seconds(2));
        Tooltip.install(btn, tooltip);
    }

    private static final String TAB_SPACE = "    ";

    private void initCodeArea(){
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.prefHeightProperty().bind(this.heightProperty());
        codeArea.prefWidthProperty().bind(this.widthProperty());
        codeArea.setContextMenu(initContextMenu());

        // 换行后自动缩进
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ENTER){
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraphIndex = codeArea.getCurrentParagraph();
                Paragraph<Collection<String>, String, Collection<String>> preParagraph = codeArea.getParagraph(currentParagraphIndex - 1);
                String tail = preParagraph.getText().trim();
                boolean preLineEndWithBracket = tail.endsWith("{") || tail.endsWith("(");

                Paragraph<Collection<String>, String, Collection<String>> currentParagraph = codeArea.getParagraph(currentParagraphIndex);
                String postText = currentParagraph.getText();
                boolean postLineStartWithBracket = postText.startsWith("}") || postText.startsWith(")");

                Matcher m0 = whiteSpace.matcher(preParagraph.getSegments().get(0));
                Platform.runLater(() -> {
                    codeArea.insertText(caretPosition, (m0.find() ? m0.group() : "") + (preLineEndWithBracket && !postLineStartWithBracket ? "    " : ""));
                });
            }else if(e.getCode() == KeyCode.F5){
                executeCode();
            }else if(e.isControlDown() && e.getCode() == KeyCode.S){
                saveScript();
            }
        });
        codeArea.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.TAB){
                IndexRange range = codeArea.getSelection();
                if(range.getLength() > 0){
                    int start = range.getStart();
                    int end = range.getEnd();
                    LiveList<Paragraph<Collection<String>, String, Collection<String>>> paragraphs = codeArea.getParagraphs();
                    if(e.isShiftDown()){
                        int lineStart = 0;
                        int newStart = start;
                        int newEnd = end;
                        boolean edit = false;
                        for (Paragraph<Collection<String>, String, Collection<String>> paragraph : paragraphs) {
                            int len = paragraph.length();
                            int lineEnd = lineStart + len;
                            if((lineStart >= start && lineStart < end) || (start >= lineStart && start < lineEnd) || (end >= lineStart && end < lineEnd)){
                                String text = paragraph.getText();
                                int l = 0;
                                while(l < len && l < 4){
                                    char ch = text.charAt(l);
                                    if(ch == ' '){
                                        l++;
                                    }else{
                                        if(ch == '\t'){ l++; }
                                        break;
                                    }
                                }
                                codeArea.deleteText(lineStart, lineStart + l);
                                if(!edit){  // 第一次移动时执行
                                    newStart -= l;
                                    if(newStart < lineStart){
                                        newStart = lineStart;
                                    }
                                }
                                newEnd -= l;
                                lineEnd = lineEnd - l;
                                edit = true;
                            }else if(edit){
                                break;
                            }
                            lineStart = lineEnd + 1;
                        }
                        codeArea.selectRange(newStart, newEnd);
                    }else{
                        int lineStart = 0;
                        int offset = 0;
                        boolean edit = false;
                        for (Paragraph<Collection<String>, String, Collection<String>> paragraph : paragraphs) {
                            int len = paragraph.length();
                            int lineEnd = lineStart + len;
                            if((lineStart >= start && lineStart < end) || (start >= lineStart && start < lineEnd) || (end >= lineStart && end < lineEnd)){
                                edit = true;
                                codeArea.insert(lineStart + offset, TAB_SPACE, "");
                                offset += 4;
                            }else if(edit){
                                break;
                            }
                            lineStart = lineEnd + 1;
                        }
                        codeArea.selectRange(start + 4, end + offset);
                    }
                }else{
                    codeArea.insert(codeArea.getCaretPosition(), TAB_SPACE, "");
                }
                e.consume();
            }
        });
        CodeAreaAssist highlight = CodeAreaAssist.getInstance();
        highlight.enableAssist(codeArea);
    }

    private void executeCode(){
        String text = codeArea.getText();
        if(text.isBlank()){
            MessageUtil.info("no script to execute");
            return;
        }
        IExecuteContext context = langRunner.newExecuteContext();
        try{
            Result result = langRunner.run(text, context);
            writer.write("▶ script execute result : " + result.value() + "\n");
        }catch (ExecuteException | GrammarException e){
            writer.write(e.getMessage() + "\n");
        }catch (Exception e){
            e.printStackTrace(new PrintWriter(writer));
        }
    }

    private ContextMenu initContextMenu(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(event -> {
            String selectedText = codeArea.getSelectedText();
            if(selectedText != null && !"".equals(selectedText)){
                ClipboardUtil.putToClipboard(selectedText);
            }
        });
        MenuItem find = new MenuItem("Find");
        find.setOnAction(event -> {
            this.searchBegin();
        });
        ObservableList<MenuItem> items = contextMenu.getItems();
        items.add(copy);
        items.add(find);
        return contextMenu;
    }

    private void searchBegin(){
        this.setTop(textSearchBox);
        textSearchBox.focusSearch(codeArea.getSelectedText());
    }

    @Override
    public void setContent(String content) {
        codeArea.clear();
        codeArea.appendText(content);
    }

    @Override
    public void setPath(String path) {
        this.filePath = path;
    }

    @Override
    public String getPath() {
        return this.filePath;
    }

    private static class LogWriter extends Writer {

        private final TextArea textArea;

        public LogWriter(TextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(String str){
            try {
                super.write(str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            String log = new String(cbuf, off, len);
            textArea.appendText(log);
        }

        @Override
        public void flush() throws IOException {
            // do nothing
        }

        @Override
        public void close() throws IOException {
            // do nothing
        }
    }

    private String filePath = null;

    private static File historyDirectory = null;

    private void saveScript(){
        if(filePath == null){
            FileChooser fileChooser = new FileChooser();
            if(historyDirectory != null){
                fileChooser.setInitialDirectory(historyDirectory);
            }
            fileChooser.setTitle("Save Script");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("frog", "*.frog"));
            File file = fileChooser.showSaveDialog(null);
            if(file == null){ return; }
            filePath = file.getAbsolutePath();
            historyDirectory = file.getParentFile();
        }
        File file = new File(filePath);
        if(file.exists()){
            if(!file.delete()){
                MessageUtil.error("script file has exist, and override failed");
                return;
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            MessageUtil.error("create file failed : " + e.getMessage());
            return;
        }
        try (
                FileWriter fileWriter = new FileWriter(file)
        ){
            fileWriter.write(codeArea.getText());
            selfTab.setText(file.getName());
            MessageUtil.info("save success");
        } catch (IOException e) {
            MessageUtil.error("save script failed : " + e.getMessage());
        }
    }

    public static File historyDirectory(){
        return historyDirectory;
    }

    public static void setHistoryDirectory(File hisDir){
        historyDirectory = hisDir;
    }


    private List<Token> tokenize(String text){
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        GeneralTokenStream tokenStream = lexicalAnalyzer.parse(new StringScriptStream(text));
        ArrayList<Token> tokens = new ArrayList<>();
        do{
            tokens.add(tokenStream.current());
            tokenStream.next();
        }while (tokenStream.current() != Token.EOF);
        return tokens;
    }

    public void prettyCode(){
        String text = codeArea.getText();
        if(!text.isBlank()){
            List<Token> tokens = tokenize(text);
            text = ExpressionFormatUtil.pretty(tokens);
            codeArea.clear();
            codeArea.appendText(text);
        }
    }

    public void compressCode(){
        String text = codeArea.getText();
        if(!text.isBlank()){
            List<Token> tokens = tokenize(text);
            text = ExpressionFormatUtil.compress(tokens);
            codeArea.clear();
            codeArea.appendText(text);
        }
    }
}
