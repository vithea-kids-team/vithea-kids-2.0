package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import javax.imageio.ImageIO;
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
import net.coobird.thumbnailator.Thumbnails;
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
            Topic newTopic = new Topic(topic, loggedCaregiver);
            newTopic.save();
            topicId = newTopic.getTopicId();
        } else {
            topicId = topicExists.getTopicId();
        }
        
        // create level (or find id by description if exists)
        Long levelId;
        Level levelExists = Level.findLevelByDescription(level);
        
        if (levelExists == null) {
            Level newLevel = new Level(level, loggedCaregiver);
            newLevel.save();
            levelId = newLevel.getLevelId();
        } else {
            levelId = levelExists.getLevelId();
        }
        
        exercise = new Exercise(loggedCaregiver, topicId, levelId, question, -1, rightAnswer, distractors);
        
        exercise.save();
        
        return ok(Json.toJson(exercise));
    }
    
    
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
           
            List<Answer> answers = new ArrayList();
            List<Answer> existingAnswers = exercise.getAnswers();
            existingAnswers.remove(0);  // remove the right answer
            
            // stimulus, answer, and distractors for text
            if(editExerciseForm.get("type").equals("text")) {
                
                // right answer
                String rightAnswer = editExerciseForm.get("rightAnswer");
                exercise.getRightAnswer().setAnswerDescription(rightAnswer);
                answers.add(exercise.getRightAnswer());
                
                // distractors
                List<String> distractors = new ArrayList();
                editExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("answers"))).forEachOrdered((key) -> {
                    distractors.add(editExerciseForm.data().get(key));
                });
                
                // redo
                if(distractors.size() == 0) {   // exercise without distractors
                    int sizeExistingAnswers = existingAnswers.size()-1;
                    
                    for(int i = sizeExistingAnswers; i >= sizeExistingAnswers; i--){
                        Answer toRemoveAns = existingAnswers.get(i);
                        exercise.removeAnswer(toRemoveAns);
                        exercise.save();
                        toRemoveAns.delete();
                    }
                }
                else if(distractors.size() <= existingAnswers.size()-1) {     // less or actual distractors than existing ones
                    
                    List<Answer> toChange = existingAnswers.subList(0, distractors.size());
                    List<Answer> toRemove = existingAnswers.subList(distractors.size(), existingAnswers.size());
                     
                    for(int i = 0; i < toChange.size(); i++){
                        Answer ans = toChange.get(i);
                        ans.setAnswerDescription(distractors.get(i));
                        answers.add(ans);
                    }
                    
                    int sizeToRemove = toRemove.size()-1;
                    for(int i = sizeToRemove; i >= sizeToRemove; i--){
                        Answer toRemoveAns = toRemove.get(i);
                        exercise.removeAnswer(toRemoveAns);
                        exercise.save();
                        toRemoveAns.delete();
                    }
                    
                }
                else if(distractors.size() > existingAnswers.size()-1) {    // greater actual distractors than existing ones
                     
                    List<String> toAdd = distractors.subList(existingAnswers.size(), distractors.size());
                    
                    for(int i = 0; i < existingAnswers.size(); i++){
                        Answer ans = existingAnswers.get(i);
                        ans.setAnswerDescription(distractors.get(i));
                        answers.add(ans);
                    }
                    
                    for(int i = 0; i < toAdd.size(); i++){
                        Answer newAns = new Answer(toAdd.get(i));
                        answers.add(newAns);
                    }
                }
                
                // stimulus
                long stimulusId;
                try {
                    stimulusId = parseLong(editExerciseForm.get("stimulus"));
                    exercise.getQuestion().setStimulus(stimulusId);
                } catch (NumberFormatException e) {
                    stimulusId = -1;
                }
                
                exercise.setAnswers(answers);
            }
            // stimulus, answer and distractors for image
            else if(editExerciseForm.get("type").equals("image")) {
                
                // right answer
                String rightAnswerDescription = "";
                int answerResourceId;
                try {
                    answerResourceId = parseInt(editExerciseForm.get("rightAnswerImg"));
                    Answer rightAnswer = exercise.getRightAnswer();
                    rightAnswer.setStimulus((long) answerResourceId);
                    rightAnswer.save();
                    answers.add(rightAnswer);
                } catch (NumberFormatException e) {
                    answerResourceId = -1;
                }
                
                // distractors                
                List<Long> distractorsResourcesIds = new ArrayList<>();
                
                Map<String, String> data = editExerciseForm.data();
                int numberDistractors = data.size();
                
                for(int i = 0; i < numberDistractors; i++){
                    String key = "answersImg[" + i + "]";
                    if(data.containsKey(key)){
                        try {
                            answerResourceId = parseInt(data.get(key));
                        } catch (NumberFormatException e) {
                            answerResourceId = -1;
                        }
                        distractorsResourcesIds.add((long)answerResourceId);
                    }
                }
                
                if(distractorsResourcesIds.size() == 0) {   // exercise without distractors
                    int sizeExistingAnswers = existingAnswers.size()-1;
                    
                    for(int i = sizeExistingAnswers; i >= sizeExistingAnswers; i--){
                        Answer toRemoveAns = existingAnswers.get(i);
                        exercise.removeAnswer(toRemoveAns);
                        exercise.save();
                        toRemoveAns.delete();
                    }
                }
                else if(distractorsResourcesIds.size() <= existingAnswers.size()-1) {     // less or actual distractors than existing ones
                    
                    List<Answer> toChange = existingAnswers.subList(0, distractorsResourcesIds.size());
                    List<Answer> toRemove = existingAnswers.subList(distractorsResourcesIds.size(), existingAnswers.size());
                     
                    for(int i = 0; i < toChange.size(); i++){
                        Answer ans = toChange.get(i);
                        ans.setAnswerDescription("");
                        ans.setStimulus(distractorsResourcesIds.get(i));
                        answers.add(ans);
                    }
                    
                    int sizeToRemove = toRemove.size()-1;
                    for(int i = sizeToRemove; i >= sizeToRemove; i--){
                        Answer toRemoveAns = toRemove.get(i);
                        exercise.removeAnswer(toRemoveAns);
                        exercise.save();
                        toRemoveAns.delete();
                    }
                }
                else if(distractorsResourcesIds.size() > existingAnswers.size()-1) {    // greater actual distractors than existing ones
                     
                    List<Long> toAdd = distractorsResourcesIds.subList(existingAnswers.size(), distractorsResourcesIds.size());
                    
                    for(int i = 0; i < existingAnswers.size(); i++){
                        Answer ans = existingAnswers.get(i);
                        ans.setAnswerDescription("");
                        ans.setStimulus(distractorsResourcesIds.get(i));
                        answers.add(ans);
                    }
                    
                    for(int i = 0; i < toAdd.size(); i++){
                        Answer newAns = new Answer("");
                        newAns.setStimulus(toAdd.get(i));
                        answers.add(newAns);
                    }
                }
                
                // stimulus 
                String stimulusText = editExerciseForm.get("stimulusText");
                exercise.getQuestion().setStimulusText(stimulusText);
                
                exercise.setAnswers(answers);
            }
               
        }
            
        exercise.save();
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
            if (seqex.getExercise().getExerciseId() == exercise.getExerciseId()) {
                sequenceExercise.remove(seqex);
                seqex.delete();
            }
        });
                
        Sequence.getAll().forEach((seq) -> {
            List<SequenceExercise> sequenceExerciseSeq = seq.getSequenceExercisesList();
            List<SequenceExercise> iterable3 = new ArrayList(sequenceExerciseSeq);
            
            iterable3.forEach((SequenceExercise seqex) -> {
                if (seqex.getExercise().getExerciseId() == exercise.getExerciseId()) {
                    sequenceExerciseSeq.remove(seqex);
                    seqex.delete();
                }
            });
            seq.save();
        });
        
        Logger.debug("Deleting " + loggedCaregiver.getCaregiverLogin().getUsername() + "'s' exercise. ");

        exercise.delete();

        return ok(buildJsonResponse("success", "Exercise deleted successfully"));
    }
    
    public String getExtension(String filename){
        int i = filename.lastIndexOf('.');
        
        if (i > 0) return filename.substring(i+1);
        else return "png";
    }
   
    public Result uploadResources(String type) {
        Logger.debug("Uploading " + type);
        MultipartFormData<File> body = request().body().asMultipartFormData();
        Logger.debug("body -> " + body);
        List<FilePart<File>> resources = body.getFiles();

        String path = ConfigFactory.load().getString("pathUploadResources") + type + File.separator;
        Logger.debug(path);
        
        Boolean DEVELOPMENT = true;
        
        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                
                 if (resource != null) {
                     
                    // Resize
                    File file = resource.getFile();
                    BufferedImage originalImage = ImageIO.read(file);
                    BufferedImage thumbnail;
                    int height = originalImage.getHeight();
                    int width  = originalImage.getWidth();
                    
                    
                    if(width > 640 || height > 480){
                        int new_height = 0;
                        int new_width  = 0;
                        double ratio = 0;
                        
                        System.out.println(width + ", " + height);
                        
                        if(width >= 640) {
                            new_width = 640;
                            ratio = (new_width * 1.0)/width;
                            new_height = (int) (height * ratio);
                            System.out.println(new_width + ", " + new_height + ", " + ratio);
                        }
                        
                        if(height >= 480) {
                            new_height = 480;
                            ratio = (new_height * 1.0)/height;
                            new_width = (int) (width * ratio);
                            System.out.println(new_width + ", " + new_height + ", " + ratio);
                        }
                        
                        thumbnail = Thumbnails.of(originalImage).size(new_width, new_height).asBufferedImage();
                    }
                    else thumbnail = originalImage;
                    
                    // Change path and names
                    String fileName = resource.getFilename();
                    String extension = this.getExtension(fileName);
                    
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
                    
                    String fileName2 = timestamp.getTime() + StringUtils.stripAccents(fileName); 
                    String folderPath = "images/" + type + "/" + fileName2;
                    
                    boolean uploaded = false;
                    
                    try {
                        if (DEVELOPMENT) {
                            String pathClient = ".." + File.separator + "client" + File.separator + "src" + File.separator + "vithea-kids" +                             
                                    File.separator + "assets" + File.separator + "images" + File.separator + type;          // path to the client
                            File fileDestinationClient = new File(pathClient, fileName2);
                            ImageIO.write(thumbnail, extension, fileDestinationClient);
                            //FileUtils.copyFile(file, fileDestinationClient);
                            
                            String pathServer = "public" + File.separator + "images" + File.separator + type;                // path to the server                   
                            File fileDestinationServer = new File(pathServer, fileName2);
                            ImageIO.write(thumbnail, extension, fileDestinationServer);                            
                            //FileUtils.copyFile(file, fileDestinationServer);
                            
                        }
                        else {
                            FileUtils.moveFile(file, new File(path, fileName2));
                        }
                        uploaded = true;
                    } catch (IOException ioe) {
                        System.out.println("Problem operating on filesystem" + ioe.getMessage());
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
        
        Logger.debug("Deleting " + loggedCaregiver.getCaregiverLogin().getUsername() + "'s' resource. ");

        try {
            System.out.println(resource.getResourcePath());
            
            Path path = Paths.get("public/" + resource.getResourcePath());
            Files.delete(path);
        } catch (IOException e) {
            return badRequest(buildJsonResponse("error", "Problem deleting resource " + resource.getResourcePath()));
        }
        
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