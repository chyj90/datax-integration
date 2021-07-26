package com.cyj.arrange.controller;

import com.cyj.arrange.bean.vo.TCfgPipelineTaskVO;
import com.cyj.arrange.entry.TLogDatax;
import com.cyj.arrange.feign.DataxClient;
import com.cyj.arrange.mapper.TCfgTaskMapper;
import com.cyj.arrange.mapper.TLogDataxMapper;
import com.cyj.arrange.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cbk")
public class CallbackController {

    @Autowired
    TLogDataxMapper logDataxMapper;

    @Autowired
    TCfgTaskMapper cfgTaskMapper;

    @Autowired
    ScheduleService scheduleService;
    @RequestMapping("/finishTask")
    public String finishTask(@RequestParam("success") Boolean success, @RequestParam("jobId") Long jobId, @RequestParam("note") String note)
    {
        TLogDatax logDatax = logDataxMapper.selectById(jobId);
        if (success)
        {
            logDatax.setStatus(true).setEndTime(new Date());
            logDataxMapper.updateById(logDatax);
            if (logDatax.getCfgPipelineTaskId()!=null)
            {
               List<TCfgPipelineTaskVO> tasks = cfgTaskMapper.selectNextCfgTaskByPipelineTaskId(logDatax.getCfgPipelineTaskId());
               if (tasks.size()>0)
               {
                   scheduleService.callDatax(tasks.get(0).getSeqId(),tasks.get(0).getJsonStr(),tasks.get(0).getPtid());
               }
            }
        }else
        {
            logDatax.setStatus(false).setEndTime(new Date()).setNotes(note);
            logDataxMapper.updateById(logDatax);
        }
        return "OK";
    }
}
