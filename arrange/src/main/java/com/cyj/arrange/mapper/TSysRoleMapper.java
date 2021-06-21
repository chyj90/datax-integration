package com.cyj.arrange.mapper;

import com.cyj.arrange.entry.TSysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Entity generator.entry.TSysRole
 */
@Component
public interface TSysRoleMapper extends BaseMapper<TSysRole> {
    List<TSysRole> selectAllByUserSeqID(@Param("userSeqID") Integer userSeqID);
}




