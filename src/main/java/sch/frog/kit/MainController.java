package sch.frog.kit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
        self.messageEmitter.emitError(message);
    }

    public void onClose(){
        for (CustomViewControl view : views) {
            view.onClose();
        }
    }
}