package com.l2f.vitheakids.model;

import java.util.List;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class SimpleMultipleChoice extends MultipleChoiceExercise {

	private String stimulusPath;
	private String rightAnswerText;
	private List<String> textDistractors;

	/**
	 * @return the stimulusPath
	 */
	public String getStimulusPath() {
		return stimulusPath;
	}
	/**
	 * @return the rightAnswerText
	 */
	public String getRightAnswerText() {
		return rightAnswerText;
	}
	/**
	 * @return the textDistractors
	 */
	public List<String> getTextDistractors() {
		return textDistractors;
	}

	/**
	 * @param stimulusPath the stimulusPath to set
	 */
	public void setStimulusPath(String stimulusPath) {
		this.stimulusPath = stimulusPath;
	}
	/**
	 * @param rightAnswerText the rightAnswerText to set
	 */
	public void setRightAnswerText(String rightAnswerText) {
		this.rightAnswerText = rightAnswerText;
	}
	/**
	 * @param textDistractors the textDistractors to set
	 */
	public void setTextDistractors(List<String> textDistractors) {
		this.textDistractors = textDistractors;
	}

}
