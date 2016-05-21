package models;

public class SimpleMultipleChoiceExercise extends Exercise {	
		
	/**
	 * Question stimulus
	 */
	//@ManyToOne
	//private Stimulus questionStimulus;
	
	/**
	 * Correct answer
	 */
	private String rightAnswerText;
	
	
	/**
	 * Wrong answer options
	 */
	//@ManyToMany
	//private List<TextDistractor> distractors;
	
	/**
	 *  Wrong answer count
	 */
	private int distractorsCount;
	
	/**
	 * List of ExerciseSequence
	 */
	//@OneToMany
	//private List<ExerciseSequence> exerciseSequences;
	
	/** 
	 * Adds an ExerciseSequence instance to the list
	 * @param es
	 */
//	public void addExerciseSequence(ExerciseSequence es) {
//		this.exerciseSequences.add(es);
//	}
//	
//	/** 
//	 * Remove an ExerciseSequence instance from the list
//	 * @param es
//	 */
//	public void removeExerciseSequence(ExerciseSequence es) {
//		this.exerciseSequences.remove(es);
//	}
//	
//	/**
//	 * @return the questionStimulus
//	 */
//	public Stimulus getQuestionStimulus() {
//		return questionStimulus;
//	}
//	/**
//	 * @param questionStimulus the questionStimulus to set
//	 */
//	public void setQuestionStimulus(Stimulus questionStimulus) {
//		this.questionStimulus = questionStimulus;
//	}
//	/**
//	 * @return the rightAnswer
//	 */
//	public String getRightAnswerText() {
//		return rightAnswerText;
//	}
//	/**
//	 * @param rightAnswer the rightAnswer to set
//	 */
//	public void setRightAnswerText(String rightAnswerText) {
//		this.rightAnswerText = rightAnswerText;
//	}
//
//	/**
//	 * @return the distractors
//	 */
//	public List<TextDistractor> getDistractors() {
//		return distractors;
//	}
//
//	/**
//	 * @param distractors the distractors to set
//	 */
//	public void setDistractors(List<TextDistractor> distractors) {
//		this.distractors = distractors;
//	}
//
//	/**
//	 * @return the distractorsCount
//	 */
//	public int getDistractorsCount() {
//		return distractorsCount;
//	}
//
//	/**
//	 * @param distractorsCount the distractorsCount to set
//	 */
//	public void setDistractorsCount(int distractorsCount) {
//		this.distractorsCount = distractorsCount;
//	}	
//	
//	/**
//	 * @return exerciseSequences the sequences that contain this exercise
//	 */
//	public List<ExerciseSequence> getExerciseSequences() {
//		return this.exerciseSequences;
//	}
//
//	/**
//	 * @param exerciseSequences the sequences to set
//	 */
//	public void setExerciseSequences(List<ExerciseSequence> exerciseSequences) {
//		this.exerciseSequences = exerciseSequences;
//	}
		

}
