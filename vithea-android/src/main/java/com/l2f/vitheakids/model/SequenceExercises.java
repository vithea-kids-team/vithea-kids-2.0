package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SequenceExercises {

     private Long sequenceId;
     private String sequenceName;
     private List<Exercise> sequenceExercises;


    public SequenceExercises(Long id, String sequenceName, List<Exercise> sequenceExercises){
        this.sequenceId = id;
        this.sequenceName = sequenceName;
        this.sequenceExercises=sequenceExercises;
    }

    public Long getSequenceId() {
        return sequenceId;
    }
    public String getName() {
        return sequenceName;
    }
    public List<Exercise> getSequenceExercises() {
        return sequenceExercises;
    }

    public void setSequenceId(Long sequenceId) { this.sequenceId = sequenceId; }
    public void setSequenceName(String sequenceName) { this.sequenceName = sequenceName; }
    public void setSequenceExercises(List<Exercise> sequenceExercises) { this.sequenceExercises = sequenceExercises; }

    @Override
    public String toString() {
        return "Exercise [name=" + sequenceName + "]";
    }

    public List<Resource> getAllResources(){
        List<Resource> listResources = new ArrayList<Resource>();
        for (Exercise exercise: sequenceExercises){
            listResources.addAll(exercise.getAllUsedResources());
        }
        return listResources;
    }

    public int getNumberOfExercises(){
        return sequenceExercises.size();
    }

}
