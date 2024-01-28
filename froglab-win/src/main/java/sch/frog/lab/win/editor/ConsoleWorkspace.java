package sch.frog.lab.win.editor;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import sch.frog.lab.lang.LangRunner;
import sch.frog.lab.lang.constant.VariableConstant;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.handle.SystemOutputHandle;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.win.ClipboardUtil;
import sch.frog.lab.win.io.ConsolePrintStream;

public class ConsoleWorkspace extends BorderPane implements IWorkspace{

    private final ConsoleCodeArea codeArea;

    private final SearchBox textSearchBox;

    private final IExecuteContext context;

    public ConsoleWorkspace(LangRunner langRunner) {
        this.context = langRunner.newExecuteContext();
        codeArea = new ConsoleCodeArea(">>>", line -> {
            try {
                return langRunner.run(line, context).value().toString();
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }, 5);
        try {
            context.setVariable(VariableConstant.OUTPUT_STREAM_VAR_NAME, Value.of(new SystemOutputHandle(new ConsolePrintStream(codeArea)))); // 控制台输出重定向
        } catch (ExecuteException e) {
            throw new RuntimeException(e);
        }
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
//        CodeAreaAssist highlight = CodeAreaAssist.getInstance();
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
