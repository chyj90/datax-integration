package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 任务表
 * @TableName t_ci_task
 */
@TableName(value ="t_ci_task")
@Data
public class TCiTask implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 任务名
     */
    private String name;

    /**
     * 任务详情
     */
    private String json;

    /**
     * 对应t_ci_user表seq_id
     */
    private Integer owner;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}