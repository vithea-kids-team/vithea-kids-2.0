/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author soraiamenesesalarcao
 */
public class ChildPreferencesLog extends Model  {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childPreferencesLogId;
    
    private Long childId;
    private List<String> greetingMessageEdited;
    private List<String> exerciseMessageEdited;    
    private List<String> reinforcementMessageEdited;
    private List<Long> animatedCharacterChanged; 
    private List<String> promptingEdited;
    private List<String> reinforcementEdited;
    private List<String> promptingStrategyTypeEdited;
    private List<Long> reinforcementImageChanged;
    private List<Long> orderEdited;
    private List<Long> capitalization;
    private List<String> emotionsEdited;  

    public ChildPreferencesLog() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Long getChildPreferencesLogId() {
        return childPreferencesLogId;
    }

    public void setChildPreferencesLogId(Long childPreferencesLogId) {
        this.childPreferencesLogId = childPreferencesLogId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public List<String> getGreetingMessageEdited() {
        return greetingMessageEdited;
    }

    public void setGreetingMessageEdited(List<String> greetingMessageEdited) {
        this.greetingMessageEdited = greetingMessageEdited;
    }

    public List<String> getExerciseMessageEdited() {
        return exerciseMessageEdited;
    }

    public void setExerciseMessageEdited(List<String> exerciseMessageEdited) {
        this.exerciseMessageEdited = exerciseMessageEdited;
    }

    public List<String> getReinforcementMessageEdited() {
        return reinforcementMessageEdited;
    }

    public void setReinforcementMessageEdited(List<String> reinforcementMessageEdited) {
        this.reinforcementMessageEdited = reinforcementMessageEdited;
    }

    public List<Long> getAnimatedCharacterChanged() {
        return animatedCharacterChanged;
    }

    public void setAnimatedCharacterChanged(List<Long> animatedCharacterChanged) {
        this.animatedCharacterChanged = animatedCharacterChanged;
    }

    public List<String> getPromptingEdited() {
        return promptingEdited;
    }

    public void setPromptingEdited(List<String> promptingEdited) {
        this.promptingEdited = promptingEdited;
    }

    public List<String> getReinforcementEdited() {
        return reinforcementEdited;
    }

    public void setReinforcementEdited(List<String> reinforcementEdited) {
        this.reinforcementEdited = reinforcementEdited;
    }

    public List<String> getPromptingStrategyTypeEdited() {
        return promptingStrategyTypeEdited;
    }

    public void setPromptingStrategyTypeEdited(List<String> promptingStrategyTypeEdited) {
        this.promptingStrategyTypeEdited = promptingStrategyTypeEdited;
    }

    public List<Long> getReinforcementImageChanged() {
        return reinforcementImageChanged;
    }

    public void setReinforcementImageChanged(List<Long> reinforcementImageChanged) {
        this.reinforcementImageChanged = reinforcementImageChanged;
    }

    public List<Long> getOrderEdited() {
        return orderEdited;
    }

    public void setOrderEdited(List<Long> orderEdited) {
        this.orderEdited = orderEdited;
    }

    public List<Long> getCapitalization() {
        return capitalization;
    }

    public void setCapitalization(List<Long> capitalization) {
        this.capitalization = capitalization;
    }

    public List<String> getEmotionsEdited() {
        return emotionsEdited;
    }

    public void setEmotionsEdited(List<String> emotionsEdited) {
        this.emotionsEdited = emotionsEdited;
    }
    
}
