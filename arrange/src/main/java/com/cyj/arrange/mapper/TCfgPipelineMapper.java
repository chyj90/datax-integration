package com.cyj.arrange.mapper;

import com.cyj.arrange.entry.TCfgPipeline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TCfgPipeline
 */
@Component
public interface TCfgPipelineMapper extends BaseMapper<TCfgPipeline> {
    int updateStatusBySeqId(@Param("status") Boolean status, @Param("seqId") Integer seqId);
}




