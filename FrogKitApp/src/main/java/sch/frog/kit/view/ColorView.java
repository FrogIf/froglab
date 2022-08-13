package sch.frog.kit.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.LogKit;

public class ColorView extends CustomViewControl {

    @FXML
    private VBox colorTable;

    @Override
    protected void init() {
        addColorRow();
    }

    private void addColorRow(){
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(2, 2, 2, 2));
        hbox.setBorder(new Border(new BorderStroke(Paint.valueOf("#b6b6b6"), BorderStrokeStyle.SOLID, new CornerRadii(2), BorderWidths.DEFAULT)));
        hbox.setAlignment(Pos.CENTER_RIGHT);
        ObservableList<Node> children = hbox.getChildren();
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            hbox.setBackground(new Background(new BackgroundFill(newValue, new CornerRadii(2), new Insets(2, 2, 2, 2))));
        });
        children.add(colorPicker);

        TextField textField = new TextField();
        textField.setPrefWidth(80);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                newValue = newValue.trim();
                if(newValue.length() == 6){
                    try{
                        colorPicker.setValue(Color.valueOf(newValue));
                    }catch (Exception e){
                        LogKit.error(e.getMessage());
                    }
                }
            }
        });
        children.add(textField);

        Button removeBtn = new Button();
        removeBtn.setText("-");
        removeBtn.setOnAction(event -> {
            ObservableList<Node> nodes = colorTable.getChildren();
            nodes.removeAll(hbox);
        });
        children.add(removeBtn);
        colorTable.getChildren().add(hbox);
    }

    @FXML
    public void appendColorRow(){
        addColorRow();
    }

    @FXML
    public void removeAllRows(){
        colorTable.getChildren().clear();
    }
}
