package com.htsx.resgov.controller;


import com.htsx.resgov.JdbcUtil.TableInfoHelper;
import com.htsx.resgov.service.TestMy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class MyController {

    @Autowired
    TableInfoHelper jdbcTables;

    @Autowired
    private TestMy testMy;


    @RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    public int test(@PathVariable Integer id){
        System.out.println("id:" + id);
        return testMy.insertTest(id);
    }


    @RequestMapping("/schema")
    @ResponseBody
    public Map<String, Map<String,String>> out() throws  Exception{


        //表名和列名
       return jdbcTables.getTableAllColumnsSchemas("exchangeOrder","%");


    }

    @RequestMapping("/index")
    @ResponseBody
    public Map<String, List<String>> out1() throws  Exception{

        //jdbcTables.getDDLStatements("t_user");
        //return jdbcTables.getTableAllColumnsSchemas("exchangeOrder","%");

        return jdbcTables.getTableIndexInfo("exchangeOrder");
    }
}
