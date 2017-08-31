/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author soraiamenesesalarcao
 */
@Entity
@Table(name = "sequence_exercise")
@AssociationOverrides({
@AssociationOverride(name = "exercise", joinColumns = @JoinColumn(name = "exercise_id")),
@AssociationOverride(name = "sequence", joinColumns = @JoinColumn(name = "sequence_id"))})

public class SequenceExercise extends Model {

    @EmbeddedId
    private SequenceExerciseId sequenceExerciseId;
    
    @ManyToOne
    @JoinColumn(name = "exercise_id", insertable = false, updatable = false)
    @JsonBackReference
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "sequence_id", insertable = false, updatable = false)
    @JsonBackReference
    private Sequence sequence;
    
    @Column(name = "exercise_order", nullable = false)
    private int exercise_order;

    public SequenceExercise() {
    }

    public SequenceExercise(Exercise exercise, Sequence sequence, int order) {
        
        this.sequenceExerciseId = new SequenceExerciseId(exercise.getExerciseId(), sequence.getSequenceId());
        
        this.exercise = exercise;
        this.sequence = sequence;
        this.exercise_order = order;
        
        //exercise.get
        
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Sequence getSequence() {
        return this.sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }
    
    public SequenceExerciseId getSequenceExerciseId() {
        return sequenceExerciseId;
    }

    public void setSequenceExerciseId(SequenceExerciseId sequenceExerciseId) {
        this.sequenceExerciseId = sequenceExerciseId;
    }
    
    public int getExerciseOrder() {
        return this.exercise_order;
    }

    public void setExerciseOrder(int exerciseOrder) {
        this.exercise_order = exerciseOrder;
    }

}
