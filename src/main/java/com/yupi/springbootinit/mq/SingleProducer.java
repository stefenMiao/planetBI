package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * 单一生产者类，用于发送消息到RabbitMQ队列
 */
public class SingleProducer {

    // 队列名称常量
    private final static String QUEUE_NAME = "hello";

    /**
     * 主函数，建立RabbitMQ连接并发送消息
     * @param argv 命令行参数
     * @throws Exception 可能抛出的连接和通道操作异常
     */
    public static void main(String[] argv) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ服务器主机名
        factory.setHost("localhost");
        // 以下三行代码可以根据实际需要设置端口、用户名和密码
//        factory.setPort();
//        factory.setUsername();
//        factory.setPassword();

        // 创建连接和通道，并在try-with-resources语句中自动管理资源
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明队列，如果队列不存在将创建它
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 要发送的消息
            String message = "Hello !";
            // 发送消息到指定队列
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            // 输出发送消息的内容
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
