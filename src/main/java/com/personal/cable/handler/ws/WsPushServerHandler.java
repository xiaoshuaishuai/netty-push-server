package com.personal.cable.handler.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.personal.cable.model.bo.WsPushBO;
import com.personal.cable.handler.common.PushChannelGroup;
import com.personal.cable.utils.TcpRequestHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/12/1 11:52
 * @description:
 */
@Slf4j
public class WsPushServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ws协议============");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
//        String text = channel.remoteAddress() + ": " +msg.text();
        String text = msg.text();
        log.info("消息原始内容:{}",text);
        WsPushBO wsPushBO =JSON.parseObject(text,new TypeReference<WsPushBO>(){});
        log.info("消息转换之后内容:{}",wsPushBO);
        ctx.channel().eventLoop().submit(() -> wirteToWs(text));
        ctx.channel().eventLoop().submit(() -> wirteToTcp(wsPushBO));

        log.info("channelgroup:{}",PushChannelGroup.CHANNELGROUP.toString());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("用户上线: " + ctx.channel().id().asLongText());
        PushChannelGroup.CHANNELGROUP.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("用户下线: " + ctx.channel().id().asLongText());
        PushChannelGroup.CHANNELGROUP.remove(ctx.channel());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    private void wirteToWs(String text){
        log.info("转发至ws协议======");

        PushChannelGroup.CHANNELGROUP.writeAndFlush(new TextWebSocketFrame(LocalTime.now().toString() + ">>>>推送至客户端信息=======:"+text));
    }
    private void wirteToTcp(WsPushBO wsPushBO){
        log.info("转发至tcp协议======");
        PushChannelGroup.CHANNELGROUP.writeAndFlush(TcpRequestHelper.buildPushRequest(wsPushBO));
    }
}
