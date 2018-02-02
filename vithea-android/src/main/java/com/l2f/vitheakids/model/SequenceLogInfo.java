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

@JsonPropertyOrder({ "childID", "sequenceID", "timestampBeginSequence", "timestampEndSequence",
		"numberOfExercises", "correctExercises", "skippedExercises", "distractorHitsAvg",
		"exercisesLogs"})
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

	@JsonProperty private float distractorHitsAvg;
	//sum of all distractors' hits of all exercises
	private int totalDistractorHits;
	
	public SequenceLogInfo(long childID, long sequenceID, int numberOfExercises) {
		this.childID = childID;
		this.sequenceID = sequenceID;
		this.numberOfExercises = numberOfExercises;
		this.correctExercises = 0;
		this.skippedExercises = 0;
		this.distractorHitsAvg = 0;
		this.totalDistractorHits = 0;

		// Time of the beginning of the sequence
		this.timestampBeginSequence = dateFormat.format(new Date());  //now

		this.exercisesLogs = new ArrayList<ExerciseLogInfo>();
	}

	public String log() {
		// Time of the end of the sequence
		this.timestampEndSequence = dateFormat.format(new Date());    //now

		for(ExerciseLogInfo e : exercisesLogs) {
			//Exercises that were skipped or right-answered
			if (e.isSkipped()) {
				skippedExercises++;
			}
			else if (e.isCorrect()) {
				correctExercises++;
			}

			//Distractors' hits sum
			totalDistractorHits += e.getNumberOfDistractorHits();
		}

		//Average of the distrators' hits
		distractorHitsAvg = totalDistractorHits / numberOfExercises;

		//Logs on logcat and on the file exercisesLogs.json on the Android device
		logger.info(sequenceLogInfoToJson());

		return sequenceLogInfoToJson();
	}

	//Prints "Pretty" Json: with EOLs and tabs
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

	//Prints regular Json: in one line, without EOLs or tabs
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


	//Every time an exercise is finished, its ExerciseLogInfo is added to the current SequenceLogInfo
	public void addFinishedExercise(ExerciseLogInfo exerciseLogInfo, int currentExercisePosition) {
		//In the case of returning to a previous exercise, it is set, not added
		if (exercisesLogs.size() > currentExercisePosition) {
			exercisesLogs.set(currentExercisePosition, exerciseLogInfo);
		}
		//Otherwise, i.e. the first time this exercise's ExerciseLogInfo is added
		else {
			exercisesLogs.add(currentExercisePosition, exerciseLogInfo);
		}
	}
}
