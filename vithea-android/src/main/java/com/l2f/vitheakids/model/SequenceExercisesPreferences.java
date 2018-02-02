package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by silvi on 07/09/2017.
 */

public class SequenceExercisesPreferences {
    @JsonProperty String sequenceExercisesOrder;
    @JsonProperty String sequenceExercisesCapitalization;


    public String getSequenceExercisesOrder() {
        return sequenceExercisesOrder;
    }

    public void setSequenceExercisesOrder(String sequenceExercisesOrder) {
        this.sequenceExercisesOrder = sequenceExercisesOrder;
    }

    public String getSequenceExerciseCapitalization() {
        return sequenceExercisesCapitalization;
    }

    public void setSequenceExerciseCapitalization(String sequenceExerciseCapitalization) {
        this.sequenceExercisesCapitalization = sequenceExerciseCapitalization;
    }
}
