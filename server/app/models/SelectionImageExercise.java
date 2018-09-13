/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue( "SelectionImageExercise" )
public class SelectionImageExercise extends Exercise{
    
    private String question1;
    private float widthOriginal;
    private float heightOriginal;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Resource stimulus;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<SelectionArea> selectionAreas;

    public SelectionImageExercise(String name, Caregiver author, Boolean defaultExercise, Long topic, Long level, String question, float widthOriginal, float heightOriginal, List<SelectionArea> selectionAreas, Resource stimulus) {
        super(name, author, defaultExercise, topic, level);
       
        this.question1 = question;
        this.widthOriginal = widthOriginal;
        this.heightOriginal = heightOriginal;
        this.selectionAreas = selectionAreas;
        this.stimulus = stimulus;
        
    }   

    public String getQuestion1() {
        return question1;
    }

    public float getWidthOriginal() {
        return widthOriginal;
    }

    public float getHeightOriginal() {
        return heightOriginal;
    }

    public Resource getStimulus() {
        return stimulus;
    }

    public List<SelectionArea> getSelectionAreas() {
        return selectionAreas;
    }
    
    public void deleleSelectionAreas(){
        this.getSelectionAreas().forEach((selectionArea) ->{
               selectionArea.delete();
        });
        this.save();
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public void setSelectionAreas(List<SelectionArea> selectionAreas) {
        
         List<SelectionArea> arrayAux =  new ArrayList<SelectionArea>(this.selectionAreas);
        //remove all answers
        for(SelectionArea a : arrayAux){
            this.selectionAreas.remove(a);
            this.save();
            a.delete();
        }
        
       this.selectionAreas = selectionAreas;
    }
    
    
    
    
    
}
