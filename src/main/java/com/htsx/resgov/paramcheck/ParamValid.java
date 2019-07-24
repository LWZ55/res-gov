package com.htsx.resgov.paramcheck;

import com.htsx.resgov.JdbcUtil.TableInfoHelper;
import com.htsx.resgov.dao.IParamValid;
import com.htsx.resgov.entity.XStepFields;
import com.htsx.resgov.utils.TagMapping;
import com.huatai.xtrade.xstep.event.IXStepEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


/**
 *
 */
public class ParamValid implements IParamValid, XStepFields {

    @Autowired
    TableInfoHelper tableInfoHelper;

    @Autowired
    TagMapping tagMapping;

    //TODO
    public int getIndexCount() {
        return 1;
    }


    //只检验输入的字段
    public boolean isCheckedByFieldsProvided(Map<Integer, String> tagColumnMap,
                                             Map<Integer, String> curRecordMap,
                                             String tableName) throws Exception {

        Set<Integer> tagSet = curRecordMap.keySet();
        for (Integer tag : tagSet) {//field为列名，如订单号
            //列名：{约束名：约束值}}
            String columnName = tagColumnMap.get(tag);//根据tag获取列名

            String fieldVal = curRecordMap.get(tag);//根据tag获取 值

            //{约束字段，约束值}
            Map<String, String> schemaMap = tableInfoHelper.getTableOneColumnSchemas(tableName, columnName);

            if (!verifyField(fieldVal, schemaMap)) {
                return false;
            }

        }//记录内的tag循环

        return true;
    }

    public boolean isCheckedByOperationType(Map<Integer, String> tagColumnMap,
                                            Map<Integer, String> curRecordMap,
                                            String operationType, String tableName) throws Exception {

        //检验索引值是否存在
        int indexCount = getIndexCount();
        boolean existIndex = indexCount > 0 ? true : false;
        List<String> notAllowedNullFields = getNotAllowedNullFields(tableName);
        switch (operationType) {
            case "delete": {
                if (!existIndex)
                //若不存在，也没必要进行索引列参数校验
                    return false;
                //存在则不需要参数类型校验
                break;
            }
            case "save": {
                if (!existIndex) {

                    //插入insert校验
                    //字段非空,先校验包含所有非空字段

                    Collection<String> columnCol = tagColumnMap.values();
                    if (!columnCol.containsAll(notAllowedNullFields))
                        return false;
                    // 然后检验所有的输入值的非空字段不为空（包含在字段校验里）


                } else {
                    //更新校验
                }
                if (!isCheckedByFieldsProvided(tagColumnMap, curRecordMap, tableName))
                    return false;
                break;
            }


        }


        return true;
    }


    public List<String> getNotAllowedNullFields(String tableName) throws Exception {
        List<String> res = new ArrayList<>();
        Map<String, Map<String, String>> fieldsSchemas = new HashMap<>();
        fieldsSchemas = tableInfoHelper.getTableAllColumnsSchemas(tableName, "%");
        Set<String> fieldSet = fieldsSchemas.keySet();
        for (String field : fieldSet) {
            if (fieldsSchemas.get(field).get("is_nullable").equals(false))
                res.add(field);
        }
        return res;

    }


    //参数验证
    public boolean paramValid(IXStepEvent xStepEvent) throws Exception {

        //XStepEvent
        Iterator<HashMap<Integer, String>> it = xStepEvent.iterator();
        String sysName = xStepEvent.getHeader().getString(XStepFields.systemType);
        //traverse every record
        while (it.hasNext()) {
            HashMap<Integer, String> curRecordMap = it.next();
            String classId = curRecordMap.get(XStepFields.classId);
            String tableName = tagMapping.getTableNameBySysNameAndClassId(sysName, classId);
            if (tableName == null)
                return false;
            else {
                //返回{tag：column},此处仅对输入值验证
                Map<Integer, String> tagColumnMap = tagMapping.getColumnsByTags(tableName, curRecordMap);
                //第一步：验证索引
                boolean existUniqueKey = existUniqueIndexFields(tableName, tagColumnMap);
                if (!existUniqueKey)
                    return false;
                //第二步：根据操作类型验证
                String operationType = curRecordMap.get(XStepFields.operationType);


            }//else


        }//iterator，记录间循环

        return true;

    }


    //TODO
    public boolean existUniqueIndexFields(String tableName, Map<Integer, String> tagColumnMap) {

        //根据表名获得一个唯一索引，待完成
        List<String> indexList = null;
        Collection<String> columnCol = tagColumnMap.values();
        if (columnCol.containsAll(indexList))
            return true;
        else
            return false;
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
    public boolean verifyField(String fieldVal, Map<String, String> schemasMap) {
        boolean isRight = false;


        String typeLimit = schemasMap.get("type");
        String sizeLimit = schemasMap.get("size");
        String nullableLimit = schemasMap.get("is_nullable");

        //验证是否为空
        if (!isNullableRight(fieldVal, nullableLimit))
            return false;
        //验证类型
        if (!isTypeRight(fieldVal, typeLimit))
            return false;
        //验证大小
        if (!isSizeRight(fieldVal, sizeLimit))
            return false;


        return isRight;

    }

}


