消息推送服务
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
          
          
** 数据包格式 **  
 
+——----——-----+——----——+——----——+——-----——+——-------——+——------------------——+  
|协议开始标志(1)| 版本(1)| 类型(2) | 子类型(2)| 数据长度(2)|  数据(protobuf序列化) |  
+——----——-----+——----——+——----——+——-----——+——-------——+——------------------——+  
  
**server启动**  
![server启动](https://github.com/xiaoshuaishuai/netty-push-server/blob/master/img/1.jpg)  
**client1启动连接server**  
![client1启动连接server](https://github.com/xiaoshuaishuai/netty-push-server/blob/master/img/2.jpg)  
**client2启动连接server**  
![client2启动连接server](https://github.com/xiaoshuaishuai/netty-push-server/blob/master/img/3.jpg)  
**消息推送管理界面**
![消息推送管理界面](https://github.com/xiaoshuaishuai/netty-push-server/blob/master/img/4.jpg)  
**server收到消息转发**
![server收到消息转发](https://github.com/xiaoshuaishuai/netty-push-server/blob/master/img/5.jpg)  
**client接收到消息**  
![client接收到消息](https://github.com/xiaoshuaishuai/netty-push-server/blob/master/img/6.jpg)  
                                  