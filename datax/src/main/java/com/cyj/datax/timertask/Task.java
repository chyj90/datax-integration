package com.cyj.datax.timertask;

import com.alipay.sofa.jraft.rhea.util.concurrent.DistributedLock;
import com.cyj.datax.entry.TCfgTask;
import com.cyj.datax.entry.TLogTask;
import com.cyj.datax.mapper.TCfgTaskMapper;
import com.cyj.datax.mapper.TLogPipelineMapper;
import com.cyj.datax.mapper.TLogTaskMapper;
import com.cyj.datax.processor.MonitorProcessor;
import com.cyj.datax.rheakv.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Component
public class Task {

    @Autowired
    Client client;

    @Autowired
    TLogPipelineMapper logPipelineMapper;

    @Autowired
    TLogTaskMapper logTaskMapper;

    @Autowired
    TCfgTaskMapper cfgTaskMapper;

    @Autowired
    MonitorProcessor monitorProcessor;

    @Scheduled(cron = "*/5 * * * * ?")
    @Async("taskExecutor")
    public void pickTask() {
        DistributedLock lock = client.getlock(Client.TASK_LOCK);
        TLogTask target = null;
        try {
            if (lock.tryLock()) {
                List<TLogTask> tasks = logTaskMapper.leftTask();
                if (tasks != null && tasks.size() > 0) {
                    TLogTask lastTask = null;
                    for (TLogTask task : tasks) {
                        if (target != null) {
                            break;
                        }
                        if (task.getStartTime() == null) {
                            if (lastTask == null) {
                                target = task;
                            } else {
                                if (lastTask.getEndTime() != null) {
                                    target = task;
                                }
                            }
                        }
                    }
                    if (target != null) {
                        target.setStartTime(new Date());
                        logTaskMapper.updateById(target);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        if (target != null) {
            TCfgTask cfgTask = cfgTaskMapper.selectById(target.getTaskId());
            // TODO: 2021/6/28 任务失败了怎么办
            monitorProcessor.process(cfgTask.getJson());
            //处理完毕设置endtime
            target.setEndTime(new Date());
            logTaskMapper.updateById(target);
            long maxTID = logTaskMapper.selectMaxTaskIDByPipelinelogID(target.getPipelineLogId());
            if (target.getSeqId()==maxTID)
            {
                logPipelineMapper.updateStatusAndEndTimeBySeqId(true,target.getEndTime(),target.getPipelineLogId());
            }
        }
    }
}
