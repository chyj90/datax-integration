package com.cyj.arrange.config.cron;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.arrange.entry.TCfgPipeline;
import com.cyj.arrange.mapper.TCfgPipelineMapper;
import com.cyj.arrange.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class CronTaskRegistrar implements DisposableBean {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    TCfgPipelineMapper pipelineMapper;

    private Map<Integer,ScheduledFuture> scheduledTasks = new ConcurrentHashMap<>();

    public void initScheduleTask()
    {
        disposeScheduleTask();
        List<TCfgPipeline> pipelines = pipelineMapper.selectList(new QueryWrapper<TCfgPipeline>().eq("status",true));
        pipelines.forEach(this::addPipelineSchedule);
    }

    public void disposeScheduleTask()
    {
        for (ScheduledFuture future:scheduledTasks.values())
        {
            if (future!=null)
            {
                future.cancel(false);
            }
        }
        scheduledTasks.clear();
    }

    public void addPipelineSchedule(TCfgPipeline cfgPipeline) {
        removeCronTask(cfgPipeline.getSeqId());
        CronTask cronTask = new CronTask(()->{
            scheduleService.startPipeline(cfgPipeline.getSeqId());
        },cfgPipeline.getCron());
        ScheduledFuture future = scheduleCronTask(cronTask);
        scheduledTasks.put(cfgPipeline.getSeqId(),future);
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
