package sch.frog.kit.win.editor;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
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
import sch.frog.kit.core.FrogLangApp;
import sch.frog.kit.core.exception.ExecuteException;
import sch.frog.kit.core.exception.GrammarException;
import sch.frog.kit.core.exception.IncorrectExpressionException;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.core.parse.lexical.Token;
import sch.frog.kit.core.util.ExpressionFormatUtil;
import sch.frog.kit.core.value.Value;
import sch.frog.kit.win.ClipboardUtil;
import sch.frog.kit.win.MessageUtil;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptWorkspace extends BorderPane implements IWorkspace{

    private final SplitPane mainContainer = new SplitPane();

    private final CustomCodeArea codeArea = new CustomCodeArea();

    private SearchBox textSearchBox;

    private final FrogLangApp frogLangApp;

    private final TextArea logTextArea = new TextArea();
    private final LogWriter writer;

    public ScriptWorkspace(FrogLangApp frogLangApp) {
        this.frogLangApp = frogLangApp;
        writer = new LogWriter(logTextArea);
        initView();
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
        CodeAreaAssist highlight = CodeAreaAssist.getInstance();
        highlight.enableAssist(codeArea);
    }

    private void executeCode(){
        String text = codeArea.getText();
        if(text.isBlank()){
            MessageUtil.info("no script to execute");
            return;
        }
        ISession session = frogLangApp.generateSession();
        session.setOutput(writer::write);
        try{
            Value result = frogLangApp.execute(text, session);
            writer.write("▶ script execute result : " + result + "\n");
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

    public void prettyCode(){
//        String text = codeArea.getText();
//        if(!text.isBlank()){
//            try {
//                List<Token> tokens = frogLangApp.tokens(text);
//                StringBuilder sb = new StringBuilder();
//                String blank = "";
//                for (Token token : tokens) {
//                    String t = token.literal();
//                    if("(".equals(t) || "{".equals(t)){
//                        sb.append(t).append("\n");
//                        blank = "    " + blank;
//                        sb.append(blank);
//                    }else if(",".equals(t)){
//                        sb.append(t).append(' ');
//                    }else if("".equals(t)){
//
//                    }
//                    sb.append(token.literal());
//                }
//                codeArea.clear();
//                codeArea.appendText(sb.toString());
//            } catch (IncorrectExpressionException e) {
//                // TODO 标记
//                MessageUtil.error(e.getMessage());
//            }
//        }
    }

    public void compressCode(){
        String text = codeArea.getText();
        if(!text.isBlank()){
            try {
                List<Token> tokens = frogLangApp.tokens(text);
                text = ExpressionFormatUtil.compress(tokens);
                codeArea.clear();
                codeArea.appendText(text);
            } catch (IncorrectExpressionException e) {
                // TODO 标记
                MessageUtil.error(e.getMessage());
            }
        }
    }
}
