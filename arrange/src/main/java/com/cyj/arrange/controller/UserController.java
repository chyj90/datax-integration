package com.cyj.arrange.controller;

import com.cyj.arrange.bean.Result;
import com.cyj.arrange.mapper.TSysUserMapper;
import com.cyj.arrange.service.SysService;
import com.cyj.arrange.util.JwtTokenUtil;
import com.cyj.arrange.util.SecurityUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    SysService sysService;

    @Autowired
    TSysUserMapper tSysUserMapper;

    @RequestMapping("/info")
    public Result loginUserInfo()
    {
        String username = SecurityUtil.userName();
        return new Result().setMessage(username);
    }

    @PostMapping("/register")
    public Result registerUser(@RequestBody String body)
    {
        Map<String,String> params = JwtTokenUtil.gson().fromJson(body,new TypeToken<Map<String,String>>(){}.getType());
        String username = params.get("username");
        String password = params.get("password");
        sysService.register(username,password);
        return new Result().setMessage("注册成功");
    }

    @GetMapping("/list")
    public Result listUser(@RequestParam(value = "username",required = false) String username,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize)
    {
        return new Result().setMessage(sysService.queryUserPager(username,pageNo,pageSize));
    }

    @GetMapping("/delete")
    public Result deleteUser(@RequestParam("seqId") Integer seqId)
    {
        sysService.deleteUser(seqId);
        return new Result().setMessage("删除成功");
    }
    @GetMapping("/changeStatus")
    public Result deleteUser(@RequestParam("seqId") Integer seqId,@RequestParam("status") Integer status)
    {
        tSysUserMapper.updateStatusBySeqId(status,seqId);
        return new Result().setMessage("修改成功");
    }

}
