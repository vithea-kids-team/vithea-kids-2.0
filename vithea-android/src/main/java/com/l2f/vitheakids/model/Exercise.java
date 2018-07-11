package com.l2f.vitheakids.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silvi on 20/04/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="dtype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MultipleChoice.class, name = "MultipleChoice"),
        @JsonSubTypes.Type(value = SelectionImageExercise.class, name = "SelectionImageExercise"),
        @JsonSubTypes.Type(value = SpeechExercise.class, name = "SpeechExercise")
})
public abstract class Exercise {

    @JsonProperty private Long id;
    @JsonProperty private Topic topic;
    @JsonProperty private Level level;

    public Exercise(){
    }

    public Exercise(Long exerciseId, Topic topic, Level level) {
        this.id = exerciseId;
        this.topic = topic;
        this.level = level;
    }

    public abstract String getType();

    public abstract ArrayList<String> getAttempts();

    public Long getId() {
        return this.id;
    }

    public void setExerciseId(Long exerciseId) {
        this.id = exerciseId;
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

    public abstract  List<Resource> getAllUsedResources();

}
