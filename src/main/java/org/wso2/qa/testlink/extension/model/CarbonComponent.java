package org.wso2.qa.testlink.extension.model;

/**
 * Stores carbon component information.
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

    @Override
    public String toString() {
        return "CarbonComponent{" +
                "componentName='" + componentName + '\'' +
                ", componentVersion='" + componentVersion + '\'' +
                '}';
    }
}
