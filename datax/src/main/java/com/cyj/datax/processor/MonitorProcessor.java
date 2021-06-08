package com.cyj.datax.processor;

import com.alibaba.datax.core.Engine;
import com.alibaba.datax.core.util.container.CoreConstant;
import com.cyj.datax.Application;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * Created by chengyajie on 2021/6/7.
 */
@Component
public class MonitorProcessor {

    public void process(String jsonPath) {
        if (jsonPath.startsWith("/")) {
            jsonPath = jsonPath.substring(1);
        }
        if (!jsonPath.trim().startsWith("{")) {
            jsonPath = CoreConstant.DATAX_HOME + jsonPath;
        }
        String[] datxArgs = {"-job", jsonPath, "-mode", "standalone"};
        try {
            Engine.entry(datxArgs);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
