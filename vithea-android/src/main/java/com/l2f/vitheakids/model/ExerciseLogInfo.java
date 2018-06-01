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


	public ExerciseLogInfo(long childID, long exerciseID, String promptingStrategy, String reinforcementStrategy) {
		this.childID = childID;
		this.exerciseID = exerciseID;
		this.promptingStrategy = promptingStrategy;
		//TODO prompting types
		this.reinforcementStrategy = reinforcementStrategy;
		this.timestampBeginExercise = dateFormat.format(new Date());  //now

		numberOfDistractorHits = 0;
		//TODO distractors hit

		//TODO exercise preview

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
