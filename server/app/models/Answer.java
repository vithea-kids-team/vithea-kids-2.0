package models;

import com.avaje.ebean.Model;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import play.Logger;

@Entity
public class Answer extends Model {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
     @ManyToOne(optional=false)
     Exercise exercise;

    private String answerDescription;

   
    
    private Boolean rightAnswer;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @Column(nullable = true)
    private Resource stimulus;
    
    
    
    /**
     * Answers of text exercises
     * @param answer
     * @param rightAnswer 
     */
    public Answer(Exercise exercise, String answer, Boolean rightAnswer) {
        this.answerDescription = answer;
        this.stimulus = null;
        this.rightAnswer = rightAnswer;
        this.exercise = exercise;
    }
    
    /**
     * Answers of image exercises
     * @param stimulus
     * @param rightAnswer 
     */
    Answer(Resource stimulus, Boolean rightAnswer) {
        this.answerDescription = null;
        this.stimulus = stimulus;
        this.rightAnswer = rightAnswer;
    }

    public Boolean getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Boolean rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    /**
     * @return the answerId
     */
    public Long getAnswerId() {
        return id;
    }
    /**
     * @return the answerDescription
     */
    public String getAnswerDescription() {
        return answerDescription;
    }
    /**
     * @return the stimulus
     */
    public Resource getStimulus() {
        return stimulus;
    }
    
    /**
     * @param answerId the answerId to set
     */
    public void setAnswerId(Long answerId) {
        this.id = answerId;
    }
    /**
     * @param answerDescription the answerDescription to set
     */
    public void setAnswerDescription(String answerDescription) {
        this.answerDescription = answerDescription;
    }
    /**
     * @param stimulus the stimulus to set
     */
    public void setStimulus(Resource stimulus) {
        this.stimulus = stimulus;
    }
    /**
     * @param stimulusId of the stimulus to set
     */
    public void setStimulus(Long stimulusId) {
        this.stimulus = Resource.findById(stimulusId);
    }
    
    public static final Finder<Long, Answer> find = new Finder<>(Answer.class);
    public static Answer findAnswerById(Long id) {
        Logger.debug("Looking for answers with id: " + id);
        return find.where().eq("id", id).findUnique();
    }
    
}
