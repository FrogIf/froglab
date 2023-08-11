package sch.frog.kit.win.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sch.frog.kit.lang.exception.ExecuteException;
import sch.frog.kit.lang.execute.IRuntimeContext;
import sch.frog.kit.lang.fun.AbstractGeneralFunction;
import sch.frog.kit.lang.value.Value;
import sch.frog.kit.win.ImageResources;

public class WinFunction extends AbstractGeneralFunction {

    private static Stage viewStage = null;

    @Override
    protected Value doExec(Value[] args, IRuntimeContext context) {
        try{
            if(viewStage == null){
                viewStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MainComponentView.class.getResource("main-component-view.fxml"));
                Scene secondScene = new Scene(fxmlLoader.load(), 600, 500);
                viewStage.setScene(secondScene);
                viewStage.setTitle("FrogKit");
                viewStage.getIcons().add(ImageResources.appIcon);
            }
            viewStage.show();
            if (viewStage.isIconified()) {
                viewStage.setIconified(false);
            }else{
                viewStage.requestFocus();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ExecuteException(e.getMessage());
        }

        return Value.VOID;
    }

    @Override
    public String name() {
        return "win";
    }

    @Override
    public String description() {
        return "win";
    }
}
