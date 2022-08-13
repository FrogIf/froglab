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
import sch.frog.kit.ApplicationContext;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.FieldInfo;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.common.StringMap;
import sch.frog.kit.common.util.StringUtil;
import sch.frog.kit.exception.GlobalExceptionThrower;
import sch.frog.kit.server.HttpServer;
import sch.frog.kit.server.handle.RequestActionBox;
import sch.frog.kit.server.handle.RequestParamInfo;
import sch.frog.kit.server.handle.WebContainer;
import sch.frog.kit.util.ClipboardUtil;

import java.lang.reflect.Method;
import java.util.List;

public class ServerManageView extends CustomViewControl {

    @FXML
    private TextField pathText;

    @FXML
    private TextField portText;

    private final HttpServer httpServer = new HttpServer();

    private final EventHandler<? super MouseEvent> eventHandler = event -> {
        String contextPath = httpServer.getContextPath();
        if(contextPath.endsWith("/")){
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        ClipboardUtil.putToClipboard(contextPath + ((Hyperlink) event.getSource()).getText());
    };

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
    private FlowPane requestUrlPane;

    @Override
    public void afterLoad(ApplicationContext context) {
        try {
            this.initHttpServer(context.getViews());
        }catch (Exception e){
            GlobalExceptionThrower.throwException(e);
        }
    }

    private void initHttpServer(List<CustomViewControl> views) throws Exception {
        httpServer.init(views);
        this.showActions();
        this.appendExternalActions(views);
    }

    private void appendExternalActions(List<CustomViewControl> views) throws NoSuchMethodException {
        ObservableList<Node> requestUrlBox = requestUrlPane.getChildren();
        Method method = ExternalViewStruct.class.getMethod(ExternalViewStruct.EXECUTE_FUNCTION_NAME, StringMap.class);
        for (CustomViewControl view : views) {
            if(view instanceof ExternalView){
                ExternalViewStruct viewStruct = ((ExternalView) view).getViewStruct();
                RequestParamInfo[] params = new RequestParamInfo[]{
                        RequestParamInfo.Builder.newBuilder()
                                .setName(WebContainer.BODY_MARK)
                                .setDescription("external request body")
                                .setRequired(true)
                                .setType(StringMap.class)
                                .build()
                };
                String path = "/external/" + viewStruct.getViewName();
                RequestActionBox actionBox = RequestActionBox.Builder.newBuilder()
                        .setInstanceObj(viewStruct)
                        .setDescription(viewStruct.getDescription())
                        .setMethod(method)
                        .setPath(path)
                        .setParams(params)
                        .build();
                httpServer.addRequestAction(actionBox);

                // 将请求参数追加到窗体中
                TitledPane pane = new TitledPane();
                pane.setText(viewStruct.getDescription());

                VBox vBox = new VBox();
                pane.setContent(vBox);

                vBox.setAlignment(Pos.CENTER_LEFT);
                ObservableList<Node> children = vBox.getChildren();

                Hyperlink hyperlink = new Hyperlink();
                hyperlink.setText(path);
                hyperlink.setOnMouseClicked(eventHandler);
                children.add(hyperlink);

                List<FieldInfo> inputField = viewStruct.getInputField();
                if(inputField != null){
                    for (FieldInfo fieldInfo : inputField) {
                        children.add(new Label(fieldInfo.getName() + "(String) -- " + fieldInfo.getDescription()));
                    }
                }

                requestUrlBox.add(pane);
            }
        }
    }

    private void showActions(){
        List<RequestActionBox> actions = httpServer.getActions();

        ObservableList<Node> requestUrlBox = requestUrlPane.getChildren();
        for (RequestActionBox act : actions) {
            TitledPane pane = new TitledPane();
            pane.setText(act.getDescription());

            VBox vBox = new VBox();
            pane.setContent(vBox);

            vBox.setAlignment(Pos.CENTER_LEFT);
            ObservableList<Node> children = vBox.getChildren();

            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setText(act.getPath());
            hyperlink.setOnMouseClicked(eventHandler);
            children.add(hyperlink);

            RequestParamInfo[] params = act.getParams();
            for (RequestParamInfo param : params) {
                children.add(new Label(param.getName() + "(" + param.getType().getSimpleName() + ") -- " + param.getDescription()));
            }

            requestUrlBox.add(pane);
        }
    }
}
