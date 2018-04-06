package com.l2f.vitheakids.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silvi on 03/04/2018.
 */

public class SelectionImageExercise extends Exercise {
   @JsonProperty private String question1;
   @JsonProperty private float widthOriginal;
   @JsonProperty private float heightOriginal;
   @JsonProperty private Resource stimulus;
   @JsonProperty private List<SelectionArea> selectionAreas;

   public SelectionImageExercise(){

   }
    public SelectionImageExercise(Long exerciseId, Topic topic, Level level, String question1, float widthOriginal, float heightOriginal, Resource stimulus, List<SelectionArea> selectionAreas) {
        super(exerciseId, topic, level);
        this.question1 = question1;
        this.widthOriginal = widthOriginal;
        this.heightOriginal = heightOriginal;
        this.stimulus = stimulus;
        this.selectionAreas = selectionAreas;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public float getWidthOriginal() {
        return widthOriginal;
    }

    public void setWidthOriginal(float widthOriginal) {
        this.widthOriginal = widthOriginal;
    }

    public float getHeightOriginal() {
        return heightOriginal;
    }

    public void setHeightOriginal(float heightOriginal) {
        this.heightOriginal = heightOriginal;
    }

    public Resource getStimulus() {
        return stimulus;
    }

    public void setStimulus(Resource stimulus) {
        this.stimulus = stimulus;
    }

    public List<SelectionArea> getSelectionAreas() {
        return selectionAreas;
    }

    public void setSelectionAreas(List<SelectionArea> selectionAreas) {
        this.selectionAreas = selectionAreas;
    }

    @Override
    public  List<Resource> getAllUsedResources() {
        List<Resource> resources = new ArrayList<Resource>();
        Log.d("SelectionImageExercise", "loading resource");
        if (stimulus!=null) {
            Log.d("SelectionImageExercise", " resource not null");

            resources.add(stimulus);
        }

        return resources;
    }
}
