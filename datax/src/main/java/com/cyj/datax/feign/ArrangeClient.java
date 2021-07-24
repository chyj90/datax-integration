package com.cyj.datax.feign;

import com.cyj.datax.config.FeignClientConfig;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "arrange-datax",configuration = FeignClientConfig.class)
public interface ArrangeClient {
    @RequestLine("GET /cbk/finishTask?success={success}&jobId={jobId}&note={note}")
    String finishTask(@Param("success") Boolean success, @Param("jobId") Long jobId, @Param("note") String note);
}
