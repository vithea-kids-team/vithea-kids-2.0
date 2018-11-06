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
import static java.lang.Long.parseLong;
import java.sql.Timestamp;
import java.util.List;
import models.Caregiver;
import models.Exercise;
import models.Level;
import models.MultipleChoice;
import models.Resource;
import models.SelectionArea;
import models.SelectionImageExercise;
import models.SpeechExercise;
import models.Topic;
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
        SelectionImageExercise exercise = (SelectionImageExercise) Exercise.findExerciseById(exerciseId);
        //deleting all references in a sequence
        exercise.deleteAllReferenceInSeq();
        //deleting all selection areas
        exercise.deleleSelectionAreas();
        exercise.delete();
    }

    @Override
    public Exercise editExercise(DynamicForm registerExerciseForm, long id, Caregiver loggedCaregiver,JsonNode json) {
        SelectionImageExercise exercise = (SelectionImageExercise) Exercise.findExerciseById(id); 
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
         // topic
        long topic;
        try {
            topic = parseLong(registerExerciseForm.get("topic"));
            Topic topicActual = exercise.getTopic();
            if(topic != topicActual.getTopicId()){
                String content = topicActual.getTopicId()+ "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "removeFromExercise" + ","  +  "," + "false" + "\n";
                String pathTopic = loggedCaregiver.getPathTopicsLog();
                //adminLogs.writeToFile(pathTopic, content);
            }
        } catch (NumberFormatException e) {
            topic = -1;
        }
        
        exercise.setTopic(topic);
        /*
        String content = topic + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "addToExercise" + ","  +  exercise.getTopic().getTopicDescription() + "," + "false" + "\n";
        String pathTopic = loggedCaregiver.getPathTopicsLog();
        adminLogs.writeToFile(pathTopic, content);*/
        long level;
        try {
            level = parseLong(registerExerciseForm.get("level"));
            Level levelActual = exercise.getLevel();
            if(level != levelActual.getLevelId()){
               // content = levelActual.getLevelId() + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                //timestamp.toLocalDateTime() + "," + "removeFromExercise" + ","  +  "," + "false" + "\n";
                //String pathLevel = loggedCaregiver.getPathLevelsLog();
                //adminLogs.writeToFile(pathLevel, content);
            }
        } catch (NumberFormatException e) {
            level = -1;
        }
        
         exercise.setLevel(level);
         
         /*
        content = level + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "addToExercise" + ","  +  exercise.getLevel().getLevelDescription() + "," + "false" + "\n";
        String pathLevel = loggedCaregiver.getPathLevelsLog();
        adminLogs.writeToFile(pathLevel, content);*/

        String question = registerExerciseForm.get("question");
        exercise.setQuestion1(question);
        exercise.setExerciseName(question);
        
        String jsonSelectionAreas = json.get("selectionsAreas").toString();
        Logger.debug(jsonSelectionAreas);
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<SelectionArea> selectionAreas = mapper.readValue(jsonSelectionAreas, TypeFactory.defaultInstance().constructCollectionType(List.class, SelectionArea.class));
            exercise.setSelectionAreas(selectionAreas);
        } catch (IOException ex) {
            Logger.debug(ex.getMessage());
        }

        exercise.save();
        
        return exercise;
    }

    
}
