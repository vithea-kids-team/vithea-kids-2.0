package controllers;

import play.mvc.*;
import play.libs.Json;
import play.data.FormFactory;
import play.Logger;

import javax.inject.Inject;

import java.util.*;

import models.Topic;
import models.Level;
import models.Exercise;
import models.Caregiver;
import models.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import static java.lang.Integer.parseInt;
import models.Sequence;
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
                sequence = parseInt(registerExerciseForm.get("sequenceId"));
                Logger.debug("Adding exercise to sequence " + sequence);
                Sequence currentSequence = Sequence.findById(sequence);
                if (currentSequence != null) {
                    currentSequence.getSequenceExercises().add(exercise);
                    currentSequence.save();
                }
            } catch (NumberFormatException e) {
                //Do nothing...
            }
        }

        return ok(Json.toJson(exercise));
    }

    public Result editExercise(long exerciseId) {
        return ok("yey");
    }

    public Result deleteExercise(long exerciseId) {
        return ok("yey");
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

    public Result getLevels() {
        return ok(Json.toJson(Level.getAll()));
    }

    public Result getResources() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        return ok(Json.toJson(Resource.findByOwner(loggedCaregiver)));
    }
    
    public Result getSequences() {
        return ok(Json.toJson(Sequence.getAll()));
    }

    public Result uploadResources(String type) {
        Logger.debug("Uploading " + type);
        /* MultipartFormData<File> body = request().body().asMultipartFormData();
        Logger.debug("body -> " + body);
        List<FilePart<File>> resources = body.getFiles();

        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                Logger.debug("resource -> " + resource);
                if (resource != null) {
                    String fileName = resource.getFilename();
                    String contentType = resource.getContentType();
                    File file = resource.getFile();

                    String path = "../client/app/images/"+ type.replace(":","") +"/" + fileName;
                    
                    Boolean uploaded = file.renameTo(new File(path));

                    String typeOfRes = type.replace(":","");
                    
                    Logger.debug("filename -> " + fileName + " " + contentType + " " + path + " "+ uploaded);
                    if (uploaded) {
                        Caregiver loggedCaregiver = Caregiver.findByUsername(session("username"));
                        Resource res = new Resource();
                        res.setOwner(loggedCaregiver);
                        res.setResourcePath("images/"+ typeOfRes +"/" + fileName);
                        res.setResourceArea(typeOfRes);
                        res.save();

                        return ok(Json.toJson(res));
                    }

                    flash("error", "Could not upload file for " + typeOfRes);
                    return badRequest();
                }    
            }            
        } catch (Exception e) {
            flash("error", "Missing file");
            return badRequest();
        }    */

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
