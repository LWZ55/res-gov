package com.htsx.resgov.dao;

import com.htsx.resgov.entity.SqlHelper;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface FieldMapper {

    String getByTableNameAndTag(HashMap<String,String> param);

    String getBySysNameAndClassId(HashMap<String,String> param);

    Integer selectBySql(SqlHelper sqlHelper);
}
