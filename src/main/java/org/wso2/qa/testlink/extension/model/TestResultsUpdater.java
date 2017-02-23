package org.wso2.qa.testlink.extension.model;


import br.eti.kinoshita.testlinkjavaapi.model.TestCase;

import java.util.ArrayList;
import java.util.List;
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

    public void update() {

        TestResultsRepository testResultsRepository = new TestResultsRepository();
        //TODO:GET clarified (couldn't use without initializing)
        Map<String, List<TestResult>> testResults = null;

        try {
            testResults = testResultsRepository.getResults(buildNo);

            //Printing each map entry in the hash map.
            //TODO : Remove below for loop. (added for testing purposes)
            for (Map.Entry<String, List<TestResult>> entry : testResults.entrySet()) {
                System.out.println("map key : " + entry.getKey());

                for (int i = 0; i < (entry.getValue().size()); i++) {
                    System.out.println(" \n List item " + (i + 1));
                    System.out.println(entry.getValue().get(i));
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        TestLinkClient testLinkClient = new TestLinkClient(testPlanName, projectName, buildNo);

        TestCase[] testCases = new TestCase[0];
        try {
            testCases = testLinkClient.getTestCases();
        } catch (TestLinkException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }

        // G
        Processor processor = new Processor(testResults,testCases);
        // updated test cases
        List <TestResult> testResultList  = new ArrayList<TestResult>();
        testResultList = processor.getProcessedResults();

        //Todo To be removed : Added to print test execution object arrayList
        for (TestResult executionResult : testResultList){
            System.out.println("ExecutionResult : " + executionResult.toString() + "\n" );
        }
    }

    //Todo remove main method (added for testing purposes)
    public static void main(String[] args) throws TestLinkException {

        //TestCase[] testCases = new TestLinkClient("TestSamplePlan","TestSample", 1100).getTestCases();
        TestResultsUpdater resultsUpdater = new TestResultsUpdater("TestSample", "samplePlan1", 1110);
        resultsUpdater.update();
    }

}
