package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.AnimatedCharacter;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

@Security.Authenticated(Secured.class)
public class AdminCtrl extends Controller {

    public Result getAnimatedCharacters() {
        return ok(Json.toJson(AnimatedCharacter.findAll()));
    }
    public String getExtension(String filename){
        int i = filename.lastIndexOf('.');
        
        if (i > 0) return filename.substring(i+1);
        else return "png";
    }
    
    public Result uploadAnimatedCharacter(String name) {
        
        String type = "animatedcharacter";
        
        AdminResourcesCtrl adminres = new AdminResourcesCtrl();
        
        return adminres.uploadResources(type + "_" + name);   
    }
    public Result uploadCSVExercises(){
        
        Logger.debug("Uploading csv exercises");
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> resource = body.getFiles().get(0);
        Result result = null;
        
        if (resource != null) {
                    
            File file = resource.getFile();
            
            String line = "";
            String cvsSplitBy = "\\|";
            String level, topic, question, rightAnswer;
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                while ((line = br.readLine()) != null) {

                    String[] lineSplitted = line.split(cvsSplitBy);
                    topic = lineSplitted[0];
                    level = lineSplitted[1];
                    question = lineSplitted[2];
                    rightAnswer = lineSplitted[3];
                    System.out.print("Add Exercise:\t" + level + "\t" + topic + "\t" + question + "\t" + rightAnswer + "\t" );
                    List <String> distractors = new ArrayList<>();
                    
                    String dist = "";
                    for(int i = 4; i < lineSplitted.length; i++){
                        dist = lineSplitted[i];
                        if(dist.matches("\\s+")) continue;
                        distractors.add(dist);
                        System.out.print(dist + "\t");
                    }
                    System.out.println();

                    boolean validateTopic = this.validateTopic(topic);
                    boolean validateLevel = this.validateLevel(level);
                    boolean validateQuestion = this.validateQuestion(question);
                    boolean validaterightAnswer = this.validaterightAnswer(rightAnswer);
                    boolean validateDistractors = this.validateDistractors(distractors);

                    if(validateTopic && validateLevel && validateQuestion && validaterightAnswer && validateDistractors){
                        Result registerExerciseFromCSV = AdminExerciseCtrl.registerExerciseFromCSV(topic, level, question, rightAnswer, distractors);
                        result = registerExerciseFromCSV;
                    }
                    else result = badRequest("Tamanho(s) do(s) atributo(s) não se encontra(m) correcto(s)");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public Result uploadCSVTopics(){
        return ok(Json.toJson("not yet implemented"));
    }
    public Result uploadCSVLevels(){
        return ok(Json.toJson("not yet implemented"));
    }
    public boolean validateTopic(String topic){
        if(!topic.matches("\\s+") && topic.length() > 0 && topic.length() <= 50) return true;
        else return false;
    }
    public boolean validateLevel(String level) {
        if(!level.matches("\\s+") && level.length() > 0 && level.length() <= 50) return true;
        else return false;
    }
    public boolean validateQuestion(String question){
        if(!question.matches("\\s+") && question.length() > 0 && question.length() <= 100) return true;
        else return false;
    }
    public boolean validaterightAnswer(String rightAnswer){
        if(!rightAnswer.matches("\\s+") && rightAnswer.length() > 0 && rightAnswer.length() <= 75) return true;
        else return false;
    }
    public boolean validateDistractors(List<String> distractors){
        for(String dist: distractors){
            if(dist.length() > 75) return false;
        }
        return true;
    }
    
}