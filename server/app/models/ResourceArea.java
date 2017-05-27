package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum ResourceArea {
    
    ANSWERS(0, "Answers"),
    STIMULI(1, "Stimuli"), 
    REINFORCEMENT(2, "Reinforcement"),
    ANIMATEDCHARACTER(3, "Animated Character");
    
    private final int id;
    private final String type;
    
    @Inject
    ResourceArea(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
