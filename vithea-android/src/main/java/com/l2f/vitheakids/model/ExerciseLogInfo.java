package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2f.vitheakids.LogHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

@JsonPropertyOrder({ "childID", "exerciseID", "promptingStrategy", "promptingTypes", "attempts",
		"typeExercise", "timestampBeginExercise", "timestampEndExercise", "reinforcementStrategy",
		"numberOfDistractorHits", "correct", "skipped"})
@JsonIgnoreProperties(ignoreUnknown = true)

public class ExerciseLogInfo {

	static long exerciseLogID = 0; // TODO unique exercise log id ?

	@JsonProperty private long childID;
	@JsonProperty private long exerciseID;
	@JsonProperty private String promptingStrategy;
	@JsonProperty private ArrayList<String> promptingTypes;
	@JsonProperty private ArrayList<String> attempts;
	@JsonProperty private String typeExercise;
	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	@JsonProperty private String timestampBeginExercise;
	@JsonProperty private String timestampEndExercise;
	@JsonProperty private String reinforcementStrategy;
	@JsonProperty private int numberOfDistractorHits;
	@JsonProperty private boolean correct;
	@JsonProperty private boolean skipped;
	@JsonProperty private Exercise exercisePreview;

	org.apache.log4j.Logger logger = LogHelper.getLogger( "ExerciseLogInfo" );


	public ExerciseLogInfo(long childID, long exerciseID, String promptingStrategy,
						   ArrayList<String> promptingTypes, String reinforcementStrategy) {
		this.childID = childID;
		this.exerciseID = exerciseID;
		this.promptingStrategy = promptingStrategy;
		this.promptingTypes = promptingTypes;
		this.reinforcementStrategy = reinforcementStrategy;
		this.timestampBeginExercise = dateFormat.format(new Date());  //now
		this.numberOfDistractorHits = 0;
		this.correct = false;
		this.skipped = false;
	}

	public boolean isCorrect() {
		return correct;
	}

	public boolean isSkipped() {
		return skipped;
	}

	public int getNumberOfDistractorHits() { return this.numberOfDistractorHits; }

	public void log(ArrayList<String> attempts, Exercise exercise,
					int numberOfWrongAttempts, boolean correctAnswer, boolean skipped,
					SequenceLogInfo currentSequenceLogInfo, int currentExercisePosition) {

		this.timestampEndExercise = dateFormat.format(new Date());  //now
		this.numberOfDistractorHits = numberOfWrongAttempts;
		this.correct = correctAnswer;
		this.skipped = skipped;
		this.typeExercise = exercise.getType();
		this.attempts = attempts;
		this.exercisePreview = exercise;

		currentSequenceLogInfo.addFinishedExercise(this, currentExercisePosition);
		//logger.info(exerciseLogInfoToJsonPretty());
	}

	public String exerciseLogInfoToJsonPretty() {
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
