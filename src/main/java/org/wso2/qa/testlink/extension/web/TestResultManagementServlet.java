package org.wso2.qa.testlink.extension.web;

import org.wso2.qa.testlink.extension.model.TestResultsUpdater;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Triggers TestLink update process
 */

public class TestResultManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        TestResultsUpdater testResultsUpdater = new TestResultsUpdater();
        testResultsUpdater.update();

        response.setContentType("application/json");

        // Respond with the update status.
        PrintWriter out = response.getWriter();
        out.println("{\"Status\":\"Successful\"}");

    }
}
