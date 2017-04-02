package org.wso2.qa.testlink.extension.web;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.wso2.qa.testlink.extension.model.Configurations;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Servlet context listener which does the initializations tasks.
 */
public class ContextListener implements ServletContextListener{

    private static final Logger LOGGER = Logger.getLogger(ContextListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        Configurations configurations = Configurations.getInstance();

        Properties properties = new Properties();

        InputStream propertiesFileInputStream = ContextListener.class.getClassLoader().getResourceAsStream("test-results-manager.properties");

        try {
            properties.load(propertiesFileInputStream);
            configurations.init(properties);
        } catch (IOException e) {
            LOGGER.error("Could not read the configurations.", e);

            // Try to stop the application starting, by throwing an exception.
            throw new RuntimeException("Could not read the configurations.", e);
        }

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        MDC.clear();
    }
}
