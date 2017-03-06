package org.wso2.qa.testlink.extension.model;

import java.net.MalformedURLException;

/**
 * Created by sewmini on 2/8/17.
 */
public class TestLinkException extends Exception {

    public TestLinkException(String message, Throwable e) {
        super(message,e);
    }
}
