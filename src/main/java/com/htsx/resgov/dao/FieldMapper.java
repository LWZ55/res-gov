package com.htsx.resgov.dao;

import com.htsx.resgov.entity.FieldInfo;
import com.htsx.resgov.entity.SqlHelper;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Set;

@Mapper
public interface FieldMapper {

    String getColumnNameByTableNameAndTag(HashMap<String,String> param);

    String getTableNameBySysNameAndClassId(HashMap<String,String> param);

    Integer selectBySql(SqlHelper sqlHelper);

    Set<FieldInfo> getFieldsInfoByTableName(String tableName);

    String getClassNameByClassId(Integer classId);
}
