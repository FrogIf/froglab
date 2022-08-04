package sch.frog.kit.view.transcode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.util.FXMLUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TranscodeSubView extends HBox implements Initializable {

    private final ITransfer transfer;

    public TranscodeSubView(ITransfer transfer) {
        FXMLLoader loader = new FXMLLoader(TranscodeSubView.class.getResource(FXMLUtil.parseCamelToFxml(TranscodeSubView.class.getSimpleName())));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
        this.transfer = transfer;
    }

    @FXML
    private TextArea originContent;

    @FXML
    private TextArea codeContent;

    @FXML
    public void encode(){
        String text = originContent.getText();
        if(text == null){
            codeContent.setText(null);
        }else{
            try {
                codeContent.setText(transfer.encode(text));
            } catch (Exception e) {
                LogKit.error(e.getMessage());
            }
        }
    }

    @FXML
    public void decode(){
        String text = codeContent.getText();
        if(text == null){
            originContent.setText(null);
        }else{
            try {
                originContent.setText(transfer.decode(text));
            } catch (Exception e) {
                LogKit.error(e.getMessage());
            }
        }
    }

    @FXML
    private CheckBox originCheckBox;

    @FXML
    private CheckBox codeCheckBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        originCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            originContent.setWrapText(newValue);
        });
        codeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            codeContent.setWrapText(newValue);
        });
    }

    public interface ITransfer{

        String encode(String content) throws Exception;

        String decode(String content) throws Exception;

    }

}
