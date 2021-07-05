package com.cyj.datax.controller;

import com.cyj.datax.processor.MonitorProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chengyajie on 2021/6/8.
 */
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    MonitorProcessor monitorProcessor;

    @Autowired()
    ThreadPoolTaskExecutor executor;
    @RequestMapping("/stream")
    public String streamIO(@RequestParam("job") String path)
    {
        monitorProcessor.process(path);
        return "OK";
    }
}
