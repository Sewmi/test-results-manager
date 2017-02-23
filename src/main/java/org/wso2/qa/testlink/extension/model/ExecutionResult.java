package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;

/**
 * Represents a test case with updated test result
 */
public class ExecutionResult {

    private int testCaseId;
    private String platform;
    private ExecutionStatus executionStatus;

    public int getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "testCaseId=" + testCaseId +
                ", platform='" + platform + '\'' +
                ", executionStatus=" + executionStatus +
                '}';
    }
}
