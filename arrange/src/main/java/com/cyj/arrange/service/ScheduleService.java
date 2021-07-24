package com.cyj.arrange.service;

import com.cyj.arrange.Application;
import com.cyj.arrange.bean.vo.TCfgPipelineTaskVO;
import com.cyj.arrange.config.cron.CronTaskRegistrar;
import com.cyj.arrange.entry.*;
import com.cyj.arrange.feign.DataxClient;
import com.cyj.arrange.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@EnableScheduling
@Slf4j
public class ScheduleService {

    @Autowired
    TCfgTaskMapper tCfgTaskMapper;

    @Autowired
    TCfgPipelineMapper cfgPipelineMapper;

    @Autowired
    TLogDataxMapper logDataxMapper;

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    DataxClient dataxClient;
    /**
     *  启动流水线
     */
    public void startPipeline(Integer pipelineID)
    {
        TCfgPipeline pipeline = cfgPipelineMapper.selectById(pipelineID);
        if (pipeline.getStatus())
        {
            List<TCfgPipelineTaskVO> tasks = tCfgTaskMapper.findTaskByPipelineID(pipelineID);
            if (tasks!=null&&tasks.size()>0)
            {
                dataxClient.exec(tasks.get(0).getSeqId(),tasks.get(0).getJsonStr(),tasks.get(0).getPtid());
            }
        }
    }

    public void startTask(Integer taskID)
    {
        TCfgTask task = tCfgTaskMapper.selectById(taskID);
        if (task.getStatus())
        {
            String json = task.getJsonStr();
            dataxClient.exec(taskID,json,-1);
        }
    }

    /**
     * 5分钟更新一次定时任务配置
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void scanScheduleTask()
    {
        if (Application.isLearder())
        {
            log.info("更新定时任务配置");
            cronTaskRegistrar.initScheduleTask();
        }
    }
}
