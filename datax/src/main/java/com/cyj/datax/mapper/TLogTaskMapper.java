package com.cyj.datax.mapper;

import com.cyj.datax.entry.TLogTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Entity generator.entry.TLogTask
 */
@Component
public interface TLogTaskMapper extends BaseMapper<TLogTask> {
    List<TLogTask> leftTask();

    Long selectMaxTaskIDByPipelinelogID(@Param("plogID") Long plogID);
}




