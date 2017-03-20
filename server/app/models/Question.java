package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class Question extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long questionId;
	
	private String questionDescription;

	@ManyToOne
	private Resource stimulus;

	private String stimulusText;

    Question(String question) {
        this.questionDescription = question;
    }

	/**
	 * @return the questionId
	 */
	public Long getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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
