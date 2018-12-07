package com.personal.cable.codec;

import com.google.protobuf.MessageLite;
import com.personal.cable.body.request.CableRequest;
import com.personal.cable.body.response.CableResponseOuterClass;
import com.personal.cable.common.Command;
import com.personal.cable.common.MessageConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *  数据包格式
 * +——----——-----+——----——+——----——+——-----——+——-------——+——-------——+
 * |协议开始标志(1)| 版本(1)| 类型(2) | 子类型(2)| 数据长度(2)|  数据     |
 * +——----——-----+——----——+——----——+——-----——+——-------——+——-------——+
 * </pre>
 * @author: shuaishuai.xiao
 * @date: 2018/11/27 15:41
 * @description:
 */
@Slf4j
public class CableMessageEncoder extends MessageToByteEncoder<MessageLite> {
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageLite messageLite, ByteBuf byteBuf) throws Exception {
        short type;
        //todo 优化
        if (messageLite instanceof CableRequest.LoginRequest) {
            type = Command.LOGIN_REQUEST.getType();
            log.info("编码->登录请求");
        } else if (messageLite instanceof CableRequest.PushRequest) {
            type = Command.PUSH_REQUEST.getType();
            log.info("编码->推送请求");
        }else if (messageLite instanceof CableResponseOuterClass.CableResponse) {
            type = Command.GLOBAL_RRESPONSE.getType();
            log.info("编码->全局响应");
        } else if (messageLite instanceof CableRequest.HeartbeatRequest) {
            type = Command.SHAKEHANDS_REQUEST.getType();
            log.info("编码->心跳请求");
        }
        else {
            type = Command.UNKNOWN.getType();
            log.info("编码->未知请求");
        }
        byte version = 1;
        byteBuf.writeByte(MessageConstants.MAGIC);
        byteBuf.writeByte(version);
        byteBuf.writeShort(type);
        byteBuf.writeShort(0);
        byte[] data = messageLite.toByteArray();
        byteBuf.writeShort(data.length);
        byteBuf.writeBytes(data);
        return;
    }
}
