package com.l2f.vitheakids.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silvi on 03/04/2018.
 */

public class SpeechExercise extends Exercise {
   @JsonProperty private String question1;
   @JsonProperty private Resource stimulus;
   @JsonProperty private List<Answer> answers;

   public SpeechExercise(){

   }

   public SpeechExercise(Long exerciseId, Topic topic, Level level, String question1, Resource stimulus, List<Answer> answers) {
        super(exerciseId, topic, level);
        this.question1 = question1;
        this.stimulus = stimulus;
        this.answers = answers;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public Resource getStimulus() {
        return stimulus;
    }

    public void setStimulus(Resource stimulus) {
        this.stimulus = stimulus;
    }

    public List<Answer> getAnswers() { return answers; }

    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    @Override
    public  List<Resource> getAllUsedResources() {
        List<Resource> resources = new ArrayList<Resource>();
        Log.d("SpeechExercise", "loading resource");
        if (stimulus!=null) {
            Log.d("SpeechExercise", " resource not null");
            resources.add(stimulus);
        }
        return resources;
    }
}
