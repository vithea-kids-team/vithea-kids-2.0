package com.l2f.vitheakids.model;

/**
 * Created by silvi on 20/04/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class Question {

    private Long questionId;
    private String questionDescription;
    private Resource stimulus;
    private String stimulusText;

    public String getQuestionDescription() {
        return questionDescription;
    }
    public Long getQuestionId() {
        return questionId;
    }
    public Resource getStimulus() {
        return stimulus;
    }
    public String getStimulusText() {
        return stimulusText;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    public void setStimulus(Resource stimulus) {
        this.stimulus = stimulus;
    }
    public void setStimulusText(String stimulusText) {
        this.stimulusText = stimulusText;
    }

}
