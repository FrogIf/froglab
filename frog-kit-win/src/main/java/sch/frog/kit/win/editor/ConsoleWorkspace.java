package sch.frog.kit.win.editor;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import sch.frog.kit.core.FrogLangApp;
import sch.frog.kit.core.execute.IOutput;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.win.ClipboardUtil;
import sch.frog.kit.win.MessageEmitter;

public class ConsoleWorkspace extends BorderPane {

    private final ConsoleCodeArea codeArea;

    private final MessageEmitter messageEmitter;

    private final SearchBox textSearchBox;

    private final FrogLangApp frogLangApp = FrogLangApp.getInstance();

    private final ISession session;

    public ConsoleWorkspace(MessageEmitter messageEmitter) {
        this.messageEmitter = messageEmitter;
        session = frogLangApp.generateSession();
        codeArea = new ConsoleCodeArea(">>>", line -> {
            try {
                return frogLangApp.execute(line, session).toString();
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }, 5);
        session.setOutput(codeArea::output);
        initCodeArea();
        VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
        super.setCenter(scrollPane);
        textSearchBox = CodeAreaSearchBoxFactory.createSearchBox(this, codeArea, messageEmitter);
    }

    private void initCodeArea(){
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.prefHeightProperty().bind(this.heightProperty());
        codeArea.prefWidthProperty().bind(this.widthProperty());
        codeArea.setContextMenu(initContextMenu());
        CodeAreaAssist highlight = CodeAreaAssist.getInstance();
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
        MenuItem test = new MenuItem("Test");
        test.setOnAction(event -> {
            codeArea.appendText("testsetset\n");
        });
        ObservableList<MenuItem> items = contextMenu.getItems();
        items.add(copy);
        items.add(find);
        items.add(test);
        return contextMenu;
    }

    private void searchBegin(){
        this.setTop(textSearchBox);
        textSearchBox.focusSearch(codeArea.getSelectedText());
    }
}
