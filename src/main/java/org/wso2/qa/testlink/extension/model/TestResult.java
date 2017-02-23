package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;

import java.util.Date;

/**
 * Represents a test result of a test case.
 */
public class TestResult {

    private String product;
    private String version;
    private long buildNo;
    private String platform;
    private String testMethod;
    private String timestamp;
    private String status;

    public int getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    private int testCaseId;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {

        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {

        this.version = version;
    }

    public long getBuildNo() {
        return buildNo;
    }

    public void setBuildNo(long buildNo) {

        this.buildNo = buildNo;
    }

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TestResult{" +
                "product='" + product + '\'' +
                ", version='" + version + '\'' +
                ", buildNo=" + buildNo +
                ", platform='" + platform + '\'' +
                ", testMethod='" + testMethod + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", status='" + status + '\'' +
                ", testCaseId=" + testCaseId +
                '}';
    }

}
