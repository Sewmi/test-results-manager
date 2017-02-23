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

    public ExecutionStatus getStatus() {
        ExecutionStatus executionStatus;
        //Todo: Replace Pass/ Fail with actual statuses written into database (check whether there is a skip)
        if (status=="PASS"){
            executionStatus = ExecutionStatus.PASSED;
            return executionStatus;
        } else if(status=="FAIL"){
            executionStatus = ExecutionStatus.FAILED;
            return executionStatus;
        }else {
            executionStatus = ExecutionStatus.NOT_RUN;
            return executionStatus;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //Overrriding toString method to print values inside result object instead of toString value given for object when printing hash map value.
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
                '}';
    }
}
