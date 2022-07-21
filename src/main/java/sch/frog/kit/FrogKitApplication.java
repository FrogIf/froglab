package sch.frog.kit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sch.frog.kit.config.GlobalInnerProperties;

public class FrogKitApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        exceptionHandle();
        FXMLLoader fxmlLoader = new FXMLLoader(FrogKitApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("FrogKit " + GlobalInnerProperties.getProperty("application.version"));
        stage.setScene(scene);
//        stage.getIcons().add(ImageResources.appIcon);
        stage.show();
    }

    private void exceptionHandle(){
        // TODO alert form
    }

}
