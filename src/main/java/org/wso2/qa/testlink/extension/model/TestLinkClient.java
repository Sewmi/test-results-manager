package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.ResponseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * To retrieve and manipulate test plans in TestLink.
 */

public class TestLinkClient {

    private String projectName;
    private String testPlanName;
    private long buildNo;
    private int buildID;
    private  int testPlanID;

    public TestLinkClient(String testPlanName, String projectName, long buildNo) {

        this.testPlanName = testPlanName;
        this.projectName = projectName;
        this.buildNo = buildNo;
    }

    // Returns array of test cases retrieve from a test plan
    public TestCase[] getTestCases() throws TestLinkException {

        //TODO: Read URL from a configuration file
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

        TestPlan testplan = api.getTestPlanByName(testPlanName, projectName);
        testPlanID = testplan.getId();

        Build build = api.createBuild(testPlanID, Long.toString(buildNo), "Automatically Created by Test-Results-Manager.");
        buildID = build.getId();

        //Get the list of automated test cases from the test plan
        TestCase[] automatedTestCases = api.getTestCasesForTestPlan(
                testPlanID,
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


        TestProject testProject = api.getTestProjectByName(projectName);

        for (int i=0 ;i< automatedTestCases.length ;i++){
            System.out.println("\n Array index : " + i);

            //Assign custom fields
            CustomField customField = api.getTestCaseCustomFieldDesignValue(
                    automatedTestCases[i].getId(),
                    null, /* testCaseExternalId */
                    automatedTestCases[i].getVersion(),
                    testProject.getId(),
                    "automationTestCase",
                    ResponseDetails.FULL);

            //ToDo Print statements are to be removed (added for testing purposes)
            automatedTestCases[i].getCustomFields().add(customField);
            System.out.println(" TestCase ID : " + automatedTestCases[i].getId());
            System.out.println("Test Case Name" + automatedTestCases[i].getName());
            System.out.println("getCustomFields : " + automatedTestCases[i].getCustomFields().get(0));

        }

        return automatedTestCases;
    }
    /* Todo: remove main method (added for testing purposes)
    public static void main(String[] args) throws TestLinkException {


        TestCase[] testCases = new TestLinkClient("TestSamplePlan","TestSample", 1100).getTestCases();


    }*/
}
