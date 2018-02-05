/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SequencePreferences extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private SequenceExercisesOrder sequenceExercisesOrder;
    
    private SequenceExercisesCapitalization sequenceExercisesCapitalization;
     
    public SequencePreferences() {
        this.sequenceExercisesOrder = SequenceExercisesOrder.DEFAULT;
        this.sequenceExercisesCapitalization = SequenceExercisesCapitalization.DEFAULT;
    }
    
    
    public SequenceExercisesOrder getSequenceExercisesOrder() {
        return this.sequenceExercisesOrder;
    }
    public void setSequenceExercisesOrder(SequenceExercisesOrder sequenceExercisesOrder) {
        this.sequenceExercisesOrder = sequenceExercisesOrder;
    }
    public SequenceExercisesCapitalization getSequenceExercisesCapitalization() {
        return this.sequenceExercisesCapitalization;
    }
    public void setSequenceExercisesCapitalization(SequenceExercisesCapitalization sequenceExercisesCapitalization) {
        this.sequenceExercisesCapitalization = sequenceExercisesCapitalization;
    }
    
}
