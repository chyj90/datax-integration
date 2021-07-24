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
 * 简单任务日志
 * @TableName t_log_datax
 */
@TableName(value ="t_log_datax")
@Accessors(chain = true)
@Data
public class TLogDatax implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long seqId;

    /**
     * 任务配置id
     */
    private Integer taskId;

    /**
     * 开始时间
     */
    private Date execTime;

    /**
     * 0 成功 1 失败
     */
    private Boolean status;

    /**
     * 备注
     */
    private String notes;

    /**
     * 完成时间
     */
    private Date endTime;

    /**
     * 关系表主键
     */
    private Integer cfgPipelineTaskId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}