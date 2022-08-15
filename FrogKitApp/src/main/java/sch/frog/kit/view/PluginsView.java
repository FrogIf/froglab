package sch.frog.kit.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sch.frog.kit.ApplicationContext;
import sch.frog.kit.MainControllerOperator;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.ExternalViewStruct;

import java.util.List;

public class PluginsView extends CustomViewControl {

    @FXML
    private FlowPane pluginContainer;

    @Override
    public void afterLoad(ApplicationContext context) {
        List<CustomViewControl> views = context.getViews();
        ObservableList<Node> container = pluginContainer.getChildren();
        for (CustomViewControl view : views) {
            if(view instanceof ExternalView){
                VBox vBox = new VBox();
                vBox.setPadding(new Insets(5, 5, 5, 5));
                container.add(vBox);
                vBox.setBorder(new Border(new BorderStroke(Paint.valueOf("#b3b3b3"), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
                ObservableList<Node> children = vBox.getChildren();
                ExternalViewStruct viewStruct = ((ExternalView) view).getViewStruct();
                String viewName = viewStruct.getViewName();
                Label title = new Label("※ " + viewName);
                title.fontProperty().set(Font.font(Font.getDefault().getSize() + 2));
                Label description = new Label(viewStruct.getDescription());
                VBox.setMargin(description, new Insets(5, 0, 0, 0));
                children.add(title);
                children.add(description);
                HBox row = new HBox();
                VBox.setMargin(row, new Insets(10, 0, 0, 0));
                row.setAlignment(Pos.CENTER_RIGHT);
                Button add = new Button("+");
                add.setOnAction(e -> MainControllerOperator.addTab(view, viewName, true));
                row.getChildren().add(add);
                children.add(row);
            }
        }
    }
}
