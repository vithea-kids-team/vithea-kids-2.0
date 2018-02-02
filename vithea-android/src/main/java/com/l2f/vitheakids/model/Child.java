package com.l2f.vitheakids.model;

import android.content.res.Resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Updated by Soraia Meneses Alarcão on 20/07/2017
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Child {

	@JsonProperty private String firstName;
	@JsonProperty private String lastName;
    @JsonProperty private String username;
    @JsonProperty private Boolean enabled;
    private List<SequenceExercises> sequencesList; // sequences or exercises in the sequence?
	@JsonProperty private List<PersonalMessage> personalMessagesList;
	@JsonProperty private Long childId;
	@JsonProperty private AnimatedCharacter animatedCharacter;
	@JsonProperty private Prompting prompting;
	@JsonProperty private Reinforcement reinforcement;
	@JsonProperty private SequenceExercisesPreferences sequenceExercisesPreferences;
	@JsonProperty private Boolean emotions;



	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getUsername() {
		return username;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public List<SequenceExercises> getSequencesList() {
		return sequencesList;
	}
	public List<PersonalMessage> getPersonalMessageList() {
		return personalMessagesList;
	}
	public Long getChildId() { return childId; }
    public AnimatedCharacter getAnimatedCharacter() {
        return animatedCharacter;
    }
	public Prompting getPrompting() {
		return prompting;
	}
	public Reinforcement getReinforcement() {
		return reinforcement;
	}
	public Boolean getEmotions() {
		return emotions;
	}
	public SequenceExercisesPreferences getSequenceExercisesPreferences() {
		return sequenceExercisesPreferences;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
    public void setSequencesList(List<SequenceExercises> sequencesList) {
        this.sequencesList = sequencesList;
    }
    public void setPersonalMessagesList(List<PersonalMessage> personalMessagesList) {
		this.personalMessagesList = personalMessagesList;
	}
	public void setChildId(Long childId) {
		this.childId = childId;
	}
	public void setAnimatedCharacter(AnimatedCharacter animatedCharacter) {
		this.animatedCharacter = animatedCharacter;
	}
    public void setPrompting(Prompting prompting) {
        this.prompting = prompting;
    }
    public void setReinforcement(Reinforcement reinforcement) {
        this.reinforcement = reinforcement;
    }
    public void setEmotions(Boolean emotions) {
        this.emotions = emotions;
    }


	public List<Resource> getAllResourcesBySeqPosition(Integer position){
		List<Resource> seqExercise = sequencesList.get(position).getAllResources();
		Resource reinforcementResource = reinforcement.getReinforcementResource();

        if(reinforcementResource!=null)
		    seqExercise.add(reinforcementResource);

		return seqExercise;
	}

	public SequenceExercises getSequenceBySequenceID(long id) {
		for (SequenceExercises seq : sequencesList) {
			if (seq.getSequenceId().equals(id)) {
				return seq;
			}
		}
		return null;
	}

}