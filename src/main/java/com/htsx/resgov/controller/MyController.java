package com.htsx.resgov.controller;


import com.htsx.resgov.JdbcOperation.JdbcTableSchemas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyController {

    @Autowired
    JdbcTableSchemas jdbcTables;


    @RequestMapping("/out")
    @ResponseBody
    public Map<String, Map<String,String>> out() throws  Exception{


        //表名和列名
       return jdbcTables.getTableAllColumnsSchemas("exchangeOrder","%");


    }

    @RequestMapping("/out1")
    @ResponseBody
    public Map<String, List<String>> out1() throws  Exception{

        //jdbcTables.getDDLStatements("t_user");
        //return jdbcTables.getTableAllColumnsSchemas("exchangeOrder","%");

        return jdbcTables.getTableIndexInfo("exchangeOrder");
    }
}
