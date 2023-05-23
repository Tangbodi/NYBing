package com.example.demo.Config;

import com.example.demo.Util.JDBCUtils;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(MyServletContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent){
        logger.info("Webservice start:::");
    }
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent){
        logger.info("Webservice stop:::");
        try{
            while(DriverManager.getDrivers().hasMoreElements()){
                DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
            }
            logger.info("JDBC driver close:::");
            AbandonedConnectionCleanupThread.checkedShutdown();
            JDBCUtils.getDataSource().close();
            logger.info("Cleaned thread:::");
        }catch (SQLException e) {
            logger.error(e.getMessage(),e);
        }
    }
}
