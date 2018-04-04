/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.ModelOperations;

/**
 *
 * @author silvi
 */
/**
 * Factory to get the right operations to apply  to a given exercise
 * @author silvi
 */
public class ContextOperation {
    String exerciseType;
    
    public ContextOperation(String type){
        this.exerciseType=type;
    }
    
    public ExerciseOperations selectExerciseOperations(){
        
        if(exerciseType.equals("text") || exerciseType.equals("image")){
            return new McOperations();
        }
        
        if(exerciseType.equals("selectionImage")){
            return new SelectionImageOperations();
        }
        return null;
    }
      
}
