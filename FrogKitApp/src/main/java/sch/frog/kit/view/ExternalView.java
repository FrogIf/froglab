package sch.frog.kit.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.FieldInfo;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.common.ValueObj;
import sch.frog.kit.exception.GlobalExceptionThrower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExternalView extends CustomViewControl {

    @FXML
    private VBox formContainer;

    private final ExternalViewStruct viewStruct;

    public ExternalView(ExternalViewStruct viewStruct) {
        this.viewStruct = viewStruct;
        viewInit();
    }

    private final HashMap<String, TextArea> inputMap = new HashMap<>();

    private final HashMap<String, TextArea> outputMap = new HashMap<>();

    @Override
    public void postInit() {
        viewStruct.onInit();
    }

    @Override
    public void onClose() {
        viewStruct.onClose();
    }

    @Override
    public void onHidden() {
        viewStruct.onHidden();
    }

    private void viewInit(){
        ObservableList<Node> container = formContainer.getChildren();

        for (FieldInfo fieldInfo : viewStruct.getInputField()) {
            String name = fieldInfo.getName();
            if(inputMap.containsKey(name)){
                GlobalExceptionThrower.throwException(new IllegalArgumentException("duplicate field name : " + name));
            }
            container.add(new Label(name + " - " + fieldInfo.getDescription()));
            TextArea textArea = new TextArea();
            inputMap.put(name, textArea);
            container.add(textArea);
        }

        HBox executeBox = new HBox();
        executeBox.setAlignment(Pos.CENTER);
        executeBox.setPadding(new Insets(10, 10, 10, 10));
        container.add(executeBox);

        Button execBtn = new Button();
        execBtn.setText("execute");
        executeBox.getChildren().add(execBtn);
        execBtn.setOnAction(event -> {
            Set<Map.Entry<String, TextArea>> entries = inputMap.entrySet();
            ArrayList<ValueObj> values = new ArrayList<>();
            for (Map.Entry<String, TextArea> entry : entries) {
                values.add(new ValueObj(entry.getKey(), entry.getValue().getText()));
            }
            for (TextArea textArea : outputMap.values()) {
                textArea.setText(null);
            }
            try{
                List<ValueObj> resultList = viewStruct.execute(values);
                if(resultList != null){
                    for (ValueObj valueObj : resultList) {
                        String name = valueObj.getName();
                        TextArea textArea = outputMap.get(name);
                        if(textArea != null){
                            textArea.setText(valueObj.getValue());
                        }else{
                            LogKit.error("failed to output : " + name);
                        }
                    }
                }
            }catch (Throwable t){
                LogKit.error("execute failed : " + t.getMessage());
            }
        });

        for (FieldInfo fieldInfo : viewStruct.getOutputFiled()) {
            String name = fieldInfo.getName();
            if(outputMap.containsKey(name)){
                GlobalExceptionThrower.throwException(new IllegalArgumentException("duplicate field name : " + name));
            }
            container.add(new Label(name + " - " + fieldInfo.getDescription()));
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            outputMap.put(name, textArea);
            container.add(textArea);
        }
    }

    @Override
    public void onShow() {
        viewStruct.onShow();
    }
}
