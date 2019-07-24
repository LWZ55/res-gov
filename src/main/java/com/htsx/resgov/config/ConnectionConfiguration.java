package com.htsx.resgov.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ConnectionConfiguration {

    @Autowired
    private ComboPooledDataSource comboPooledDataSource;

    @Bean
    public Connection getConnection(){
        Connection connection = null;

        try {
            connection = comboPooledDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
