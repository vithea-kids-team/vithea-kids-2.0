package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.ModelOperations.ContextOperation;
import controllers.ModelOperations.ExerciseOperations;
import controllers.ModelOperations.McOperations;
import java.sql.Timestamp;
import java.util.*;
import javax.inject.Inject;
import models.Caregiver;
import models.Exercise;
import models.Level;
import models.MultipleChoice;
import models.Topic;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;

@Security.Authenticated(Secured.class)
public class AdminExerciseCtrl extends Controller {
    
    public AdminLogs adminLogs = new AdminLogs();
    
    
    //FIXME Adaptar
    public static Result registerExerciseFromCSV(String topic, String level, String question, String rightAnswer, List<String> distractors){
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
                    
        Exercise exercise = null;
        
        // create topic (or find id by description if exists)
        Long topicId;
        Topic topicExists = Topic.findTopicByDescription(topic);
        
        if (topicExists == null) {
            Topic newTopic = new Topic(topic, loggedCaregiver, false);
            newTopic.save();
            topicId = newTopic.getTopicId();
        } else {
            topicId = topicExists.getTopicId();
        }
        
        // create level (or find id by description if exists)
        Long levelId;
        Level levelExists = Level.findLevelByDescription(level);
        
        if (levelExists == null) {
            Level newLevel = new Level(level, loggedCaregiver, false);
            newLevel.save();
            levelId = newLevel.getLevelId();
        } else {
            levelId = levelExists.getLevelId();
        }
        
        //exercise = new MultipleChoice(loggedCaregiver, topicId, levelId, question, -1, rightAnswer, distractors, false);
        
        //exercise.save();
        
        return ok(Json.toJson(exercise));
    }
    
    
    @Inject
    FormFactory formFactory;
    public Result registerExercise() {
        DynamicForm registerExerciseForm = formFactory.form().bindFromRequest();
        JsonNode json = request().body().asJson();
        Logger.debug("JSON " + json);
        if (registerExerciseForm.hasErrors()) {
            return badRequest(registerExerciseForm.errorsAsJson());
        }
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        Logger.debug(registerExerciseForm.get("type"));
        
        ExerciseOperations contextExercise = new ContextOperation().selectExerciseOperations(registerExerciseForm.get("type"));
        Exercise exercise = contextExercise.createExercise(registerExerciseForm, json);
         
        return ok(Json.toJson(exercise));
    }
    
    /**
     * EditExercise action
     *
     * @param exerciseId
     * @return
     */
    @SuppressWarnings("empty-statement")
    public Result editExercise(long exerciseId) {
        
        DynamicForm editExerciseForm = formFactory.form().bindFromRequest();

        Logger.debug("DEBUG:" + editExerciseForm);

        if (editExerciseForm.hasErrors()) {
            return badRequest(editExerciseForm.errorsAsJson());
        }
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        Exercise exercise = Exercise.findExerciseById(exerciseId);
        if (exercise == null) {
            return badRequest(buildJsonResponse("error", "Exercise doesn't exist"));
        }
             
        ExerciseOperations  contextExercise = new ContextOperation().selectExerciseOperations(editExerciseForm.get("type"));
        exercise = contextExercise.editExercise(editExerciseForm, exerciseId, loggedCaregiver);
       
        return ok(Json.toJson(exercise));        
    }
    
    public Result getExercise(Long exercise) {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        Exercise ex = Exercise.findExerciseById(exercise);
        Logger.debug("Exercise found");
        return ok(Json.toJson(ex));
    }
    public Result getExercises() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        List<Exercise> exercises = Exercise.findByAuthor(loggedCaregiver);
        Logger.debug(exercises.size() + " exercises registered.");
        return ok(Json.toJson(exercises));
    }
    public Result getTopics() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        return ok(Json.toJson(Topic.findByAuthor(loggedCaregiver)));
    }
    public Result getLevels() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        return ok(Json.toJson(Level.findByAuthor(loggedCaregiver)));
    }

    public Result addTopic() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        DynamicForm addTopicForm = formFactory.form().bindFromRequest();

        if (addTopicForm.hasErrors()) {
            return badRequest(addTopicForm.errorsAsJson());
        }
        
        String topicDesc = addTopicForm.get("newTopic");
        
        Topic topic = new Topic(topicDesc, loggedCaregiver, false);
        topic.save();
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = topic.getTopicId()+ "," + loggedCaregiver.getCaregiverId() + "," + "," + 
                timestamp.toLocalDateTime() + "," + "create" + "," + topic.getTopicDescription() + "," + "false" + "\n";
        String pathTopic = loggedCaregiver.getPathTopicsLog();
        adminLogs.writeToFile(pathTopic, content);
        
        return ok();
    }
    public Result addLevel() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        DynamicForm addLevelForm = formFactory.form().bindFromRequest();

        if (addLevelForm.hasErrors()) {
            return badRequest(addLevelForm.errorsAsJson());
        }
        
        String levelDesc = addLevelForm.get("newLevel");
        
        Level level = new Level(levelDesc, loggedCaregiver, false);
        level.save();
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = level.getLevelId()+ "," + loggedCaregiver.getCaregiverId() + "," + "," +
                timestamp.toLocalDateTime() + "," + "create" + "," + level.getLevelDescription()+ "," + "false" + "\n";
        String pathLevel = loggedCaregiver.getPathLevelsLog();
        adminLogs.writeToFile(pathLevel, content);
        
        return ok();
    }
    
    public Result removeTopic(Long topic) {
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        Topic existingTopic = Topic.findTopicById(topic);
        existingTopic.delete();
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = existingTopic.getTopicId()+ "," + loggedCaregiver.getCaregiverId() + "," + "," +
                timestamp.toLocalDateTime() + "," + "delete" + "," + "," + "false" + "\n";
        String pathTopic = loggedCaregiver.getPathTopicsLog();
        adminLogs.writeToFile(pathTopic, content);
        
        return ok();
    }
    public Result removeLevel(Long level) {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        Level existingLevel = Level.findLevelById(level);
        existingLevel.delete();
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = existingLevel.getLevelId()+ "," + loggedCaregiver.getCaregiverId() + "," +  "," +
                timestamp.toLocalDateTime() + "," + "delete" + "," + "," + "false" + "\n";
        String pathLevel = loggedCaregiver.getPathLevelsLog();
        adminLogs.writeToFile(pathLevel, content);
        
        return ok();
    }
    
    public Result deleteExercise(long exerciseId) {
        Exercise exercise =  Exercise.findExerciseById(exerciseId); //TODO: generate this for any kinf

        if (exercise == null) {
            return badRequest(buildJsonResponse("error", "Exercise doesn't exist"));
        }

        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        ExerciseOperations  contextExercise = new ContextOperation().selectExerciseOperations(exercise);
        contextExercise.deleteExercise(exerciseId, loggedCaregiver);
        
        return ok(buildJsonResponse("success", "Exercise deleted successfully"));
    }
    
    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }

}