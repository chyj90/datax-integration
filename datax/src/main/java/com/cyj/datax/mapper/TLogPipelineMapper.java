package com.cyj.datax.mapper;
import java.util.Date;

import com.cyj.datax.entry.TLogPipeline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TLogPipeline
 */
@Component
public interface TLogPipelineMapper extends BaseMapper<TLogPipeline> {

    int updateStatusAndEndTimeBySeqId(@Param("status") Boolean status, @Param("endTime") Date endTime, @Param("seqId") Long seqId);
}




