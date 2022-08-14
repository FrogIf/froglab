package sch.frog.kit.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import sch.frog.kit.FrogKitApplication;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.common.util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64ImageView extends CustomViewControl {

    @FXML
    private ImageView imageView;

    @FXML
    private TextArea codeContent;

    public void encode(){
        Image image = imageView.getImage();
        if(image != null){
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                String imgBase64 = Base64.getEncoder().encodeToString(bytes);
                codeContent.setText("data:image/png;base64," + imgBase64);
            } catch (IOException e) {
                LogKit.error("encode image failed, msg : " + e.getMessage());
            }
        }else{
            LogKit.error("no image");
        }
    }

    public void decode(){
        String text = codeContent.getText();
        if(StringUtil.isNotBlank(text)){
            try{
                int splitPos = text.indexOf(";");
                byte[] decode = Base64.getDecoder().decode(text.substring(splitPos + 8));
                ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);
                Image image = new Image(inputStream);
                fitImageSize(image);
                imageView.setImage(image);
            }catch (Exception e){
                LogKit.error("failed to convert base64 to image, " + e.getMessage());
            }
        }
    }

    private File loadDir = null;

    public void uploadImage(){
        FileChooser fileChooser = new FileChooser();
        if(this.loadDir != null){
            fileChooser.setInitialDirectory(this.loadDir);  // 指定上次加载路径为当前加载路径
        }
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(FrogKitApplication.self.getPrimaryStage());
        if(file != null){
            if(file.length() / 1024 / 1024 > 1){
                LogKit.error("image size must less than 1M.");
                return;
            }
            try(
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))
                    ){
                Image image = new Image(inputStream);
                fitImageSize(image);
                this.imageView.setImage(image);
                this.codeContent.setText(null);
                this.loadDir = file.getParentFile();
            }catch (Exception e){
                LogKit.error("load image failed, msg : " + e.getMessage());
            }
        }
    }

    public void clear(){
        this.imageView.setImage(null);
        this.codeContent.setText(null);
    }

    private void fitImageSize(Image image){
        double width = imageView.getFitHeight() / image.getHeight() * image.getWidth();
        this.imageView.setFitWidth(width);
    }


}
