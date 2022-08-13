package sch.frog.kit.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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

    private ContextMenu contextMenu;

    @Override
    protected void init() {
        contextMenu = buildContextMenu();
        addColorRow();
    }

    private void addColorRow(){
        colorTable.getChildren().add(buildColorRow());
    }

    private HBox triggerBox;

    private HBox buildColorRow(){
        HBox hbox = new HBox();
        hbox.setOnContextMenuRequested(e -> {
            triggerBox = hbox;
            contextMenu.show(triggerBox, e.getScreenX(), e.getScreenY());
        });

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
        textField.setPromptText("#");
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
        return hbox;
    }

    private ContextMenu buildContextMenu(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem append = new MenuItem("Append");
        append.setOnAction(event -> {
            colorTable.getChildren().add(locateInsertPos() + 1, buildColorRow());
        });
        MenuItem insert = new MenuItem("Insert");
        insert.setOnAction(event -> {
            colorTable.getChildren().add(locateInsertPos(), buildColorRow());
        });
        MenuItem remove = new MenuItem("Remove");
        remove.setOnAction(event -> {
            colorTable.getChildren().remove(locateInsertPos());
        });
        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction(event -> {
            colorTable.getChildren().clear();
        });
        contextMenu.getItems().addAll(append, insert, remove, clear);
        return contextMenu;
    }

    private int locateInsertPos(){
        ObservableList<Node> children = colorTable.getChildren();
        int i = 0;
        for (Node child : children) {
            if(child == this.triggerBox){
                break;
            }
            i++;
        }
        return i;
    }

    @FXML
    public void addRow(){
        this.colorTable.getChildren().add(buildColorRow());
    }

    @FXML
    public void clear(){
        this.colorTable.getChildren().clear();
    }
}
