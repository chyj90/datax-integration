package com.cyj.arrange;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.cyj.arrange.config.cron.CronTaskRegistrar;
import com.cyj.arrange.election.ElectionNode;
import com.cyj.arrange.election.ElectionNodeOptions;
import com.cyj.arrange.election.LeaderStateListener;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.UnknownHostException;

/**
 * Created by chengyajie on 2021/6/7.
 */
@SpringBootApplication
@EnableFeignClients
@Slf4j
@MapperScan("com.cyj.arrange.mapper")
@EnableScheduling
public class Application {
    @Value("${election.dataPath}")
    private String dataPath;
    @Value("${election.groupId}")
    private String groupId;
    @Value("${election.serverIdStr}")
    private String serverIdStr;
    @Value("${election.initialConfStr}")
    private String initialConfStr;
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static ApplicationContext context;
    public static void main(String[] args){
        context = SpringApplication.run(Application.class,args);
        Application application = context.getBean(Application.class);
        application.initElectNode();
    }

    // 最新版
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    private void initElectNode()
    {
        final ElectionNodeOptions electionOpts = new ElectionNodeOptions();
        electionOpts.setDataPath(dataPath);
        electionOpts.setGroupId(groupId);
        electionOpts.setServerAddress(serverIdStr);
        electionOpts.setInitialServerAddressList(initialConfStr);

        final ElectionNode node = new ElectionNode();
        node.addLeaderStateListener(new LeaderStateListener() {

            @Override
            public void onLeaderStart(long leaderTerm) {
               log.info("[ElectionBootstrap] Leader start");
                CronTaskRegistrar registrar = context.getBean(CronTaskRegistrar.class);
                //registrar.initScheduleTask();
            }

            @Override
            public void onLeaderStop(long leaderTerm) {
                log.info("[ElectionBootstrap] Leader stop on term: " + leaderTerm);
                CronTaskRegistrar registrar = context.getBean(CronTaskRegistrar.class);
                registrar.disposeScheduleTask();
            }
        });
        node.init(electionOpts);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
