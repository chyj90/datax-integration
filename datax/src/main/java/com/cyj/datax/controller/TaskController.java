package com.cyj.datax.controller;

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


    @RequestMapping("/stream")
    public String streamIO(@RequestParam("job") String job,@RequestParam("jobID") Long jobID)
    {
        taskExecutor.execute(()->{
            monitorProcessor.process(job,jobID);
        });
        return "OK";
    }
}
