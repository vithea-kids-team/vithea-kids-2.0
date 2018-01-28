package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l2f.vitheakids.LogHelper;
import com.l2f.vitheakids.rest.SendLogs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

@JsonPropertyOrder({ "childID", "sequenceID", "numberOfExercises", "correctExercises",
		"skippedExercises", "exercisesLogs"})
@JsonIgnoreProperties(ignoreUnknown = true)

public class SequenceLogInfo {
	static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	org.apache.log4j.Logger logger = LogHelper.getLogger( "ExerciseLogInfo" );

	@JsonProperty private long childID;
	@JsonProperty private long sequenceID;

	@JsonProperty private int numberOfExercises;
	@JsonProperty private int correctExercises;
	@JsonProperty private int skippedExercises;

	@JsonProperty private String timestampBeginSequence;
	@JsonProperty private String timestampEndSequence;

	@JsonProperty private List<ExerciseLogInfo> exercisesLogs;
	private float distractorHitsAvg;
	
	public SequenceLogInfo(long childID, long sequenceID, int numberOfExercises) {
		this.childID = childID;
		this.sequenceID = sequenceID;
		this.numberOfExercises = numberOfExercises;
		this.correctExercises = 0;
		this.skippedExercises = 0;

		// Time of the beginning of the sequence
		this.timestampBeginSequence = dateFormat.format(new Date());  //now

		this.exercisesLogs = new ArrayList<ExerciseLogInfo>();
	}

	public String log() {
		// Time of the end of the sequence
		this.timestampEndSequence = dateFormat.format(new Date());    //now

		logger.info(sequenceLogInfoToJsonPretty());

		return sequenceLogInfoToJson();
	}

	public String sequenceLogInfoToJsonPretty() {
		ObjectMapper mapper = new ObjectMapper();

		String logJsonString = "";

		try {
			logJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return logJsonString;
	}

	public String sequenceLogInfoToJson() {
		ObjectMapper mapper = new ObjectMapper();

		String logJsonString = "";

		try {
			logJsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return logJsonString;
	}


	public void addFinishedExercise(ExerciseLogInfo exerciseLogInfo) {
		if (exerciseLogInfo.isCorrect()) {		//correct exercise
			this.correctExercises++;
		}
		else if (exerciseLogInfo.isSkipped()) {	//exercise skipped
			this.skippedExercises++;
		}

		exercisesLogs.add(exerciseLogInfo);
	}
}
