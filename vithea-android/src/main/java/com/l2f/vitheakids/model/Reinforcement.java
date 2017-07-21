package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Claudia on 07/06/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reinforcement {

    @JsonProperty private String reinforcementStrategy;
    @JsonProperty private Resource reinforcementResource;

    public String getReinforcementStrategy() {
        return reinforcementStrategy;
    }
    public Resource getReinforcementResource() {
        return reinforcementResource;
    }

    public void setReinforcementStrategy(String reinforcementStrategy) { this.reinforcementStrategy = reinforcementStrategy; }
    public void setReinforcementResource(Resource reinforcementResource) {this.reinforcementResource = reinforcementResource; }

}
