package com.htsx.resgov.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.Properties;

@Intercepts(@Signature(type= StatementHandler.class,method="prepare",args={Connection.class,Integer.class}))
public class PluginForUniqueKey  implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //key
        //反射key

        //第一步：正则表达式匹配updatePrimaryKey方法
        //更改
        return null;
    }

    @Override
    public Object plugin(Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
