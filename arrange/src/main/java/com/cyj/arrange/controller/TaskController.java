package com.cyj.arrange.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyj.arrange.bean.Result;
import com.cyj.arrange.entry.TCiPipeline;
import com.cyj.arrange.entry.TCiTask;
import com.cyj.arrange.service.SysService;
import com.cyj.arrange.service.TaskService;
import com.cyj.arrange.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
        UserDetails ud = SecurityUtil.UD();
        String username = ud.getUsername();
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
        UserDetails ud = SecurityUtil.UD();
        String username = ud.getUsername();
        Integer userID = sysService.userID(username);
        if (userID!=null)
        {
            return new Result().setMessage(taskService.queryPipelinePager(pipelineName,userID,pageNo == null ? DEFAULT_PAGE : pageNo, pageSize == null ? DEFAULT_PAGE_SIZE : pageSize));
        }else
        {
            return new Result().setCode(HttpStatus.FORBIDDEN.value()).setMessage("用户不存在");
        }
    }
}
