package com.htsx.resgov.utils;

import com.htsx.resgov.dao.FieldMapper;
import com.htsx.resgov.entity.XStepFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class TagMapping implements XStepFields {


    @Autowired
    FieldMapper fieldMapper;

    public  String getTableNameBySysNameAndClassId(String sysName,String classId) {
        String tableName;
        HashMap<String, String> param = new HashMap<>();
        param.put("sysName", sysName);
        param.put("classId", classId);
        tableName = fieldMapper.getBySysNameAndClassId(param);
        return tableName;
    }




    //确保能获取tableName再执行
    //return {tag,fields}
    //
    public  Map<Integer, String> getColumnsByTags(String tableName,HashMap<Integer, String> oneRecordMap) {
        Set<Integer> tagSet = oneRecordMap.keySet();
        Map<Integer, String> res = new HashMap<>();

        //结构化查询
        for (Integer tag : tagSet) {
            //查找到对应的field
            if (tag != XStepFields.operationType && tag != XStepFields.classId)
                res.put(tag, getColumnFromTagAndTableName(tableName,tag.toString()));
        }
        return res;

    }

    public String getColumnFromTagAndTableName(String tableName, String tag) {
        String columnName;
        HashMap<String, String> param = new HashMap<>();
        param.put("tableName", tableName);
        param.put("tag", tag);
        //若找不到，则返回null
        columnName = fieldMapper.getByTableNameAndTag(param);

        return columnName;
    }


}
