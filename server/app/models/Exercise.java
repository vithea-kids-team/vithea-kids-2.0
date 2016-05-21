package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.avaje.ebean.Model;

@MappedSuperclass
public abstract class Exercise extends Model {
	/**
	 * Exercise ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exerciseId", unique = true, nullable = false)
	private Long exerciseId;
	
	/* Common parts */
	
	/**
	 * Topic of the exercise
	 */
//	@ManyToOne
//	private Topic topic;
	
	@ManyToOne
	private Caregiver author;

	/**
	 * Difficulty level
	 */
	private int level;
	
	
	/** 
	 * Specifies whether the exercise is automatic or manual
	 */
	private boolean isAutomatic; 
	
	/**
	 * How the answer should be provided by the child
	 */
	private String instruction;
	

	/**
	 * @param exerciseId
	 *            The exercise ID
	 */
	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	/**
	 * @return The exercise ID
	 */
	public Long getExerciseId() {
		return exerciseId;
	}

	/**
	 * @param topic The topic of the exercise
	 */
//	public void setTopic(Topic topic) {
//		this.topic = topic;
//	}
//
//	/**
//	 * @return The topic of the exercise
//	 */
//	public Topic getTopic() {
//		return topic;
//	}

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

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}


	/**
	 * @return the isAutomatic
	 */
	public boolean isAutomatic() {
		return isAutomatic;
	}

	/**
	 * @param isAutomatic the isAutomatic to set
	 */
	public void setAutomatic(boolean isAutomatic) {
		this.isAutomatic = isAutomatic;
	}

	/**
	 * @return the instruction
	 */
	public String getInstruction() {
		return instruction;
	}

	/**
	 * @param instruction the instruction to set
	 */
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
}