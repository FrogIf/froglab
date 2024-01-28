package sch.frog.lab.win;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sch.frog.lab.win.config.GlobalInnerProperties;

public class FroglabApplication extends Application {

    public static FroglabApplication self;

    private Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        self = this;
        exceptionHandle();
        FXMLLoader fxmlLoader = new FXMLLoader(FroglabApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add(FroglabApplication.class.getResource("css/code-assist.css").toExternalForm());
        stage.setTitle("froglab " + GlobalInnerProperties.getProperty("application.version"));
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
