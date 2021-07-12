package com.cyj.arrange.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.arrange.entry.TSysUser;
import com.cyj.arrange.entry.TSysUserRole;
import com.cyj.arrange.mapper.TSysUserMapper;
import com.cyj.arrange.mapper.TSysUserRoleMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class SysService {
    @Autowired
    TSysUserMapper tSysUserMapper;

    @Autowired
    TSysUserRoleMapper userRoleMapper;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public Integer userID(String username) {
        TSysUser user = tSysUserMapper.selectOne(new QueryWrapper<TSysUser>().eq("username",username));
        if (user!=null)
        {
            return user.getSeqId();
        }
        return null;
    }

    @Transactional
    public void register(String username,String password)
    {
        TSysUser user = new TSysUser().setNickname(username).setUsername(username).setPassword(passwordEncoder.encode(password)).setStatus(1);
        tSysUserMapper.insert(user);
        TSysUserRole userRole = new TSysUserRole().setUserSeqId(user.getSeqId()).setRoleSeqId(2);
        userRoleMapper.insert(userRole);
    }

    public IPage<TSysUser> queryUserPager(String userName, int pageNo, int pageSize)
    {
        QueryWrapper<TSysUser> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(userName))
        {
            queryWrapper.like("username",userName);
        }
        Page<TSysUser> page = new Page<>(pageNo,pageSize);
        return tSysUserMapper.selectPage(page,queryWrapper);
    }

    public void deleteUser(Integer seqID)
    {
        tSysUserMapper.deleteById(seqID);
    }
}
