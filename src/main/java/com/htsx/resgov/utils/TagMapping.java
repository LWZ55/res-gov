package com.htsx.resgov.utils;

import com.htsx.resgov.dao.FieldMapper;
import com.htsx.resgov.entity.SqlHelper;
import com.htsx.resgov.entity.XStepFields;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
public class TagMapping implements XStepFields {


    @Autowired
    FieldMapper fieldMapper;

    public String getIndexCountSql(Map<String, String>indexMap,String tableName) {

        StringBuilder sb = new StringBuilder("select count(0) from ");
        sb.append(tableName+" where");
        Set<String>set = indexMap.keySet();
        for(String column:set){
            String val = indexMap.get(column);
            sb.append(" "+column+"="+val+" and");
        }
        return  sb.subSequence(0,sb.length()-4).toString();
    }




    @Test
    public void test(){
        HashMap<String,String> map = new HashMap<>();
        map.put("kiy","1");
        map.put("uut","2");
        map.put("yq","22");
        System.out.println(getIndexCountSql(map,"exor"));
    }

    public int getIndexCount(Map<String, String>indexMap,String tableName){
        String sql = getIndexCountSql(indexMap,tableName);
        return fieldMapper.selectBySql(new SqlHelper(sql));
    }

    public int getIndexCountBySql(String sql){
        return fieldMapper.selectBySql(new SqlHelper(sql));
    }

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
