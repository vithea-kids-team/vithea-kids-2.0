package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum PromptingStrategy {
    
    ALWAYS(0, "Always"), 
    IF_NEEDED(1, "IfNeeded"),
    WITH_DELAY(2, "WithDelay"),
    OFF(3, "Off");

    private final int id;
    private final String type;
    
    @Inject
    PromptingStrategy(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public static final Finder<Integer, PromptingStrategy> find = new Finder<>(PromptingStrategy.class);
    
    public static PromptingStrategy find(int id) {
       return find
               .where()
               .eq("id", id)
               .findUnique();
    }
}
