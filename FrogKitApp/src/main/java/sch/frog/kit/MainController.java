package sch.frog.kit;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import sch.frog.kit.common.BeanContainer;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.common.util.StringUtil;
import sch.frog.kit.demo.DemoExternalViewStruct;
import sch.frog.kit.view.ExternalView;

import java.awt.*;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static MainController self;

    public static final String VIEWS_BEAN_NAME = "views";

    @FXML
    private TextArea msgText;

    @FXML
    private TabPane mainTabPane;

    private final LinkedList<CustomViewControl> views = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LogKit.init(new GlobalLogPrinter());
        MainController.self = this;

        ObservableList<Tab> tabs = this.mainTabPane.getTabs();
        for (Tab tab : tabs) {
            CustomViewControl view = (CustomViewControl) tab.getContent();
            views.add(view);
        }

        addExternalView(new DemoExternalViewStruct());

        for (CustomViewControl view : views) {
            view.init();
        }

        BeanContainer.add(VIEWS_BEAN_NAME, views);

        this.mainTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            CustomViewControl oldView = (CustomViewControl) oldValue.getContent();
            CustomViewControl newView = (CustomViewControl) newValue.getContent();
            oldView.onHidden();
            newView.onShow();
        });

        for (CustomViewControl view : views) {
            view.postInit();
        }
    }

    private void addExternalView(ExternalViewStruct struct){
        Tab tab = new Tab();
        ExternalView externalView = new ExternalView(struct);
        tab.setContent(new ExternalView(struct));
        tab.setText(struct.getViewName());
        this.mainTabPane.getTabs().add(tab);
        this.views.add(externalView);
    }

    private static final int MAX_LOG_ROWS = 100;

    private void outputLog(String message){
        if(StringUtil.isBlank(message)){ return; }
        if(StringUtil.isNotBlank(msgText.getText())){
            msgText.appendText("\n");
        }
        msgText.appendText(message);

        // control max rows
        ObservableList<CharSequence> paragraphs = msgText.getParagraphs();
        int rows = paragraphs.size();
        if(rows > MAX_LOG_ROWS){
            Iterator<CharSequence> iterator = paragraphs.iterator();
            int end = 0;
            while(iterator.hasNext() && rows > MAX_LOG_ROWS){
                CharSequence sequence = iterator.next();
                end += sequence.length() + 1;
                rows--;
            }
            msgText.deleteText(0, end);
        }
    }

    @FXML
    public void clearLog(){
        msgText.setText("");
    }

    public static void error(String message){
        Platform.runLater(() -> {
            self.outputLog(message);
            Toolkit.getDefaultToolkit().beep();
        });
    }

    public static void warn(String message){
        Platform.runLater(() -> {
            self.outputLog(message);
            Toolkit.getDefaultToolkit().beep();
        });
    }

    public static void info(String message){
        Platform.runLater(() -> {
            self.outputLog(message);
        });
    }

    public void onClose(){
        for (CustomViewControl view : views) {
            view.onClose();
        }
    }

    private static class GlobalLogPrinter implements LogKit.ILoggerPrinter {

        @Override
        public void info(String msg) {
            MainController.info(msg);
        }

        @Override
        public void warn(String msg) {
            MainController.warn(msg);
        }

        @Override
        public void error(String msg) {
            MainController.error(msg);
        }
    }
}