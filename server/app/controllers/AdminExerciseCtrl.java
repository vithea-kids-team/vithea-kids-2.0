package controllers;

import com.avaje.ebean.Ebean;
import play.mvc.*;
import play.libs.Json;
import play.data.FormFactory;
import play.Logger;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;

import javax.inject.Inject;

import java.util.*;

import models.Topic;
import models.Level;
import models.Exercise;
import models.Caregiver;
import models.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import static java.lang.Integer.parseInt;
import java.sql.Timestamp;
import models.ResourceArea;
import models.Sequence;
import org.apache.commons.lang3.StringUtils;
import play.data.DynamicForm;

@Security.Authenticated(Secured.class)
public class AdminExerciseCtrl extends Controller {

    @Inject
    FormFactory formFactory;

    public Result registerExercise() {
        DynamicForm registerExerciseForm = formFactory.form().bindFromRequest();

        if (registerExerciseForm.hasErrors()) {
            return badRequest(registerExerciseForm.errorsAsJson());
        }

        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        Exercise exercise = null;
        
        int topic;
        try {
            topic = parseInt(registerExerciseForm.get("topic"));
        } catch (NumberFormatException e) {
            topic = -1;
        } 
 
        int level;
        try {
            level = parseInt(registerExerciseForm.get("level"));
        } catch (NumberFormatException e) {
            level = -1;
        }
        
        int stimulusId;
        try {
            stimulusId = parseInt(registerExerciseForm.get("stimulus"));
        } catch (NumberFormatException e) {
            stimulusId = -1;
        }
        
        String question = registerExerciseForm.get("question");
        
        if(registerExerciseForm.get("type").equals("text")) {
            String answer = registerExerciseForm.get("rightAnswer");
            List<String> distractors = new ArrayList();
            registerExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("answers"))).forEachOrdered((key) -> {
                distractors.add(registerExerciseForm.data().get(key));
            });


            exercise = new Exercise(loggedCaregiver, topic, level, question, stimulusId, answer, distractors);
   
        } else if(registerExerciseForm.get("type").equals("image")) {
            int answerResourceId;
            try {
                answerResourceId = parseInt(registerExerciseForm.get("rightAnswerImg"));
            } catch (NumberFormatException e) {
                answerResourceId = -1;
            }
            
            List<Long> distractorsResourcesIds = new ArrayList<Long>();
            int i = 0;
            while(true) {
                String key = "answersImg[" + i + "]";
                if (registerExerciseForm.data().containsKey(key)) {
                    int answerId;
                    try {
                        answerId = parseInt(registerExerciseForm.data().get(key));
                    } catch (NumberFormatException e) {
                        answerId = -1;
                    }
                    
                    distractorsResourcesIds.add((long)answerId);
                } else {
                    break;
                }
                i++;
            }
            
            exercise = new Exercise(loggedCaregiver, topic, level, question, stimulusId, answerResourceId, distractorsResourcesIds);
        }
        
        exercise.save();

        String sequenceId = registerExerciseForm.get("sequenceId");
        if(sequenceId != null && !"".equals(sequenceId)) {
            int sequence;
            try {
                sequence = parseInt(sequenceId);
                Logger.debug("Adding exercise to sequence " + sequence);
                Sequence currentSequence = Sequence.findById((long)sequence);
                if (currentSequence != null) {
                    currentSequence.getSequenceExercises().add(exercise);
                    currentSequence.save();
                } else Logger.debug("Sequence " + sequenceId + " does not exist!");
            } catch (NumberFormatException e) {
                Logger.debug("ERROR: " + e);
            }
        }

        return ok(Json.toJson(exercise));
    }

    public Result editExercise(long exerciseId) {
        return ok("yey");
    }

    public Result deleteExercise(long exerciseId) {
        Exercise exercise = Exercise.findById(exerciseId);

        if (exercise == null) {
            return badRequest(buildJsonResponse("error", "Exercise doesn't exist"));
        }

        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        Sequence.getAll().forEach((seq) -> {
            seq.getSequenceExercises().remove(exercise);
            seq.save();
        });
        
        Logger.debug("Deleting " + loggedCaregiver.getCaregiverLogin().getUsername() + "'s' exercise. ");

        exercise.delete();

        return ok(buildJsonResponse("success", "Exercise deleted successfully"));
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
        return ok(Json.toJson(Topic.getAll()));
    }
    
    public Result addTopic() {
        DynamicForm addTopicForm = formFactory.form().bindFromRequest();

        if (addTopicForm.hasErrors()) {
            return badRequest(addTopicForm.errorsAsJson());
        }
        
        String topicDesc = addTopicForm.get("newTopic");
        
        Topic topic = new Topic(topicDesc);
        topic.save();
        
        return ok();
    }
    
    public Result removeTopic(Long topic) {
        Topic existingTopic = Topic.findTopicById(topic);
        existingTopic.delete();
        return ok();
    }
    
    public Result removeLevel(Long level) {
        Level existingLevel = Level.findLevelById(level);
        existingLevel.delete();
        return ok();
    }

    public Result getLevels() {
        return ok(Json.toJson(Level.getAll()));
    }
    
    public Result addLevel() {
        DynamicForm addLevelForm = formFactory.form().bindFromRequest();

        if (addLevelForm.hasErrors()) {
            return badRequest(addLevelForm.errorsAsJson());
        }
        
        String levelDesc = addLevelForm.get("newLevel");
        
        Level level = new Level(levelDesc);
        level.save();
        
        return ok();
    }

    public Result getResources() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        return ok(Json.toJson(Resource.findByOwner(loggedCaregiver)));
    }

    public Result uploadResources(String type) {
        Logger.debug("Uploading " + type);
        MultipartFormData<File> body = request().body().asMultipartFormData();
        Logger.debug("body -> " + body);
        List<FilePart<File>> resources = body.getFiles();

        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                Logger.debug("resource -> " + resource);
                if (resource != null) {
                    String fileName = resource.getFilename();
                    File file = resource.getFile();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
                    String folderPath = "images/" + type +"/" + timestamp.getTime() + StringUtils.stripAccents(fileName);
                    String path = "../client/src/" + folderPath;
                    
                    Boolean uploaded = file.renameTo(new File(path));
                    
                    Logger.debug(folderPath + " " + uploaded);
                    if (uploaded) { ResourceArea resourceArea;
                        
                        switch (type) {
                            case "stimuli":
                                resourceArea = ResourceArea.STIMULI;
                                break;
                            case "reinforcement":
                                resourceArea = ResourceArea.REINFORCEMENT;
                                break;    
                            case "answers":
                                resourceArea = ResourceArea.ANSWERS;
                                break;
                            default:
                                resourceArea = ResourceArea.STIMULI;
                        }
                        
                        Resource res = new Resource(loggedCaregiver, folderPath, resourceArea);
                        res.save();
                        return ok(Json.toJson(res));
                    }
                    return badRequest("Not possible to upload");
                }    
            }            
        } catch (Exception e) {
            return badRequest(e.getLocalizedMessage());
        }
        return null;
    }

    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}
