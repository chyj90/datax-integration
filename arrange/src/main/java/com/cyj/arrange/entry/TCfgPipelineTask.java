package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 流水线-任务关系表
 * @TableName t_cfg_pipeline_task
 */
@TableName(value ="t_cfg_pipeline_task")
@Data
@Accessors(chain = true)
public class TCfgPipelineTask implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 流水线id
     */
    private Integer pipelineId;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 执行顺序
     */
    private Integer orderNo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}