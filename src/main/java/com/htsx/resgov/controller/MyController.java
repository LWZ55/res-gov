package com.htsx.resgov.controller;


import com.htsx.resgov.JdbcUtil.TableInfoHelper;
import com.htsx.resgov.service.TestMy;
import com.htsx.resgov.JdbcUtil.TagMappingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class MyController {

    @Autowired
    TableInfoHelper jdbcTables;

    @Autowired
    private TestMy testMy;

    @Autowired
    private TagMappingHelper tagMappingHelper;


//    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
//    public int test(@PathVariable Integer id){
//        System.out.println("id:" + id);
//        return testMy.insertTest(id);
//    }


    @RequestMapping("/schema")
    @ResponseBody
    public Map<String, Map<String,String>> out() throws  Exception{

        System.out.println(tagMappingHelper.getTableNameBySysNameAndClassId("OMS","100"));
        System.out.println(tagMappingHelper.getColumnNameFromTagAndTableName("exchangeorder","11418"));
        System.out.println(tagMappingHelper.getColumnNameFromTagAndTableName("exchangeorder","1148"));
        System.out.println(jdbcTables.getTableAllColumns("exchangeorder").size());


        System.out.println(tagMappingHelper.getIndexCountBySql("select count(0) from fieldsinfo"));


        System.out.println(tagMappingHelper.getFieldsInfo());
        //表名和列名
       return jdbcTables.getTableAllColumnsSchemas("exchangeOrder","%");


    }

    @RequestMapping("/index")
    @ResponseBody
    public Map<String, List<String>> out1() throws  Exception{

        return jdbcTables.getTableIndexInfo("exchangeOrder");
    }
}
