package com.l2f.vitheakids.model;

import java.util.ArrayList;

import java.util.logging.*;

public class ExerciseLogInfo {
	static int latestExerciseID = 0;

	private int exerciseId;

	private long timestampBegin;  //Date.getTime()
	private long timestampEnd;

	private int numberOfDistractorHits;
	private int numberOfWrongAttempts;
	private boolean correct;
	private boolean skipped;

	private ArrayList<String> promptingTypes;
	private String promptingStrategy;

	private String reinforcementStrategy;



	private SimpleMultipleChoice exercise;
	private boolean usingPrompting;
	private boolean usingReinforcement;
	
	public ExerciseLogInfo() {
		numberOfDistractorHits = 0;
	}
	public ExerciseLogInfo(SimpleMultipleChoice exercise) {
		this.exercise = exercise;
		numberOfDistractorHits = 0;
	}
	
	@Override
	public String toString() {
		return "Exercise log:" +
				"\n\tId: " + this.exercise.getExerciseId() +
				"\n\tSkipped: " + this.skipped +
				"\n\tCorrect: " + this.correct +
				"\n\tDistractors Count: " + this.numberOfDistractorHits;
	}
	
	public void incDistractorHitsCount() {
		numberOfDistractorHits++;
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
     * @return the numberOfDistractorHits
     */
    public int getNumberOfDistractorHits() {
        return numberOfDistractorHits;
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
	 * @param numberOfDistractorHits the numberOfDistractorHits to set
	 */
	public void setNumberOfDistractorHits(int numberOfDistractorHits) {
		this.numberOfDistractorHits = numberOfDistractorHits;
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
