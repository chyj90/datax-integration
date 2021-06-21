package com.cyj.arrange.mapper;
import org.apache.ibatis.annotations.Param;

import com.cyj.arrange.entry.TCiPipeline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TCiPipeline
 */
@Component
public interface TCiPipelineMapper extends BaseMapper<TCiPipeline> {
    int updateStatusBySeqId(@Param("status") Boolean status, @Param("seqId") Integer seqId);
}




