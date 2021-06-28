package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 流水线执行日志表
 * @TableName t_log_pipeline
 */
@TableName(value ="t_log_pipeline")
@Data
@Accessors(chain = true)
public class TLogPipeline implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seqId;

    /**
     * 流水线配置表主键
     */
    private Integer pipelineId;

    /**
     * 流水线开始时间
     */
    private Date startTime;

    /**
     * 流水线结束时间
     */
    private Date endTime;

    /**
     * 0 未完成 1 完成
     */
    private Boolean status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}