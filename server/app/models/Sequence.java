package models;

import java.util.List;

import com.avaje.ebean.Model;


public class Sequence extends Model {


	private Long sequenceId;
	
	private String sequenceName;
	

    private List<Exercise> exerciseList; 
	
	public Sequence(String name) {
		this.sequenceName = name;
	}
	
	public Long getSequenceId() {
		return this.sequenceId;
	}

	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	public String getSequenceName() {
		return this.sequenceName;
	}

	public void setSequenceName(String name) {
		this.sequenceName = name;
	}
		
	public List<Exercise> getSequenceExercises() {
		return this.exerciseList;
	}

	public void setSequenceExercises(List<Exercise> exerciseList) {
		this.exerciseList = exerciseList;
	}
		
}