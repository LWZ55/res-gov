package com.htsx.resgov.JdbcUtil;

import com.htsx.resgov.dao.FieldMapper;
import com.htsx.resgov.entity.FieldInfo;
import com.htsx.resgov.entity.SqlHelper;
import com.htsx.resgov.entity.XStepFields;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
public class MappingHelper implements XStepFields {

    @Autowired
    FieldMapper fieldMapper;

    public Set<FieldInfo> getFieldsInfo(String tableName){
        Set<FieldInfo> res=fieldMapper.getFieldsInfoByTableName(tableName);
        return  res;
    }

    public String getClassNameByClassId(Integer classId){
        String className = fieldMapper.getClassNameByClassId(classId);
        return  className;
    }


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
        tableName = fieldMapper.getTableNameBySysNameAndClassId(param);
        return tableName;
    }


    /**
     * 系统名，表名，tag号——>列名
     * @param curRecordMap
     * @param sysName
     * @return  {tag,columnName}
     */
    public  Map<Integer, String> getColumnsByTags(HashMap<Integer, String> curRecordMap,String sysName) {
        String tableName;
        String classId = curRecordMap.get(XStepFields.classId);
        tableName = getTableNameBySysNameAndClassId(sysName, classId);
        if (tableName == null)
            return null;
        Set<Integer> tagSet = curRecordMap.keySet();
        Map<Integer, String> res = new HashMap<>();

        //结构化查询
        for (Integer tag : tagSet) {
            //查找到对应的field
            if (tag != XStepFields.operationType && tag != XStepFields.classId)
                res.put(tag, getColumnNameFromTagAndTableName(tableName,tag.toString()));
        }
        return res;

    }


    public String getColumnNameFromTagAndTableName(String tableName, String tag) {
        String columnName;
        HashMap<String, String> param = new HashMap<>();
        param.put("tableName", tableName);
        param.put("tag", tag);
        //若找不到，则返回null
        columnName = fieldMapper.getColumnNameByTableNameAndTag(param);
        return columnName;
    }


}
