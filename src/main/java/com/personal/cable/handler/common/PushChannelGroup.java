package com.personal.cable.handler.common;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/12/4 17:21
 * @description:
 */
public class PushChannelGroup {
    public static final ChannelGroup CHANNELGROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
