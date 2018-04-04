/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.ModelOperations;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.AdminLogs;
import controllers.SecurityController;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import models.Answer;
import models.Caregiver;
import models.Exercise;
import models.Level;
import models.MultipleChoice;
import models.Question;
import models.Sequence;
import models.SequenceExercise;
import models.Topic;
import play.Logger;
import play.data.DynamicForm;
import scala.collection.concurrent.Debug;

/**
 *
 * @author silvi
 */
public class McOperations implements ExerciseOperations {
    public AdminLogs adminLogs = new AdminLogs();
    
    @Override
    public Exercise createExercise(DynamicForm registerExerciseForm, JsonNode json) {  
        int answers = 0;
        boolean stimulus = false;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
       
        System.out.println("createExercise" + registerExerciseForm);
        
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
            stimulus = true;
        } catch (NumberFormatException e) {
            stimulusId = -1;
            stimulus = false;
        }
        
        String question = registerExerciseForm.get("question");
        
        if(registerExerciseForm.get("type").equals("text")) {
                        
            List<String> rightAnswers = new ArrayList<String>();
            List<String> distractors = new ArrayList<String>();
            
            registerExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("rightAnswers"))).forEachOrdered((key) -> {
                rightAnswers.add(registerExerciseForm.data().get(key));
            });
            registerExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("distractors"))).forEachOrdered((key) -> {
                distractors.add(registerExerciseForm.data().get(key));
            });
          
            exercise = new MultipleChoice(loggedCaregiver, topic, level, question, stimulusId, rightAnswers, distractors, false);
            exercise.save();
            
            String content = stimulusId + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
            timestamp.toLocalDateTime() + "," + "Stimuli" + "," + "addToExercise" + "," + "false" + "\n";
            String pathResources = loggedCaregiver.getPathResourcesLog();
            adminLogs.writeToFile(pathResources, content);
           
        } else if(registerExerciseForm.get("type").equals("image")) {
            
            String stimulusText = registerExerciseForm.get("stimulusText");
            if(stimulusText != null) stimulus = true;
            else stimulus = false;
            
            List<Long> rightAnswers = new ArrayList<Long>();
            List<Long> distractors = new ArrayList<Long>();
            
            registerExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("rightAnswers"))).forEachOrdered((key) -> {
                rightAnswers.add(Long.parseLong(registerExerciseForm.data().get(key)));
            });
            registerExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("distractors"))).forEachOrdered((key) -> {
                distractors.add(Long.parseLong(registerExerciseForm.data().get(key)));
            });
            
            /*
       
            int answerResourceId;
            String sresourcesid = "";
            
            try {
                answerResourceId = parseInt(registerExerciseForm.get("rightAnswerImg"));
                sresourcesid += answerResourceId + " ";
                answers++;
            } catch (NumberFormatException e) {
                answerResourceId = -1;
            }
            
            
            
            List<Long> rigthAnswersResourcesIds = new ArrayList<>();
            Map<String, String> data = registerExerciseForm.data();
            int numberRightAnswers = data.size();
            for(int i = 0; i < numberRightAnswers; i++){
                String key = "rightAnswers[" + i + "]";
                if(data.containsKey(key)){
                    int answerId;
                    try {
                        answerId = parseInt(data.get(key));
                        sresourcesid += answerId + " ";
                    } catch (NumberFormatException e) {
                        answerId = -1;
                    }
                    rigthAnswersResourcesIds.add((long)answerId);
                }
            }
            
            List<Long> distractorsResourcesIds = new ArrayList<>();
            Map<String, String> data2 = registerExerciseForm.data();
            int numberDistractors = data2.size();
            for(int i = 0; i < numberDistractors; i++){
                String key = "distractors[" + i + "]";
                if(data.containsKey(key)){
                    int answerId;
                    try {
                        answerId = parseInt(data.get(key));
                        sresourcesid += answerId + " ";
                    } catch (NumberFormatException e) {
                        answerId = -1;
                    }
                    distractorsResourcesIds.add((long)answerId);
                }
            }
            */
            
            exercise = new MultipleChoice(loggedCaregiver, topic, level, question, stimulusText, rightAnswers, distractors, false);
            exercise.save();
            
            /*String content = answerResourceId + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "Answers" + "," + "addToExercise" + ","  + "," + "false" + "\n";
            String pathResources = loggedCaregiver.getPathResourcesLog();
            adminLogs.writeToFile(pathResources, content);
            
            Object[] toArray = distractorsResourcesIds.toArray();
            for(int i = 0; i < toArray.length; i++){
                content = toArray[i] + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "Answers" + "," + "addToExercise" + "," + "," + "false" + "\n";
                pathResources = loggedCaregiver.getPathResourcesLog();
                adminLogs.writeToFile(pathResources, content);
            }*/
        }
        
        exercise.save();
        
        String content = exercise.getExerciseId()+ "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + ","  +
                registerExerciseForm.get("type") + "," + "create" + "," + answers + "," + stimulus + "," + "false" + "\n";
        String pathExercise = loggedCaregiver.getPathExercisesLog();
        adminLogs.writeToFile(pathExercise, content);
        
        String content2 = level + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "addToExercise" + ","  + "false" + "\n";
        String pathLevel = loggedCaregiver.getPathLevelsLog();
        adminLogs.writeToFile(pathLevel, content2);
        
        String content3 = topic + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "addToExercise" + "," + "false" + "\n";
        String pathTopics = loggedCaregiver.getPathTopicsLog();
        adminLogs.writeToFile(pathTopics, content3);   
       
        return exercise;
     }

    @Override
    public void deleteExercise(long exerciseId, Caregiver loggedCaregiver, MultipleChoice exercise) {
        
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

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = exercise.getExerciseId()+ "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + "," + "delete\n";
        String pathExercise = loggedCaregiver.getPathExercisesLog();
        adminLogs.writeToFile(pathExercise, content);
        
        exercise.delete();
        
        
    }
    

    @Override
    public Exercise editExercise(DynamicForm editExerciseForm, long exerciseId, Caregiver loggedCaregiver) {
        Debug.log(editExerciseForm);
        String sresourcesid = "";
        MultipleChoice exercise = (MultipleChoice) Exercise.findExerciseById(exerciseId); //getting exercise
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        Logger.debug("Editing exercise with id " + exerciseId);

        // topic
        long topic;
        try {
            topic = parseLong(editExerciseForm.get("topic"));
            Topic topicActual = exercise.getTopic();
            if(topic != topicActual.getTopicId()){
                String content = topicActual.getTopicId()+ "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "removeFromExercise" + ","  +  "," + "false" + "\n";
                String pathTopic = loggedCaregiver.getPathTopicsLog();
                adminLogs.writeToFile(pathTopic, content);
            }
        } catch (NumberFormatException e) {
            topic = -1;
        }
        exercise.setTopic(topic);
        String content = topic + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "addToExercise" + ","  +  exercise.getTopic().getTopicDescription() + "," + "false" + "\n";
        String pathTopic = loggedCaregiver.getPathTopicsLog();
        adminLogs.writeToFile(pathTopic, content);

        // level
        long level;
        try {
            level = parseLong(editExerciseForm.get("level"));
            Level levelActual = exercise.getLevel();
            if(level != levelActual.getLevelId()){
                content = levelActual.getLevelId() + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "removeFromExercise" + ","  +  "," + "false" + "\n";
                String pathLevel = loggedCaregiver.getPathLevelsLog();
                adminLogs.writeToFile(pathLevel, content);
            }
        } catch (NumberFormatException e) {
            level = -1;
        }

        exercise.setLevel(level);
        content = level + "," + loggedCaregiver.getCaregiverId() + "," + exercise.getExerciseId() + "," + 
                timestamp.toLocalDateTime() + "," + "addToExercise" + ","  +  exercise.getLevel().getLevelDescription() + "," + "false" + "\n";
        String pathLevel = loggedCaregiver.getPathLevelsLog();
        adminLogs.writeToFile(pathLevel, content);

        // question
        String question = editExerciseForm.get("question");
        exercise.getQuestion().setQuestionDescription(question);
        exercise.setExerciseName(question);

        int answerssize = 0; 
        boolean stimulus = false;


        // stimulus, answer, and distractors for text
        if(editExerciseForm.get("type").equals("text")) {

            // right answer
            //String rightAnswer = editExerciseForm.get("rightAnswer");
            //exercise.getRightAnswer().setAnswerDescription(rightAnswer);
            //answers.add(exercise.getRightAnswer());
            List<String> rightAnswers = new ArrayList<String>();
            List<String> distractors = new ArrayList<String>();
            
            editExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("rightAnswers"))).forEachOrdered((key) -> {
                rightAnswers.add(editExerciseForm.data().get(key));
            });
            editExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("distractors"))).forEachOrdered((key) -> {
                distractors.add(editExerciseForm.data().get(key));
            });
            
            exercise.setAnswersText(rightAnswers, distractors);
            

            // stimulus
            int stimulusId;
            try {
                stimulusId = parseInt(editExerciseForm.get("stimulus"));
                exercise.getQuestion().setStimulus((long)stimulusId);
                stimulus = true;
            } catch (NumberFormatException e) {
                stimulusId = -1;
                exercise.removeQuestion();
                exercise.setQuestion(new Question(question));
                exercise.setExerciseName(question);

                stimulus = false;
            }
            System.out.println("image stimulus:" + stimulusId);
        }
        // stimulus, answer and distractors for image
        else if(editExerciseForm.get("type").equals("image")) {
            
            List<Long> rightAnswers = new ArrayList<Long>();
            List<Long> distractors = new ArrayList<Long>();
            
            //right answers
            editExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("rightAnswers"))).forEachOrdered((key) -> {
                rightAnswers.add(Long.parseLong(editExerciseForm.data().get(key)));
            });
            
           //distractors
            editExerciseForm.data().keySet().stream().filter((key) -> (key.startsWith("distractors"))).forEachOrdered((key) -> {
                distractors.add(Long.parseLong(editExerciseForm.data().get(key)));
            });

            exercise.setAnswersImg(rightAnswers, distractors);

            // stimulus
            String stimulusText = editExerciseForm.get("stimulusText");
            exercise.getQuestion().setStimulusText(stimulusText);
            if(stimulusText != null) {
                if(stimulusText.isEmpty()) stimulus = false;
                else stimulus = true;
            }

        }
        exercise.save();

        content = exercise.getExerciseId()+ "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + ","  +
            editExerciseForm.get("type") + "," + "edit" + "," + answerssize + "," + stimulus + "," + "false" + "\n";
        String pathExercise = loggedCaregiver.getPathExercisesLog();
        adminLogs.writeToFile(pathExercise, content);

        return exercise;
    }
    
}
