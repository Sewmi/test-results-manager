package org.wso2.qa.testlink.extension.web;

import org.apache.log4j.Logger;
import org.wso2.qa.testlink.extension.model.TestResultsManagerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The error handler servlet, which logs the exception properly and sends out the error message.
 */
public class ErrorHandlerServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ErrorHandlerServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");

        if (servletName == null){
            servletName = "Unknown";
        }

        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null){
            requestUri = "Unknown";
        }

        String responsePayload = null;
        String errorMessage = String.format("Error occurred while serving the request.\n\tStatus Code : '%d'\n\tServlet Name : '%s'" +
                "\n\tRequest URI : '%s'", statusCode, servletName, requestUri);

        // If the immediate cause is a TestResultsManagerException, the log message and the response payload should be
        // based on whether the error is a runtime error or not.
        // Runtime errors are not logged in this class since they should be logged where they originated and
        // the response payload should contain any 'Exception' related info.
        if(throwable instanceof TestResultsManagerException){

            TestResultsManagerException testResultsManagerException = (TestResultsManagerException) throwable;

            if(!testResultsManagerException.isRuntimeError()){
                responsePayload = String.format("{\"reason\":\"%s\", \"exceptionClass\":\"%s\"}",
                        throwable.getMessage(), throwable.getCause().getClass().getName());
                LOGGER.error(errorMessage, throwable);
            }else{
                responsePayload = String.format("{\"reason\":\"%s\"}", testResultsManagerException.getMessage());
            }
        }else{
            responsePayload = String.format("{\"reason\":\"%s\", \"exceptionClass\":\"%s\"}",
                    throwable.getMessage(), throwable.getClass().getName());
            LOGGER.error(errorMessage, throwable);
        }

        // Set response content type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(responsePayload);
    }
}
