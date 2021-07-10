package com.cyj.arrange.service;

import com.cyj.arrange.Application;
import com.cyj.arrange.bean.vo.TCfgPipelineTaskVO;
import com.cyj.arrange.config.cron.CronTaskRegistrar;
import com.cyj.arrange.entry.TCfgPipeline;
import com.cyj.arrange.entry.TCfgTask;
import com.cyj.arrange.entry.TLogPipeline;
import com.cyj.arrange.entry.TLogTask;
import com.cyj.arrange.feign.DataxClient;
import com.cyj.arrange.mapper.TCfgPipelineMapper;
import com.cyj.arrange.mapper.TCfgTaskMapper;
import com.cyj.arrange.mapper.TLogPipelineMapper;
import com.cyj.arrange.mapper.TLogTaskMapper;
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
    TLogPipelineMapper tLogPipelineMapper;

    @Autowired
    TLogTaskMapper tLogTaskMapper;

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    DataxClient dataxClient;
    /**
     *  启动流水线
     */
    @Transactional
    public void startPipeline(Integer pipelineID)
    {
        TCfgPipeline pipeline = cfgPipelineMapper.selectById(pipelineID);
        if (pipeline.getStatus())
        {
            List<TCfgPipelineTaskVO> tasks = tCfgTaskMapper.findTaskByPipelineID(pipelineID);
            if (tasks!=null&&tasks.size()>0)
            {
                Date startTime = new Date();
                TLogPipeline pipelineLog = new TLogPipeline();
                pipelineLog.setPipelineId(pipelineID).setStartTime(startTime).setStatus(false);
                tLogPipelineMapper.insert(pipelineLog);
                for (TCfgTask task:tasks)
                {
                    TLogTask taskLog = new TLogTask();
                    taskLog.setPipelineLogId(pipelineLog.getSeqId()).setTaskId(task.getSeqId());
                    tLogTaskMapper.insert(taskLog);
                }
            }
        }
    }

    public void startTask(Integer taskID)
    {
        TCfgTask task = tCfgTaskMapper.selectById(taskID);
        if (task.getStatus())
        {
            String json = task.getJsonStr();
            dataxClient.exec(taskID,json);
        }
    }

    /**
     * 1点清除半年历史数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void clearHalfYearHistory()
    {
        Date startTime = DateUtils.addMonths(new Date(),-6);
        String param = DateFormatUtils.format(startTime,"yyyy-MM-dd HH:mm:ss");
        tLogTaskMapper.deleteTaskHalfYear(param);
        tLogPipelineMapper.deletePipelineHalfYear(param);
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
