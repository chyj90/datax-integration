package com.cyj.arrange.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.arrange.entry.TSysRole;
import com.cyj.arrange.entry.TSysUser;
import com.cyj.arrange.mapper.TSysRoleMapper;
import com.cyj.arrange.mapper.TSysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengyajie on 2021/6/9.
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    TSysUserMapper tSysUserMapper;

    @Autowired
    TSysRoleMapper tSysRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TSysUser tSysUser = tSysUserMapper.selectOne(new QueryWrapper<TSysUser>().eq("username",username));
        if (tSysUser!=null)
        {
            List<TSysRole> roles = tSysRoleMapper.selectAllByUserSeqID(tSysUser.getSeqId());
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (TSysRole role:roles)
            {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
            return new User(username, tSysUser.getPassword(), authorities);
        }
        return null;
    }
}
