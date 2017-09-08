package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
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
import models.SequenceExercise;
import models.Topic;
import org.apache.commons.io.FileUtils;
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
            
            exercise = new Exercise(loggedCaregiver, topic, level, question, stimulusText, answerResourceId, distractorsResourcesIds);
        }
        
        exercise.save();

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
                exercise.resetAnswers();
                
                String rightAnswerDescription = "";
                int answerResourceId;
                try {
                    answerResourceId = parseInt(editExerciseForm.get("rightAnswerImg"));
                } catch (NumberFormatException e) {
                    answerResourceId = -1;
                }
                exercise.setRightAnswer(rightAnswerDescription, (long) answerResourceId);

                
                // distractors
                List<Long> distractorsResourcesIds = new ArrayList<>();
                List<String> distractorResourcesDecription = new ArrayList<>();
                Map<String, String> data = editExerciseForm.data();
                int numberDistractors = data.size();
                for(int i = 0; i < numberDistractors; i++){
                    String key = "answersImg[" + i + "]";
                    if(data.containsKey(key)){
                        int answerId;
                        String answerDescription = "";
                        try {
                            answerId = parseInt(data.get(key));
                            Logger.debug("STUFF: " + answerId);
                        } catch (NumberFormatException e) {
                            answerId = -1;
                        }
                        distractorResourcesDecription.add(answerDescription);
                        distractorsResourcesIds.add((long)answerId);
                    }
                }
                exercise.setAnswers(distractorResourcesDecription, distractorsResourcesIds);

            
                // stimulus 
                String stimulusText = editExerciseForm.get("stimulusText");
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
        
        List<Answer> answers = exercise.getAnswers();
        List<Answer> iterable = new ArrayList(answers);
        
        iterable.forEach((Answer ans) -> {
            answers.remove(ans);
            exercise.save();
            ans.delete();
        });
        
        
        List<SequenceExercise> sequenceExercise = exercise.getSequenceExercise();
        List<SequenceExercise> iterable2 = new ArrayList(sequenceExercise);
        
        iterable2.forEach((SequenceExercise seqex) -> {
            if (seqex.getExercise().getExerciseId() == exercise.getExerciseId())
                sequenceExercise.remove(seqex);
                seqex.delete();
        });
                
        Sequence.getAll().forEach((seq) -> {
            List<SequenceExercise> sequenceExerciseSeq = seq.getSequenceExercisesList();
            List<SequenceExercise> iterable3 = new ArrayList(sequenceExerciseSeq);
            
            iterable3.forEach((SequenceExercise seqex) -> {
            if (seqex.getExercise().getExerciseId() == exercise.getExerciseId())
                sequenceExerciseSeq.remove(seqex);
                seqex.delete();
            });
            seq.save();
        });
        
        Logger.debug("Deleting " + loggedCaregiver.getCaregiverLogin().getUsername() + "'s' exercise. ");

        exercise.delete();

        return ok(buildJsonResponse("success", "Exercise deleted successfully"));
    }
 
    
   
    public Result uploadResources(String type) {
        Logger.debug("Uploading " + type);
        MultipartFormData<File> body = request().body().asMultipartFormData();
        Logger.debug("body -> " + body);
        List<FilePart<File>> resources = body.getFiles();

        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                
                 if (resource != null) {
                    String fileName = resource.getFilename();
                    
                    File file = resource.getFile();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
                    
                    //Thumbnails.of(new File("original.jpg"))
                    //.size(160, 160)
                    //.toFile(new File("thumbnail.jpg"));
                    
                    
                    
                    String fileName2 = timestamp.getTime() + StringUtils.stripAccents(fileName);
                    //String path = ".." + File.separator + "client" + File.separator + "src" + File.separator + "vithea-kids" + File.separator + "assets" + File.separator + "images" + File.separator;
                    String path = File.separator + "public" + File.separator + "images" + File.separator + type + File.separator; 
                    
                    String folderPath = "images" + File.separator + type + File.separator + fileName2;
                    
                    boolean uploaded = false;
                    try {
                        FileUtils.moveFile(file, new File(path, fileName2));
                        uploaded = true;
                    } catch (IOException ioe) {
                        System.out.println("Problem operating on filesystem" + ioe);
                        uploaded = false;
                    }
                    
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
    public Result getResources() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        return ok(Json.toJson(Resource.findByOwner(loggedCaregiver)));
    } 
    public Result removeResource(long resourceId) {
         Resource resource = Resource.findById(resourceId);

        if (resource == null) {
            return badRequest(buildJsonResponse("error", "Resource doesn't exist"));
        }

        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        /*Sequence.getAll().forEach((seq) -> {
            
            seq.getSequenceExercises().remove(exercise);
            seq.save();
        });*/
        
        Logger.debug("Deleting " + loggedCaregiver.getCaregiverLogin().getUsername() + "'s' resource. ");

        resource.delete();

        return ok(buildJsonResponse("success", "Resource deleted successfully"));
     }
    
    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }

}