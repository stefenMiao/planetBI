package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.AiManagerSpark;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.utils.ExcelUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义消息消费者
 * 用于处理来自RabbitMQ的消息
 */
@Component
@Slf4j
public class BiMessageConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManagerSpark aiManagerSpark;



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
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME},ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receive message: {}", message);
        if(StringUtils.isBlank(message)){
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }

        long chartId=Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表不存在");
        }

        //把图表状态设置为执行中
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            chartService.handleChartUpdateError(chart.getId(), "更新图表状态为running时失败");
            return;
        }
        String goal=chart.getGoal();
        String chartType=chart.getChartType();
        String csvData=chart.getChartData();
        String userInput=chartService.buildUserInput(goal, chartType, csvData);


        String result = aiManagerSpark.sendMesToAIUseXingHuo(userInput);
        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            channel.basicNack(deliveryTag, false, false);
            chartService.handleChartUpdateError(chart.getId(), "AI生成错误");
            return;
        }
        String genChart = splits[1].trim();
        //去掉生成图表的代码前后的单引号
        if (genChart.startsWith("'") && genChart.endsWith("'")) {
            genChart = genChart.substring(1, genChart.length() - 1);
        }
        //去掉结果前后的单引号
        String genResult = splits[2].trim();
        if (genResult.startsWith("'") ) {
            genResult = genResult.substring(1, genResult.length() );
        }
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        updateChartResult.setStatus("succeed");
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            channel.basicNack(deliveryTag, false, false);
            chartService.handleChartUpdateError(chart.getId(), "更新图表状态为succeed时失败");
        }

        channel.basicAck(deliveryTag, false);
    }

}

