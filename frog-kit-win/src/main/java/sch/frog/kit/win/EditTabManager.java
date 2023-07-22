package sch.frog.kit.win;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import sch.frog.kit.win.editor.LangEditor;

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
        LangEditor jsonEditor = new LangEditor(emitter, Constants.EDITOR_TYPE_CONSOLE.equals(type));
        newTab.setContent(jsonEditor);
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
        return new TabElement(newTab, jsonEditor);
    }

    public static class TabElement{
        private final Tab tab;
        private final LangEditor editor;

        public TabElement(Tab tab, LangEditor editor) {
            this.tab = tab;
            this.editor = editor;
        }

        public Tab getTab() {
            return tab;
        }

        public LangEditor getEditor() {
            return editor;
        }
    }

}
