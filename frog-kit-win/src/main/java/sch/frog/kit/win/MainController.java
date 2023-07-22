package sch.frog.kit.win;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Label msgText;

    @FXML
    private ComboBox<String> editorType;

    private MessageEmitter messageEmitter;

    @FXML
    protected void onNewTabBtnClick() {
        EditTabManager.addTab(mainTabPane, this.messageEmitter, editorType.getSelectionModel().getSelectedItem());
    }

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
        messageEmitter = new MessageEmitter(msgText);
        EditTabManager.addTab(mainTabPane, messageEmitter, Constants.EDITOR_TYPE_CONSOLE);
        mainTabPane.setContextMenu(initTabPaneContextMenu(mainTabPane));
        mainTabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
        editorType.setItems(FXCollections.observableArrayList(Constants.EDITOR_TYPE_CONSOLE, Constants.EDITOR_TYPE_SCRIPT));
        editorType.getSelectionModel().select(0);
    }

    private ContextMenu initTabPaneContextMenu(TabPane tabPane) {
        ContextMenu treeContextMenu = new ContextMenu();
        MenuItem closeSelect = new MenuItem("Close");
        closeSelect.setOnAction(actionEvent -> {
            Tab selectTab = tabPane.getSelectionModel().getSelectedItem();
            if(selectTab != null){
                tabPane.getTabs().remove(selectTab);
            }else{
                messageEmitter.emitWarn("no tab select");
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
                messageEmitter.emitWarn("no tab select");
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
                messageEmitter.emitWarn("no tab select");
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
                messageEmitter.emitWarn("no tab select");
            }
        });

        MenuItem renameTab = new MenuItem("Rename");
        renameTab.setOnAction(actionEvent -> {
            Tab selectTab = tabPane.getSelectionModel().getSelectedItem();
            if(selectTab != null){
                openRenameStage();
            }else{
                messageEmitter.emitWarn("no tab select");
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
}
