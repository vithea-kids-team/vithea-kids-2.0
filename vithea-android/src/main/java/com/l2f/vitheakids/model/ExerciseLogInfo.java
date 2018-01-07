package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2f.vitheakids.LogHelper;

import org.apache.log4j.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.logging.*;

@JsonPropertyOrder({ "childID", "exerciseID", "promptingStrategy", "reinforcementStrategy",
		"timestampBeginExercise", "timestampEndExercise", "numberOfDistractorHits",
		"correct", "skipped"})
@JsonIgnoreProperties(ignoreUnknown = true)

public class ExerciseLogInfo {
	static int latestExerciseID = 0;
	static long exerciseLogID = 0; // TODO unique exercise log id ?
	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	org.apache.log4j.Logger logger = LogHelper.getLogger( "ExerciseLogInfo" );

	@JsonProperty private long childID;
	@JsonProperty private long exerciseID;

	//private ArrayList<String> promptingTypes;	//TODO
	@JsonProperty private String promptingStrategy;

	@JsonProperty private String reinforcementStrategy;

	@JsonProperty private String timestampBeginExercise;
	@JsonProperty private String timestampEndExercise;

	@JsonProperty private int numberOfDistractorHits;		//numberOfWrongAttempts
	@JsonProperty private boolean correct;
	@JsonProperty private boolean skipped;

	//private SimpleMultipleChoice exercise;

	/*
	public ExerciseLogInfo() {
		numberOfDistractorHits = 0;

		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	public ExerciseLogInfo(SimpleMultipleChoice exercise) {
		this.exercise = exercise;
		numberOfDistractorHits = 0;
	}
	*/

	public ExerciseLogInfo(long childID, long exerciseID, String promptingStrategy, String reinforcementStrategy) {
		this.childID = childID;
		this.exerciseID = exerciseID;
		this.promptingStrategy = promptingStrategy;
		this.reinforcementStrategy = reinforcementStrategy;
		this.timestampBeginExercise = dateFormat.format(new Date());  //now
		//dateFormat.format(timestampBeginExercise)

		//TODO prompting types

		numberOfDistractorHits = 0;
	}

	/*
	@Override
	public String toString() {
		return "Exercise log:" +
				"\n\tId: " + this.exercise.getExerciseId() +
				"\n\tSkipped: " + this.skipped +
				"\n\tCorrect: " + this.correct +
				"\n\tDistractors Count: " + this.numberOfDistractorHits;
	}
	*/

	/***** Getters and Setters *****/

	public long getExerciseID() {
		return exerciseID;
	}

	public void setExerciseID(long exerciseID) {
		this.exerciseID = exerciseID;
	}

	public long getChildID() {
		return childID;
	}

	public void setChildID(long childID) {
		this.childID = childID;
	}

	public String getTimestampBeginExercise() {
		return timestampBeginExercise;
	}

	public void setTimestampBeginExercise(String timestampBeginExercise) {
		this.timestampBeginExercise = timestampBeginExercise;
	}

	public String getTimestampEndExercise() {
		return timestampEndExercise;
	}

	public void setTimestampEndExercise(String timestampEndExercise) {
		this.timestampEndExercise = timestampEndExercise;
	}

	public String getPromptingStrategy() {
		return promptingStrategy;
	}

	public void setPromptingStrategy(String promptingStrategy) {
		this.promptingStrategy = promptingStrategy;
	}

	public String getReinforcementStrategy() {
		return reinforcementStrategy;
	}

	public void setReinforcementStrategy(String reinforcementStrategy) {
		this.reinforcementStrategy = reinforcementStrategy;
	}
	
	public void incDistractorHitsCount() {
		numberOfDistractorHits++;
	}

	/**
	 * @return the exercise
	 */
	/*
	public SimpleMultipleChoice getExercise() {
		return exercise;
	}
	*/

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
	 * @param numberOfDistractorHits the numberOfDistractorHits to set
	 */
	public void setNumberOfDistractorHits(int numberOfDistractorHits) {
		this.numberOfDistractorHits = numberOfDistractorHits;
	}

    /**
     * @return the numberOfDistractorHits
     */
    public int getNumberOfDistractorHits() {
        return numberOfDistractorHits;
    }

	/**
	 * @param exercise the exercise to set
	 */
	/*
	public void setExercise(SimpleMultipleChoice exercise) {
		this.exercise = exercise;
	}
	*/

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


	public void log(int numberOfWrongAttempts, boolean correctAnswer) {
		this.timestampEndExercise = dateFormat.format(new Date());  //now

		this.numberOfDistractorHits = numberOfWrongAttempts;
		if (correctAnswer) {
			this.correct = true;
			this.skipped = false;
		}
		else {	//skipped
			this.correct = false;
			this.skipped = true;
		}

		logger.info(exerciseLogIntoToJson());
	}

	public String exerciseLogIntoToJson() {
		ObjectMapper mapper = new ObjectMapper();

		String logJsonString = "";

		try {
			logJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return logJsonString;
	}
}
