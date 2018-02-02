package com.l2f.vitheakids.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silvi on 20/04/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Exercise {

    @JsonProperty private Long exerciseId;
    @JsonProperty private Topic topic;
    @JsonProperty private Level level;
    @JsonProperty private Question question;
    @JsonProperty private Answer rightAnswer;
    @JsonProperty private List<Answer> answers;
    @JsonProperty private String type;

    public Long getId() {
        return exerciseId;
    }
    public Topic getTopic() {
        return topic;
    }
    public Level getLevel() {
        return level;
    }
    public Question getQuestion() {
        return question;
    }
    public Answer getRightAnswer() {
        return rightAnswer;
    }
    public List<Answer> getAnswers() {
        return answers;
    }
    public String getType() {
        return type;
    }

    public void setId(Long id) {
        this.exerciseId = id;
    }
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    public void setRightAnswer(Answer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    public void setType(String type) {this.type = type;}

    public  List<Resource> getAllUsedResources() {
        List<Resource> resources = new ArrayList<Resource>();
        Resource res1 = question.getStimulus();
        if (res1!=null) {
            resources.add(res1);
        }
        for (Answer answer : answers) {
            res1 = answer.getStimulus();
            if (res1!=null) {
                resources.add(res1);
            }
        }
        return resources;
    }



}
