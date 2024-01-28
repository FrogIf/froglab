package sch.frog.kit.win.component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Md5Component extends CustomViewControl{
    @FXML
    public TextField md5Value;
    private File loadDir = null;

    public void selectFile(){
        {
            FileChooser fileChooser = new FileChooser();
            if(this.loadDir != null){
                fileChooser.setInitialDirectory(this.loadDir);  // 指定上次加载路径为当前加载路径
            }
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(null);
            if(file != null){
                this.loadDir = file.getParentFile();
                try(
                        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))
                ){
                    String md5 = DigestUtils.md5Hex(inputStream);
                    md5Value.setText(md5);
                    LogKit.info(md5);
                }catch (Exception e){
                    md5Value.setText(e.getMessage());
                    LogKit.info(e.getMessage());
                }
            }
        }
    }
}
