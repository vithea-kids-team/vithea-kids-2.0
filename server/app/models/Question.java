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

@Entity
public class Question extends Model {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionDescription;
    
    @OneToOne
    @PrimaryKeyJoinColumn
    private MultipleChoice exercise;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Column(nullable = true)
    private Resource stimulus;

    private String stimulusText;

    public Question(String question) {
        this.questionDescription = question;
    }

    Question(String question, Resource stimulus) {
        this.questionDescription = question;
        this.stimulus = stimulus;
    }

    Question(String question, String stimulusText) {
        this.questionDescription = question;
        this.stimulusText = stimulusText;
    }

    /**
     * @return the questionId
     */
    public Long getQuestionId() {
        return id;
    }

    /**
     * @param questionId the questionId to set
     */
    public void setQuestionId(Long questionId) {
        this.id = questionId;
    }

    /**
     * @return the questionDescription
     */
    public String getQuestionDescription() {
        return questionDescription;
    }

    /**
     * @param questionDescription the questionDescription to set
     */
    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
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
     * @param stimulusid of the stimulus to set
     */
    public void setStimulus(Long stimulus) {
        this.stimulus = Resource.findById(stimulus);
    }

    /**
     * @return the stimulusText
     */
    public String getStimulusText() {
        return stimulusText;
    }

    /**
     * @param the stimulusText to set
     */
    public void setStimulusText(String stimulusText) {
        this.stimulusText = stimulusText;
    }
		
	
}
