package com.cyj.arrange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.arrange.entry.TLogPipeline;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TLogPipeline
 */
@Component
public interface TLogPipelineMapper extends BaseMapper<TLogPipeline> {
    void deletePipelineHalfYear(@Param("startTime") String startTime);
}




