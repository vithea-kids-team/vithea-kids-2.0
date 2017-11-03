package controllers;

import com.typesafe.config.ConfigFactory;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import models.AnimatedCharacter;
import models.Caregiver;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import play.*;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import static play.mvc.Results.badRequest;

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
        Logger.debug("Uploading animated character");
        MultipartFormData<File> body = request().body().asMultipartFormData();
        List<FilePart<File>> resources = body.getFiles();
        
        String type = "animatedcharacter";
        
        String path = ConfigFactory.load().getString("vitheaRoot") + "/public/vithea-kids/assets/images/" + type + "/";
        Logger.debug(path);
        
        Boolean DEVELOPMENT = Boolean.parseBoolean(ConfigFactory.load().getString("development"));

        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                if (resource != null) {
                    
                     // Resize
                    File file = resource.getFile();
                    BufferedImage originalImage = ImageIO.read(file);
                    
                    String fileName = resource.getFilename();
                    String extension = this.getExtension(fileName);
                    
                    int height = originalImage.getHeight();
                    int width  = originalImage.getWidth();
                    
                    int new_height = 0;
                    int new_width  = 0;
                    
                    if(width > 640 || height > 480){
                        double ratio = 0;
                        
                        System.out.println(width + ", " + height);
                        
                        if(width >= 640) {
                            new_width = 640;
                            ratio = (new_width * 1.0)/width;
                            new_height = (int) (height * ratio);
                        }
                        
                        if(height >= 480) {
                            new_height = 480;
                            ratio = (new_height * 1.0)/height;
                            new_width = (int) (width * ratio);
                        }
                    }
                    else {
                        new_width = width;
                        new_height = height;
                    }                   
                    
                    // Change path and names                    
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
                            if(!extension.equals("gif")) Thumbnails.of(file.getAbsolutePath()).size(new_width, new_height).imageType(BufferedImage.TYPE_INT_ARGB).toFile(fileDestinationClient);
                            else FileUtils.copyFile(file, fileDestinationClient);

                            
                            String pathServer = "public" + File.separator + "images" + File.separator + type;                // path to the server                   
                            File fileDestinationServer = new File(pathServer, fileName2);
                            if(!extension.equals("gif")) Thumbnails.of(file.getAbsolutePath()).size(new_width, new_height).imageType(BufferedImage.TYPE_INT_ARGB).toFile(fileDestinationServer);
                            else FileUtils.copyFile(file, fileDestinationServer);
                            
                        }
                        else {
                            //Thumbnails.of(file.getAbsolutePath()).size(new_width, new_height).imageType(BufferedImage.TYPE_INT_ARGB).toFile(fileDestinationServer);
                            FileUtils.moveFile(file, new File(path, fileName2));
                        }
                        uploaded = true;
                    } catch (IOException ioe) {
                        System.out.println("Problem operating on filesystem" + ioe.getMessage());
                        uploaded = false;
                    }
                    
                    if (uploaded) {
                        Logger.debug("name: " + name);
                        AnimatedCharacter newChar = new AnimatedCharacter(folderPath, name);
                        newChar.save();
                        
                        return ok(Json.toJson(newChar));
                    }
                    return badRequest("Not possible to upload");
                }    
            }            
        } catch (Exception e) {
            return badRequest(e.getLocalizedMessage());
        }
        return null;
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