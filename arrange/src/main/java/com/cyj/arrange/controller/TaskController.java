package com.cyj.arrange.controller;

import com.cyj.arrange.bean.Result;
import com.cyj.arrange.bean.vo.TCfgPipelineVO;
import com.cyj.arrange.entry.TCfgPipeline;
import com.cyj.arrange.entry.TCfgPipelineTask;
import com.cyj.arrange.entry.TCfgTask;
import com.cyj.arrange.service.SysService;
import com.cyj.arrange.service.TaskService;
import com.cyj.arrange.util.JwtTokenUtil;
import com.cyj.arrange.util.SecurityUtil;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private Integer DEFAULT_PAGE = 0;
    private Integer DEFAULT_PAGE_SIZE = 20;
    @Autowired
    TaskService taskService;

    @Autowired
    SysService sysService;

    @RequestMapping("/datax/list")
    public Result taskListPagion(@RequestParam(value = "name",required = false) String taskName, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        String username = SecurityUtil.userName();
        Integer userID = sysService.userID(username);
        if (userID!=null)
        {
            return new Result().setMessage(taskService.queryTaskPager(taskName,userID,pageNo == null ? DEFAULT_PAGE : pageNo, pageSize == null ? DEFAULT_PAGE_SIZE : pageSize));
        }else
        {
            return new Result().setCode(HttpStatus.FORBIDDEN.value()).setMessage("用户不存在");
        }
    }

    @RequestMapping("/pipeline/list")
    public Result pipelineListPagion(@RequestParam(value = "name",required = false) String pipelineName, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        String username = SecurityUtil.userName();
        Integer userID = sysService.userID(username);
        if (userID!=null)
        {
            return new Result().setMessage(taskService.queryPipelinePager(pipelineName,userID,pageNo == null ? DEFAULT_PAGE : pageNo, pageSize == null ? DEFAULT_PAGE_SIZE : pageSize));
        }else
        {
            return new Result().setCode(HttpStatus.FORBIDDEN.value()).setMessage("用户不存在");
        }
    }
    @RequestMapping("/all")
    public Result allTask()
    {
        String username = SecurityUtil.userName();
        Integer userID = sysService.userID(username);
        if (userID!=null)
        {
            return new Result().setMessage(taskService.queryAllTask(userID));
        }else
        {
            return new Result().setCode(HttpStatus.FORBIDDEN.value()).setMessage("用户不存在");
        }
    }

    @RequestMapping("/saveTask")
    public Result saveTask(@RequestBody String request)
    {
        String username = SecurityUtil.userName();
        Integer userID = sysService.userID(username);
        TCfgTask task = JwtTokenUtil.gson().fromJson(request,new TypeToken<TCfgTask>(){}.getType());
        task.setOwner(userID);
        if (task.getSeqId()==null)
        {
            task.setStatus(false);
        }
        taskService.saveTask(task);
        return new Result().setMessage("保存成功");
    }

    @RequestMapping("/savePipeline")
    public Result savePipeline(@RequestBody String request)
    {
        String username = SecurityUtil.userName();
        Integer userID = sysService.userID(username);
        TCfgPipeline pipeline = JwtTokenUtil.gson().fromJson(request,new TypeToken<TCfgPipeline>(){}.getType());
        pipeline.setOwner(userID);
        //默认流水线不开启 配置确认无误后手动开启
        if (pipeline.getSeqId()==null)
        {
            pipeline.setStatus(false);
        }
        taskService.savePipeline(pipeline);
        return new Result().setMessage("保存成功");
    }

    /**
     *给流水线配置任务
     */
    @RequestMapping("/savePipeTask")
    public Result savePipelineTask(@RequestBody String request)
    {
        TCfgPipelineTask pipelineTask = JwtTokenUtil.gson().fromJson(request,new TypeToken<TCfgPipelineTask>(){}.getType());
        taskService.addPipelineTask(pipelineTask);
        return new Result().setMessage("保存成功");
    }

    @RequestMapping("/del/pipelineTask")
    public Result deletePipelineTask(@RequestParam("seqId") Integer pipelineTaskID)
    {
        taskService.delPipelineTask(pipelineTaskID);
        return new Result().setMessage("保存成功");
    }

    @RequestMapping("/del/task")
    public Result deleteTask(@RequestParam(value = "seqId") Integer taskID)
    {
        boolean rs = taskService.delTask(taskID);
        if (rs)
        {
            return new Result().setMessage("删除成功");
        }else
        {
            return new Result().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMessage("有关联流水线任务不能删除");
        }
    }

    @RequestMapping("/del/pipeline")
    public Result deletePipeline(@RequestParam(value = "seqId") Integer taskID)
    {
        taskService.delPipeline(taskID);
        return new Result().setMessage("删除成功");
    }

    @RequestMapping("/switch/pipeline/status")
    public Result switchPipelineStatus(@RequestParam("seqId") Integer pipelineID,@RequestParam("status") Boolean status)
    {
        taskService.setPipelineStatus(pipelineID,status);
        return new Result().setMessage("保存成功");
    }

    @RequestMapping("/switch/task/status")
    public Result switchTaskStatus(@RequestParam("seqId") Integer pipelineID,@RequestParam("status") Boolean status)
    {
        taskService.setTaskStatus(pipelineID,status);
        return new Result().setMessage("保存成功");
    }

    @RequestMapping("/pipeline/detail")
    public Result pipelineDatail(@RequestParam("seqId") Integer pipelineID)
    {
        TCfgPipelineVO vo = taskService.queryPipeline(pipelineID);
        return new Result().setMessage(vo);
    }

    @RequestMapping("/list/logs")
    public Result taskLogsPagion(@RequestParam(value = "name",required = false) String taskName, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        String username = SecurityUtil.userName();
        Integer userID = sysService.userID(username);
        if (userID!=null)
        {
            return new Result().setMessage(taskService.queryTaskLogsPager(taskName,userID,pageNo == null ? DEFAULT_PAGE : pageNo, pageSize == null ? DEFAULT_PAGE_SIZE : pageSize));
        }else
        {
            return new Result().setCode(HttpStatus.FORBIDDEN.value()).setMessage("用户不存在");
        }
    }
}
