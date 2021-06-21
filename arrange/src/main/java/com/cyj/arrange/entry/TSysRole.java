package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 角色表
 * @TableName t_sys_role
 */
@TableName(value ="t_sys_role")
@Data
public class TSysRole implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 角色名
     */
    private String roleName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}