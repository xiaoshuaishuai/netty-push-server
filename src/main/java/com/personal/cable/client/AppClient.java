package com.personal.cable.client;

import com.personal.cable.codec.CableMessageDecoder;
import com.personal.cable.codec.CableMessageEncoder;
import com.personal.cable.handler.common.ExceptionCaughtHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/7 14:56
 * @description:
 */
public class AppClient {
    public static final Bootstrap b = new Bootstrap();
    public static void main(String[] args) {
        EventLoopGroup work = new NioEventLoopGroup();
        b.group(work)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(300, 180, 360, TimeUnit.SECONDS));
                        pipeline.addLast(new CableMessageEncoder());
                        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
                        pipeline.addLast(new CableMessageDecoder());
                        pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
                        pipeline.addLast(new AppClientHandler());
                        pipeline.addLast(new ExceptionCaughtHandler());
                    }
                });
        try {
            ChannelFuture f = b.connect("127.0.0.1", 9999).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
        }
    }
    public static void connect() {
        b.connect("127.0.0.1", 9999).addListener((ChannelFutureListener) future -> {
            if(null != future.cause()){
                System.out.println("连接失败==========="+future.toString());
            }
        });
    }
}
