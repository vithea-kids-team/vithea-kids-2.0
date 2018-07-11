/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author soraiamenesesalarcao
 */
@Entity
public class ExerciseLog extends Model  {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseLogId;
    
    private String typeExercise;
    private Long exerciseId;
    private Long childId;
    private Long sequenceId;
    @Column(columnDefinition = "TEXT")
    private String exercisePreview;
    private String timestampInit;
    private String timestampEnd;
    private String attempsMade;
    private boolean correct; 
    private boolean skipped;
    private String reinforcement;
    private String prompting;
    private String promptingTypes;
    private int numberOfDistractorHits;
    
    public ExerciseLog(String typeExercise, Long exerciseId, long childId,
            Long sequenceId, String exercisePreview, String timestampInit, 
            String timestampEnd, String attempsMade, boolean correct, 
            boolean skipped, String reinforcement, String prompting, 
            String promptingTypes, int numberOfDistractorHits){
        this.typeExercise = typeExercise;
        this.exerciseId = exerciseId;
        this.childId = childId;
        this.sequenceId = sequenceId;
        this.exercisePreview = exercisePreview;
        this.timestampInit = timestampInit;
        this.timestampEnd = timestampEnd;
        this.attempsMade = attempsMade;
        this.correct = correct;
        this.skipped = skipped;
        this.reinforcement = reinforcement;
        this.prompting = prompting;
        this.promptingTypes = promptingTypes;
        this.numberOfDistractorHits = numberOfDistractorHits;
    }

    public String getTypeExercise() {
        return typeExercise;
    }

    public void setTypeExercise(String typeExercise) {
        this.typeExercise = typeExercise;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getReinforcement() {
        return reinforcement;
    }

    public void setReinforcement(String reinforcement) {
        this.reinforcement = reinforcement;
    }

    public int getNumberOfDistractorHits() {
        return numberOfDistractorHits;
    }

    public void setNumberOfDistractorHits(int numberOfDistractorHits) {
        this.numberOfDistractorHits = numberOfDistractorHits;
    }

    public Long getExerciseLogId() {
        return exerciseLogId;
    }

    public void setExerciseLogId(Long exerciseLogId) {
        this.exerciseLogId = exerciseLogId;
    }

    public String getExercisePreview() {
        return exercisePreview;
    }

    public void setExercisePreview(String exercisePreview) {
        this.exercisePreview = exercisePreview;
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

    public void setReinforcment(String reinforcement) {
        this.reinforcement = reinforcement;
    }

    public String isPrompting() {
        return prompting;
    }

    public void setPrompting(String prompting) {
        this.prompting = prompting;
    }

    public String getPromptingTypes() {
        return promptingTypes;
    }

    public void setPromptingTypes(String promptingTypes) {
        this.promptingTypes = promptingTypes;
    }
}
