package sch.frog.kit.win;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import sch.frog.kit.win.editor.ConsoleWorkspace;

class EditTabManager {

    private static  int tabIndex = 0;

    private static String generateTitle(){
        return "Tab " + (tabIndex++);
    }

    public static void addTab(TabPane tabPane, MessageEmitter emitter, String type){
        addTab(tabPane, null, emitter, type);
    }

    public static TabElement addTab(TabPane tabPane, String title, MessageEmitter emitter, String type){
        if(tabPane.getTabs().isEmpty()){
            tabIndex = 0;
        }
        if(title == null || title.length() == 0){
            title = EditTabManager.generateTitle();
        }
        Tab newTab = new Tab(title);
        Pane pane;
        if(Constants.EDITOR_TYPE_CONSOLE.equals(type)){
            pane = new ConsoleWorkspace(emitter);
        }else{
            // TODO
            pane = new ConsoleWorkspace(emitter);
        }
        newTab.setContent(pane);
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
        return new TabElement(newTab, pane);
    }

    public static class TabElement{
        private final Tab tab;
        private final Pane editor;

        public TabElement(Tab tab, Pane editor) {
            this.tab = tab;
            this.editor = editor;
        }

        public Tab getTab() {
            return tab;
        }

        public Pane getEditor() {
            return editor;
        }
    }

}
