package com.htsx.resgov.JdbcUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;



@Repository
public class TableInfoHelper {


    @Autowired
    private Connection connection;

    @Autowired
    private DatabaseMetaData dbMetaData;


    @Bean
    public DatabaseMetaData getDbMetaData() throws SQLException {
        return connection.getMetaData();
    }




    //返回数据库的表名，null表示所有
    public Set<String> getTableNames(String tableNamePattern) throws Exception {

        HashSet<String> tableNamesSet = new HashSet<>();
        ResultSet rs = dbMetaData.getTables(null, null, tableNamePattern, new String[]{"TABLE"});
        while (rs.next()) {
            tableNamesSet.add(rs.getString("TABLE_NAME"));
        }

        return tableNamesSet;
    }


    //找到数据库某个表的某个列的约束
    //只针对单个表：{列名：{约束名：约束值}}
    public Map<String, Map<String, String>> getTableAllColumnsSchemas(String tableNamePattern, String columnNamePattern) throws Exception {
        Map<String, Map<String, String>> fieldsSchemas = new HashMap<>();

        ResultSet rs = dbMetaData.getColumns(null, "%", tableNamePattern, columnNamePattern);
        while (rs.next()) {
            HashMap<String, String> oneFieldSchema = new HashMap<>();
            oneFieldSchema.put("type", rs.getString("TYPE_NAME"));
            oneFieldSchema.put("size", rs.getString("COLUMN_SIZE"));
            oneFieldSchema.put("remarks", rs.getString("REMARKS"));
            oneFieldSchema.put("is_nullable", rs.getString("IS_NULLABLE"));
            fieldsSchemas.put(rs.getString("COLUMN_NAME"), oneFieldSchema);
        }
        return fieldsSchemas;
    }

    public Set<String> getTableAllColumns(String tableNamePattern) throws Exception {
        Set<String> res = new HashSet<>();
        ResultSet rs = dbMetaData.getColumns(null, "%", tableNamePattern, "%");
        while (rs.next()) {
            res.add(rs.getString("COLUMN_NAME").toLowerCase());
        }
        return res;
    }




    //已知表名，列名，返回{约束字段：约束}
    public Map<String, String> getTableOneColumnSchemas(String tableNamePattern, String columnNamePattern) throws Exception {
        Map<String, String> fieldSchemas = new HashMap<>();

        ResultSet rs = dbMetaData.getColumns(null, "%", tableNamePattern, columnNamePattern);

        fieldSchemas.put("type", rs.getString("TYPE_NAME"));
        fieldSchemas.put("size", rs.getString("COLUMN_SIZE"));
        fieldSchemas.put("remarks", rs.getString("REMARKS"));
        fieldSchemas.put("decimalDigital", rs.getString("DECIMAL_DIGITS"));
        fieldSchemas.put("default", rs.getString(" COLUMN_DEF"));
        fieldSchemas.put("is_nullable", rs.getString("IS_NULLABLE"));


        return fieldSchemas;
    }


    //find unique key,including primary key
    //{联合索引名：（组成联合索引的各索引列表）}
    public Map<String, List<String>> getTableIndexInfo(String tableNamePattern) {
        Map<String, List<String>> tableIndexInfo = new HashMap<>();
        ResultSet rs = null;
        try {
            rs = dbMetaData.getIndexInfo(null, null, tableNamePattern, true, false);
            List<String> oneIndexInfo = new ArrayList<>();
            while (rs.next()) {

                //System.out.println(rs.getString("ORDINAL_POSITION"));  //分列名的index
                if (Integer.parseInt(rs.getString("ORDINAL_POSITION")) == 1)
                    oneIndexInfo = new ArrayList<>();

                oneIndexInfo.add(rs.getString("COLUMN_NAME")); //分列名

                tableIndexInfo.put(rs.getString("INDEX_NAME"), oneIndexInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return tableIndexInfo;
    }


    public void getDDLStatements(String tableName) {
        String sql = String.format("SHOW CREATE TABLE %s", tableName);//查询sql
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));//第一个参数获取的是tableName
                System.out.println(rs.getString(2));//第二个参数获取的是表的ddl语句
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != ps) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
