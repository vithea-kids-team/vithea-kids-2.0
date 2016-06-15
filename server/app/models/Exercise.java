package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

@Entity
public class Exercise extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long exerciseId;
	
	@ManyToOne
	private Topic topic;
	
	@ManyToOne
	private Level level;
	
	@ManyToOne
	private Question question;
	
	@OneToOne
	private Answer rightAnswer;
	
	@ManyToMany
	private List<Answer> distractors;
	
	@ManyToOne
	private Caregiver author;

	/**
	 * @return the exerciseId
	 */
	public Long getExerciseId() {
		return exerciseId;
	}

	/**
	 * @param exerciseId the exerciseId to set
	 */
	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	/**
	 * @return the topic
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 * @return the level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return the rightAnswer
	 */
	public Answer getRightAnswer() {
		return rightAnswer;
	}

	/**
	 * @param rightAnswer the rightAnswer to set
	 */
	public void setRightAnswer(Answer rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	/**
	 * @return the distractors
	 */
	public List<Answer> getDistractors() {
		return distractors;
	}

	/**
	 * @param distractors the distractors to set
	 */
	public void setDistractors(List<Answer> distractors) {
		this.distractors = distractors;
	}

	/**
	 * @return the author
	 */
	public Caregiver getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Caregiver author) {
		this.author = author;
	}
	
}