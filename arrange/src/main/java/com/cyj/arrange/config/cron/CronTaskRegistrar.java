package com.cyj.arrange.config.cron;

import com.cyj.arrange.Application;
import com.cyj.arrange.feign.DataxClient;
import com.cyj.arrange.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class CronTaskRegistrar implements DisposableBean {

    @Autowired
    private TaskScheduler taskScheduler;

    private Map<Integer,ScheduledFuture> scheduledTasks = new ConcurrentHashMap<>();

    public void addDataxTask(Integer taskID,final String json, String cronExpression) {
        removeCronTask(taskID);
        CronTask cronTask = new CronTask(()->{
            /*if (JwtTokenUtil.validate(json)) {
                ApplicationContext context = Application.getContext();
                DataxClient client = context.getBean(DataxClient.class);
                client.exec(json);
            }*/
            log.info(taskID+":"+cronExpression);
        },cronExpression);
        ScheduledFuture future = scheduleCronTask(cronTask);
        scheduledTasks.put(taskID,future);
    }

    public void removeCronTask(Integer taskID) {
        ScheduledFuture future = this.scheduledTasks.remove(taskID);
        if (future != null){
            future.cancel(false);
        }
    }


    public ScheduledFuture scheduleCronTask(CronTask cronTask) {
        return this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
    }

    @Override
    public void destroy(){
        for (ScheduledFuture future : this.scheduledTasks.values()) {
            future.cancel(false);
        }
        this.scheduledTasks.clear();
    }
}
