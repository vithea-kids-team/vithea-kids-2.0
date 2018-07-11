package com.l2f.vitheakids.model;

import java.util.ArrayList;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

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
     * @return the topicTitle
     */
    public String getTopicTitle() {
        return topic;
    }
    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }
    /**
     * @return the instruction
     */
    public String getInstruction() {
        return instruction;
    }


	/**
	 * @param exerciseId the exerciseId to set
	 */
	public void setExerciseId(Long exerciseId) {
		this.id = exerciseId;
	}
	/**
	 * @param topicTitle the topicTitle to set
	 */
	public void setTopicTitle(String topicTitle) {
		this.topic = topicTitle;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	/**
	 * @param instruction the instruction to set
	 */
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}


}
