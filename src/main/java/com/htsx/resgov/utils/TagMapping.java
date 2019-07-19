package com.htsx.resgov.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagMapping {

    public String getTableNameByClassId(String classId){
        String tableName = null;
        //to do 数据库查询
        return tableName;
    }


    //确保能获取tableName再执行
    //return {tag,fields}
    public Map<Integer,String> getColumnsByTags(HashMap<Integer,String> oneRecordMap){
        Set<Integer> tagSet = oneRecordMap.keySet();
        Map<Integer,String > res = new HashMap<>();

        //结构化查询
        for(Integer tag:tagSet){
            //查找到对应的field
            res.put(tag,"对应的fields");
        }
        return res;

    }


}
