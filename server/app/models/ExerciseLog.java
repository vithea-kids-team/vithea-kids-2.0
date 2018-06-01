/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author soraiamenesesalarcao
 */
public class ExerciseLog extends Model  {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseLogId;
    
    private Exercise exercise;
    private String timestampInit;
    private String timestampEnd;
    private String attempsMade;
    private boolean skipped;
    private boolean reinforcement;
    private boolean prompting;
    private String promptingTypes;
    
    public ExerciseLog(Exercise exercise, String timestampInit, 
            String timestampEnd, String attempsMade, boolean skipped,
            boolean reinforcement, boolean prompting, String promptingTypes){
        this.exercise = exercise;
        this.timestampInit = timestampInit;
        this.timestampEnd = timestampEnd;
        this.attempsMade = attempsMade;
        this.skipped = skipped;
        this.reinforcement = reinforcement;
        this.prompting = prompting;
        this.promptingTypes = promptingTypes;
    }

    public Long getExerciseLogId() {
        return exerciseLogId;
    }

    public void setExerciseLogId(Long exerciseLogId) {
        this.exerciseLogId = exerciseLogId;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public String getTimestampInit() {
        return timestampInit;
    }

    public void setTimestampInit(String timestampInit) {
        this.timestampInit = timestampInit;
    }

    public String getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(String timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public String getAttempsMade() {
        return attempsMade;
    }

    public void setAttempsMade(String attempsMade) {
        this.attempsMade = attempsMade;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    public boolean isReinforcment() {
        return reinforcement;
    }

    public void setReinforcment(boolean reinforcement) {
        this.reinforcement = reinforcement;
    }

    public boolean isPrompting() {
        return prompting;
    }

    public void setPrompting(boolean prompting) {
        this.prompting = prompting;
    }

    public String getPromptingTypes() {
        return promptingTypes;
    }

    public void setPromptingTypes(String promptingTypes) {
        this.promptingTypes = promptingTypes;
    }
}
