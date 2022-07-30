package sch.frog.kit.server;

import io.netty.bootstrap.ServerBootstrap;
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
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;

public class HttpServer {

    private final HttpRequestHandler handler;

    private int port = -1;

    private String path;

    public HttpServer(HttpRequestHandler handler) {
        this.handler = handler;
    }

    private Channel channel;

    public void start(){
        if(this.port < 0){
            LogKit.error("port not configured");
            return;
        }
        if(this.channel != null){
            LogKit.info("server is running");
            return;
        }
        new Thread(() -> {
            try{
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                EventLoopGroup workerGroup = new NioEventLoopGroup();

                ServerBootstrap serverBootstrap = new ServerBootstrap()
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.ERROR))
                        .childHandler(new HttpServerInitializer());

                channel = serverBootstrap.bind(port).sync().channel();
                LogKit.info("http server start success");
                channel.closeFuture().sync();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }catch (Exception e){
                LogKit.error(e.getMessage());
            }
        }).start();
    }

    public void shutdown(){
        if(channel != null){
            channel.close();
            channel = null;
            LogKit.info("http server closed");
        }
    }

    private void handleRequest(ChannelHandlerContext ctx, Object msg){
        if(msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            try {
                String uri = new URI(request.uri()).getPath();
                HttpResponse response = null;
                if(uri.startsWith(path)){
                    response = handler.handle(request);
                }else{
                    response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.NOT_FOUND);
                    response.headers().set(CONNECTION, CLOSE).set(CONTENT_TYPE, APPLICATION_JSON);
                }
                ctx.writeAndFlush(response);
            } catch (URISyntaxException e) {
                LogKit.error(e.getMessage());
            }finally {
                ctx.close();
            }
        }
    }

    public static final int MAX_CONTENT_LENGTH = 5 * 1024 * 1024;

    private class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new HttpServerCodec());
            p.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
            p.addLast(new HttpServerExpectContinueHandler());
            p.addLast(new HttpServerHandler());
        }
    }

    private class HttpServerHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            handleRequest(ctx, msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LogKit.error("unexpected exception for http server : " + cause.getMessage());
            ctx.close();
        }
    }

    public interface HttpRequestHandler{
        HttpResponse handle(FullHttpRequest request);
    }

    public void setPort(int port) {
        if(port < 5000 || port > 65535){
            LogKit.error("port is illegal, the range is [5000, 65535]");
            return;
        }
        this.port = port;
    }

    public void setPath(String path) {
        if(path == null){ path = "/"; }
        path = path.trim();
        if(StringUtils.isBlank(path)){
            path = "/";
        }
        this.path = path;
    }
}
