package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author steafen
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-08-05 17:21:34
*/
public interface ChartService extends IService<Chart> {

    String buildUserInput(String goal, String chartType, String csvData);

    void handleChartUpdateError(long chartId, String execMessage);
}
