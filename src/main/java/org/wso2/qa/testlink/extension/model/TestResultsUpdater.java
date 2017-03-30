package org.wso2.qa.testlink.extension.model;


import br.eti.kinoshita.testlinkjavaapi.model.Platform;
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
    private  List<CarbonComponent> carbonComponents = new ArrayList<CarbonComponent>();

    public TestResultsUpdater(String projectName, String testPlanName, long buildNo, List<CarbonComponent> carbonComponents) {

        this.projectName = projectName;
        this.testPlanName = testPlanName;
        this.buildNo = buildNo;
        this.carbonComponents = carbonComponents;
    }

    public void update() throws TestResultsManagerException {

        TestResultsRepository testResultsRepository = new TestResultsRepository();

        Map<String, List<TestResult>> testResults = null;

        try {
            testResults = testResultsRepository.getResults(buildNo,carbonComponents);

        } catch (RepositoryException e) {
            throw new TestResultsManagerException("Cannot fetch test results from the repository", e);
        }

        TestLinkClient testLinkClient = null;
        try {
            testLinkClient = new TestLinkClient(testPlanName, projectName, buildNo);
        } catch (TestLinkException e) {
            e.printStackTrace();
        }

        TestCase[] testCases = null;
        try {
            testCases = testLinkClient.getTestCases();
        } catch (TestLinkException e) {
            //TODO: Handle exception
            e.printStackTrace();
        }

        Platform[] platforms = testLinkClient.getPlatforms();
        Processor processor = new Processor(testResults,testCases,platforms);
        // updated test cases
        List <TestResult> updatedTestResults  =  processor.getProcessedResults();

        try {
            testLinkClient.updateTestExecution(updatedTestResults);
        } catch (TestLinkException e) {
            // TODO : Handle this exception
            e.printStackTrace();
        }

        //Todo To be removed : Added to print test execution object arrayList
        for (TestResult executionResult : updatedTestResults){
            System.out.println("ExecutionResult : " + executionResult.toString() + "\n" );
        }
    }
}
