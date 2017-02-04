package org.wso2.qa.testlink.extension.model;

import java.util.ArrayList;

/**
 * Reads test results from the database and update TestLink accordingly.
 */
public class TestResultsUpdater {

    public void update(){

        TestResultsRepository testResultsRepository = new TestResultsRepository();
        try {
            ArrayList<TestResult> testResults = testResultsRepository.getResults();

            for(int i = 0; i < testResults.size(); i++){

                System.out.println("##### Result Array index " + i + " #####");
                System.out.println(testResults.get(i).getProduct());
                System.out.println(testResults.get(i).getVersion());
                System.out.println(testResults.get(i).getBuildNo());
                System.out.println(testResults.get(i).getTestMethod());
                System.out.println(testResults.get(i).getStatus());
                System.out.println(testResults.get(i).getTimestamp());
                System.out.println(testResults.get(i).getPlatform());
            }

        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        TestLinkClient testLinkClient = new TestLinkClient();
        //Todo - testLinkClient.getTestPlan() method







    }
}
