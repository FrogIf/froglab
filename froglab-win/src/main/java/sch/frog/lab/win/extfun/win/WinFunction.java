package sch.frog.lab.win.extfun.win;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sch.frog.lab.lang.exception.ExecuteException;
import sch.frog.lab.lang.fun.IFunction;
import sch.frog.lab.lang.semantic.IExecuteContext;
import sch.frog.lab.lang.value.Value;
import sch.frog.lab.win.ImageResources;
import sch.frog.lab.win.component.MainComponentView;

public class WinFunction implements IFunction {

    private static Stage viewStage = null;

    @Override
    public String name() {
        return "win";
    }

    @Override
    public String description() {
        return "窗体工具";
    }

    @Override
    public Value execute(Value[] args, IExecuteContext context) throws ExecuteException {
        try{
            if(viewStage == null){
                viewStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MainComponentView.class.getResource("main-component-view.fxml"));
                Scene secondScene = new Scene(fxmlLoader.load(), 600, 500);
                viewStage.setScene(secondScene);
                viewStage.setTitle("froglab");
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
}
