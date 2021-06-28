package com.cyj.datax;

import com.cyj.datax.rheakv.Client;
import com.cyj.datax.rheakv.Server;
import lombok.SneakyThrows;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.UnknownHostException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by chengyajie on 2021/6/7.
 */
@SpringBootApplication
@EnableAsync
@MapperScan("com.cyj.datax.mapper")
public class Application implements ApplicationListener<ContextRefreshedEvent> {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    @Autowired
    Server rheakvServer;

    @Autowired
    Client rheakvClient;
    public static void main(String[] args) throws UnknownHostException {
        Environment env = SpringApplication.run(Application.class,args).getEnvironment();
    }

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor getAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("Timer-Task-");
        // 线程池对拒绝任务的处理策略 直接丢弃不执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            rheakvServer.init();
            rheakvClient.init();
        }
    }
}
