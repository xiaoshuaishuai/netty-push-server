消息推送服务器
=

__场景__:
服务端推送消息至Android、ios等客户端,可以用于文字直播,新闻推送等等。仅学习研究使用,还缺失很多东西。

__涉及点__:
1.netty自定义编解码
2.单端口支持自定义协议、ws协议、http协议
3.netty心跳
4.protobuf序列化
5.消息广播


                                      -----------
                          ---ws----- |web控制界面 |
                         |            -----------
                         |
                   ------------
              ----| push server| ---   
             |     ------------    |
             t                     t
             c                     c
             p                     p
             |                     |
             |                     |
          --------           -------- 
         | client |         | client | 
          --------           --------  
          
          
**server启动**
**client1启动连接server**
**client2启动连接server**
**消息推送管理界面**
**server收到消息转发**
**client接收到消息**

                                  