package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息队列初始化主类
 * 该类负责初始化RabbitMQ的连接、通道、交换机和队列  只用在程序启动前执行一次
 */
public class BiInitMain {
    /**
     * 程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            // 设置RabbitMQ服务器地址
            factory.setHost("localhost");
            // 基于工厂创建连接
            Connection connection=factory.newConnection();
            // 基于连接创建通道
            Channel channel=connection.createChannel();
            // 定义交换机名称
            String EXCHANGE_NAME=BiMqConstant.BI_EXCHANGE_NAME;
            // 声明交换机，如果不存在则创建，类型为direct
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            // 定义队列名称
            String QUEUE_NAME=BiMqConstant.BI_QUEUE_NAME;
            // 声明队列，如果不存在则创建，设置为持久化
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            // 将队列绑定到交换机，指定路由键为my_routingKey
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,BiMqConstant.BI_ROUTING_KEY);
        }catch (Exception e){
            // 异常处理，实际使用时应添加具体的异常处理逻辑
        }
    }
}
