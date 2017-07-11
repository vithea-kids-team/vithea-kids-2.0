package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Claudia on 07/06/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prompting {
    @JsonProperty
    private String promptingStrategy;
    @JsonProperty
    private Boolean promptingColor;
    @JsonProperty
    private Boolean promptingSize;
    @JsonProperty
    private Boolean promptingScratch;

    public String getPromptingStrategy() {
        return promptingStrategy;
    }

    public Boolean getPromptingColor() {
        return promptingColor;
    }

    public Boolean getPromptingSize() {
        return promptingSize;
    }

    public Boolean getPromptingScratch() {
        return promptingScratch;
    }

    public Boolean getPromptingHide() {
        return promptingHide;
    }

    @JsonProperty

    private Boolean promptingHide;
}
