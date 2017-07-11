package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by silvi on 20/04/2017.
 */

public class Answer {
    @JsonProperty
    private Long answerId;
    @JsonProperty
    private String answerDescription;
    @JsonProperty
    private Resource stimulus;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Resource getStimulus() {
        return stimulus;
    }

    public void setStimulus(Resource stimulus) {
        this.stimulus = stimulus;
    }

    public String getAnswerDescription() {
        return answerDescription;
    }

    public void setAnswerDescription(String answerDescription) {
        this.answerDescription = answerDescription;
    }
}
