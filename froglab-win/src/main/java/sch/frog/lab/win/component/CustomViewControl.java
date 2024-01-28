package sch.frog.lab.win.component;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import sch.frog.lab.win.component.exception.GlobalExceptionThrower;
import sch.frog.lab.win.component.util.FXMLUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class CustomViewControl extends VBox implements Initializable {

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
//    public void afterLoad(ApplicationContext context){
//        // do nothing
//    }

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
