package sch.frog.kit.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import sch.frog.kit.server.handle.IWebView;
import sch.frog.kit.util.FXMLUtil;

import java.io.IOException;

/**
 * custom control parent class, must observe rule:
 * 1. for example class name is 'AbcDef', the fxml name must be abc-def.fxml, and the file must in view directory;
 * 2. fxml file's root must be:
 *         <fx:root type="javafx.scene.layout.VBox"  ... ></fx:root>
 */
public abstract class CustomViewControl extends VBox implements IWebView {

    public CustomViewControl() {
        Class<? extends CustomViewControl> clazz = getClass();
        FXMLLoader loader = new FXMLLoader(clazz.getResource(FXMLUtil.parseCamelToFxml(clazz.getSimpleName())));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void init(){
        // do nothing
    }

    // 初始化结束执行
    public void postInit(){
        // do nothing
    }

    public void onClose(){
        // do nothing
    }

    public void onHidden(){
        // do nothing
    }

    public void onShow(){
        // to nothing
    }

}
