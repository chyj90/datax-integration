package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户-角色关系表
 * @TableName t_sys_user_role
 */
@TableName(value ="t_sys_user_role")
@Accessors(chain = true)
@Data
public class TSysUserRole implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 用户表主键
     */
    private Integer userSeqId;

    /**
     * 角色表主键
     */
    private Integer roleSeqId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}