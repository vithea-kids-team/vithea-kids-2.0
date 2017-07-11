package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by silvi on 20/04/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Exercise {
    @JsonProperty
    private Long exerciseId;
    @JsonProperty
    private Topic topic;
    @JsonProperty
    private Level level;
    @JsonProperty
    private Question question;
    @JsonProperty
    private Answer rightAnswer;
    @JsonProperty
    private List<Answer> answers;
    @JsonProperty
    private String type;

    public Long getId() {
        return exerciseId;
    }

    public void setId(Long id) {
        this.exerciseId = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Answer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getType() {
        return type;
    }
}
