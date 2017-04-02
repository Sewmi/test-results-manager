package org.wso2.qa.testlink.extension.model;


import br.eti.kinoshita.testlinkjavaapi.model.Platform;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reads test results from the database and update TestLink accordingly.
 */

public class TestResultsUpdater {

    private static Logger LOGGER = Logger.getLogger(TestResultsUpdater.class);

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
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(String.format("Retrieving test results for the build number '%d' and carbon components '%s'", buildNo, carbonComponents));
            }

            testResults = testResultsRepository.getResults(buildNo,carbonComponents);

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(String.format("Retrieved %d test results for the build number '%d' and carbon components '%s'", testResults.size(), buildNo, carbonComponents));
            }
        } catch (RepositoryException e) {
            throw new TestResultsManagerException("Error occurred while retrieving test results from the repository", e);
        }

        TestLinkClient testLinkClient = null;
        try {
            testLinkClient = new TestLinkClient(testPlanName, projectName, buildNo);
        } catch (TestLinkException e) {
            throw new TestResultsManagerException("Error occurred while connecting to TestLink", e);
        }

        TestCase[] testCases = null;
        try {
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(String.format("Retrieving test cases for the test plan name '%s' and the project name '%s'", testPlanName, projectName));
            }

            testCases = testLinkClient.getTestCases();

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(String.format("Retrieved %d test cases for the test plan name '%s' and the project name '%s'", testCases.length, testPlanName, projectName));
            }
        } catch (TestLinkException e) {
            throw new TestResultsManagerException("Error occurred while retrieving test cases from TestLink", e);
        }

        Platform[] platforms = testLinkClient.getPlatforms();

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Available platforms : " + StringUtils.join(platforms, ","));
        }

        Processor processor = new Processor(testResults,testCases,platforms);
        List <TestResult> updatedTestResults  =  processor.getProcessedResults();

        try {
            testLinkClient.updateTestExecution(updatedTestResults);
        } catch (TestLinkException e) {
            throw new TestResultsManagerException("Error occurred while updating test execution results in TestLink", e);
        }

        //Todo To be removed : Added to print test execution object arrayList
        for (TestResult executionResult : updatedTestResults){
            System.out.println("ExecutionResult : " + executionResult.toString() + "\n" );
        }
    }
}
