package com.cyj.arrange.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.arrange.bean.vo.TCfgPipelineTaskVO;
import com.cyj.arrange.bean.vo.TCfgPipelineVO;
import com.cyj.arrange.entry.TCfgPipeline;
import com.cyj.arrange.entry.TCfgPipelineTask;
import com.cyj.arrange.entry.TCfgTask;
import com.cyj.arrange.mapper.TCfgPipelineMapper;
import com.cyj.arrange.mapper.TCfgPipelineTaskMapper;
import com.cyj.arrange.mapper.TCfgTaskMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TCfgTaskMapper tCfgTaskMapper;

    @Autowired
    TCfgPipelineMapper tCfgPipelineMapper;

    @Autowired
    TCfgPipelineTaskMapper tCfgPipelineTaskMapper;

    public void saveTask(TCfgTask task) {
        if (task.getSeqId() != null) {
            tCfgTaskMapper.updateById(task);
        } else {
            tCfgTaskMapper.insert(task);
        }
    }

    public void savePipeline(TCfgPipeline tCiPipeline)
    {
        if (tCiPipeline.getSeqId()==null)
        {
            tCfgPipelineMapper.insert(tCiPipeline);
        }else
        {
            tCfgPipelineMapper.updateById(tCiPipeline);
        }
    }

    public boolean delTask(Integer taskID)
    {
        boolean rs = false;
        if (taskID!=null)
        {
            if (tCfgPipelineTaskMapper.selectCount(new QueryWrapper<TCfgPipelineTask>().eq("task_id",taskID))==0)
            {
                tCfgTaskMapper.deleteById(taskID);
                rs = true;
            }
        }
        return rs;
    }

    @Transactional
    public void delPipeline(Integer pipelineID)
    {
        if (pipelineID!=null)
        {
            tCfgPipelineMapper.deleteById(pipelineID);
            tCfgPipelineTaskMapper.delete(new QueryWrapper<TCfgPipelineTask>().eq("pipeline_id",pipelineID));
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
            tCfgPipelineMapper.updateStatusBySeqId(status,pipelineID);
        }
    }

    public void addPipelineTask(TCfgPipelineTask pipelineTask)
    {
        tCfgPipelineTaskMapper.insert(pipelineTask);
    }

    public void delPipelineTask(Integer seqId)
    {
        tCfgPipelineTaskMapper.deleteById(seqId);
    }

    public IPage<TCfgTask> queryTaskPager(String taskName,Integer ownerID,int pageNo,int pageSize)
    {
        QueryWrapper<TCfgTask> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(taskName))
        {
            queryWrapper.like("name",taskName);
        }
        queryWrapper.eq("owner",ownerID);
        Page<TCfgTask> page = new Page<>(pageNo,pageSize);
        return tCfgTaskMapper.selectPage(page,queryWrapper);
    }

    public IPage<TCfgPipeline> queryPipelinePager(String pipelineName,Integer ownerID,int pageNo,int pageSize)
    {
        QueryWrapper<TCfgPipeline> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(pipelineName))
        {
            queryWrapper.like("name",pipelineName);
        }
        queryWrapper.eq("owner",ownerID);
        Page<TCfgPipeline> page = new Page<>(pageNo,pageSize);
        return tCfgPipelineMapper.selectPage(page,queryWrapper);
    }

    public TCfgPipelineVO queryPipeline(Integer pipelineID)
    {
        TCfgPipeline pipeline = tCfgPipelineMapper.selectById(pipelineID);
        List<TCfgPipelineTaskVO> tasks = tCfgTaskMapper.findTaskByPipelineID(pipelineID);
        TCfgPipelineVO vo = new TCfgPipelineVO();
        BeanUtils.copyProperties(pipeline,vo);
        vo.setTasks(tasks);
        return vo;
    }

    public List<TCfgTask> queryAllTask(Integer userID)
    {
        return tCfgTaskMapper.selectList(new QueryWrapper<TCfgTask>().eq("owner",userID));
    }
}
