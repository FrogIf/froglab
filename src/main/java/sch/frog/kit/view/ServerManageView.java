package sch.frog.kit.view;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sch.frog.kit.MainController;
import sch.frog.kit.server.RequestFormatIllegalException;
import sch.frog.kit.server.RequestJson;
import sch.frog.kit.server.ResponseJson;
import sch.frog.kit.util.StringUtils;

import java.net.URI;
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
        if(portNum < 5000 || portNum > 65535){
            MainController.error("port is illegal, the range is [5000, 65535]");
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

        startHttpServer(portNum, path);
    }

    @FXML
    public void stopServer(){
        pathText.setEditable(true);
        portText.setEditable(true);
    }

    private void startHttpServer(int port, String path) {
        new Thread(() -> {
            try{
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.ERROR))
                        .childHandler(new HttpServerInitializer());

                Channel channel = serverBootstrap.bind(port).sync().channel();
                channel.closeFuture().sync();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }catch (Exception e){
                MainController.error(e.getMessage());
            }
        }).start();
    }

    public static final int MAX_CONTENT_LENGTH = 5 * 1024 * 1024;

    private static class HttpServerInitializer extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new HttpServerCodec());
            p.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
            p.addLast(new HttpServerExpectContinueHandler());
            p.addLast(new HttpServerHandler());
        }
    }

    private static class HttpServerHandler extends ChannelInboundHandlerAdapter {
        private String path = "/";

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if(msg instanceof FullHttpRequest){
                FullHttpRequest request = (FullHttpRequest) msg;
                try{
                    String uri = new URI(request.uri()).getPath();
                    if(uri.startsWith(path)){
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
                        ctx.writeAndFlush(response);
                    }
                }finally {
                    ctx.close();
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            MainController.error("unexpected exception for http server : " + cause.getMessage());
            ctx.close();
        }
    }



}
