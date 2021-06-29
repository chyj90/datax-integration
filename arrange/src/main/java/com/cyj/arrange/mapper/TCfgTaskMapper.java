package com.cyj.arrange.mapper;

import com.cyj.arrange.bean.vo.TCfgPipelineTaskVO;
import com.cyj.arrange.entry.TCfgTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Entity generator.entry.TCfgTask
 */
@Component
public interface TCfgTaskMapper extends BaseMapper<TCfgTask> {
    List<TCfgPipelineTaskVO> findTaskByPipelineID(@Param("pipelineID") Integer pipelineID);
}




