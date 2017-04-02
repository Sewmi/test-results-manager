package org.wso2.qa.testlink.extension.web;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
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
import java.util.UUID;

/**
 * Triggers TestLink update process
 */

public class TestResultManagementServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TestResultManagementServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Generate a request ID and put it in Log4j MDC for log co-relation.
        MDC.put("requestID", UUID.randomUUID().toString());

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug(String.format("Request received. Request URL : '%s'", request.getRequestURI()));
        }

        try{
            String projectName = getAndValidateQueryParam(request, "projectName", true);
            String testPlanName = getAndValidateQueryParam(request, "testPlanName", true);
            String buildNumberParameterValue = getAndValidateQueryParam(request, "buildNo", true);

            long buildNumber;
            try {
                buildNumber = Long.parseLong(buildNumberParameterValue);
            }catch (NumberFormatException nfe){
                throw new TestResultsManagerException("Build number (buildNo) should be an integer value");
            }

            String componentsQueryParameterValue = request.getParameter("components");

            if(LOGGER.isDebugEnabled()){
                LOGGER.debug(String.format("Project Name : '%s', Test Plan Name : '%s', Build Number : '%d'",
                        projectName, testPlanName, buildNumber));
                LOGGER.debug("'components' parameter value : " + componentsQueryParameterValue);
            }


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

            if(LOGGER.isDebugEnabled()){
                if(carbonComponents != null && !carbonComponents.isEmpty()){
                    LOGGER.debug("Component dependencies : " + StringUtils.join(carbonComponents, ","));
                }else{
                    LOGGER.debug("The product doesn't have component dependencies.");
                }
            }

            TestResultsUpdater testResultsUpdater = new TestResultsUpdater(projectName, testPlanName,buildNumber, carbonComponents);
            testResultsUpdater.update();

            response.setContentType("application/json");

            // Respond with the update status.
            PrintWriter out = response.getWriter();
            out.println("{\"Status\":\"Successful\"}");


        }catch (TestResultsManagerException e) {

            // Just wrap with a ServletException and throw.
            // The exception handling servlet will handler the rest.
            throw new ServletException(e);
        }finally {
            MDC.clear();
        }

    }

    private String getAndValidateQueryParam(HttpServletRequest request, String paramName, boolean isMandatory) throws TestResultsManagerException {

        String paramValue = request.getParameter(paramName);

        if(isMandatory && paramValue == null){
            throw new TestResultsManagerException(String.format("Mandatory parameter '%s' is missing in the request.", paramName));
        }else{
            return paramValue;
        }

    }
}
