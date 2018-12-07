package com.personal.cable.utils;

import com.personal.cable.model.bo.WsPushBO;
import com.personal.cable.body.request.CableRequest;
import com.personal.cable.common.Command;

import java.util.UUID;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/12/5 11:08
 * @description:
 */
public class TcpRequestHelper {
    /**
     * 功能描述: 登录请求参数
     * @param: [17612186902]
     * @return: com.personal.cable.body.request.CableRequest.LoginRequest
     * @auther: shuaishuai.xiao
     * @date: 2018/12/5 11:44
     */
    public static CableRequest.LoginRequest buildLoginRequest() {
        CableRequest.LoginRequest.Builder builder = CableRequest.LoginRequest.newBuilder();
        builder.setUid(UUID.randomUUID().toString());
        builder.setCreateTime(System.currentTimeMillis());
        builder.setCommand(Command.LOGIN_REQUEST.getType());
        builder.setUname("root");
        builder.setPwd("123456");
        return builder.build();
    }
    /**
     * 功能描述: 推送通知请求
     * @param: [wsPushBO]
     * @return: com.personal.cable.body.request.CableRequest.PushRequest
     * @auther: shuaishuai.xiao
     * @date: 2018/12/5 11:45
     */
    public static CableRequest.PushRequest buildPushRequest(WsPushBO wsPushBO){
        CableRequest.PushRequest.Builder builder = CableRequest.PushRequest.newBuilder();
        builder.setTitle(wsPushBO.getTitle());
        builder.setMessage(wsPushBO.getMessage());
        builder.setAuthor(wsPushBO.getAuthor());
        builder.setUid(UUID.randomUUID().toString());
        builder.setCreateTime(System.currentTimeMillis());
        builder.setCommand(Command.PUSH_REQUEST.getType());
        return builder.build();
    }    
    /**
     * 功能描述: 心跳请求
     * @param: []
     * @return: com.personal.cable.body.request.CableRequest.HeartbeatRequest
     * @auther: shuaishuai.xiao
     * @date: 2018/12/5 11:45
     */
    public static CableRequest.HeartbeatRequest buildHeartbeatRequest(){
        CableRequest.HeartbeatRequest.Builder builder = CableRequest.HeartbeatRequest.newBuilder();
        builder.setMessage("ping");
        builder.setUid(UUID.randomUUID().toString());
        builder.setCreateTime(System.currentTimeMillis());
        builder.setCommand(Command.SHAKEHANDS_REQUEST.getType());
        return builder.build();
    }
}
