package com.htsx.resgov.paramcheck;

import com.htsx.resgov.JdbcUtil.TableInfoHelper;
import com.htsx.resgov.entity.XStepFields;
import com.htsx.resgov.JdbcUtil.MappingHelper;
import com.huatai.xtrade.xstep.event.IXStepEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * TODO:总约束依赖注入,而不是每次查找
 */

@Component
public class ParamCheck implements IParamCheck, XStepFields {

    @Autowired
    TableInfoHelper tableInfoHelper;
    @Autowired
    MappingHelper mappingHelper;
    @Autowired
    FieldsCheckingHelper fieldsCheckingHelper;


    /**
     * 参数校验的主函数
     *
     * @param xStepEvent
     * @return
     * @throws Exception
     */

    public boolean paramCheck(IXStepEvent xStepEvent) throws Exception {
        if (xStepEvent == null)
            return false;
        //XStepEvent
        Iterator<HashMap<Integer, String>> it = xStepEvent.iterator();
        String sysName = xStepEvent.getHeader().getString(XStepFields.systemType);
        //iteration
        while (it.hasNext()) {
            HashMap<Integer, String> curRecordMap = it.next();
            String classId = curRecordMap.get(XStepFields.classId);
            String tableName = mappingHelper.getTableNameBySysNameAndClassId(sysName, classId);
            //返回{tag：columnName}，只返回输入字段
            Map<Integer, String> tagColumnMap = mappingHelper.getColumnsByTags(curRecordMap, sysName);
            if (tagColumnMap == null)
                return false;
            //第一步：验证记录里是否存在索引字段
            boolean existUniqueKey = existUniqueIndexFields(tableName, tagColumnMap);
            if (!existUniqueKey)
                return false;
            //第二步：根据操作类型验证
            String operationType = curRecordMap.get(XStepFields.operationType);
            if (!isCheckedByOperationType(tagColumnMap, curRecordMap, operationType, tableName))
                return false;
        }//iterator，记录间循环
        return true;
    }


    /**
     * 只验证输入字段
     *
     * @param tagColumnMap 只包含输入字段的{tag:coloumName}
     * @param curRecordMap 记录：{tag:value}
     * @param tableName
     * @return
     * @throws Exception
     */
    public boolean isCheckedByFieldsProvided(Map<Integer, String> tagColumnMap,
                                             Map<Integer, String> curRecordMap,
                                             String tableName) throws Exception {
        if (tagColumnMap == null || curRecordMap == null || tableName == null)
            return false;
        Set<Integer> tagSet = tagColumnMap.keySet();
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

    //TODO
    public boolean isCheckedByOperationType(Map<Integer, String> tagColumnMap,
                                            Map<Integer, String> curRecordMap,
                                            String operationType, String tableName) throws Exception {

        //检验索引值是否存在
        int indexCount = mappingHelper.getIndexCount(getIndexMap(tableName), tableName);
        boolean existIndex = indexCount != 0 ? true : false;
        List<String> notAllowedNullFields = getNotAllowedNullFields(tableName);
        if (operationType == null)
            operationType = "save";
        switch (operationType) {
            case "delete": {
                if (!existIndex)
                    //若不存在，也没必要进行索引列参数校验
                    return false;
                //存在则不需要参数类型校验
                break;
            }
            case "insert": {
                if (allowInsert(tagColumnMap, curRecordMap, tableName, existIndex, notAllowedNullFields))
                    return false;
                break;
            }
            case "update": {
                if (allowUpdate(tagColumnMap, curRecordMap, tableName, existIndex)) return false;
                break;
            }
            case "save": {
                if (!existIndex) {
                    if (allowInsert(tagColumnMap, curRecordMap, tableName, existIndex, notAllowedNullFields))
                        return false;
                } else {
                    if (allowUpdate(tagColumnMap, curRecordMap, tableName, existIndex)) return false;
                    //更新校验
                }
                break;
            }
        }
        return true;
    }


    private boolean allowUpdate(Map<Integer, String> tagColumnMap, Map<Integer, String> curRecordMap, String tableName, boolean existIndex) throws Exception {
        if (!existIndex)
            return true;
        else {
            //检验Xstep消息提供的字段
            if (!isCheckedByFieldsProvided(tagColumnMap, curRecordMap, tableName))
                return true;
        }
        return false;
    }

    private boolean allowInsert(Map<Integer, String> tagColumnMap, Map<Integer, String> curRecordMap, String tableName, boolean existIndex, List<String> notAllowedNullFields) throws Exception {
        if (existIndex)
            return true;
        else {
            //字段非空,先校验包含所有非空字段
            Collection<String> columnCol = tagColumnMap.values();
            if (!columnCol.containsAll(notAllowedNullFields))
                return true;
            // 然后检验所有的输入值的非空字段不为空（包含在字段校验里）
            if (!isCheckedByFieldsProvided(tagColumnMap, curRecordMap, tableName))
                return true;
        }
        return false;
    }

    /**
     * 根据表名获取非空字段
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public List<String> getNotAllowedNullFields(String tableName) throws Exception {
        List<String> res = new ArrayList<>();
        Map<String, Map<String, String>> fieldsSchemas = tableInfoHelper.getTableAllColumnsSchemas(tableName, "%");
        Set<String> fieldSet = fieldsSchemas.keySet();
        for (String field : fieldSet) {
            if (fieldsSchemas.get(field).get("is_nullable").equals(false) && fieldsSchemas.get(field).get("default") == null)
                res.add(field);
        }
        return res;

    }


    //TODO
    //前端选择，根据表名获得一个唯一索引，待完成
    //{列名，列值}
    public Map<String, String> getIndexMap(String tableName) {
        Map<String, String> indexMap = new HashMap<>();
        return indexMap;
    }


    public boolean existUniqueIndexFields(String tableName, Map<Integer, String> tagColumnMap) {
        if (tagColumnMap == null || tableName == null)
            return false;
        Collection<String> indexList = getIndexMap(tableName).values();
        Collection<String> columnCol = tagColumnMap.values();
        if (columnCol.containsAll(indexList))
            return true;
        else
            return false;
    }


    public boolean verifyField(String fieldVal, Map<String, String> schemasMap) {
        if (schemasMap == null)
            return false;
        String typeLimit = schemasMap.get("type").toUpperCase();
        String sizeLimit = schemasMap.get("size");
        String nullableLimit = schemasMap.get("is_nullable");
        String decimalLimit = schemasMap.get("decimalDigital");
        //验证是否为空
        if (!fieldsCheckingHelper.isNullableRight(fieldVal, nullableLimit))
            return false;
        //验证类型和大小
        if (!fieldsCheckingHelper.isTypeAndSizeRight(fieldVal, typeLimit, sizeLimit, decimalLimit))
            return false;
        return true;
    }
}


