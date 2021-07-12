package com.cyj.arrange.mapper;
import org.apache.ibatis.annotations.Param;

import com.cyj.arrange.entry.TSysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @Entity generator.entry.TSysUser
 */
@Component
public interface TSysUserMapper extends BaseMapper<TSysUser> {
    int updateStatusBySeqId(@Param("status") Integer status, @Param("seqId") Integer seqId);
}




