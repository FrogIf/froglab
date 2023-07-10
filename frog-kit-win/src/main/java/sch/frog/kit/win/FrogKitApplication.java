package sch.frog.kit.win;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sch.frog.kit.win.config.GlobalInnerProperties;

public class FrogKitApplication extends Application {

    public static FrogKitApplication self;

    private Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        self = this;
        exceptionHandle();
        FXMLLoader fxmlLoader = new FXMLLoader(FrogKitApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
//        scene.getStylesheets().add(FrogKitApplication.class.getResource("css/json-assist.css").toExternalForm());
        scene.getStylesheets().add(FrogKitApplication.class.getResource("css/common.css").toExternalForm());
        stage.setTitle("FrogJson " + GlobalInnerProperties.getProperty("application.version"));
        stage.setScene(scene);
        stage.getIcons().add(ImageResources.appIcon);
        stage.show();
        this.primaryStage = stage;
        stage.setOnCloseRequest(e -> {
            GlobalApplicationLifecycleUtil.stop();
            Platform.exit();
            System.exit(0);
        });
    }

    public static HostServices getAppHostServices(){
        return self.getHostServices();
    }

    private void exceptionHandle(){
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> GlobalExceptionThrower.INSTANCE.throwException(e));
    }

    public Stage getPrimaryStage(){
        return this.primaryStage;
    }
}
