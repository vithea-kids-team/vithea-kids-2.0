/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.ModelOperations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import controllers.SecurityController;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.List;
import models.Caregiver;
import models.Exercise;
import models.Resource;
import models.SelectionArea;
import models.SelectionImageExercise;
import play.Logger;
import play.data.DynamicForm;


public class SelectionImageOperations implements ExerciseOperations{

    @Override
    public Exercise createExercise(DynamicForm form, JsonNode json) {
        Exercise exercise = null;
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        long topic;
        try {
            topic = parseInt(form.get("topic"));
        } catch (NumberFormatException e) {
            topic = -1;
        }
        long level;
       
        try {
            level = parseInt(form.get("level"));
        } catch (NumberFormatException e) {
            level = -1;
        }
        
        String question = form.get("question"); //question
        System.out.println("question: " + question);
        Long stimulusId = json.get("stimulus").longValue(); //stimulus
        Resource stimulus = Resource.findById(stimulusId); //resource
        

        Long widthOriginal = json.get("widthOriginal").longValue(); //stimulus
        Long heighOriginal = json.get("heightOriginal").longValue(); //stimulus

        //getting selectionAreas
        String jsonSelectionAreas = json.get("selectionsAreas").toString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<SelectionArea> selectionAreas = mapper.readValue(jsonSelectionAreas, TypeFactory.defaultInstance().constructCollectionType(List.class, SelectionArea.class));
            exercise = new SelectionImageExercise(question, loggedCaregiver, false, topic, level, question, widthOriginal, heighOriginal, selectionAreas, stimulus);
            exercise.save();
        } catch (IOException ex) {
            Logger.debug(ex.getMessage());
        }

        return exercise;
    }

    @Override
    public void deleteExercise(long exerciseId, Caregiver loggedCaregiver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Exercise editExercise(DynamicForm registerExerciseForm, long id, Caregiver loggedCaregiver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
