package com.cyj.arrange.controller;

import com.cyj.arrange.bean.Result;
import com.cyj.arrange.entry.TCfgPipeline;
import com.cyj.arrange.entry.TCfgTask;
import com.cyj.arrange.service.SysService;
import com.cyj.arrange.service.TaskService;
import com.cyj.arrange.util.JwtTokenUtil;
import com.cyj.arrange.util.SecurityUtil;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @ResponseBody
    public Result taskListPagion(@RequestParam("name") String taskName, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
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
    @ResponseBody
    public Result pipelineListPagion(@RequestParam("name") String pipelineName, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
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
    @ResponseBody
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
    @ResponseBody
    public Result saveTask(@RequestBody String request)
    {
        TCfgTask task = JwtTokenUtil.gson().fromJson(request,new TypeToken<TCfgTask>(){}.getType());
        taskService.saveTask(task);
        return new Result().setMessage("保存成功");
    }

    @RequestMapping("/savePipeline")
    @ResponseBody
    public Result savePipeline(@RequestBody String request)
    {
        TCfgPipeline pipeline = JwtTokenUtil.gson().fromJson(request,new TypeToken<TCfgPipeline>(){}.getType());
        pipeline.setStatus(true);
        taskService.savePipeline(pipeline);
        return new Result().setMessage("保存成功");
    }

    /**
     *给流水线配置任务
     */
    @RequestMapping("/savePipeTask")
    @ResponseBody
    public Result savePipelineTask(@RequestBody String request)
    {
        Map<String,Object> objectMap = JwtTokenUtil.gson().fromJson(request,new TypeToken<Map<String,Object>>(){}.getType());
        Double pipelineID = (Double) objectMap.get("pipelineID");
        List<Map<String,Double>> tasks = (List<Map<String, Double>>) objectMap.get("tasks");
        Integer i_pipe_id = pipelineID.intValue();
        List<Tuple2<Integer,Integer>> t_tasks = new ArrayList<>();
        for (Map<String,Double> task:tasks)
        {
            Tuple2<Integer,Integer> tuple = Tuples.of(task.get("id").intValue(),task.get("order").intValue());
            t_tasks.add(tuple);
        }
        taskService.setPipelineTask(i_pipe_id,t_tasks);
        return new Result().setMessage("保存成功");
    }
}
