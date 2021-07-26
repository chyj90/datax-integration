package com.cyj.arrange.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_cfg_datasource
 */
@TableName(value ="t_cfg_datasource")
@Data
public class TCfgDatasource implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer seqId;

    /**
     * 数据源名称
     */
    private String dsName;

    /**
     * 连接
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 驱动
     */
    private String driverName;

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