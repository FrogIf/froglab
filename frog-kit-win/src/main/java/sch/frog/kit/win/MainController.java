package sch.frog.kit.win;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sch.frog.kit.core.FrogLangApp;
import sch.frog.kit.win.editor.ScriptWorkspace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Label msgText;

    private FrogLangApp frogLangApp;

    private Stage aboutStage = null;

    @FXML
    protected void onAboutBtnClick() throws IOException {
        if (aboutStage == null) {
            aboutStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("about-view.fxml"));
            Scene secondScene = new Scene(fxmlLoader.load(), 300, 200);
            aboutStage.setScene(secondScene);
            aboutStage.resizableProperty().setValue(false);
            aboutStage.setTitle("About");
            aboutStage.getIcons().add(ImageResources.appIcon);
        }
        aboutStage.show();
        if (aboutStage.isIconified()) {
            aboutStage.setIconified(false);
        }else{
            aboutStage.requestFocus();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File externalDir = new File("external");
        File[] fileList = externalDir.listFiles();
        if(fileList != null){
            ArrayList<String> jarFiles = new ArrayList<>(fileList.length);
            for (File f : fileList) {
                if(f.getName().endsWith(".jar")){
                    jarFiles.add(f.getPath());
                }
            }
            frogLangApp = FrogLangApp.getInstance(jarFiles);
        }else{
            frogLangApp = FrogLangApp.getInstance();
        }

        MessageUtil.messageEmitter = new MessageEmitter(msgText);
        EditTabManager.addTab(mainTabPane, Constants.EDITOR_TYPE_CONSOLE, frogLangApp);
        mainTabPane.setContextMenu(initTabPaneContextMenu(mainTabPane));
        mainTabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
    }

    private ContextMenu initTabPaneContextMenu(TabPane tabPane) {
        ContextMenu treeContextMenu = new ContextMenu();
        MenuItem closeSelect = new MenuItem("Close");
        closeSelect.setOnAction(actionEvent -> {
            Tab selectTab = tabPane.getSelectionModel().getSelectedItem();
            if(selectTab != null){
                tabPane.getTabs().remove(selectTab);
            }else{
                MessageUtil.warn("no tab select");
            }
        });

        MenuItem closeOthers = new MenuItem("Close Other");
        closeOthers.setOnAction(actionEvent -> {
            Tab selectTab = tabPane.getSelectionModel().getSelectedItem();
            if(selectTab != null){
                ObservableList<Tab> tabs = tabPane.getTabs();
                tabs.clear();
                tabs.add(selectTab);
            }else{
                MessageUtil.warn("no tab select");
            }
        });

        MenuItem closeAll = new MenuItem("Close All");
        closeAll.setOnAction(actionEvent -> {
            tabPane.getTabs().clear();
        });

        MenuItem closeToLeft = new MenuItem("Close to Left");
        closeToLeft.setOnAction(actionEvent -> {
            Tab selectTab = tabPane.getSelectionModel().getSelectedItem();
            if(selectTab != null){
                ObservableList<Tab> tabs = tabPane.getTabs();
                Iterator<Tab> iterator = tabs.iterator();
                while(iterator.hasNext()){
                    if(iterator.next() == selectTab){
                        break;
                    }
                    iterator.remove();
                }
            }else{
                MessageUtil.warn("no tab select");
            }
        });

        MenuItem closeToRight = new MenuItem("Close to Right");
        closeToRight.setOnAction(actionEvent -> {
            Tab selectTab = tabPane.getSelectionModel().getSelectedItem();
            if(selectTab != null){
                ObservableList<Tab> tabs = tabPane.getTabs();
                Iterator<Tab> iterator = tabs.iterator();
                boolean startRemove = false;
                while(iterator.hasNext()){
                    Tab next = iterator.next();
                    if(startRemove){
                        iterator.remove();
                    }else{
                        startRemove = next == selectTab;
                    }
                }
            }else{
                MessageUtil.warn("no tab select");
            }
        });

        MenuItem renameTab = new MenuItem("Rename");
        renameTab.setOnAction(actionEvent -> {
            Tab selectTab = tabPane.getSelectionModel().getSelectedItem();
            if(selectTab != null){
                openRenameStage();
            }else{
                MessageUtil.warn("no tab select");
            }
        });

        ObservableList<MenuItem> items = treeContextMenu.getItems();
        items.add(renameTab);
        items.add(closeOthers);
        items.add(closeAll);
        items.add(closeToLeft);
        items.add(closeToRight);
        items.add(closeSelect);
        return treeContextMenu;
    }

    private Stage renameStage = null;

    private void openRenameStage() {
        if(renameStage == null){
            renameStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("rename-tab.fxml"));
            Scene secondScene = null;
            try {
                secondScene = new Scene(fxmlLoader.load(), 300, 200);
            } catch (IOException e) {
                GlobalExceptionThrower.INSTANCE.throwException(e);
            }
            final RenameTabController renameTabController = fxmlLoader.getController();
            renameTabController.setConfirmCallback(name -> {
                if(name == null){ return; }
                Tab selectTab = mainTabPane.getSelectionModel().getSelectedItem();
                if(selectTab != null){
                    selectTab.setText(name);
                }
            });
            renameStage.setScene(secondScene);
            renameStage.resizableProperty().setValue(false);
            renameStage.setTitle("Rename");
            renameStage.getIcons().add(ImageResources.appIcon);
            renameStage.initStyle(StageStyle.UTILITY);
            renameStage.setAlwaysOnTop(true);
            renameStage.setOnShown(event -> {
                Tab selectTab = mainTabPane.getSelectionModel().getSelectedItem();
                if(selectTab != null){
                    renameTabController.setOriginTabName(selectTab.getText());
                }
            });
        }
        renameStage.show();
        if(renameStage.isIconified()){  // 判断是否最小化
            renameStage.setIconified(false);
        }else{
            renameStage.requestFocus();
        }
    }

    @FXML
    public void addConsole(){
        EditTabManager.addTab(mainTabPane, Constants.EDITOR_TYPE_CONSOLE, frogLangApp);
    }

    @FXML
    public void addScript(){
        EditTabManager.addTab(mainTabPane, Constants.EDITOR_TYPE_SCRIPT, frogLangApp);
    }

    @FXML
    public void loadScript(){
        FileChooser fileChooser = new FileChooser();
        if(ScriptWorkspace.historyDirectory() != null){
            fileChooser.setInitialDirectory(ScriptWorkspace.historyDirectory());  // 指定上次加载路径为当前加载路径
        }
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Script File (*.frog)", "*.frog");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            ScriptWorkspace.setHistoryDirectory(file.getParentFile());
            EditTabManager.TabElement tabElement = EditTabManager.addOrSelectForScript(mainTabPane, file.getName(), frogLangApp, file.getAbsolutePath());
            try (
                    FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(fileReader)
            ){
                StringBuilder sb = new StringBuilder();
                String line;
                boolean start = true;
                while((line = reader.readLine()) != null){
                    if(!start){
                        sb.append('\n');
                    }else{
                        start = false;
                    }
                    sb.append(line);
                }
                tabElement.getWorkspace().setContent(sb.toString());
            } catch (IOException e) {
                MessageUtil.error(e.getMessage());
            }
        }
    }
}
