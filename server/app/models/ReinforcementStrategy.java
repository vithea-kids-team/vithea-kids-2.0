package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum ReinforcementStrategy {
    
    ALWAYS(0, "Always"), 
    DIFFERENTIAL(1, "Differential"),
    AT_FIRST(2, "AtFirst"),
    OFF(3, "Off");
    
    private final int id;
    private final String type;
    
    @Inject
    ReinforcementStrategy(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public static final Finder<Integer, ReinforcementStrategy> find = new Finder<>(ReinforcementStrategy.class);
    
    public static ReinforcementStrategy find(int id) {
       return find
               .where()
               .eq("id", id)
               .findUnique();
    }    
}
