package com.cyj.arrange.feign;

import com.cyj.arrange.config.FeignClientConfig;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

/**
 * Created by chengyajie on 2021/6/8.
 */
@FeignClient(name = "server-datax",configuration = FeignClientConfig.class)
public interface DataxClient {
    @RequestLine("GET /task/stream?job={job}&jobID={jobID}")
    String exec(@Param("job") String job,@Param("jobID") Long jobID);

    @RequestLine("GET /actuator/metrics")
    String metrics();

    @RequestLine("GET /actuator/metrics/{requiredMetricName}")
    String metricValue(URI baseUri,@Param(value = "requiredMetricName")String requiredMetricName);
}
