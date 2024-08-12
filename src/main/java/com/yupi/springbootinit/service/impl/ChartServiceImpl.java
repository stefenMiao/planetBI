package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.mapper.ChartMapper;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author steafen
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-08-05 17:21:34
*/
@Slf4j
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

    /**
     * 构建用户输入字符串，用于描述图表分析的需求和数据
     *
     * @param goal 分析目标
     * @param chartType 图表类型，如果提供将尝试使用指定类型
     * @param csvData 原始数据，用于生成图表
     * @return 构建的用户输入字符串
     */
    @Override
    public String buildUserInput(String goal, String chartType, String csvData) {
        // 使用StringBuilder高效拼接字符串
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求").append("\n");
        // 根据是否提供了图表类型，构建用户目标描述
        String userGoal = StringUtils.isNotBlank(chartType) ? goal + "，请使用" + chartType : goal;
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }



    /**
     * 处理图表更新失败的情况
     * 当图表更新操作由于某种原因失败时，此方法被调用记录错误信息
     *
     * @param chartId     图表的唯一标识符
     * @param execMessage 执行过程中产生的错误信息
     */
    @Override
    public void handleChartUpdateError(long chartId, String execMessage) {
        // 创建一个表示更新失败的图表对象
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage(execMessage);

        // 尝试更新图表的状态
        boolean updateResult = this.updateById(updateChartResult);

        // 如果更新操作失败，则记录错误日志
        if (!updateResult) {
            log.error("更新图表失败 updateChartResult={}", updateChartResult);
        }
    }
}





