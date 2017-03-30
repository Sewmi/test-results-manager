package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.ResponseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * To retrieve and manipulate test plans in TestLink.
 */

public class TestLinkClient {

    private String projectName;
    private String testPlanName;
    private long buildNo;
    private int buildID;

    private TestLinkAPI testLinkAPI;
    private TestPlan testPlan;
    private Platform[] platforms;

    public TestLinkClient(String testPlanName, String projectName, long buildNo) throws TestLinkException {

        this.testPlanName = testPlanName;
        this.projectName = projectName;
        this.buildNo = buildNo;
        this.init();
    }

    // Returns array of test cases retrieve from a test plan
    public TestCase[] getTestCases() throws TestLinkException{

        //Get the list of automated test cases from the test plan
        TestCase[] automatedTestCases = testLinkAPI.getTestCasesForTestPlan(
                testPlan.getId(),
                null,       /* test cases IDs*/
                null,    /* build ID */
                null,       /* key words IDs */
                null,   /* Key words */
                false,  /* executed */
                null,   /* assigned To */
                null,   /* Executed status */
                ExecutionType.AUTOMATED,
                false,                  /*get step info */
                TestCaseDetails.FULL); /* Test case Detail */

        TestProject testProject = testLinkAPI.getTestProjectByName(projectName);


        // NOTE : TestLink API doesn't add custom fields to the test cases when querying.
        //        So we need to do another API call to fetch those.

        for (TestCase automatedTestCase: automatedTestCases){
            CustomField customFieldForIntegrationTestMethods = testLinkAPI.getTestCaseCustomFieldDesignValue(
                    automatedTestCase.getId(),
                    null, /* testCaseExternalId */
                    automatedTestCase.getVersion(),
                    testProject.getId(),
                    Constants.CUSTOM_FIELD_INTEGRATION_TEST,
                    ResponseDetails.FULL);

            //Adding the custom for integration tests.
            automatedTestCase.getCustomFields().add(customFieldForIntegrationTestMethods);

            CustomField customFieldForUnitTestMethods = testLinkAPI.getTestCaseCustomFieldDesignValue(
                    automatedTestCase.getId(),
                    null, /* testCaseExternalId */
                    automatedTestCase.getVersion(),
                    testProject.getId(),
                    Constants.CUSTOM_FIELD_UNIT_TEST,
                    ResponseDetails.FULL);

            //Adding the custom field for unit tests.
            automatedTestCase.getCustomFields().add(customFieldForUnitTestMethods);

        }

        return automatedTestCases;
    }

    public Platform[] getPlatforms(){
        return platforms;
    }

    public void updateTestExecution(List<TestResult> testResultList) throws TestLinkException {

        createBuild();
        List<TestResult> testResults = testResultList;
        TestLinkAPI api = connectToTestLink();

        for (TestResult testResult : testResults){

            int platformId = getPlatformIdByName(testResult.getPlatform());

            if (platformId != -1){

                ExecutionStatus executionStatus = convertToExecutionStatus(testResult.getStatus());
                if(!executionStatus.equals(ExecutionStatus.NOT_RUN)){
                    ReportTCResultResponse response = api.setTestCaseExecutionResult(testResult.getTestCaseId(),
                            null,   //test case external id
                            testPlan.getId(), // test plan id
                            executionStatus, // Executed status
                            buildID,        // build ID
                            Long.toString(buildID),  // Build name
                            null,
                            null,
                            null,
                            platformId,
                            null,
                            null,
                            null);
                }
            }else{
                //Todo This is for temporary error handling
                System.out.println("No platform id found for :" + testResult.getPlatform() );
            }
        }
    }

    private void init() throws TestLinkException {
        testLinkAPI = connectToTestLink();
        // Fetch master data
        testPlan = testLinkAPI.getTestPlanByName(testPlanName, projectName);
        platforms = testLinkAPI.getTestPlanPlatforms(testPlan.getId());
    }

    private TestLinkAPI connectToTestLink() throws TestLinkException {
        //TODO: Read URL, devkey from a configuration file
        String url = "http://192.168.48.112/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "314b1563861a71354bdfe5de96b91ff5";
        TestLinkAPI api = null;
        URL testLinkURL = null;

        try     {
            testLinkURL = new URL(url);
            api = new TestLinkAPI(testLinkURL, devKey);
        } catch ( MalformedURLException e )   {
            //TODO Log this exception (MalformedURLException)
            throw new TestLinkException("TestLink connection URL is incorrect", e);
        } catch( TestLinkAPIException e) {
            //TODO Log this exception (TestLinkAPIException)
            throw new TestLinkException("Cannot connect to TestLink",e);
        }
        return api;
    }

    private void createBuild() {
        Build build = testLinkAPI.createBuild(testPlan.getId(), Long.toString(buildNo), "Automatically Created by Test-Results-Manager.");
        buildID = build.getId();
    }

    private ExecutionStatus convertToExecutionStatus(String status){

        if (status.equals("PASS")) {
            return ExecutionStatus.PASSED;

        } else if (status.equals("FAIL")) {
            return ExecutionStatus.FAILED;

        }else{
            return ExecutionStatus.NOT_RUN;
        }
    }

    private int getPlatformIdByName(String platformName) throws TestLinkException {

        for (Platform platform : this.platforms ){
            if (platform.getName().equals(platformName)){
                return platform.getId();
            }
        }
        return -1;
    }
}
