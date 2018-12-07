package com.personal.cable.handler.tcp;

import com.personal.cable.body.request.CableRequest;
import com.personal.cable.handler.common.PushChannelGroup;
import com.personal.cable.utils.TcpResponseHelper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/27 15:44
 * @description:
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestServerHandler extends SimpleChannelInboundHandler<CableRequest.LoginRequest> {

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CableRequest.LoginRequest loginRequest) throws Exception {
        if (checkLogin(loginRequest.getUname(), loginRequest.getPwd())) {
            channelHandlerContext.writeAndFlush(TcpResponseHelper.success());
            PushChannelGroup.CHANNELGROUP.add(channelHandlerContext.channel());
            log.info(channelHandlerContext.channel().remoteAddress().toString() + "登录成功...");
        } else {
            channelHandlerContext.writeAndFlush(TcpResponseHelper.fail());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        PushChannelGroup.CHANNELGROUP.remove(ctx.channel());
        log.info(ctx.channel().remoteAddress().toString() +"断开连接...");
    }

    /**
     * 功能描述: 校验用户名密码
     *
     * @param: [uname, pwd]
     * @return: boolean
     * @auther: shuaishuai.xiao
     * @date: 2018/11/28 10:56
     */
    private boolean checkLogin(String uname, String pwd) {
        return uname.equals("root") && pwd.equals("123456");
    }
}
