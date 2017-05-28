package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum ExerciseType {
    
    TEXT(0, "text"),
    IMAGE(1, "image");
    
    private final int id;
    private final String type;
    
    @Inject
    ExerciseType(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
