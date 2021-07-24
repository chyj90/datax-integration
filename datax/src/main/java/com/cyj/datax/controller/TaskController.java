package com.cyj.datax.controller;

import com.cyj.datax.entry.TLogDatax;
import com.cyj.datax.mapper.TLogDataxMapper;
import com.cyj.datax.processor.MonitorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by chengyajie on 2021/6/8.
 */
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    MonitorProcessor monitorProcessor;

    @Qualifier("taskExecutor")
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    TLogDataxMapper logDataxMapper;

    @RequestMapping("/stream")
    public String streamIO(@RequestParam("taskID") Integer taskID,@RequestParam("job") String job,@RequestParam("relID") Integer relID)
    {
        taskExecutor.execute(()->{
            TLogDatax logDatax = new TLogDatax().setTaskId(taskID).setExecTime(new Date());
            if (relID!=null&&relID>0)
            {
                logDatax.setCfgPipelineTaskId(relID);
            }
            logDataxMapper.insert(logDatax);
            monitorProcessor.process(job,logDatax.getSeqId());
        });
        return "OK";
    }
}
