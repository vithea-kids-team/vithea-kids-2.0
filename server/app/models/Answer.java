package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;

@Entity
public class Answer extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long answerId;
	
	private String answerDescription;

	@ManyToOne
	private Resource stimulus;

	/**
	 * @return the answerId
	 */
	public Long getAnswerId() {
		return answerId;
	}

	/**
	 * @param answerId the answerId to set
	 */
	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
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
	
	
	
}
