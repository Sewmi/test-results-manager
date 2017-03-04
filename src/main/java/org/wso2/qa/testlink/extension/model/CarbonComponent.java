package org.wso2.qa.testlink.extension.model;

/**
 * Store carbon component information that is needed when querying testResultRepository.
 */

public class CarbonComponent {

    private String componentName;
    private String componentVersion;

    public CarbonComponent(String componentName, String componentVersion) {
        this.componentName = componentName;
        this.componentVersion = componentVersion;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentVersion() {
        return componentVersion;
    }

}
