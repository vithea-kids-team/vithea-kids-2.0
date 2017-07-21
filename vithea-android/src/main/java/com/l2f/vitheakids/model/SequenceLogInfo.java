package com.l2f.vitheakids.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class SequenceLogInfo {
	
	private String username;
	private Date timestamp;
	private List<ExerciseLogInfo> exercisesDetails;
	private int totalCount;
	private int correctCount;
	private int skippedCount;
	private float distractorHitsAvg;
	private Long sequenceId;
	
	public SequenceLogInfo() {
		exercisesDetails = new ArrayList<ExerciseLogInfo>();		
		totalCount = 0;		
		correctCount= 0;		
		skippedCount = 0;
		distractorHitsAvg = 0;
	}
	
	@Override
	public String toString() {
		
		return "Sequence log:" +
				"\n - Child: " + this.username +
				"\n\tTimestamp: " + this.timestamp +
				"\n\tTotal: " + this.totalCount +
				"\n\tSkipped Count: " + this.skippedCount +
				"\n\tCorrect Count: " + this.correctCount +
				"\n\tDistractors Avg: " + this.distractorHitsAvg;
	}
	public String toJSON() {
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		ObjectNode subNode;
		ArrayNode array = node.putArray("exercisesDetails");
		
		for(ExerciseLogInfo eli : exercisesDetails) {
			subNode = new ObjectMapper().createObjectNode();
			subNode.put("exerciseId", eli.getExercise().getExerciseId());
			subNode.put("correct", eli.getCorrect());
			subNode.put("skipped", eli.getSkipped());
			subNode.put("distractorHitsCount", eli.getDistractorHitsCount());
			subNode.put("usingPrompting", eli.getUsingPrompting());
			subNode.put("usingReinforcement", eli.getUsingReinforcement());
			array.add(subNode);
		}
		
		node.put("username", this.username);
		node.put("timestamp", this.timestamp.getTime());
		
//		node.put("exercisesDetails", array.to);
		
		node.put("totalCount", this.totalCount);
		node.put("correctCount", this.correctCount);
		node.put("skippedCount", this.skippedCount);
		node.put("distractorHitsAvg", this.distractorHitsAvg);
		node.put("sequenceId", this.sequenceId);
		
		try {
			return mapper.writeValueAsString(node);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void incCorrectCount() {
		correctCount++;
	}
	public void incSkippedCount() {
		skippedCount++;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }
    /**
     * @return the exercisesDetails
     */
    public List<ExerciseLogInfo> getExercisesDetails() {
        return exercisesDetails;
    }
    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }
    /**
     * @return the correctCount
     */
    public int getCorrectCount() {
        return correctCount;
    }
    /**
     * @return the skippedCount
     */
    public int getSkippedCount() {
        return skippedCount;
    }
    /**
     * @return the distractorHitsAvg
     */
    public float getDistractorHitsAvg() {
        return distractorHitsAvg;
    }
    /**
     * @return the sequenceId
     */
    public Long getSequenceId() {
        return sequenceId;
    }

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @param exercisesDetails the exercisesDetails to set
	 */
	public void setExercisesDetails(List<ExerciseLogInfo> exercisesDetails) {
		this.exercisesDetails = exercisesDetails;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @param correctCount the correctCount to set
	 */
	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}
	/**
	 * @param skippedCount the skippedCount to set
	 */
	public void setSkippedCount(int skippedCount) {
		this.skippedCount = skippedCount;
	}
	/**
	 * @param distractorHitsAvg the distractorHitsAvg to set
	 */
	public void setDistractorHitsAvg(float distractorHitsAvg) {
		this.distractorHitsAvg = distractorHitsAvg;
	}
	/**
	 * @param sequenceId the sequenceId to set
	 */
	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}

}
