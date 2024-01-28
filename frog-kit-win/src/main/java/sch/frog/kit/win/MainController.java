package sch.frog.kit.win;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sch.frog.kit.lang.LangRunner;
import sch.frog.kit.lang.fun.IFunction;
import sch.frog.kit.lang.value.VMap;
import sch.frog.kit.lang.value.VMapImpl;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.win.editor.ScriptWorkspace;
import sch.frog.kit.win.extfun.ExternalFunctionLoadUtil;
import sch.frog.kit.win.extfun.FunPackage;
import sch.frog.kit.win.extfun.code.DeBase64Function;
import sch.frog.kit.win.extfun.code.DeHexFunction;
import sch.frog.kit.win.extfun.code.DeUnicodeFunction;
import sch.frog.kit.win.extfun.code.EnBase64Function;
import sch.frog.kit.win.extfun.code.EnHexFunction;
import sch.frog.kit.win.extfun.code.EnUnicodeFunction;
import sch.frog.kit.win.extfun.code.Md5Function;
import sch.frog.kit.win.extfun.code.UrlDecodeFunction;
import sch.frog.kit.win.extfun.code.UrlEncodeFunction;
import sch.frog.kit.win.extfun.time.DateFunction;
import sch.frog.kit.win.extfun.time.NowFunction;
import sch.frog.kit.win.extfun.time.TimeFunction;
import sch.frog.kit.win.extfun.win.WinFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Label msgText;

    private LangRunner langRunner;

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

    private static final IFunction[] INNER_FUN_ARRAY = new IFunction[]{
            new DateFunction(),
            new NowFunction(),
            new TimeFunction(),
            new DeBase64Function(),
            new EnBase64Function(),
            new DeHexFunction(),
            new EnHexFunction(),
            new DeUnicodeFunction(),
            new EnUnicodeFunction(),
            new UrlDecodeFunction(),
            new UrlEncodeFunction(),
            new Md5Function(),
            new WinFunction()
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<IFunction> funs = new ArrayList<>(Arrays.asList(INNER_FUN_ARRAY));

        File externalDir = new File("external");
        File[] fileList = externalDir.listFiles();
        HashMap<String, Value> valueMap = new HashMap<>();
        if(fileList != null){
            for (File f : fileList) {
                if(f.getName().endsWith(".jar")){
                    try{
                        List<FunPackage> packages = ExternalFunctionLoadUtil.load(f.getPath());
                        for (FunPackage pak : packages) {
                            VMap funMap = new VMapImpl();
                            List<IFunction> funList = pak.getFunctions();
                            for (IFunction fun : funList) {
                                funMap.put(fun.name(), new Value(fun));
                            }
                            valueMap.put(pak.getName(), new Value(funMap));
                        }
                    }catch (Exception e){
                        throw new Error(e);
                    }
                }
            }
        }

        for (IFunction fun : funs) {
            valueMap.put(fun.name(), Value.of(fun));
        }

        langRunner = new LangRunner(valueMap);

        MessageUtil.messageEmitter = new MessageEmitter(msgText);
        EditTabManager.addTab(mainTabPane, Constants.EDITOR_TYPE_CONSOLE, langRunner);
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
        EditTabManager.addTab(mainTabPane, Constants.EDITOR_TYPE_CONSOLE, langRunner);
    }

    @FXML
    public void addScript(){
        EditTabManager.addTab(mainTabPane, Constants.EDITOR_TYPE_SCRIPT, langRunner);
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
            EditTabManager.TabElement tabElement = EditTabManager.addOrSelectForScript(mainTabPane, file.getName(), langRunner, file.getAbsolutePath());
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
