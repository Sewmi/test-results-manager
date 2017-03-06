package org.wso2.qa.testlink.extension.model;

/**
 * Custom exception for repository handling
 */
public class RepositoryException extends Exception {

    public RepositoryException(String message, Throwable e) {
        super(message, e);
    }
}
