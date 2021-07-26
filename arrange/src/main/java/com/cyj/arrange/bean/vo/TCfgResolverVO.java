package com.cyj.arrange.bean.vo;

import com.cyj.arrange.entry.TCfgResolver;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TCfgResolverVO extends TCfgResolver {
    private String dsName;
}
