package sch.frog.kit.win.editor;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import sch.frog.kit.core.FrogLangApp;
import sch.frog.kit.core.execute.ISession;
import sch.frog.kit.win.ClipboardUtil;

public class ConsoleWorkspace extends BorderPane implements IWorkspace{

    private final ConsoleCodeArea codeArea;

    private final SearchBox textSearchBox;

    private final ISession session;

    public ConsoleWorkspace(FrogLangApp frogLangApp) {
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
        textSearchBox = CodeAreaSearchBoxFactory.createSearchBox(this, codeArea);
    }

    private void initCodeArea(){
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.prefHeightProperty().bind(this.heightProperty());
        codeArea.prefWidthProperty().bind(this.widthProperty());
        codeArea.setContextMenu(initContextMenu());
        CodeAreaAssist highlight = CodeAreaAssist.getInstance();
//        highlight.enableAssist(codeArea);
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
        throw new UnsupportedOperationException("console workspace can't set content");
    }

    @Override
    public void setPath(String path) {
        throw new UnsupportedOperationException("console workspace can't set path");
    }

    @Override
    public String getPath() {
        return null;
    }
}
