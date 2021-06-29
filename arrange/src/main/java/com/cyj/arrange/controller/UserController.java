package com.cyj.arrange.controller;

import com.cyj.arrange.bean.Result;
import com.cyj.arrange.util.SecurityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/info")
    public Result loginUserInfo()
    {
        String username = SecurityUtil.userName();
        return new Result().setMessage(username);
    }
}
