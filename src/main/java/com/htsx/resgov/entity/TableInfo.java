package com.htsx.resgov.entity;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {

    private int id;
    private String tableName;
    private String className;
    private String classId;
    private String version;
    private List<String> uniqueIndex;
}
