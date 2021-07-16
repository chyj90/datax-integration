package com.cyj.arrange.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MetricsEntry {
    String name;

    String description;

    List<Map<String,Object>> measurements;
}
