package models;


public class ImageMultipleChoiceExercise extends Exercise {

	private String questionText;
	
	
	//@ManyToOne
	//private Stimulus rightAnswerStimulus;

	
	//@ManyToMany(targetEntity = StimulusDistractor.class)
	//private List<StimulusDistractor> distractors;
	
	private int distractorsCount;
	
	/**
	 * @return the questionText
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * @return the rightAnswerStimulus
	 */
//	public Stimulus getRightAnswerStimulus() {
//		return rightAnswerStimulus;
//	}
//
//	/**
//	 * @param rightAnswerStimulus the rightAnswerStimulus to set
//	 */
//	public void setRightAnswerStimulus(Stimulus rightAnswerStimulus) {
//		this.rightAnswerStimulus = rightAnswerStimulus;
//	}
//
//	/**
//	 * @return the wrongAnswers
//	 */
//	public List<StimulusDistractor> getDistractors() {
//		return distractors;
//	}
//
//	/**
//	 * @param distractors the wrongAnswers to set
//	 */
//	public void setDistractors(List<StimulusDistractor> distractors) {
//		this.distractors = distractors;
//	}

	/**
	 * @return the distractorsCount
	 */
	public int getDistractorsCount() {
		return distractorsCount;
	}

	/**
	 * @param distractorsCount the distractorsCount to set
	 */
	public void setDistractorsCount(int distractorsCount) {
		this.distractorsCount = distractorsCount;
	}


}
