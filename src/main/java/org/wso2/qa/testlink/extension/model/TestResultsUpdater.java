package org.wso2.qa.testlink.extension.model;


import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads test results from the database and update TestLink accordingly.
 */
public class TestResultsUpdater {

    private String projectName;
    private String testPlanName;
    private long buildNo;

    public TestResultsUpdater(String projectName, String testPlanName, long buildNo) {

        this.projectName = projectName;
        this.testPlanName = testPlanName;
        this.buildNo = buildNo;
    }

    public void update(){

        TestResultsRepository testResultsRepository = new TestResultsRepository();
        //TODO:GET clarified (couldn't use without initializing)
        HashMap<String, TestResult> testResults = null;

        try {
            testResults = testResultsRepository.getResults();

            //Printing each map entry in the hash map.
            //TODO : Remove below for loop. (added for testing purposes)
            for (Map.Entry<String, TestResult> entry: testResults.entrySet()){

                System.out.println("map key : " + entry.getKey() + "map value : " + entry.getValue() );
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        TestLinkClient testLinkClient = new TestLinkClient(testPlanName, projectName, buildNo);

        //TODO:GET clarified (couldn't use without initializing)
        TestCase[] testCases = new TestCase[0];
        try {
            testCases = testLinkClient.getTestCases();
        } catch (TestLinkException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }

        //Create Processor object
        Processor processor = new Processor(testResults,testCases);






    }
}
