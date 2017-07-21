package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by silvi on 20/04/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

    @JsonProperty private String resourcePath;
    @JsonProperty private String resourceTypeDescription;

    public String getResourcePath() {
        return resourcePath;
    }
    public String getResourceTypeDescription() {
        return resourceTypeDescription;
    }

    public void setResourcePath(String resourcePath) { this.resourcePath = resourcePath; }
    public void setResourceTypeDescription(String resourceTypeDescription) {this.resourceTypeDescription = resourceTypeDescription; }

    public boolean isAnimation() {
        return resourceTypeDescription != null && resourceTypeDescription.equals("animation");
    }
}
