package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import java.sql.Timestamp;
import java.util.*;
import javax.inject.Inject;
import models.Answer;
import models.Caregiver;
import models.Exercise;
import models.Level;
import models.Resource;
import models.ResourceArea;
import models.Sequence;
import models.Topic;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

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
            
            String stimulusText = registerExerciseForm.get("stimulusText");
            
            List<Long> distractorsResourcesIds = new ArrayList<>();
            Map<String, String> data = registerExerciseForm.data();
            int numberDistractors = data.size();
            System.out.println(data);
            for(int i = 0; i < numberDistractors; i++){
                String key = "answersImg[" + i + "]";
                if(data.containsKey(key)){
                    int answerId;
                    try {
                        answerId = parseInt(data.get(key));
                    } catch (NumberFormatException e) {
                        answerId = -1;
                    }
                    distractorsResourcesIds.add((long)answerId);
                }
            }
            
            /*
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
            }*/
            exercise = new Exercise(loggedCaregiver, topic, level, question, stimulusText, answerResourceId, distractorsResourcesIds);
        }
        
        exercise.save();

        String sequenceId = registerExerciseForm.get("sequenceId");
        if(sequenceId != null && !"".equals(sequenceId)) {
            int sequence;
            try {
                sequence = parseInt(sequenceId);
                Logger.debug("Adding exercise to sequence " + sequence);
                Sequence currentSequence = Sequence.findSequenceById((long)sequence);
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
               
        Exercise exercise = Exercise.findExerciseById(exerciseId);
        
        if (exercise == null) {
            return badRequest(buildJsonResponse("error", "Exercise doesn't exist"));
        } else {
            Logger.debug("Editing exercise with id " + exerciseId);
            
            // topic
            long topic;
            try {
                topic = parseLong(editExerciseForm.get("topic"));
            } catch (NumberFormatException e) {
                topic = -1;
            }
            exercise.setTopic(topic);
            
            // level
            long level;
            try {
                level = parseLong(editExerciseForm.get("level"));
            } catch (NumberFormatException e) {
                level = -1;
            }
            exercise.setLevel(level);
            
            // question
            String question = editExerciseForm.get("question");
            exercise.getQuestion().setQuestionDescription(question);
           
        
            // stimulus, answer, and distractors for text
            if(editExerciseForm.get("type").equals("text")) {
                // right answer
                String answer = editExerciseForm.get("rightAnswer");
                exercise.getRightAnswer().setAnswerDescription(answer);
                
                List<Answer> answers = new ArrayList();
                answers.add(exercise.getRightAnswer());
                
                // distractors
                List<String> distractors = new ArrayList();
                editExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("answers"))).forEachOrdered((key) -> {
                    distractors.add(editExerciseForm.data().get(key));
                });
                distractors.forEach((s) -> {
                    Logger.debug("stuff;" + s);
                     answers.add(new Answer(s));
                });
                exercise.setAnswers(answers);
                
                // stimulus
                long stimulusId;
                try {
                    stimulusId = parseLong(editExerciseForm.get("stimulus"));
                } catch (NumberFormatException e) {
                    stimulusId = -1;
                }
                exercise.getQuestion().setStimulus(stimulusId);
                
            }
            // stimulus, answer and distractors for image
            else if(editExerciseForm.get("type").equals("image")) {
                
                String rightAnswerDescription;
                int answerResourceId;
                try {
                    rightAnswerDescription = editExerciseForm.get("rightAnswer");
                    answerResourceId = parseInt(editExerciseForm.get("rightAnswer"));
                } catch (NumberFormatException e) {
                    rightAnswerDescription = "";
                    answerResourceId = -1;
                }
                
                // distractors
                List<Long> distractorsResourcesIds = new ArrayList<>();
                List<String> distractorResourcesDecription = new ArrayList<>();
                Map<String, String> data = editExerciseForm.data();
                int numberDistractors = data.size();
                for(int i = 0; i < numberDistractors; i++){
                    String key = "answersImg[" + i + "]";
                    if(data.containsKey(key)){
                        int answerId;
                        String answerDescription;
                        try {
                            answerDescription = data.get(key);
                            answerId = parseInt(data.get(key));
                        } catch (NumberFormatException e) {
                            answerDescription = "";
                            answerId = -1;
                        }
                        distractorResourcesDecription.add(answerDescription);
                        distractorsResourcesIds.add((long)answerId);
                    }
                }
            
                // stimulus 
                String stimulusText = editExerciseForm.get("stimulusText");
                
                // store
                exercise.setRightAnswer(rightAnswerDescription, (long) answerResourceId);
                exercise.setAnswers(distractorResourcesDecription, distractorsResourcesIds);
                exercise.getQuestion().setStimulusText(stimulusText);

            }
            
            
            
            
            
            exercise.save();
            return ok(Json.toJson(exercise));        
        }
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
    public Result getResources() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        return ok(Json.toJson(Resource.findByOwner(loggedCaregiver)));
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
        
        Topic topic = new Topic(topicDesc, loggedCaregiver);
        topic.save();
        
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
        
        Level level = new Level(levelDesc, loggedCaregiver);
        level.save();
        
        return ok();
    }
    public Result uploadResources(String type) {
        Logger.debug("Uploading " + type);
        MultipartFormData<File> body = request().body().asMultipartFormData();
        Logger.debug("body -> " + body);
        List<FilePart<File>> resources = body.getFiles();

        // TODO: Ver pq nao funciona
        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                
                Logger.debug("Resource -> " + resource);
                
                if (resource != null) {
                    String fileName = resource.getFilename();
                    Logger.debug("Filename -> " + fileName);
                    
                    File file = resource.getFile();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
                    
                    String folderPath = "images/" + type +"/" + timestamp.getTime() + StringUtils.stripAccents(fileName);
                    String path = "public/" + folderPath;
                    File file2 = new File(path);
                    
                    
                    Logger.debug("Write?" + file2.canWrite());
                    Logger.debug("Execute?" + file2.canExecute());
                    Logger.debug("Read?" + file2.canRead());
                    
                    Boolean uploaded = file.renameTo(file2);
                    Logger.debug(path + " " + uploaded);
                    
                    if (uploaded) { 
                        ResourceArea resourceArea;
                        
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
                        
                        Logger.debug("Uploaded: " + uploaded);
                        
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
    public Result deleteExercise(long exerciseId) {
        Exercise exercise = Exercise.findExerciseById(exerciseId);

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
    
    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }

}