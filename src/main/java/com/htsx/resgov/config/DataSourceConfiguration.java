package com.htsx.resgov.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;


/**
 * 数据库配置类
 */
@Configuration
public class DataSourceConfiguration {

    static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;




    @Bean(name = "dataSouce")
    public ComboPooledDataSource createDataSouce() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(jdbcDriver);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        //关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        return dataSource;
    }





    public ComboPooledDataSource createDataSouce(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword) {

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        jdbcUrl = jdbcDriver + "serverTimezone=CTT";
        try {
            dataSource.setDriverClass(jdbcDriver);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUser(jdbcUsername);
            dataSource.setPassword(jdbcPassword);
            //关闭连接后不自动commit
            dataSource.setAutoCommitOnClose(false);
        } catch (Exception e) {
            logger.error(jdbcUrl + "connect fails", e);
        }
        return dataSource;
    }
}
