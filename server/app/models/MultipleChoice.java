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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import static models.Exercise.find;
import play.Logger;

/**
 *
 * @author silvi
 */



@Entity
@DiscriminatorValue( "multipleChoice" )
public class MultipleChoice extends Exercise{
    
    @Column(nullable = false)
    private ExerciseType type; //type of multiple_choice 
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @Column(nullable = true)
    private Topic topic;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Column(nullable = true)
    private Level level;

    @OneToOne(mappedBy="exercise", cascade = CascadeType.ALL)
    private Question question;

    //@OneToOne(mappedBy="exercise", cascade = CascadeType.ALL)
    //private Answer rightAnswer;

    @ManyToMany(cascade = CascadeType.ALL)
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
        super(question, loggedCaregiver, def);
        
        this.type = ExerciseType.TEXT;
        
        if (topic != -1) {
            this.topic = Topic.findTopicById(topic);
        }
        
        if (level != -1) {
            this.level = Level.findLevelById(level);
        }
        
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
        super(question, loggedCaregiver, def);

        this.type = ExerciseType.IMAGE;
         
        if (topic != -1) {
            this.topic = Topic.findTopicById(topic);
        }
        
        if (level != -1) {
            this.level = Level.findLevelById(level);
        }
        
        if (stimulusText != null && !stimulusText.isEmpty()) {
            this.question = new Question(question, stimulusText);
        } else {
            this.question = new Question(question);
        }
        
        List<Answer> answers = new ArrayList();
        
        //Resource rightAnswer = Resource.findById(answerResourceId);
        //this.rightAnswer = new Answer(rightAnswer, true);
        //answers.add(this.rightAnswer);
        
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
    
    public Topic getTopic() {
        return topic;
    }
    
    public Level getLevel() {
        return level;
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
        return type;
    }
  
    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    public void setTopic(Long topicId) {
        Topic topic = Topic.findTopicById(topicId);
        if (topic == null) throw new NullPointerException("Topic does not exist");
        Logger.debug("New exercise :: setTopic: " + topic.getTopicDescription());
        this.topic = topic;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setLevel(Long levelId) {
        Level level = Level.findLevelById(levelId);
        if (level == null) throw new NullPointerException("Level does not exist");
        Logger.debug("New exercise :: setLevel: " + level.getLevelDescription());
        this.level = level;
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
    /*public void setRightAnswer(Answer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
    public void setRightAnswer(String rightAnswerDescription, Long resource) {
        Answer rightAnswer = new Answer(rightAnswerDescription, true);
        rightAnswer.setStimulus(resource);
        rightAnswer.save();
        Logger.debug("New exercise :: setRightAnswer: " + rightAnswer.getAnswerDescription() +" (" + rightAnswer.getAnswerId() + ")");
        this.rightAnswer = rightAnswer;
        this.answersList.add(rightAnswer);
    }*/
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
    
   
    public void setType(ExerciseType type) {
        this.type = type;
    }

    public static List<Exercise> getAll() {
        return find.all();
    }
    
}
