package com.cyj.datax.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class TaskThreadMetric implements MeterBinder {

    @Qualifier("taskExecutor")
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder("task.thread.inuse",()->taskExecutor.getActiveCount()).register(meterRegistry);
        Gauge.builder("task.thread.total",()->taskExecutor.getPoolSize()).register(meterRegistry);
    }
}
