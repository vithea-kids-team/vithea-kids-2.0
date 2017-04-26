package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum PersonalMessageType {
    
    GREETING_MESSAGE(0, "GreetingMessage"), 
    EXERCISE_REINFORCEMENT(1, "ExerciseReinforcementMessage"),
    SEQUENCE_REINFORCEMENT(2, "SequenceReinforcementMessage");
    
    private final int id;
    private final String type;
    
    @Inject
    PersonalMessageType(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public static final Finder<Integer, PersonalMessageType> find = new Finder<>(PersonalMessageType.class);
    
    public static PersonalMessageType find(int id) {
       return find
               .where()
               .eq("id", id)
               .findUnique();
    }    
}
