package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by silvi on 20/04/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {
    @JsonProperty
    private String resourcePath;
    @JsonProperty
    private String resourceTypeDescription;

    public String getResourcePath() {
        return resourcePath;
    }

    public String getResourceType() {
        return resourceTypeDescription;
    }

    public boolean isAnimation() {
        return resourceTypeDescription != null && resourceTypeDescription.equals("animation");
    }
}
