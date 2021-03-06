/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 *
 * @author soraiamenesesalarcao
 */
@Embeddable
public class SequenceExerciseId implements Serializable {

    @ManyToOne
    @Column(name = "exercise_id")
    private Long exercise_id;
    
    @ManyToOne
    @Column(name = "sequence_id")
    private Long sequence_id;

    
    public SequenceExerciseId() {
    }

    public SequenceExerciseId(Long exerciseId, Long sequenceId) {
        this.exercise_id = exerciseId;
        this.sequence_id = sequenceId;
    }
    
    public Long getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(Long exercise_id) {
        this.exercise_id = exercise_id;
    }

    public Long getSequence_id() {
        return sequence_id;
    }

    public void setSequence_id(Long sequence_id) {
        this.sequence_id = sequence_id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SequenceExerciseId that = (SequenceExerciseId) o;

        if (exercise_id != null ? !exercise_id.equals(that.exercise_id) : that.exercise_id != null) {
            return false;
        }
        if (sequence_id != null ? !sequence_id.equals(that.sequence_id) : that.sequence_id != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (exercise_id != null ? exercise_id.hashCode() : 0);
        result = 31 * result + (sequence_id != null ? sequence_id.hashCode() : 0);
        return result;
    }

}
