package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Claudia on 07/06/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prompting {

    @JsonProperty private String promptingStrategy;
    @JsonProperty private Boolean promptingColor;
    @JsonProperty private Boolean promptingSize;
    @JsonProperty private Boolean promptingScratch;
    @JsonProperty private Boolean promptingHide;
    @JsonProperty private Boolean promptingRead;

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
    public Boolean getPromptingRead() {
        return promptingRead;
    }

    public void setPromptingStrategy(String promptingStrategy) { this.promptingStrategy = promptingStrategy; }
    public void setPromptingColor(Boolean promptingColor) {this.promptingColor = promptingColor; }
    public void setPromptingSize(Boolean promptingSize) {this.promptingSize = promptingSize; }
    public void setPromptingScratch(Boolean promptingScratch) {this.promptingScratch = promptingScratch; }
    public void setPromptingHide(Boolean promptingHide) {this.promptingHide = promptingHide; }
    public void setPromptingRead(Boolean promptingRead) {
        this.promptingRead = promptingRead;
    }


}
