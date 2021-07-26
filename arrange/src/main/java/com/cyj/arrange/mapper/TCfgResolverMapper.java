package com.cyj.arrange.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyj.arrange.bean.vo.TCfgResolverVO;
import com.cyj.arrange.bean.vo.TLogDataxVO;
import com.cyj.arrange.entry.TCfgResolver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TCfgResolver
 */
@Component
public interface TCfgResolverMapper extends BaseMapper<TCfgResolver> {
    IPage<TCfgResolverVO> selectPageVo(IPage<?> page, @Param("resolverName") String resolverName, @Param("owner") Integer owner);
}




