package com.cyj.arrange.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.arrange.entry.TSysUser;
import com.cyj.arrange.mapper.TSysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class SysService {
    @Autowired
    TSysUserMapper tSysUserMapper;

    public Integer userID(String username) {
        TSysUser user = tSysUserMapper.selectOne(new QueryWrapper<TSysUser>().eq("username",username));
        if (user!=null)
        {
            return user.getSeqId();
        }
        return null;
    }
}
