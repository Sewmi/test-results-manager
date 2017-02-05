package org.wso2.qa.testlink.extension.model;

import java.sql.*;
import java.util.HashMap;

/**
 * Represents the test results database which is populated at the end of a Jenkins build.
 */
public class TestResultsRepository {

    public HashMap<String, TestResult> getResults() throws RepositoryException {
        
        HashMap<String, TestResult> testResults = new HashMap<String, TestResult>();

        try{
            // Loading MySQL JDBC driver.
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            //TODO : log this exception
            throw new RepositoryException("Database driver could not be loaded", e);
        }

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            // TODO : Get the database configs form a config file.
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_data", "root", "root");
            statement = connection.createStatement();
            String sqlQuery = "SELECT * FROM integration_tests";
            resultSet = statement.executeQuery(sqlQuery);

            //Extracting data from the result set
            while(resultSet.next()){
                TestResult testResult = new TestResult();

                testResult.setProduct(resultSet.getString("product"));
                testResult.setVersion(resultSet.getString("version"));
                testResult.setPlatform(resultSet.getString("platform"));
                testResult.setTestMethod(resultSet.getString("test"));
                testResult.setTimestamp(resultSet.getString("timestamp"));
                testResult.setStatus(resultSet.getString("status"));
                testResult.setBuildNo(resultSet.getLong("buildNo"));

                testResults.put(testResult.getTestMethod(),testResult);
            }

        } catch (SQLException e) {
            //TODO Log this exception
            throw new RepositoryException("Cannot fetch results from the results database", e);
        }finally {
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException ignore) {}
            }

            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException ignore) {}
            }

            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException ignore) {}
            }
        }

        return testResults;
    }
}
