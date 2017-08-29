/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
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
//private SequenceExerciseId sequenceExerciseId = new SequenceExerciseId();
    
    @ManyToOne
    @JoinColumn(name = "exercise_id", insertable = false, updatable = false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name = "sequence_id", insertable = false, updatable = false)
    private Sequence sequence;
    
    @Column(name = "exercise_order", nullable = false)
    private int exercise_order;

    public SequenceExercise() {
    }

    public SequenceExercise(Exercise exercise, Sequence sequence, int order) {
        
        this.sequenceExerciseId = new SequenceExerciseId(exercise.getExerciseId(), sequence.getSequenceId());
        
        this.setExercise(exercise);
        this.setSequence(sequence);
        this.exercise_order = order;
        
        //exercise.get
        
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Sequence getSequence() {
        return sequence;
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
    
    /*@Transient
    public Exercise getExercise() {
        return getSequenceExerciseId().getExercise();
    }

    public void setExercise(Exercise exercise) {
        getSequenceExerciseId().setExercise(exercise);
    }

    @Transient
    public Sequence getSequence() {
        return getSequenceExerciseId().getSequence();
    }

    public void setSequence(Sequence sequence) {
        getSequenceExerciseId().setSequence(sequence);
    }*/



//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//
//        SequenceExercise that = (SequenceExercise) o;
//
//        if (getSequenceExerciseId() != null ? !getSequenceExerciseId().equals(that.getSequenceExerciseId())
//                : that.getSequenceExerciseId() != null) {
//            return false;
//        }
//
//        return true;
//    }
//
//    public int hashCode() {
//        return (getSequenceExerciseId() != null ? getSequenceExerciseId().hashCode() : 0);
//    }

}
