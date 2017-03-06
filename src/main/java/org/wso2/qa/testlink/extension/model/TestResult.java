package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;

import java.util.Date;

/**
 * Represents a test result of a test case.
 */

public class TestResult {

    private String platform;
    private String testMethod;
    private String status;
    private int testCaseId;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                ", platform='" + platform + '\'' +
                ", testMethod='" + testMethod + '\'' +
                ", status='" + status + '\'' +
                ", testCaseId=" + testCaseId +
                '}';
    }

}
