package com.cyj.arrange.controller;

import com.cyj.arrange.bean.Result;
import com.cyj.arrange.feign.DataxClient;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitor")
@Slf4j
public class ActuatorController {

    @Autowired
    DataxClient dataxClient;

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Gson gson;
    @GetMapping("/metrics")
    public Result listMetrics()
    {
        String metics = dataxClient.metrics();
        Map<String, List<String>> names = gson.fromJson(metics,new TypeToken<Map<String, List<String>>>(){}.getType());
        List<String> meticsNames = names.get("names");
        return new Result().setMessage(meticsNames);
    }

    @PostMapping("/values")
    public Result collectMeticValue(@RequestBody String metics) throws URISyntaxException {
        List<String> meticsList = gson.fromJson(metics,new TypeToken<List<String>>(){}.getType());
        List<ServiceInstance> instances = discoveryClient.getInstances("server-datax");
        List<String> rtn = new ArrayList<>();
        for (ServiceInstance instance:instances)
        {
            for (String metric:meticsList)
            {
                String rs = restTemplate.getForObject(instance.getUri().toString()+"/actuator/metrics/"+metric,String.class);
                rtn.add(rs);
            }

        }
        return new Result().setMessage(rtn);
    }
}
