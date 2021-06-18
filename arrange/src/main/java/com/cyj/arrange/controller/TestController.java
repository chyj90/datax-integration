package com.cyj.arrange.controller;

import com.cyj.arrange.config.cron.CronTaskRegistrar;
import com.cyj.arrange.feign.DataxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by chengyajie on 2021/6/8.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;
    @Autowired
    DataxClient dataxClient;
    @RequestMapping("/exec")
    public String exec()
    {
        return dataxClient.exec("job/testjob.json");
    }

    @RequestMapping("/str")
    public String str()
    {
        return "OK";
    }

    @RequestMapping("/addTask")
    public String addTask(String id,String cron)
    {
        Integer iid = Integer.parseInt(id);
        cronTaskRegistrar.addDataxTask(iid,"{'a':1}",cron);
        return "OK";
    }

    @RequestMapping("/removeTask")
    public String removeTask(String id)
    {
        cronTaskRegistrar.removeCronTask(Integer.parseInt(id));
        return "OK";
    }
}
