package sch.frog.kit.server;

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
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.server.handle.IWebView;
import sch.frog.kit.server.handle.RequestActionBox;
import sch.frog.kit.server.handle.WebContainer;
import sch.frog.kit.util.StringUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HttpServer {

    private WebContainer container;

    private static final boolean initialized = false;

    public void init(List<? extends IWebView> views){
        if(initialized){
            throw new IllegalStateException("web container initialized");
        }else{
            HashMap<Class<?>, WebContainer.IRequestConverter> converterMap = new HashMap<>();
            converterMap.put(Date.class, request -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            converterMap.put(LocalDate.class, request ->  {
                try{
                    return LocalDate.parse(request, dateFormatter);
                }catch (DateTimeParseException e){
                    return LocalDate.parse(request, timeFormatter);
                }
            });
            this.container = new WebContainer(converterMap, views);
        }
    }

    private final HttpRequestHandler handler;

    private int port = -1;

    private String contextPath = "/";

    public HttpServer() {
        this.handler = this.initHttpHandler();
    }

    private HttpRequestHandler initHttpHandler(){
        return request -> {
            String path = request.uri();
            HttpMethod method = request.method();
            if(!HttpHeaderValues.APPLICATION_JSON.toString().equals(request.headers().get(HttpHeaderNames.CONTENT_TYPE))){
                return new DefaultFullHttpResponse(request.protocolVersion(),
                        HttpResponseStatus.FORBIDDEN,
                        Unpooled.wrappedBuffer(new ResponseJson(ResponseJson.CODE_FORBIDDEN, "only application/json support")
                                .toJson().getBytes(StandardCharsets.UTF_8)));
            }
            if(!HttpMethod.POST.equals(method)){
                return new DefaultFullHttpResponse(request.protocolVersion(),
                        HttpResponseStatus.FORBIDDEN,
                        Unpooled.wrappedBuffer(new ResponseJson(ResponseJson.CODE_FORBIDDEN, "only post request support")
                                .toJson().getBytes(StandardCharsets.UTF_8)));
            }
            path = path.replaceFirst(HttpServer.this.contextPath, "");
            if(!path.startsWith("/")){
                path = "/" + path;
            }
            path = path.split("\\?", 2)[0];
            String json = new String(ByteBufUtil.getBytes(request.content()), StandardCharsets.UTF_8);
            ResponseJson handleResponse = null;
            try{
                RequestJson requestJson = new RequestJson(json);
                handleResponse = container.handle(path, requestJson);
            }catch (RequestFormatIllegalException e){
                handleResponse = ResponseJson.ERROR;
                LogKit.error(e.getMessage());
            }

            FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),
                    HttpResponseStatus.valueOf(handleResponse.code),
                    Unpooled.wrappedBuffer(handleResponse.toJson().getBytes(StandardCharsets.UTF_8)));
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE).set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            return response;
        };
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
                if(uri.startsWith(contextPath)){
                    response = handler.handle(request);
                }else{
                    response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.NOT_FOUND);
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE).set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
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

    public void setContextPath(String path) {
        if(path == null){ path = "/"; }
        path = path.trim();
        if(StringUtil.isBlank(path)){
            path = "/";
        }
        this.contextPath = path;
    }

    public String getContextPath(){
        return this.contextPath;
    }

    public Collection<RequestActionBox> getActions(){
        return this.container.getActions();
    }
}
