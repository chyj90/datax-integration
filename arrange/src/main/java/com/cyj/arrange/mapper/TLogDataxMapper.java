package com.cyj.arrange.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyj.arrange.bean.vo.TLogDataxVO;
import com.cyj.arrange.entry.TLogDatax;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TLogDatax
 */
@Component
public interface TLogDataxMapper extends BaseMapper<TLogDatax> {
    IPage<TLogDataxVO> selectPageVo(IPage<?> page, String taskName, Integer ownerID);
}




