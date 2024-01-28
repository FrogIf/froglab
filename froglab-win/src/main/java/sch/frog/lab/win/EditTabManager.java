package sch.frog.lab.win;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import sch.frog.lab.lang.LangRunner;
import sch.frog.lab.win.editor.ConsoleWorkspace;
import sch.frog.lab.win.editor.IWorkspace;
import sch.frog.lab.win.editor.ScriptWorkspace;

import java.util.ArrayList;
import java.util.List;

class EditTabManager {

    private static final List<TabElement> tabList = new ArrayList<>();

    private static  int tabIndex = 0;

    private static String generateTitle(String type){
        return type + " " + (tabIndex++);
    }

    public static void addTab(TabPane tabPane, String type, LangRunner langRunner){
        addTab(tabPane, null, type, langRunner);
    }

    public static TabElement addOrSelectForScript(TabPane tabPane, String title, LangRunner langRunner, String filePath){
        TabElement tabElement = null;
        for (TabElement te : tabList) {
            if(filePath.equals(te.getWorkspace().getPath())){
                tabElement = te;
                break;
            }
        }
        if(tabElement != null){
            tabPane.getSelectionModel().select(tabElement.tab);
        }else{
            tabElement = addTab(tabPane, title, Constants.EDITOR_TYPE_SCRIPT, langRunner);
            tabElement.workspace.setPath(filePath);
        }
        return tabElement;
    }

    public static TabElement addTab(TabPane tabPane, String title, String type, LangRunner langRunner){
        if(tabPane.getTabs().isEmpty()){
            tabIndex = 0;
        }
        if(title == null || title.length() == 0){
            title = EditTabManager.generateTitle(type);
        }
        Tab newTab = new Tab(title);
        IWorkspace pane;
        if(Constants.EDITOR_TYPE_CONSOLE.equals(type)){
            pane = new ConsoleWorkspace(langRunner);
        }else{
            pane = new ScriptWorkspace(langRunner, newTab);
        }
        newTab.setContent((Node) pane);
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
        TabElement tabElement = new TabElement(newTab, pane);
        tabList.add(tabElement);
        newTab.setOnCloseRequest(event -> {
            tabList.remove(tabElement);
        });
        return tabElement;
    }

    public static class TabElement{
        private final Tab tab;
        private final IWorkspace workspace;

        public TabElement(Tab tab, IWorkspace workspace) {
            this.tab = tab;
            this.workspace = workspace;
        }

        public Tab getTab() {
            return tab;
        }

        public IWorkspace getWorkspace() {
            return workspace;
        }
    }

}
