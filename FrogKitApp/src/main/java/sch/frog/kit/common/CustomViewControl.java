package sch.frog.kit.common;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import sch.frog.kit.ApplicationContext;
import sch.frog.kit.exception.GlobalExceptionThrower;
import sch.frog.kit.server.handle.IWebView;
import sch.frog.kit.util.FXMLUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * custom control parent class, must observe rule:
 * 1. for example class name is 'AbcDef', the fxml name must be abc-def.fxml, and the file must in view directory;
 * 2. fxml file's root must be:
 *         <fx:root type="javafx.scene.layout.VBox"  ... ></fx:root>
 */
public abstract class CustomViewControl extends VBox implements IWebView, Initializable {

    public CustomViewControl() {
        Class<? extends CustomViewControl> clazz = getClass();
        FXMLLoader loader = new FXMLLoader(clazz.getResource(FXMLUtil.parseCamelToFxml(clazz.getSimpleName())));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            GlobalExceptionThrower.throwExceptionLazy(e);
        }
    }

    protected void init(){
        // do nothing
    }

    /**
     * 视图加载完毕后执行
     */
    public void afterLoad(ApplicationContext context){
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

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        this.init();
    }
}
