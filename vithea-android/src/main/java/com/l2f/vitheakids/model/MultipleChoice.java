package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class MultipleChoice extends Exercise {
    @JsonProperty private String type;
    @JsonProperty private Question question;
    @JsonProperty private List<Answer> answers;
    private ArrayList<String> attempts = new ArrayList<>();

    public MultipleChoice(){

    }

    public MultipleChoice(Long exerciseId, Topic topic, Level level, String type, Question question, List<Answer> answers) {
        super(exerciseId, topic, level);
        this.type = type;
        this.question = question;
        this.answers = answers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Answer> getAnswersList() {
        return answers;
    }

    public void setAnswersList(List<Answer> answersList) {
        this.answers = answersList;
    }

    public ArrayList<String> getAttempts(){
        return attempts;
    }

    public void setAttempts(ArrayList<String> attempts) {
        this.attempts = attempts;
    }

    @Override
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

    public void addAttempts(String attempt) {
        attempts.add(attempt);
    }
}
