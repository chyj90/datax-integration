package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户表
 * @TableName t_sys_user
 */
@TableName(value ="t_sys_user")
@Data
public class TSysUser implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 登陆账号
     */
    private String username;

    /**
     * md5密码
     */
    private String password;

    /**
     * 昵称-显示名
     */
    private String nickname;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}