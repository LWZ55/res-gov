package com.htsx.resgov.service;

import com.htsx.resgov.JdbcOperation.JdbcTableSchemas;
import com.htsx.resgov.dao.IParamValid;
import com.htsx.resgov.entity.XStepFields;
import com.htsx.resgov.utils.TagMapping;
import com.huatai.xtrade.xstep.event.IXStepEvent;

import java.util.*;


public class ParamValid implements IParamValid, XStepFields {

    public boolean paramValid(IXStepEvent xStepEvent) throws Exception {

        //XStepEvent
        Iterator<HashMap<Integer, String>> it = xStepEvent.iterator();
        //traverse every record
        while (it.hasNext()) {
            HashMap<Integer, String> curRecordMap = it.next();
            String classId = curRecordMap.get(XStepFields.classId);
            String tableName = TagMapping.getTableNameByClassId(classId);
            if (tableName == null)
                return false;
            else {
                //{tag：column}
                Map<Integer, String> tagColumnMap = TagMapping.getColumnsByTags(curRecordMap);
                Set<Integer> tagSet = curRecordMap.keySet();
                for (Integer tag : tagSet) {//field为列名，如订单号
                    //列名：{约束名：约束值}}
                    String columnName = tagColumnMap.get(tag);
                    String fieldVal = curRecordMap.get(tag);

                    Map<String, String> schemaMap = JdbcTableSchemas.getTableOneColumnSchemas(tableName, columnName);

                    if (!verifyFields(fieldVal, schemaMap)) {
                        return false;
                    }

                }


            }


        }//iterator

        return true;

    }


    public String getTypesFromString(String fieldVal) {
        String type = null;

        return type;
    }

    public boolean isTypeRight(String fieldVal, String targetType) {
        boolean isRight = false;

        String curType = getTypesFromString(fieldVal);
        String javaType = null;
        switch (targetType) {
            case "VARCHAR":
            case "CHAR":
                javaType = "String";
                break;
            case "DECIMAL":
                javaType = "Double";
                break;
            case "INT":
                javaType = "Int";
                break;
            default:
                break;
        }
        if (!javaType.equalsIgnoreCase(curType)) {
            isRight = false;
        } else
            isRight = true;


        return isRight;
    }

    public boolean isSizeRight(String fieldVal, String size) {
        boolean isRight = false;

        return isRight;
    }

    public boolean isNullableRight(String fieldVal, String nullableLimit) {
        boolean isRight = false;

        return isRight;
    }

    //just for one field
//应该考虑操作类型
    public boolean verifyFields(String fieldVal, Map<String, String> schemasMap) {
        boolean isRight = false;


        String typeLimit = schemasMap.get("type");
        String sizeLimit = schemasMap.get("size");
        String nullableLimit = schemasMap.get("is_nullable");

        //验证类型
        if (!isTypeRight(fieldVal, typeLimit))
            return false;
        //验证大小
        if (!isSizeRight(fieldVal, sizeLimit))
            return false;
        //验证是否为空
        if (!isNullableRight(fieldVal, nullableLimit))
            return false;


        return isRight;

    }

}


