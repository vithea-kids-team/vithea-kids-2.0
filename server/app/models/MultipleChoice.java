/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import static models.Exercise.find;
import play.Logger;


@Entity
@DiscriminatorValue( "MultipleChoice" )
public class MultipleChoice extends Exercise{
    
    private ExerciseType type1; //type of multiple_choice 
    
    @OneToOne(mappedBy="exercise", cascade = CascadeType.ALL)
    private Question question;

    //@OneToOne(mappedBy="exercise", cascade = CascadeType.ALL)
    //private Answer rightAnswer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Answer> answersList;
    
    
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
    public MultipleChoice(Caregiver loggedCaregiver, long topic, long level, String question, long stimulusId, List<String> rightAnswers, List<String> distractors, Boolean def) {
        super(question, loggedCaregiver, def, topic, level);
        
        this.type1 = ExerciseType.TEXT;
        
        
        if (stimulusId != -1) {
            Resource stimulus = Resource.findById(stimulusId);
            this.question = new Question(question, stimulus);
        } else {
            this.question = new Question(question);
        }

        List<Answer> answers = new ArrayList();
        
        //this.rightAnswer = new Answer(answer, true);
        //answers.add(this.rightAnswer);
        
        rightAnswers.forEach((s) -> {
            answers.add(new Answer(s,true));
        }); 
        
        distractors.forEach((s) -> {
            answers.add(new Answer(s,false));
        }); 
        
        this.answersList = answers; 
    }
    
    /**
     * Create an exercise of multiple choice with  images
     * @param loggedCaregiver
     * @param topic
     * @param level
     * @param question
     * @param stimulusText
     * @param answerResourceId
     * @param distractorsResourcesIds
     * @param def 
     */
    
     public MultipleChoice(Caregiver loggedCaregiver, long topic, long level, String question, String stimulusText, List<Long> rightAnswersResourceIds, List<Long> distractorsResourcesIds, Boolean def) {
        super(question, loggedCaregiver, def, topic, level);

        this.type1 = ExerciseType.IMAGE;
        
        if (stimulusText != null && !stimulusText.isEmpty()) {
            this.question = new Question(question, stimulusText);
        } else {
            this.question = new Question(question);
        }
        
        List<Answer> answers = new ArrayList();
        
        for(Long a : rightAnswersResourceIds) {
            System.out.println("rightAnswer id: " + a);
            Resource rightAnswer = Resource.findById(a);
            answers.add(new Answer(rightAnswer, true));
        }   
        
        for(Long d : distractorsResourcesIds) {
            System.out.println("distractor id: " + d);
            Resource distractor = Resource.findById(d);
            answers.add(new Answer(distractor, false));
        }   
        
        this.answersList = answers;
    }
    
    public Question getQuestion() {
        return question;
    }
    public List<Answer> getAnswers() {
       return answersList;
    }
    public void resetAnswers(){
        answersList.clear();
    }
    public void removeAnswer(Answer ans){
        answersList.remove(ans);
    }
    public void removeQuestion(){
        question.delete();
    }
 
    public ExerciseType getType() {
        return type1;
    }
  
    public void setQuestion(Question question) {
            this.question = question;
    }
    public void setQuestion(String questionDescription, Long stimulus) {
        Question question = new Question(questionDescription);
        if(stimulus != 0) question.setStimulus(stimulus);
        question.save();
        Logger.debug("New exercise :: setQuestion: " + question.getQuestionDescription() + " (" + question.getQuestionId() + ")");
        this.question = question;
    }    
 
    public void setAnswers(List<String> answerDescriptions, List<Long> answerStimulus) {
        Iterator<String> i = answerDescriptions.iterator(); 
        Iterator<Long> j = answerStimulus.iterator();		
        while(i.hasNext() || j.hasNext()) {
            String description = i.next();
            Long stimulus = j.next();
            Answer answer = new Answer(description, true);
            answer.setStimulus(stimulus);
            answer.save();
            Logger.debug("New exercise :: addDistractor: " + answer.getAnswerDescription() +" (" + answer.getAnswerId() + ")");
            this.answersList.add(answer);
        }		
    }
    public void setAnswers(List<Answer> answers){
        this.answersList = answers;
    }
    
    public void  setAnswersText (List<String> rightAnswers, List<String> distractors){
      //remove all answers
      List<Answer> arrayAux =  new ArrayList<Answer>(this.answersList);
    
      for(Answer a : arrayAux){
        this.answersList.remove(a);
        this.save();
        a.delete();
      }
      
      List<Answer> answers =  new ArrayList<Answer>();
      for(String r : rightAnswers){
          Answer rightAnswer = new Answer(r,true);
          answers.add(rightAnswer);
      }
      
      for(String d : distractors){
         Answer rightAnswer = new Answer(d,false);
        answers.add(rightAnswer);
      }
      
      this.answersList = answers;
        
    }
    
    /**
     * set all answers
     * @param rightAnswers
     * @param distractors 
     */   
    public void  setAnswersImg (List<Long> rightAnswers, List<Long> distractors){
        
        List<Answer> arrayAux =  new ArrayList<Answer>(this.answersList);
        //remove all answers
        for(Answer a : arrayAux){
        this.answersList.remove(a);
        this.save();
        a.delete();
        }
        
        Resource stimulus;
        
        List<Answer> answers =  new ArrayList<Answer>();

        for(Long r : rightAnswers){
            stimulus = Resource.findById(r);
            Answer rightAnswer = new Answer(stimulus,true);
            answers.add(rightAnswer);
        }
      
        for(Long d : distractors){
          stimulus = Resource.findById(d);
          Answer rightAnswer = new Answer(stimulus,false);
          answers.add(rightAnswer);
        }
      
        this.answersList = answers;
      
    }

    public void setType(ExerciseType type) {
        this.type1 = type;
    }

    public static List<Exercise> getAll() {
        return find.all();
    }
    
    
}
