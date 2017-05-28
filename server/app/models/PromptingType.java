package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum PromptingType {
    
    //NOT IN USE - check Prompting class
    
    SIZE(0, "Size"), 
    COLOR(1, "Color"),
    SCRATCH(2, "Scratch"),
    HIDE(3, "Hide");
    
    private final int id;
    private final String type;
    
    @Inject
    PromptingType(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    public static final Finder<Integer, PromptingType> find = new Finder<>(PromptingType.class);
    
    public static PromptingType find(int id) {
       return find
               .where()
               .eq("id", id)
               .findUnique();
    }    
}
