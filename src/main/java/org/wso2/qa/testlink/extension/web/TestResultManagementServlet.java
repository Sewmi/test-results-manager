package org.wso2.qa.testlink.extension.web;

import org.apache.commons.lang.StringUtils;
import org.wso2.qa.testlink.extension.model.CarbonComponent;
import org.wso2.qa.testlink.extension.model.TestResultsManagerException;
import org.wso2.qa.testlink.extension.model.TestResultsUpdater;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Triggers TestLink update process
 */

public class TestResultManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String projectName = request.getParameter("projectName");
        String testPlanName = request.getParameter("testPlanName");
        long buildNo = Long.parseLong(request.getParameter("buildNo"));
        String componentsQueryParameterValue = request.getParameter("components");
        List<CarbonComponent> carbonComponents = new ArrayList<CarbonComponent>();

        String[] dependencies = componentsQueryParameterValue.split("-");
        String[] componentInfo;

        for (String dependency : dependencies ){
            if(StringUtils.isNotBlank(dependency)){
                componentInfo = dependency.split(":");
                CarbonComponent carbonComponent = new CarbonComponent(componentInfo[1],componentInfo[3]);
                carbonComponents.add(carbonComponent);
            }
        }

        TestResultsUpdater testResultsUpdater = new TestResultsUpdater(projectName, testPlanName,buildNo, carbonComponents);
        try {
            testResultsUpdater.update();
        } catch (TestResultsManagerException e) {
            e.printStackTrace();
            response.sendError(500, e.getMessage());

        }

        response.setContentType("application/json");

        // Respond with the update status.
        PrintWriter out = response.getWriter();
        out.println("{\"Status\":\"Successful\"}");

    }
}
