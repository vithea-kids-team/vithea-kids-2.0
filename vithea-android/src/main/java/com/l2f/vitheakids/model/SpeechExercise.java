package com.l2f.vitheakids.model;

import android.util.Log;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soraiamenesesalarcao on 09/04/2018.
 */

public class SpeechExercise extends Exercise {
   @JsonProperty private String questionSpeech;
   @JsonProperty private Resource stimulusSpeech;
   @JsonProperty private List<Answer> answers;

   public SpeechExercise(){

   }

   public SpeechExercise(Long exerciseId, Topic topic, Level level, String questionSpeech, Resource stimulusSpeech, List<Answer> answers) {
        super(exerciseId, topic, level);
        Log.d("stimulus", stimulusSpeech.toString());
        this.questionSpeech = questionSpeech;
        this.stimulusSpeech = stimulusSpeech;
        this.answers = answers;
    }

    public String getQuestionSpeech() {
        return questionSpeech;
    }

    public void setQuestionSpeech(String questionSpeech) {
        this.questionSpeech = questionSpeech;
    }

    public Resource getStimulusSpeech() {
        return stimulusSpeech;
    }

    public void setStimulusSpeech(Resource stimulusSpeech) {
        this.stimulusSpeech = stimulusSpeech;
    }

    public List<Answer> getAnswers() { return answers; }

    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    public String getType(){
       return this.getClass().getName();
    }

    public ArrayList<String> getAttempts(){
        return null;
    }

    @Override
    public  List<Resource> getAllUsedResources() {
        List<Resource> resources = new ArrayList<Resource>();
        Log.d("SpeechExercise", "loading resource");
        if (stimulusSpeech != null) {
            Log.d("SpeechExercise", " resource not null");
            resources.add(stimulusSpeech);
        }
        else Log.d("SpeechExerciseStimulus", "loading resource failed");
        return resources;
    }
}
