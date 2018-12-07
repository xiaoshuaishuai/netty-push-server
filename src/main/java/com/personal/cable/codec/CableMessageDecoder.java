package com.personal.cable.codec;

import com.google.protobuf.MessageLite;
import com.personal.cable.body.request.CableRequest;
import com.personal.cable.body.response.CableResponseOuterClass;
import com.personal.cable.common.Command;
import com.personal.cable.common.MessageConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <pre>
 *  数据包格式
 * +——----——-----+——----——+——----——+——-----——+——-------——+——-------——+
 * |协议开始标志(1)| 版本(1)| 类型(2) | 子类型(2)| 数据长度(2)|  数据     |
 * +——----——-----+——----——+——----——+——-----——+——-------——+——-------——+
 * </pre>
 * @author: shuaishuai.xiao
 * @date: 2018/11/27 15:42
 * @description:
 */
@Slf4j
public class CableMessageDecoder extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() > 8) {
            //防止socket字节流攻击
            if(byteBuf.readableBytes() > 2048){
                log.warn("解码数据流过大，忽略->");
                byteBuf.skipBytes(byteBuf.readableBytes());
            }
            //记录包头开始的index
            int beginReader = byteBuf.readerIndex();//获取读指针
            while(true){//循环读取，直到包头读取完毕
                if (byteBuf.readByte() == MessageConstants.MAGIC) {//1
                    break;
                }
            }
            byte version = byteBuf.readByte();//1
            short type = byteBuf.readShort();//2
            short subType = byteBuf.readShort();//2
            short length = byteBuf.readShort();//2
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);//等待后面数据包
                return;
            }
            byte[] data = new byte[(int)length];
            byteBuf.readBytes(data);
            //反序列化
            MessageLite result = decodeBody(type, data, 0, length);
            if(null == result){
                return;
            }
            list.add(result);
        }
    }
    public MessageLite decodeBody(short type, byte[] array, int offset, int length) throws Exception {
        //todo 等待优化
        if (type == Command.LOGIN_REQUEST.getType()) {
            log.info("解码->登录请求");
            return CableRequest.LoginRequest.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        } else if (type == Command.PUSH_REQUEST.getType()) {
            log.info("解码->推送请求");
            return CableRequest.PushRequest.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        } else if (type == Command.GLOBAL_RRESPONSE.getType()) {
            log.info("解码->全局响应");
            return CableResponseOuterClass.CableResponse.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }else if (type == Command.SHAKEHANDS_RRESPONSE.getType()) {
            log.info("解码->心跳响应");
            return CableResponseOuterClass.CableResponse.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }else if (type == Command.SHAKEHANDS_REQUEST.getType()) {
            log.info("解码->心跳请求");
            return CableRequest.HeartbeatRequest.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }
        log.error("type:{},反序列未找到类型对应的类",type);
        return null; // or throw exception
    }

}
