package com.yupi.springbootinit.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息生产者类，用于向MQ（Message Queue）发送消息
 */
@Component
public class MyMessageProducer {
    /**
     * RabbitTemplate用于操作RabbitMQ，发送和接收消息
     */
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定的交换机和路由键
     *
     * @param exchange 交换机名称，用于消息的交换和转发
     * @param routingKey 路由键，用于将消息路由到指定的队列
     * @param message 消息内容，将要发送的消息
     */
    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
