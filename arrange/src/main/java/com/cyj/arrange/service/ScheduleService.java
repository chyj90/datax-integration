package com.cyj.arrange.service;

import com.cyj.arrange.entry.TCiPipelineLog;
import com.cyj.arrange.entry.TCiTask;
import com.cyj.arrange.entry.TCiTaskLog;
import com.cyj.arrange.mapper.TCiPipelineLogMapper;
import com.cyj.arrange.mapper.TCiTaskLogMapper;
import com.cyj.arrange.mapper.TCiTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    TCiTaskMapper tCiTaskMapper;

    @Autowired
    TCiPipelineLogMapper tCiPipelineLogMapper;

    @Autowired
    TCiTaskLogMapper tCiTaskLogMapper;
    /**
     *  启动流水线
     */
    @Transactional
    public void startPipeline(Integer pipelineID)
    {
        List<TCiTask> tasks = tCiTaskMapper.findTaskByPipelineID(pipelineID);
        if (tasks!=null&&tasks.size()>0)
        {
            Date startTime = new Date();
            TCiPipelineLog pipelineLog = new TCiPipelineLog();
            pipelineLog.setPipelineId(pipelineID).setStartTime(startTime);
            tCiPipelineLogMapper.insert(pipelineLog);
            for (TCiTask task:tasks)
            {
                TCiTaskLog taskLog = new TCiTaskLog();
                taskLog.setPipelineLogId(pipelineLog.getSeqId()).setTaskId(task.getSeqId());
                tCiTaskLogMapper.insert(taskLog);
            }
        }
    }
}
