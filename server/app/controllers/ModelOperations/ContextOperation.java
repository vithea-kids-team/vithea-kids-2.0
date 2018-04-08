/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.ModelOperations;

import models.Exercise;
import models.MultipleChoice;
import models.SelectionImageExercise;
import models.SpeechExercise;

/**
 *
 * @author silvi
 */
/**
 * Factory to get the right operations to apply  to a given exercise
 * @author silvi
 */
public class ContextOperation {
    
    
    public ExerciseOperations selectExerciseOperations(String exerciseType){
        
        if(exerciseType.equals("text") || exerciseType.equals("image")){
            return new McOperations();
        }
        
        if(exerciseType.equals("selectionImage")){
            return new SelectionImageOperations();
        }
        
        if(exerciseType.equals("speech")){
            return new SpeechOperations();
        }
        
        return null;
    }
    
     public ExerciseOperations selectExerciseOperations(Exercise exercise){
         if(exercise instanceof MultipleChoice){
               return new McOperations();
         }
         
         if(exercise instanceof SelectionImageExercise){
               return new SelectionImageOperations();
         }
         
         if(exercise instanceof SpeechExercise){
               return new SpeechOperations();
         }
         return null;
     }
      
}
