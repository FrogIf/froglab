package sch.frog.kit.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.codec.digest.DigestUtils;
import sch.frog.kit.FrogKitApplication;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.LogKit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Md5View extends CustomViewControl {

    private File loadDir;

    @FXML
    private TextField fileName;

    @FXML
    private TextField md5Val;

    @FXML
    private TextArea md5Record;

    public void uploadFile(){
        FileChooser fileChooser = new FileChooser();
        if(this.loadDir != null){
            fileChooser.setInitialDirectory(this.loadDir);  // 指定上次加载路径为当前加载路径
        }
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(FrogKitApplication.self.getPrimaryStage());
        if(file != null){
            fileName.setText(file.getName());
            try(
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))
            ){
                String md5 = DigestUtils.md5Hex(inputStream);
                md5Val.setText(md5);
                md5Record.appendText(file.getAbsolutePath() + " --> " + md5 + "\n");
                this.loadDir = file.getParentFile();
            }catch (Exception e){
                LogKit.error("load file failed, msg : " + e.getMessage());
            }
        }
    }

}
