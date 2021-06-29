package com.cyj.arrange.bean.vo;

import com.cyj.arrange.entry.TCfgPipeline;
import lombok.Data;

import java.util.List;

@Data
public class TCfgPipelineVO extends TCfgPipeline {
    private List<TCfgPipelineTaskVO> tasks;
}
