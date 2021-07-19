package com.cyj.arrange.controller;

import com.cyj.arrange.bean.MetricsEntry;
import com.cyj.arrange.bean.Pager;
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
import java.util.HashMap;
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
        List<Map<String,Object>> rtn = new ArrayList<>();
        for (ServiceInstance instance:instances)
        {
            Map<String,Object> row = new HashMap<>();
            row.put("seqId",instance.getInstanceId());
            for (String metric:meticsList)
            {
                String rs = restTemplate.getForObject(instance.getUri().toString()+"/actuator/metrics/"+metric,String.class);
                MetricsEntry entry = gson.fromJson(rs,new TypeToken<MetricsEntry>(){}.getType());
                String metricName = entry.getName();
                List<Map<String,Object>> values = entry.getMeasurements();
                Object value = 0;
                if (values!=null&&values.size()>0)
                {
                    value = values.get(0).get("value");
                    if (value instanceof Double)
                    {
                        if (metricName.contains("memory"))
                        {
                            value = (Double)value/(1024*1024);
                        }
                        double dvalue = ((Double) value).doubleValue();
                        int ivalue = (int) dvalue;
                        if (ivalue!=dvalue)
                        {
                            value = String.format("%.4f",value);
                        }
                    }
                }
                row.put(metricName,value);
            }
            rtn.add(row);
        }
        return new Result().setMessage(new Pager().setData(rtn));
    }
}
