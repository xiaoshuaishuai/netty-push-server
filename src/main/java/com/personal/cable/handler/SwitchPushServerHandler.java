package com.personal.cable.handler;

import com.personal.cable.codec.CableMessageDecoder;
import com.personal.cable.codec.CableMessageEncoder;
import com.personal.cable.common.MessageConstants;
import com.personal.cable.handler.common.ExceptionCaughtHandler;
import com.personal.cable.handler.http.HttpTimeServerHandler;
import com.personal.cable.handler.tcp.HeartbeatRequestServerHandler;
import com.personal.cable.handler.tcp.LoginRequestServerHandler;
import com.personal.cable.handler.ws.WsPushServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/12/3 19:00
 * @description:  协议分发器
 */
@Slf4j
public class SwitchPushServerHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() > 2){
            int readerIndex = in.readerIndex();
            final byte magic1 = in.readByte();
            final byte magic2 = in.readByte();
            if(isHttp(magic1,magic2)){
                in.readerIndex(readerIndex);
                switchToHttp(ctx);
            }else if(isPrivately(magic1)){
                in.readerIndex(readerIndex);
                switchToPrivately(ctx);
            }else {
                log.error("Unknown protocol; discard everything and close the connection.");
                in.clear();
                ctx.close();
            }
        }
    }
    /**
     * 功能描述: http & ws协议支持
     * @param: [ctx]
     * @return: void
     * @auther: shuaishuai.xiao
     * @date: 2018/12/6 14:36
     */
    private void switchToHttp(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new WsPushServerHandler());
        pipeline.addLast(new HttpTimeServerHandler());
        pipeline.addLast(new ExceptionCaughtHandler());
        pipeline.remove(this);
    }
    /**
     * 功能描述: 私有协议
     * @param: [ctx]
     * @return: void
     * @auther: shuaishuai.xiao
     * @date: 2018/12/6 14:36
     */
    private void switchToPrivately(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast(new CableMessageEncoder());
        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
        pipeline.addLast(new CableMessageDecoder());
        pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast(new LoginRequestServerHandler());
        pipeline.addLast(new HeartbeatRequestServerHandler());
        pipeline.addLast(new ExceptionCaughtHandler());
        pipeline.remove(this);
    }
    /**
     * 功能描述: 是否http协议
     * @param: [magic1, magic2]
     * @return: boolean
     * @auther: shuaishuai.xiao
     * @date: 2018/12/6 14:38
     */
    private static boolean isHttp(byte magic1, byte magic2) {
        return
                magic1 == 'G' && magic2 == 'E' || // GET
                        magic1 == 'P' && magic2 == 'O' || // POST
                        magic1 == 'P' && magic2 == 'U' || // PUT
                        magic1 == 'H' && magic2 == 'E' || // HEAD
                        magic1 == 'O' && magic2 == 'P' || // OPTIONS
                        magic1 == 'P' && magic2 == 'A' || // PATCH
                        magic1 == 'D' && magic2 == 'E' || // DELETE
                        magic1 == 'T' && magic2 == 'R' || // TRACE
                        magic1 == 'C' && magic2 == 'O';   // CONNECT
    }
    /**
     * 功能描述: 是否私有协议
     * @param: [magic1]
     * @return: boolean
     * @auther: shuaishuai.xiao
     * @date: 2018/12/6 14:39
     */
    private static boolean isPrivately(byte magic1) {
        return magic1 == MessageConstants.MAGIC;
    }
}
