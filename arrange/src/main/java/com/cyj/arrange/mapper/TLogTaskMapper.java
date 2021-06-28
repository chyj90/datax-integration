package com.cyj.arrange.mapper;

import com.cyj.arrange.entry.TLogTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TLogTask
 */
@Component
public interface TLogTaskMapper extends BaseMapper<TLogTask> {
    void deleteTaskHalfYear(@Param("startTime") String startTime);
}




