package com.personal.cable.client;

import com.personal.cable.body.request.CableRequest;
import com.personal.cable.utils.TcpRequestHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/27 15:44
 * @description:
 */
@Slf4j
public class AppClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(TcpRequestHelper.buildLoginRequest());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof CableRequest.PushRequest){
            CableRequest.PushRequest pushRequest = (CableRequest.PushRequest) msg;
            log.info("主题:{},作者:{},消息:{}",converChnString(pushRequest.getTitle()),converChnString(pushRequest.getAuthor()),converChnString(pushRequest.getMessage()));
        }else {
            log.info("接收到消息=======:"+msg.toString());
        }

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("连接channelRegistered->");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("断开重连->");
        reconnect(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开->");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state().equals(IdleState.READER_IDLE)) {
                //300s没获取到服务消息....重连
                log.warn("300s没获取到服务消息....重连=======");
                reconnect(ctx);
            } else if (idleStateEvent.state().equals(IdleState.WRITER_IDLE)) {
                //每180s发送一次心跳信息
                log.info("heartbeat request");
                ctx.writeAndFlush(TcpRequestHelper.buildHeartbeatRequest());
            } else if (idleStateEvent.state().equals(IdleState.ALL_IDLE)) {
                //读写事件都没有、有可能客户端下线
                log.error("读写事件无、客户端下线=======");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
    private String converChnString(String src){
        return new String(src.getBytes(), Charset.forName("UTF-8"));
    }
    private void reconnect(ChannelHandlerContext ctx){
        EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> AppClient.connect(), 2L, TimeUnit.SECONDS);
    }
}
