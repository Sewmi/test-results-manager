package org.wso2.qa.testlink.extension.model;

import java.util.Properties;

/**
 * Configurations for the repositories etc.
 */
public class Configurations {

    private static Configurations instance;
    private String databaseName;
    private String tableName;
    private String databaseUsername;
    private String databasePassword;
    private String databaseHost;
    private String databasePort;
    private String testLinkAPIURL;
    private String testLinkAPIKey;
    private String developerTestCustomFieldName;
    private String systemTestCustomFieldName;
    private String uiTestCustomFieldName;


    private Configurations() {

    }

    public static Configurations getInstance() {
        if (instance == null) {
            synchronized (Configurations.class) {
                if (instance == null) {
                    instance = new Configurations();
                }
            }
        }
        return instance;
    }

    public void init(Properties properties) {
        databaseHost = properties.getProperty("resultsDatabase.host", "localhost");
        databasePort = properties.getProperty("resultsDatabase.port", "3306");
        databaseName = properties.getProperty("resultsDatabase.name");
        tableName = properties.getProperty("resultsDatabase.tableName");
        databaseUsername = properties.getProperty("resultsDatabase.username");
        databasePassword = properties.getProperty("resultsDatabase.password");
        testLinkAPIURL = properties.getProperty("testLink.APIURL");
        testLinkAPIKey = properties.getProperty("testLink.APIKey");
        developerTestCustomFieldName = properties.getProperty("testlink.customField.test.dev");
        systemTestCustomFieldName = properties.getProperty("testlink.customField.test.system");
        uiTestCustomFieldName = properties.getProperty("testlink.customField.test.ui");
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getTestLinkAPIURL() {
        return testLinkAPIURL;
    }

    public String getTestLinkAPIKey() {
        return testLinkAPIKey;
    }

    public String getIntegrationTestCustomFieldName() {
        return systemTestCustomFieldName;
    }

    public String getUnitTestCustomFieldName() {
        return developerTestCustomFieldName;
    }

    public String getUiTestCustomFieldName() {
        return uiTestCustomFieldName;
    }
}
