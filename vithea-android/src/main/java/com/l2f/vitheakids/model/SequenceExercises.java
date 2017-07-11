package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SequenceExercises {
    @JsonProperty
    private Long sequenceId;
    @JsonProperty
    private String sequenceName;
    @JsonProperty
    private List<Exercise> sequenceExercises;

    public Long getSequenceId() {
        return sequenceId;
    }

    public String getName() {
        return sequenceName;
    }

    public List<Exercise> getSequenceExercises() {
        return sequenceExercises;
    }

    @Override
    public String toString() {
        return "Exercise [name=" + sequenceName + "]";
    }

}
