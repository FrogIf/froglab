package sch.frog.kit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sch.frog.kit.config.GlobalInnerProperties;
import sch.frog.kit.exception.GlobalExceptionThrower;

public class FrogKitApplication extends Application {

    @Override
    public void start(Stage stage) {
        exceptionHandle();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("FrogKit " + GlobalInnerProperties.getProperty("application.version"));
            stage.setScene(scene);
            stage.getIcons().add(ImageResources.appIcon);
            stage.show();

            MainController controller = fxmlLoader.getController();
            stage.setOnCloseRequest((event) -> {
                controller.onClose();
            });
        }catch (Exception e){
            GlobalExceptionThrower.INSTANCE.throwException(e);
        }
    }

    private void exceptionHandle(){
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> GlobalExceptionThrower.INSTANCE.throwException(e));
    }

}
