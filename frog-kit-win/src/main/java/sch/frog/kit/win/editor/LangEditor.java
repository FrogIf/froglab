package sch.frog.kit.win.editor;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import sch.frog.kit.win.ClipboardUtil;
import sch.frog.kit.win.MessageEmitter;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LangEditor extends BorderPane {

    private final CustomCodeArea codeArea = new CustomCodeArea();

    private final MessageEmitter messageEmitter;

    private final SearchBox textSearchBox;

    public LangEditor(MessageEmitter messageEmitter) {
        initCodeArea();
        this.messageEmitter = messageEmitter;
        VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
        super.setCenter(scrollPane);
        textSearchBox = CodeAreaSearchBoxFactory.createSearchBox(this, codeArea, messageEmitter);
    }

    private void initCodeArea(){
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.prefHeightProperty().bind(this.heightProperty());
        codeArea.prefWidthProperty().bind(this.widthProperty());
        codeArea.setContextMenu(initContextMenu());
        // 换行后自动缩进
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, e ->
        {
            if(e.getCode() == KeyCode.ENTER){
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraphIndex = codeArea.getCurrentParagraph();
                Paragraph<Collection<String>, String, Collection<String>> preParagraph = codeArea.getParagraph(currentParagraphIndex - 1);
                String tail = preParagraph.getText().trim();
                boolean preLineEndWithBracket = tail.endsWith("{") || tail.endsWith("[");

                Paragraph<Collection<String>, String, Collection<String>> currentParagraph = codeArea.getParagraph(currentParagraphIndex);
                String postText = currentParagraph.getText();
                boolean postLineStartWithBracket = postText.startsWith("}") || postText.startsWith("]");

                Matcher m0 = whiteSpace.matcher(preParagraph.getSegments().get(0));
                Platform.runLater(() -> {
                    codeArea.insertText(caretPosition, (m0.find() ? m0.group() : "") + (preLineEndWithBracket && !postLineStartWithBracket ? "    " : ""));
                });
            }
        });
        EditorAssist highlight = EditorAssist.getInstance();
        highlight.enableAssist(codeArea);
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
        MenuItem undo = new MenuItem("Undo");
        undo.setOnAction(event -> {
            codeArea.undo();
        });
        MenuItem redo = new MenuItem("Redo");
        redo.setOnAction(event -> {
            codeArea.redo();
        });
        ObservableList<MenuItem> items = contextMenu.getItems();
        items.add(copy);
        items.add(find);
        items.add(undo);
        items.add(redo);
        return contextMenu;
    }

    private void searchBegin(){
        this.setTop(textSearchBox);
        textSearchBox.focusSearch(codeArea.getSelectedText());
    }

    public void enableTerminalMode(){
        codeArea.setOnKeyTyped(keyEvent -> {

        });
//        codeArea.setOnKeyPressed(keyEvent -> {
//            if(keyEvent.getCode().equals(KeyCode.BACK_SPACE)){
//                keyEvent.getTarget()
//            }
//        });
    }
}
