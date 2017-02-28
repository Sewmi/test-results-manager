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
    TestPlan testPlan;
    private Platform[] platforms;

    public TestLinkClient(String testPlanName, String projectName, long buildNo) throws TestLinkException {

        this.testPlanName = testPlanName;
        this.projectName = projectName;
        this.buildNo = buildNo;
        this.init();
    }

    public void init() throws TestLinkException {
        // Connect to TestLink
        testLinkAPI = connectToTestLink();

        // Fetch master data
        testPlan = testLinkAPI.getTestPlanByName(testPlanName, projectName);
        platforms = testLinkAPI.getTestPlanPlatforms(testPlan.getId());
    }

    public TestLinkAPI connectToTestLink() throws TestLinkException {
        //TODO: Read URL, devkey from a configuration file
        String url = "http://192.168.48.112/lib/api/xmlrpc/v1/xmlrpc.php";
        String devKey = "314b1563861a71354bdfe5de96b91ff5";
        TestLinkAPI api = null;

        URL testlinkURL = null;

        try     {
            testlinkURL = new URL(url);
            api = new TestLinkAPI(testlinkURL, devKey);
        } catch ( MalformedURLException e )   {
            //TODO Log this exception (MalformedURLException)
            throw new TestLinkException("TestLink connection URL is incorrect", e);
        } catch( TestLinkAPIException e) {
            //TODO Log this exception (TestLinkAPIException)
            throw new TestLinkException("Cannot connect to TestLink",e);
        }

        return api;
    }

    // Returns array of test cases retrieve from a test plan
    public TestCase[] getTestCases() throws TestLinkException{

        Build build = testLinkAPI.createBuild(testPlan.getId(), Long.toString(buildNo), "Automatically Created by Test-Results-Manager.");
        buildID = build.getId();

        //Get the list of automated test cases from the test plan
        TestCase[] automatedTestCases = testLinkAPI.getTestCasesForTestPlan(
                testPlan.getId(),
                null,       /* test cases IDs*/
                buildID,    /* build ID */
                null,       /* key words IDs */
                null,   /* Key words */
                false,  /* executed */
                null,   /* assigned To */
                null,   /* Executed status */
                ExecutionType.AUTOMATED,
                false,                  /*get step info */
                TestCaseDetails.FULL); /* Test case Detail */


        TestProject testProject = testLinkAPI.getTestProjectByName(projectName);

        for (int i=0 ;i< automatedTestCases.length ;i++){
            //Todo: remove ptint
            //System.out.println("\n Array index : " + i);

            //Assign custom fields
            CustomField customField = testLinkAPI.getTestCaseCustomFieldDesignValue(
                    automatedTestCases[i].getId(),
                    null, /* testCaseExternalId */
                    automatedTestCases[i].getVersion(),
                    testProject.getId(),
                    "automationTestCase",
                    ResponseDetails.FULL);

            //Adding a custom field to get custom field array.
            automatedTestCases[i].getCustomFields().add(customField);

            //ToDo Print statements are to be removed (added for testing purposes)
            /*
            System.out.println(" TestCase ID : " + automatedTestCases[i].getId());
            System.out.println("Test Case Name" + automatedTestCases[i].getName());
            System.out.println("getCustomFields : " + automatedTestCases[i].getCustomFields().get(0));
            */

        }

        //Todo: This is for testing perpusos.
        /*
        api.setTestCaseExecutionResult(automatedTestCases[0].getId(),
                null,   //test case external id
                testPlanID, // test plan id
                ExecutionStatus.PASSED, // Executed status
                buildID,        // build ID
                Long.toString(buildID),  // Build name
                null,
                null,
                null,
                49,     // platform ID
                null,
                null,
                null);
        */

        return automatedTestCases;

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

    public int getPlatformIdByName(String platformName) throws TestLinkException {
        for (Platform platform : this.platforms ){
            if (platform.getName().equals(platformName)){
                return platform.getId();
            }
        }
        return -1;
    }


    public void updateTestExecution(List<TestResult> testResultList) throws TestLinkException {

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
    //Todo: remove main method (added for testing purposes)
    /*
    public static void main(String[] args) throws TestLinkException {


        TestCase[] testCases = new TestLinkClient("sewmiNewPlan","TestSample", 1107).getTestCases();


    } */
}
