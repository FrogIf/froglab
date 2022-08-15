package sch.frog.kit.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.FieldInfo;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.common.StringMap;
import sch.frog.kit.exception.GlobalExceptionThrower;
import sch.frog.kit.view.util.DragResizer;

import java.util.HashMap;
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

    private void viewInit(){
        ObservableList<Node> container = formContainer.getChildren();

        for (FieldInfo fieldInfo : viewStruct.getInputField()) {
            String name = fieldInfo.getName();
            if(inputMap.containsKey(name)){
                GlobalExceptionThrower.throwException(new IllegalArgumentException("duplicate field name : " + name));
            }

            HBox row = new HBox();
            container.add(row);
            row.getChildren().add(new Label(name + " - " + fieldInfo.getDescription()));
            Pane pane = new Pane();
            HBox.setHgrow(pane, Priority.ALWAYS);
            row.getChildren().add(pane);
            CheckBox cb = new CheckBox("word wrap");
            cb.setSelected(true);
            TextArea textArea = this.draggableTextarea();
            textArea.setWrapText(true);
            cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                textArea.setWrapText(newValue);
            });
            row.getChildren().add(cb);

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
            StringMap params = new StringMap();
            for (Map.Entry<String, TextArea> entry : entries) {
                params.put(entry.getKey(), entry.getValue().getText());
            }
            for (TextArea textArea : outputMap.values()) {
                textArea.setText(null);
            }
            try{
                StringMap result = viewStruct.execute(params);
                if(result != null){
                    for (Map.Entry<String, String> entry : result.entrySet()) {
                        String name = entry.getKey();
                        TextArea textArea = outputMap.get(name);
                        if(textArea != null){
                            textArea.setText(entry.getValue());
                        }else{
                            LogKit.error("failed to output : " + name);
                        }
                    }
                }
            }catch (Throwable t){
                LogKit.error("execute failed : " + t.getMessage());
            }
        });
        Button clear = new Button();
        clear.setText("clear");
        HBox.setMargin(clear, new Insets(0, 0, 0, 10));
        executeBox.getChildren().add(clear);
        clear.setOnAction(e -> {
            for (TextArea value : inputMap.values()) {
                value.setText(null);
            }
            for (TextArea value : outputMap.values()) {
                value.setText(null);
            }
        });

        for (FieldInfo fieldInfo : viewStruct.getOutputFiled()) {
            String name = fieldInfo.getName();
            if(outputMap.containsKey(name)){
                GlobalExceptionThrower.throwException(new IllegalArgumentException("duplicate field name : " + name));
            }
            HBox row = new HBox();
            container.add(row);
            row.getChildren().add(new Label(name + " - " + fieldInfo.getDescription()));
            Pane pane = new Pane();
            HBox.setHgrow(pane, Priority.ALWAYS);
            row.getChildren().add(pane);
            CheckBox cb = new CheckBox("word wrap");
            cb.setSelected(true);
            TextArea textArea = this.draggableTextarea();
            textArea.setWrapText(true);
            cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                textArea.setWrapText(newValue);
            });
            row.getChildren().add(cb);
            textArea.setEditable(false);
            outputMap.put(name, textArea);
            container.add(textArea);
        }
    }

    private final static BorderWidths BORDER_DRAG = new BorderWidths(0, 0, 3, 0, false, false, false, false);

    private TextArea draggableTextarea(){
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(100);
        textArea.setBorder(new Border(new BorderStroke(Paint.valueOf("#b6b6b6"), BorderStrokeStyle.SOLID, new CornerRadii(2), BORDER_DRAG)));
        DragResizer.makeResizable(textArea);
        return textArea;
    }

    public ExternalViewStruct getViewStruct(){
        return this.viewStruct;
    }
}
