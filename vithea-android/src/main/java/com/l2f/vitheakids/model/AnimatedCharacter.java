package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Claudia on 28/05/2017.
 * Updated by Soraia Meneses Alarcão on 20/07/2017
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimatedCharacter {

    @JsonProperty private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
