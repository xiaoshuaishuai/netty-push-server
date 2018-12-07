package com.personal.cable.handler.tcp;

import com.personal.cable.body.request.CableRequest;
import com.personal.cable.common.Command;
import com.personal.cable.utils.TcpResponseHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/27 15:44
 * @description:
 */
@Slf4j
public class HeartbeatRequestServerHandler extends SimpleChannelInboundHandler<CableRequest.HeartbeatRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CableRequest.HeartbeatRequest msg) throws Exception {
        ctx.writeAndFlush(TcpResponseHelper.success(Command.SHAKEHANDS_RRESPONSE.getType()));
        log.info("heartbeat response");
    }
}
