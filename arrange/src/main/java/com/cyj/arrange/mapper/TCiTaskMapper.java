package com.cyj.arrange.mapper;

import com.cyj.arrange.entry.TCiTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Entity generator.entry.TCiTask
 */
@Component
public interface TCiTaskMapper extends BaseMapper<TCiTask> {

    List<TCiTask> findTaskByPipelineID(@Param("pipelineID") Integer pipelineID);
}




