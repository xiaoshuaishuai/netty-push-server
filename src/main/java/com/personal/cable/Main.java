package com.personal.cable;

import com.personal.cable.server.PushServer;

/**
 * @author: shuaishuai.xiao
 * @date: 2018/12/6 15:30
 * @description:
 */
public class Main {
    public static void main(String[] args) {
        PushServer server = new PushServer();
        server.start();
    }
}
