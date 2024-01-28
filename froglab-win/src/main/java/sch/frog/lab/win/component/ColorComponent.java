package sch.frog.lab.win.component;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
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
import sch.frog.lab.win.ClipboardUtil;

public class ColorComponent extends CustomViewControl {

    public static final BorderWidths BOLD = new BorderWidths(2, 2, 2, 2, false, false, false, false);

    @FXML
    private VBox colorTable;

    private ContextMenu contextMenu;

    private ColorRow selectRow;

    private void addColorRow(){
        colorTable.getChildren().add(buildColorRow());
    }

    private ColorRow buildColorRow(){
        ColorRow row = new ColorRow();

        row.setOnContextMenuRequested(e -> {
            contextMenu.show(selectRow, e.getScreenX(), e.getScreenY());
        });

        row.setOnMouseClicked(event -> {
            if(selectRow != null){
                selectRow.setBorder(new Border(new BorderStroke(Paint.valueOf("#b6b6b6"), BorderStrokeStyle.SOLID, new CornerRadii(2), BorderWidths.DEFAULT)));
            }
            selectRow = row;
            selectRow.setBorder(new Border(new BorderStroke(Paint.valueOf("#20bdec"), BorderStrokeStyle.SOLID, new CornerRadii(2), BOLD)));
        });

        return row;
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
            Node node = colorTable.getChildren().remove(locateInsertPos());
            if(node == selectRow){
                selectRow = null;
            }
        });
        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction(event -> {
            colorTable.getChildren().clear();
            selectRow = null;
        });
        MenuItem copyColor = new MenuItem("Copy Color");
        copyColor.setOnAction(event -> {
            if(selectRow == null){
                LogKit.error("no select row.");
            }else{
                Color color = selectRow.colorPicker.getValue();
                long r = Math.round(color.getRed() * 255.0);
                long g = Math.round(color.getGreen() * 255.0);
                long b = Math.round(color.getBlue() * 255.0);
                ClipboardUtil.putToClipboard(String.format("#%02x%02x%02x", r, g, b));
            }
        });
        contextMenu.getItems().addAll(copyColor, append, insert, remove, clear);
        return contextMenu;
    }

    private int locateInsertPos(){
        ObservableList<Node> children = colorTable.getChildren();
        int i = 0;
        for (Node child : children) {
            if(child == this.selectRow){
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

    @Override
    public void init() {
        contextMenu = buildContextMenu();
        addColorRow();
    }


    private static class ColorRow extends HBox {

        private final ColorPicker colorPicker;
        public ColorRow(){
            this.setPadding(new Insets(2, 2, 2, 2));
            this.setBorder(new Border(new BorderStroke(Paint.valueOf("#b6b6b6"), BorderStrokeStyle.SOLID, new CornerRadii(2), BorderWidths.DEFAULT)));
            this.setAlignment(Pos.CENTER_RIGHT);
            ObservableList<Node> children = this.getChildren();
            this.colorPicker = new ColorPicker();
            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                this.setBackground(new Background(new BackgroundFill(newValue, new CornerRadii(2), new Insets(2, 2, 2, 2))));
            });
            this.setBackground(new Background(new BackgroundFill(colorPicker.getValue(), new CornerRadii(2), new Insets(2, 2, 2, 2))));
            children.add(colorPicker);

            TextField textField = new TextField();
            textField.setPromptText("#");
            textField.setPrefWidth(80);
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null){
                    newValue = newValue.trim();
                    if(newValue.length() == 7 && newValue.startsWith("#")){
                        newValue = newValue.substring(1);
                    }
                    if(newValue.length() == 6){
                        try{
                            colorPicker.setValue(Color.valueOf(newValue));
                        }catch (Exception e){
                            e.printStackTrace();
//                            LogKit.error(e.getMessage());
                        }
                    }
                }
            });
            children.add(textField);
        }
    }

}
