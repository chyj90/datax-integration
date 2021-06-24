package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 流水线执行日志表
 * @TableName t_ci_pipeline_log
 */
@TableName(value ="t_ci_pipeline_log")
@Accessors(chain = true)
@Data
public class TCiPipelineLog implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}