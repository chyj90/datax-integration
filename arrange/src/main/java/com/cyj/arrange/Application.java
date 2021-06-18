package com.cyj.arrange;

import com.alipay.sofa.jraft.entity.PeerId;
import com.cyj.arrange.election.ElectionNode;
import com.cyj.arrange.election.ElectionNodeOptions;
import com.cyj.arrange.election.LeaderStateListener;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;

/**
 * Created by chengyajie on 2021/6/7.
 */
@SpringBootApplication
@EnableEurekaServer
@EnableFeignClients
@Slf4j
@MapperScan("com.cyj.arrange.mapper")
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
            }

            @Override
            public void onLeaderStop(long leaderTerm) {
                log.info("[ElectionBootstrap] Leader stop on term: " + leaderTerm);
            }
        });
        node.init(electionOpts);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
