/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import javax.inject.Inject;

/**
 *
 * @author soraiamenesesalarcao
 */
public enum SequenceExercisesCapitalization {
    
    CAPITALIZATION(0, "Capitalization"), 
    DEFAULT(1, "Default");
    
    private final int id;
    private final String type;
    
    @Inject
    SequenceExercisesCapitalization(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public static final Model.Finder<Integer, SequenceExercisesCapitalization> find = new Model.Finder<>(SequenceExercisesCapitalization.class);
    
    public static SequenceExercisesCapitalization find(int id) {
       return find.where().eq("id", id).findUnique();
    }    
}