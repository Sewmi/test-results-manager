package org.wso2.qa.testlink.extension.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the test results database which is populated at the end of a Jenkins build.
 */
public class TestResultsRepository {

    public Map<String, List<TestResult>> getResults(long buildNo) throws RepositoryException {

        Map<String, List<TestResult>> testResults = new HashMap<String, List<TestResult>>();

        try {
            // Loading MySQL JDBC driver.
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            //TODO : log this exception
            throw new RepositoryException("Database driver could not be loaded", e);
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            // TODO : Get the database configs form a config file.
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_data", "root", "root");

            String queryForPreparedStatement = "SELECT * FROM integration_tests WHERE buildNo=?";
            preparedStatement = connection.prepareStatement(queryForPreparedStatement);
            preparedStatement.setLong(1, buildNo);
            resultSet = preparedStatement.executeQuery();

            //Extracting data from the result set
            while (resultSet.next()) {
                TestResult testResult = new TestResult();

                testResult.setProduct(resultSet.getString("product"));
                testResult.setVersion(resultSet.getString("version"));
                testResult.setPlatform(resultSet.getString("platform"));
                testResult.setTestMethod(resultSet.getString("test"));
                testResult.setTimestamp(resultSet.getString("timestamp"));
                testResult.setStatus(resultSet.getString("status"));
                testResult.setBuildNo(resultSet.getLong("buildNo"));

                //Get value for the given key
                List<TestResult> resultsForPlatforms = testResults.get(testResult.getTestMethod());

                //If Not exist a key value pair, add a map entry by setting a key and an empty list
                if (resultsForPlatforms == null) {
                    resultsForPlatforms = new ArrayList<TestResult>();
                    testResults.put(testResult.getTestMethod(), resultsForPlatforms);
                }

                //Add Result to the list
                resultsForPlatforms.add(testResult);
            }

        } catch (SQLException e) {
            //TODO Log this exception
            throw new RepositoryException("Cannot fetch results from the results database", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ignore) {
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ignore) {
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {
                }
            }
        }

        return testResults;
    }
}
