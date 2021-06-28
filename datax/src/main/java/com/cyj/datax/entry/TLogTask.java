package com.cyj.datax.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 任务日志表
 * @TableName t_log_task
 */
@TableName(value ="t_log_task")
@Data
public class TLogTask implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long seqId;

    /**
     * 流水线日志表id
     */
    private Long pipelineLogId;

    /**
     * 任务配置表ID
     */
    private Integer taskId;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 任务结束详情
     */
    private String finishDetail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}