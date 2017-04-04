package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import javax.persistence.Column;

@Entity
public class Answer extends Model {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerDescription;

    @ManyToOne
    @Column(nullable = true)
    private Resource stimulus;

    Answer(String answer) {
        this.answerDescription = answer;
        this.stimulus = null;
    }
    
    Answer(Resource stimulus) {
        this.answerDescription = null;
        this.stimulus = stimulus;
    }

    /**
     * @return the answerId
     */
    public Long getAnswerId() {
            return id;
    }

    /**
     * @param answerId the answerId to set
     */
    public void setAnswerId(Long answerId) {
            this.id = answerId;
    }

    /**
     * @return the answerDescription
     */
    public String getAnswerDescription() {
            return answerDescription;
    }

    /**
     * @param answerDescription the answerDescription to set
     */
    public void setAnswerDescription(String answerDescription) {
            this.answerDescription = answerDescription;
    }

    /**
     * @return the stimulus
     */
    public Resource getStimulus() {
            return stimulus;
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
}
