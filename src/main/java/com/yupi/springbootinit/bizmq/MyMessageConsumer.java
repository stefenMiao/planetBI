package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 自定义消息消费者
 * 用于处理来自RabbitMQ的消息
 */
@Component
@Slf4j
public class MyMessageConsumer {

    /**
     * 监听RabbitMQ的"code_queue"队列以接收消息
     * 该方法被RabbitListener注解标记，表示它将被Spring Rabbit自动调用，以处理队列中的消息
     * 使用手动确认模式("MANUAL")来确保消息在被正确处理后才从队列中移除
     *
     * @param message 消费者接收到的消息内容
     * @param channel RabbitMQ的通道对象，用于与RabbitMQ服务器交互
     * @param deliveryTag 用于确认消息的标记，确保消息可以被正确确认
     * @throws Exception 如果处理消息时发生异常，可以手动确认或拒绝消息
     */
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"},ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receive message: {}", message);
        channel.basicAck(deliveryTag, false);
    }
}

