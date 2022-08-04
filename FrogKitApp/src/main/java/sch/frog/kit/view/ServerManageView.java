package sch.frog.kit.view;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import sch.frog.kit.MainController;
import sch.frog.kit.common.BeanContainer;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.server.HttpServer;
import sch.frog.kit.server.handle.RequestActionBox;
import sch.frog.kit.util.ClipboardUtil;
import sch.frog.kit.common.util.StringUtil;

import java.util.List;

public class ServerManageView extends CustomViewControl {

    @FXML
    private TextField pathText;

    @FXML
    private TextField portText;

    private final HttpServer httpServer = new HttpServer();

    @FXML
    public void startServer(){
        String port = portText.getText();
        if(StringUtil.isBlank(port)){
            LogKit.error("port is required.");
            return;
        }
        int portNum = 0;
        try{
            portNum = Integer.parseInt(port);
        }catch (NumberFormatException e){
            LogKit.error("port is not a number.");
            return;
        }

        String path = pathText.getText();
        if(path == null){ path = "/"; }
        path = path.trim();
        if(StringUtil.isBlank(path)){
            path = "/";
        }
        pathText.setText(path);
        pathText.setEditable(false);
        portText.setEditable(false);

        httpServer.setPort(portNum);
        httpServer.setContextPath(path);

        httpServer.start();
    }

    @FXML
    public void stopServer(){
        pathText.setEditable(true);
        portText.setEditable(true);

        httpServer.shutdown();
    }

    @Override
    public void onClose() {
        httpServer.shutdown();
    }


    @FXML
    private FlowPane requestUrl;

    @Override
    public void postInit() {
        List<CustomViewControl> views = BeanContainer.get(MainController.VIEWS_BEAN_NAME);
        httpServer.init(views);

        List<RequestActionBox> actions = httpServer.getActions();
        EventHandler<? super MouseEvent> eventHandler = event -> {
            String contextPath = httpServer.getContextPath();
            if(contextPath.endsWith("/")){
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
            ClipboardUtil.putToClipboard(contextPath + ((Hyperlink) event.getSource()).getText());
        };

        ObservableList<Node> requestUrlBox = requestUrl.getChildren();
        for (RequestActionBox act : actions) {
            TitledPane pane = new TitledPane();
//            pane.setExpanded(false);
            pane.setText(act.getDescription());

            VBox vBox = new VBox();
            pane.setContent(vBox);

            vBox.setAlignment(Pos.CENTER_LEFT);
            ObservableList<Node> children = vBox.getChildren();

            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setText(act.getPath());
            hyperlink.setOnMouseClicked(eventHandler);
            children.add(hyperlink);

            RequestActionBox.RequestParamInfo[] params = act.getParams();
            for (RequestActionBox.RequestParamInfo param : params) {
                children.add(new Label(param.getName() + "(" + param.getType().getSimpleName() + ") -- " + param.getDescription()));
            }

            requestUrlBox.add(pane);
        }

    }
}
