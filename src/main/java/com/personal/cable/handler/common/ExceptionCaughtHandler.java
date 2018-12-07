package com.personal.cable.handler.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import javax.sound.midi.Soundbank;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/30 15:29
 * @description:
 */
@Slf4j
public class ExceptionCaughtHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("异常了===");
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
