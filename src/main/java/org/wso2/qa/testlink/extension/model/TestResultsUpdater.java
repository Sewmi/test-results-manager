package org.wso2.qa.testlink.extension.model;


import java.util.*;

/**
 * Reads test results from the database and update TestLink accordingly.
 */
public class TestResultsUpdater {

    public void update(){

        TestResultsRepository testResultsRepository = new TestResultsRepository();

        try {
            HashMap<String, TestResult> testResults = testResultsRepository.getResults();

            //Printing each map entry in the hash map.
            for (Map.Entry<String, TestResult> entry: testResults.entrySet()){

                System.out.println("map key : " + entry.getKey() + "map value : " + entry.getValue() );
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        TestLinkClient testLinkClient = new TestLinkClient();
        //Todo - testLinkClient.getTestPlan() method







    }
}
