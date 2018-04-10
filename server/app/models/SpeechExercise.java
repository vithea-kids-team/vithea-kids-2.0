/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
@DiscriminatorValue( "SpeechExercise" )
public class SpeechExercise extends Exercise{
    
    private String questionSpeech;
    //@OneToOne(mappedBy="exercise", cascade = CascadeType.ALL)
    //private Question question;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Resource stimulusSpeech;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy="exercise")
    private List<Answer> answersListSpeech;
    
    
    /**
     * Create an exercise of multiple choice with text choices
     * @param loggedCaregiver
     * @param topic
     * @param level
     * @param question
     * @param stimulusId
     * @param answer
     * @param distractors
     * @param def  
     */
    public SpeechExercise(Caregiver loggedCaregiver, long topic, long level, String question, long stimulusId, List<String> rightAnswers, Boolean def) {
        super(question, loggedCaregiver, def, topic, level);
        
        if (stimulusId != -1) {
            stimulusSpeech = Resource.findById(stimulusId);
        } else {
            stimulusSpeech = null;
        }
        
        this.questionSpeech = question;

        List<Answer> answers = new ArrayList();
        
        rightAnswers.forEach((s) -> {
            answers.add(new Answer(this,s,true));
        });
        
        this.answersListSpeech = answers; 
    }
    
    public String getQuestionSpeech(){
        return questionSpeech;
    }
    
    public void setQuestionSpeech(String question){
        this.questionSpeech = question;
    }
    
    public Resource getStimulusSpeech(){
        return stimulusSpeech;
    }
    
    public void setStimulusSpeech(long stimulusId){
        if (stimulusId != -1) {
            this.stimulusSpeech = Resource.findById(stimulusId);
        } else {
            this.stimulusSpeech = null;
        }
    }
    
    public List<Answer> getAnswers() {
       return answersListSpeech;
    }
    public void resetAnswers(){
        answersListSpeech.clear();
    }
    public void setAnswers(List<Answer> answers) {
        this.answersListSpeech = answers;
    }
    
    public void removeAnswer(Answer ans){
        answersListSpeech.remove(ans);
    }
    
}
