package sch.frog.kit;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.view.CustomViewControl;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static MainController self;

    @FXML
    private Label msgText;

    @FXML
    private TabPane mainTabPane;

    private MessageEmitter messageEmitter;

    private final LinkedList<CustomViewControl> views = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LogKit.init(new GlobalLogPrinter());
        messageEmitter = new MessageEmitter(msgText);
        MainController.self = this;

        ObservableList<Tab> tabs = this.mainTabPane.getTabs();
        for (Tab tab : tabs) {
            CustomViewControl view = (CustomViewControl) tab.getContent();
            views.add(view);
        }

        this.mainTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            CustomViewControl oldView = (CustomViewControl) oldValue.getContent();
            CustomViewControl newView = (CustomViewControl) newValue.getContent();
            oldView.onHidden();
            newView.onShow();
        });
    }

    public static void error(String message){
        Platform.runLater(() -> {
            self.messageEmitter.emitError(message);
        });
    }

    public static void warn(String message){
        Platform.runLater(() -> {
            self.messageEmitter.emitWarn(message);
        });
    }

    public static void info(String message){
        Platform.runLater(() -> {
            self.messageEmitter.emitInfo(message);
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