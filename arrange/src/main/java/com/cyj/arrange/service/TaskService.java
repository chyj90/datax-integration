package com.cyj.arrange.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.arrange.entry.TCiPipeline;
import com.cyj.arrange.entry.TCiPipelineTask;
import com.cyj.arrange.entry.TCiTask;
import com.cyj.arrange.mapper.TCiPipelineMapper;
import com.cyj.arrange.mapper.TCiPipelineTaskMapper;
import com.cyj.arrange.mapper.TCiTaskMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TCiTaskMapper tCiTaskMapper;

    @Autowired
    TCiPipelineMapper tCiPipelineMapper;

    @Autowired
    TCiPipelineTaskMapper tCiPipelineTaskMapper;

    public void saveTask(TCiTask task) {
        if (task.getSeqId() != null) {
            tCiTaskMapper.updateById(task);
        } else {
            tCiTaskMapper.insert(task);
        }
    }

    public void savePipeline(TCiPipeline tCiPipeline)
    {
        if (tCiPipeline.getSeqId()==null)
        {
            tCiPipelineMapper.insert(tCiPipeline);
        }else
        {
            tCiPipelineMapper.updateById(tCiPipeline);
        }
    }

    public void delTask(Integer taskID)
    {
        if (taskID!=null)
        {
            tCiTaskMapper.deleteById(taskID);
        }
    }

    public void delPipeline(Integer pipelineID)
    {
        if (pipelineID!=null)
        {
            tCiPipelineMapper.deleteById(pipelineID);
        }
    }

    /**
     *
     * @param pipelineID
     * @param status
     * 关闭/开启流水线
     */
    public void setPipelineStatus(Integer pipelineID,boolean status)
    {
        if (pipelineID!=null)
        {
            tCiPipelineMapper.updateStatusBySeqId(status,pipelineID);
        }
    }

    @Transactional
    public void setPipelineTask(Integer pipelineID, List<Tuple2<Integer,Integer>> tasks)
    {
        tCiPipelineTaskMapper.delete(new QueryWrapper<TCiPipelineTask>().eq("pipeline_id",pipelineID));
        for (Tuple2<Integer,Integer> tuple:tasks)
        {
            TCiPipelineTask tCiPipelineTask = new TCiPipelineTask();
            tCiPipelineTask.setPipelineId(pipelineID).setTaskId(tuple.getT1()).setOrderNo(tuple.getT2());
            tCiPipelineTaskMapper.insert(tCiPipelineTask);
        }
    }

    public IPage<TCiTask> queryTaskPager(String taskName,Integer ownerID,int pageNo,int pageSize)
    {
        QueryWrapper<TCiTask> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(taskName))
        {
            queryWrapper.like("name",taskName);
        }
        queryWrapper.eq("owner",ownerID);
        Page<TCiTask> page = new Page<>(pageNo,pageSize);
        return tCiTaskMapper.selectPage(page,queryWrapper);
    }

    public IPage<TCiPipeline> queryPipelinePager(String pipelineName,Integer ownerID,int pageNo,int pageSize)
    {
        QueryWrapper<TCiPipeline> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",pipelineName).eq("owner",ownerID);
        Page<TCiPipeline> page = new Page<>(pageNo,pageSize);
        return tCiPipelineMapper.selectPage(page,queryWrapper);
    }

    public List<TCiPipelineTask> queryPipelineTask(Integer pipelineID)
    {
        return tCiPipelineTaskMapper.selectList(new QueryWrapper<TCiPipelineTask>().eq("pipeline_id",pipelineID));
    }

    public List<TCiTask> queryAllTask(Integer userID)
    {
        return tCiTaskMapper.selectList(new QueryWrapper<TCiTask>().eq("owner",userID));
    }
}
