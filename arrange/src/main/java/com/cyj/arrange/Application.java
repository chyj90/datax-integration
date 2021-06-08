package com.cyj.arrange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;

/**
 * Created by chengyajie on 2021/6/7.
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws UnknownHostException {
        Environment env = SpringApplication.run(Application.class,args).getEnvironment();
    }
}
