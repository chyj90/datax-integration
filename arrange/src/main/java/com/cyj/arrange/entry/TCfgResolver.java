package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 占位符配置
 * @TableName t_cfg_resolver
 */
@TableName(value ="t_cfg_resolver")
@Data
public class TCfgResolver implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 占位符名称
     */
    private String resolverName;

    /**
     * 数据源主键
     */
    private Integer datasourceId;

    /**
     * sql表达式
     */
    private String express;

    /**
     * 隶属用户
     */
    private Integer owner;

    /**
     * 状态 0：关闭 1：开启
     */
    private Boolean status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}