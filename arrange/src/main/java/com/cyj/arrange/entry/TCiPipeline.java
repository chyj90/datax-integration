package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 流水线配置表
 * @TableName t_ci_pipeline
 */
@TableName(value ="t_ci_pipeline")
@Data
public class TCiPipeline implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 流水线名称
     */
    private String name;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 所有者 对应t_ci_name seq_id
     */
    private Integer owner;

    /**
     * 开始状态 0：关闭 1：开启
     */
    private Boolean status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}