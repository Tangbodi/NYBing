package com.example.demo.Util;

import com.example.demo.Service.CategoryService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class JDBCUtils {
    private static final Logger logger = LoggerFactory.getLogger(JDBCUtils.class);

    private static HikariDataSource dataSource;

    static {
        try {
            Properties properties = new Properties();
            properties.load(JDBCUtils.class.getClassLoader().getResourceAsStream("hikari.properties"));
            HikariConfig config = new HikariConfig(properties);
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}

