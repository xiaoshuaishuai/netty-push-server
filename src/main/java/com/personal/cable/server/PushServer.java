package com.personal.cable.server;

import com.personal.cable.handler.SwitchPushServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/27 15:38
 * @description: 消息推送服务器
 */
@Slf4j
public class PushServer implements Server {
    public static final String HTTP_HOST = "127.0.0.1";
    public static final int HTTP_PORT = 9999;
    public void start() {
        EventLoopGroup HTTP_BOSS = new NioEventLoopGroup();
        EventLoopGroup HTTP_WORK = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(HTTP_BOSS, HTTP_WORK)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new SwitchPushServerHandler());
                    }
                });
        ChannelFuture f;
        try {
            f = bootstrap.bind(HTTP_HOST, HTTP_PORT).sync();
            log.info("CablePushServer.start === 服务启动,端口:"+HTTP_PORT);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            HTTP_BOSS.shutdownGracefully();
            HTTP_WORK.shutdownGracefully();
        }
    }
}
