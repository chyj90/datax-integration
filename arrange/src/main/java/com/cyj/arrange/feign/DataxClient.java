package com.cyj.arrange.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by chengyajie on 2021/6/8.
 */
@FeignClient("server-datax")
public interface DataxClient {
    @RequestMapping(method = RequestMethod.GET, value = "/task/stream")
    String exec(@RequestParam("job") String job);
}
