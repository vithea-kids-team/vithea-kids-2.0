package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Child {
	@JsonProperty
	private String firstName;
	@JsonProperty
	private String lastName;
    @JsonProperty
    private String username;
    @JsonProperty
    private Boolean enabled;
	@JsonProperty
	private List<SequenceExercises> sequencesList;
	@JsonProperty
	private List<PersonalMessage> personalMessagesList;
	@JsonProperty
	private Long childId;
	@JsonProperty
	private AnimatedCharacter animatedCharacter;
	@JsonProperty
	private Prompting prompting;
	@JsonProperty
	private Reinforcement reinforcement;
	@JsonProperty
	private Boolean emotions;

	public Prompting getPrompting() {
		return prompting;
	}

	public Reinforcement getReinforcement() {
		return reinforcement;
	}

	public String getFirstName() {
		return firstName;
	}

	public List<PersonalMessage> getPersonalMessageList() {
		return personalMessagesList;
	}

	public List<SequenceExercises> getSequencesList() {
		return sequencesList;
	}

    public String getUsername() {
        return username;
    }

	public AnimatedCharacter getAnimatedCharacter() {
		return animatedCharacter;
	}

	public Boolean getEmotions() {
		return emotions;
	}
}