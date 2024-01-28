package sch.frog.lab.win.component.exception;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sch.frog.lab.win.ImageResources;

import java.lang.reflect.InvocationTargetException;

public class GlobalExceptionThrower {

    private static final GlobalExceptionThrower INSTANCE = new GlobalExceptionThrower();

    private Stage exceptionStage = null;

    private final ExceptionView exceptionView = new ExceptionView();

    public static void throwException(Throwable t){
        throwException(t, false);
    }

    public static void throwException(Throwable t, boolean fromLazy){
        if(INSTANCE.exceptionStage == null){
            try{
                INSTANCE.exceptionStage = new Stage();
            }catch (IllegalStateException e){
                if(!fromLazy){
                    throwExceptionLazy(t);
                }else{
                    throw e;
                }
            }
            Scene secondScene = new Scene(INSTANCE.exceptionView, 500, 400);
            INSTANCE.exceptionStage.setScene(secondScene);
            INSTANCE.exceptionStage.setTitle("An error occurred");
            INSTANCE.exceptionStage.getIcons().add(ImageResources.appIcon);
        }

        Throwable cause = t.getCause();
        if(cause instanceof InvocationTargetException){
            t = ((InvocationTargetException) cause).getTargetException();
        }

        INSTANCE.exceptionView.setException(t);
        INSTANCE.exceptionStage.show();
        if (INSTANCE.exceptionStage.isIconified()) {
            INSTANCE.exceptionStage.setIconified(false);
        }else{
            INSTANCE.exceptionStage.requestFocus();
        }

        INSTANCE.exceptionStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void throwExceptionLazy(Throwable t){
        Platform.runLater(() -> {
            throwException(t, true);
        });
    }

}
