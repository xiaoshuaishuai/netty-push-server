package com.personal.cable.utils;

import com.personal.cable.body.response.CableResponseOuterClass;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/11/27 21:21
 * @description:  服务器返回处理
 */
public class TcpResponseHelper {
    /**
     * 功能描述: success
     * @param: []
     * @return: com.personal.cable.body.response.CableResponseOuterClass.CableResponse
     * @auther: shuaishuai.xiao
     * @date: 2018/11/27 21:23
     */
    public static CableResponseOuterClass.CableResponse success(){
        return CableResponseOuterClass.CableResponse.newBuilder().setCode(200).setMessage("success").build();
    }
    public static CableResponseOuterClass.CableResponse success(int command){
        return CableResponseOuterClass.CableResponse.newBuilder().setCode(200).setMessage("success").setCommand(command).build();
    }
    /**
     * 功能描述: fail
     * @param: []
     * @return: com.personal.cable.body.response.CableResponseOuterClass.CableResponse
     * @auther: shuaishuai.xiao
     * @date: 2018/11/27 21:25
     */
    public static CableResponseOuterClass.CableResponse fail(){
        return CableResponseOuterClass.CableResponse.newBuilder().setCode(500).setMessage("fail").build();
    }
}
