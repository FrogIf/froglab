package sch.frog.kit.view;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sch.frog.kit.MainController;
import sch.frog.kit.server.HttpServer;
import sch.frog.kit.server.RequestFormatIllegalException;
import sch.frog.kit.server.RequestJson;
import sch.frog.kit.server.ResponseJson;
import sch.frog.kit.util.StringUtils;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;

public class ServerManageView extends CustomViewControl {

    @FXML
    private TextField pathText;

    @FXML
    private TextField portText;

    private final HttpServer httpServer = new HttpServer(request -> {
        String json = new String(ByteBufUtil.getBytes(request.content()));
        ResponseJson handleResponse = null;
        try{
            RequestJson requestJson = new RequestJson(json);

            // TODO handler mapping
            handleResponse = ResponseJson.SUCCESS;
        }catch (RequestFormatIllegalException e){
            handleResponse = ResponseJson.ERROR;
            MainController.error(e.getMessage());
        }

        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
                handleResponse.ok ? HttpResponseStatus.OK : HttpResponseStatus.INTERNAL_SERVER_ERROR,
                Unpooled.wrappedBuffer(handleResponse.toJson().getBytes(StandardCharsets.UTF_8)));
        response.headers().set(CONNECTION, CLOSE).set(CONTENT_TYPE, APPLICATION_JSON);
        return response;
    });

    @FXML
    public void startServer(){
        String port = portText.getText();
        if(StringUtils.isBlank(port)){
            MainController.error("port is required.");
            return;
        }
        int portNum = 0;
        try{
            portNum = Integer.parseInt(port);
        }catch (NumberFormatException e){
            MainController.error("port is not a number.");
            return;
        }

        String path = pathText.getText();
        if(path == null){ path = "/"; }
        path = path.trim();
        if(StringUtils.isBlank(path)){
            path = "/";
        }
        pathText.setText(path);
        pathText.setEditable(false);
        portText.setEditable(false);

        httpServer.setPort(portNum);
        httpServer.setPath(path);

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
}
