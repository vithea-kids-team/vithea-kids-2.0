package com.l2f.vitheakids.model;

public class MultipleChoiceExercise {
	
	private Long id;
	
	private String topic;
	
	private int level;
	
	private String instruction;

	/**
	 * @return the exerciseId
	 */
	public Long getExerciseId() {
		return id;
	}

	/**
	 * @param exerciseId the exerciseId to set
	 */
	public void setExerciseId(Long exerciseId) {
		this.id = exerciseId;
	}

	/**
	 * @return the topicTitle
	 */
	public String getTopicTitle() {
		return topic;
	}

	/**
	 * @param topicTitle the topicTitle to set
	 */
	public void setTopicTitle(String topicTitle) {
		this.topic = topicTitle;
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
