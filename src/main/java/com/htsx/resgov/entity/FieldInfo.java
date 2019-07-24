package com.htsx.resgov.entity;

import lombok.Data;

@Data
public class FieldInfo {
    private String name;
    private Integer tag;
    private String type;
    private String length;
    private String comment;
    private String sampleValue;
}
