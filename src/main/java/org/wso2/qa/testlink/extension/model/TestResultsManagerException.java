package org.wso2.qa.testlink.extension.model;

/**
 * Represent an error
 */
public class TestResultsManagerException extends Exception {

    public TestResultsManagerException(String message, Throwable e) {
        super(message, e);
    }

    public TestResultsManagerException(String message) {
        super(message);
    }

    public boolean isRuntimeError() {
        return this.getCause() == null;
    }

}
