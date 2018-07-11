/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author soraiamenesesalarcao
 */

@Entity
public class SequenceLog extends Model  {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequenceLogId;
    private Long sequenceId;
    private Long childId;
    private String name;
    private String timestampBegin;
    private String timestampEnd;
    private int numberExercisesCorrect;
    private int numberExercisesSkipped;
    private int numberExercisesTotal;
    private Long distractorHitsAvg;
    private List<ExerciseLog> exercisesLog;
    
    public SequenceLog(Long sequenceId, Long childId, String name, 
            String timestampBegin, String timestampEnd, 
            int numberExercisesTotal, int numberExercisesCorrect, 
            int numberExercisesSkipped, Long distractorHitsAvg, 
            List<ExerciseLog> exercisesLog){
        this.sequenceId = sequenceId;
        this.childId = childId;
        this.name = name;
        this.timestampBegin = timestampBegin;
        this.timestampEnd = timestampEnd;
        this.numberExercisesCorrect = numberExercisesCorrect;
        this.numberExercisesSkipped = numberExercisesSkipped;
        this.numberExercisesTotal = numberExercisesTotal;
        this.distractorHitsAvg = distractorHitsAvg;
        this.exercisesLog = exercisesLog;
    }  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDistractorHitsAvg() {
        return distractorHitsAvg;
    }

    public void setDistractorHitsAvg(Long distractorHitsAvg) {
        this.distractorHitsAvg = distractorHitsAvg;
    }
    
    public Long getSequenceLogId() {
        return sequenceLogId;
    }

    public void setSequenceLogId(Long sequenceLogId) {
        this.sequenceLogId = sequenceLogId;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getTimestampBegin() {
        return timestampBegin;
    }

    public void setTimestampBegin(String timestampBegin) {
        this.timestampBegin = timestampBegin;
    }

    public String getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(String timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public int getNumberExercisesCorrect() {
        return numberExercisesCorrect;
    }

    public void setNumberExercisesCorrect(int numberExercisesCorrect) {
        this.numberExercisesCorrect = numberExercisesCorrect;
    }

    public int getNumberExercisesSkipped() {
        return numberExercisesSkipped;
    }

    public void setNumberExercisesSkipped(int numberExercisesSkipped) {
        this.numberExercisesSkipped = numberExercisesSkipped;
    }

    public int getNumberExercisesTotal() {
        return numberExercisesTotal;
    }

    public void setNumberExercisesTotal(int numberExercisesTotal) {
        this.numberExercisesTotal = numberExercisesTotal;
    }

    public List<ExerciseLog> getExercisesLog() {
        return exercisesLog;
    }

    public void setExercisesLog(List<ExerciseLog> exercisesLog) {
        this.exercisesLog = exercisesLog;
    }
}
