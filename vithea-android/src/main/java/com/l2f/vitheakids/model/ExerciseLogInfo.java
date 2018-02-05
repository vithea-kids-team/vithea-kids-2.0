package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2f.vitheakids.LogHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

@JsonPropertyOrder({ "childID", "exerciseID", "prompting", "reinforcementStrategy",
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

	//Prompting Strategy + Prompting Types
	@JsonProperty private Prompting prompting;

	@JsonProperty private String reinforcementStrategy;

	@JsonProperty private String timestampBeginExercise;
	@JsonProperty private String timestampEndExercise;

	@JsonProperty private int numberOfDistractorHits;		//numberOfWrongAttempts

	@JsonProperty private boolean correct;
	@JsonProperty private boolean skipped;



	public ExerciseLogInfo(long childID, long exerciseID, Prompting prompting, String reinforcementStrategy) {
		this.childID = childID;
		this.exerciseID = exerciseID;
		this.prompting = prompting;
		this.reinforcementStrategy = reinforcementStrategy;
		this.timestampBeginExercise = dateFormat.format(new Date());  //now

		numberOfDistractorHits = 0;

		correct = false;
		skipped = false;
	}

	public boolean isCorrect() {
		return correct;
	}

	public boolean isSkipped() {
		return skipped;
	}

	public int getNumberOfDistractorHits() { return this.numberOfDistractorHits; }

	public void log(int numberOfWrongAttempts, boolean correctAnswer, boolean skipped, SequenceLogInfo currentSequenceLogInfo, int currentExercisePosition) {
		this.timestampEndExercise = dateFormat.format(new Date());  //now

		this.numberOfDistractorHits = numberOfWrongAttempts;

		this.correct = correctAnswer;
		this.skipped = skipped;

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
