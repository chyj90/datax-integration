package com.cyj.arrange.config.cron;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.arrange.entry.TCfgPipeline;
import com.cyj.arrange.entry.TCfgTask;
import com.cyj.arrange.mapper.TCfgPipelineMapper;
import com.cyj.arrange.mapper.TCfgTaskMapper;
import com.cyj.arrange.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    TCfgTaskMapper taskMapper;

    private final String PIPELINE_PREFIX = "P_";

    private final String TASK_PREFIX = "T_";

    private Set<String> scheduleKeys = new HashSet<>();

    private Map<String,ScheduledFuture> scheduledTasks = new ConcurrentHashMap<>();

    public void initScheduleTask()
    {
        scheduleKeys.clear();
        List<TCfgPipeline> pipelines = pipelineMapper.selectList(new QueryWrapper<TCfgPipeline>().eq("status",true));
        pipelines.forEach(this::addPipelineSchedule);
        List<TCfgTask> tasks = taskMapper.selectList(new QueryWrapper<TCfgTask>().eq("status",true));
        tasks.forEach(this::addTaskSchedule);
        this.disposeScheduleTask();
    }

    public void disposeScheduleTask()
    {
        Set<String> keySet = new HashSet<>();
        keySet.addAll(scheduledTasks.keySet());
        for (String key:keySet)
        {
            if (!scheduleKeys.contains(key))
            {
                this.removeCronTask(key);
            }
        }
    }

    public void addPipelineSchedule(TCfgPipeline cfgPipeline) {
        if (cfgPipeline.getStatus()&& StringUtils.isNotEmpty(cfgPipeline.getCron()))
        {
            if (!scheduledTasks.containsKey(PIPELINE_PREFIX+cfgPipeline.getSeqId()))
            {
                CronTask cronTask = new CronTask(()->{
                    scheduleService.startPipeline(cfgPipeline.getSeqId());
                },cfgPipeline.getCron());
                ScheduledFuture future = scheduleCronTask(cronTask);
                scheduledTasks.put(PIPELINE_PREFIX+cfgPipeline.getSeqId(),future);
            }
            scheduleKeys.add(PIPELINE_PREFIX+cfgPipeline.getSeqId());
        }
    }

    public void addTaskSchedule(TCfgTask cfgTask) {
        if (cfgTask.getStatus()&& StringUtils.isNotEmpty(cfgTask.getCron()))
        {
            if (!scheduledTasks.containsKey(TASK_PREFIX+cfgTask.getSeqId()))
            {
                CronTask cronTask = new CronTask(()->{
                    scheduleService.startTask(cfgTask.getSeqId());
                },cfgTask.getCron());
                ScheduledFuture future = scheduleCronTask(cronTask);
                scheduledTasks.put(TASK_PREFIX+cfgTask.getSeqId(),future);
            }
            scheduleKeys.add(TASK_PREFIX+cfgTask.getSeqId());
        }
    }

    public void removeCronTask(String taskKey) {
        ScheduledFuture future = this.scheduledTasks.remove(taskKey);
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
