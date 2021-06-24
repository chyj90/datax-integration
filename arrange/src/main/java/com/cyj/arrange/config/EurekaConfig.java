package com.cyj.arrange.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.server.EurekaServerMarkerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "eureka.client",name = "enabled",havingValue = "true")
@Import(EurekaServerMarkerConfiguration.class)
public class EurekaConfig {

}
