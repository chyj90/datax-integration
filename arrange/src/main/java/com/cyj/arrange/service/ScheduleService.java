package com.cyj.arrange.service;

import com.cyj.arrange.Application;
import com.cyj.arrange.bean.vo.TCfgPipelineTaskVO;
import com.cyj.arrange.config.cron.CronTaskRegistrar;
import com.cyj.arrange.entry.*;
import com.cyj.arrange.feign.DataxClient;
import com.cyj.arrange.mapper.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@EnableScheduling
@Slf4j
public class ScheduleService {

    @Autowired
    TCfgTaskMapper tCfgTaskMapper;

    @Autowired
    TCfgPipelineMapper cfgPipelineMapper;

    @Autowired
    TLogDataxMapper logDataxMapper;

    @Autowired
    CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    DataxClient dataxClient;

    @Autowired
    MetaService metaService;

    public void callDatax(Integer cfgTaskId, String taskJSON, Integer cfgPipelineTaskId) {
        TLogDatax logDatax = new TLogDatax().setTaskId(cfgTaskId).setExecTime(new Date());
        if (cfgPipelineTaskId != null && cfgPipelineTaskId > 0) {
            logDatax.setCfgPipelineTaskId(cfgPipelineTaskId);
        }
        try{
            if (taskJSON.contains("${")) {
                Pattern pattern = Pattern.compile("\\$\\{([^\\{\\}]*)\\}");
                Matcher m = pattern.matcher(taskJSON);
                while (m.find()) {
                    String symbol = m.group(1);
                    String value = getValueOfSymbol(symbol);
                    taskJSON = taskJSON.replace("${"+symbol+"}",value);
                }
            }
            logDataxMapper.insert(logDatax);
            dataxClient.exec(taskJSON, logDatax.getSeqId());
        }catch (Exception e)
        {
            logDatax.setStatus(false).setEndTime(new Date()).setNotes(e.getMessage());
            logDataxMapper.insert(logDatax);
        }
    }

    private String getValueOfSymbol(String symbol) throws ClassNotFoundException {
        TCfgResolver resolver = metaService.getResolver(symbol);
        TCfgDatasource datasource = metaService.getDatasource(resolver.getDatasourceId());
        Class.forName(datasource.getDriverName());
        try (
                Connection conn = DriverManager.getConnection(datasource.getUrl(), datasource.getUserName(), datasource.getPassWord());
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(resolver.getExpress());
        ) {
            rs.next();
            Object rtn = rs.getObject(1);
            return rtn+"";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 启动流水线
     */
    public void startPipeline(Integer pipelineID) {
        TCfgPipeline pipeline = cfgPipelineMapper.selectById(pipelineID);
        if (pipeline.getStatus()) {
            List<TCfgPipelineTaskVO> tasks = tCfgTaskMapper.findTaskByPipelineID(pipelineID);
            if (tasks != null && tasks.size() > 0) {
                callDatax(tasks.get(0).getSeqId(), tasks.get(0).getJsonStr(), tasks.get(0).getPtid());
            }
        }
    }

    public void startTask(Integer taskID) {
        TCfgTask task = tCfgTaskMapper.selectById(taskID);
        if (task.getStatus()) {
            String json = task.getJsonStr();
            callDatax(taskID, json, -1);
        }
    }

    /**
     * 5分钟更新一次定时任务配置
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void scanScheduleTask() {
        if (Application.isLearder()) {
            log.info("更新定时任务配置");
            cronTaskRegistrar.initScheduleTask();
        }
    }
}
