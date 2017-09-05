package models;

import com.avaje.ebean.Model;
import javax.persistence.*;

@Entity
public class Prompting extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    private PromptingStrategy promptingStrategy;

    //TODO use @ElementCollection when supported by Ebean
    // promptingTypes should hold prompting types enabled
    //@ElementCollection(targetClass = PromptingType.class) 
    //private List<PromptingType> promptingTypes;
    
    private Boolean promptingColor;
    private Boolean promptingSize;
    private Boolean promptingScratch;
    private Boolean promptingHide;
    private Boolean promptingRead;
    
    public Prompting() { 
        this.promptingStrategy = PromptingStrategy.OFF;
        this.promptingColor = false;
        this.promptingSize = false;
        this.promptingScratch = false;
        this.promptingHide = false;
        this.promptingRead = false;
    }

    public PromptingStrategy getPromptingStrategy() {
        return promptingStrategy;
    }

    public void setPromptingStrategy(PromptingStrategy promptingStrategy) {
        this.promptingStrategy = promptingStrategy;
    }

    public Boolean getPromptingColor() {
        return promptingColor;
    }

    public void setPromptingColor(Boolean promptingColor) {
        this.promptingColor = promptingColor;
    }

    public Boolean getPromptingSize() {
        return promptingSize;
    }

    public void setPromptingSize(Boolean promptingSize) {
        this.promptingSize = promptingSize;
    }

    public Boolean getPromptingScratch() {
        return promptingScratch;
    }

    public void setPromptingScratch(Boolean promptingScratch) {
        this.promptingScratch = promptingScratch;
    }

    public Boolean getPromptingHide() {
        return promptingHide;
    }

    public void setPromptingHide(Boolean promptingHide) {
        this.promptingHide = promptingHide;
    }
    
    public Boolean getPromptingRead() {
        return promptingRead;
    }

    public void setPromptingRead(Boolean promptingRead) {
        this.promptingRead = promptingRead;
    }
}