package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum SequenceExercisesOrder {
    
    RANDOM(0, "Random"), 
    DEFAULT(1, "Default");
    
    private final int id;
    private final String type;
    
    @Inject
    SequenceExercisesOrder(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public static Finder<Integer, SequenceExercisesOrder> find = new Finder<>(SequenceExercisesOrder.class);
    
    public static SequenceExercisesOrder find(int id) {
       return find.where().eq("id", id).findUnique();
    }    
}