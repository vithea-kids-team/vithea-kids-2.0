package com.l2f.vitheakids.model;

public class ExerciseLogInfo {
	
	private SimpleMultipleChoice exercise;
	private boolean correct;
	private boolean skipped;
	private int distractorHitsCount;
	private boolean usingPrompting;
	private boolean usingReinforcement;
	
	public ExerciseLogInfo() {
		distractorHitsCount = 0;
	}
	public ExerciseLogInfo(SimpleMultipleChoice exercise) {
		this.exercise = exercise;
		distractorHitsCount = 0;
	}
	
	@Override
	public String toString() {
		return "Exercise log:" +
				"\n\tId: " + this.exercise.getExerciseId() +
				"\n\tSkipped: " + this.skipped +
				"\n\tCorrect: " + this.correct +
				"\n\tDistractors Count: " + this.distractorHitsCount;
	}
	
	public void incDistractorHitsCount() {
		distractorHitsCount++;
	}

	/**
	 * @return the exercise
	 */
	public SimpleMultipleChoice getExercise() {
		return exercise;
	}
	/**
	 * @return the correct
	 */
	public boolean getCorrect() {
		return correct;
	}
    /**
     * @return the skipped
     */
    public boolean getSkipped() {
        return skipped;
    }
    /**
     * @return the distractorHitsCount
     */
    public int getDistractorHitsCount() {
        return distractorHitsCount;
    }
    /**
     * @return the usingPrompting
     */
    public boolean getUsingPrompting() {
        return usingPrompting;
    }
    /**
     * @return the usingReinforcement
     */
    public boolean getUsingReinforcement() {
        return usingReinforcement;
    }

	/**
	 * @param exercise the exercise to set
	 */
	public void setExercise(SimpleMultipleChoice exercise) {
		this.exercise = exercise;
	}
	/**
	 * @param correct the correct to set
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	/**
	 * @param skipped the skipped to set
	 */
	public void setSkipped(boolean skipped) {
		this.skipped = skipped;
	}
	/**
	 * @param distractorHitsCount the distractorHitsCount to set
	 */
	public void setDistractorHitsCount(int distractorHitsCount) {
		this.distractorHitsCount = distractorHitsCount;
	}
	/**
	 * @param usingPrompting the usingPrompting to set
	 */
	public void setUsingPrompting(boolean usingPrompting) {
        this.usingPrompting = usingPrompting;
	}
	/**
	 * @param usingReinforcement the usingReinforcement to set
	 */
	public void setUsingReinforcement(boolean usingReinforcement) {
		this.usingReinforcement = usingReinforcement;
	}

}
