package com.htsx.resgov.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldInfo {


   // private Integer id;
    private String columnName;
    private Integer tag;
    private String type;
    private Integer length;
    private String comment;
    private String sampleValue;
    private String tableName;
}
