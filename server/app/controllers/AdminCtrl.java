package controllers;

import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import models.AnimatedCharacter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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

    public Result uploadAnimatedCharacter(String name) {
        Logger.debug("Uploading animated character");
        MultipartFormData<File> body = request().body().asMultipartFormData();
        List<FilePart<File>> resources = body.getFiles();
        
        String type = "animatedcharacter";
        String path = ConfigFactory.load().getString("pathUploadResources") + type + "/";
        Logger.debug(path);

        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                if (resource != null) {
                    String fileName = resource.getFilename();
                    
                    File file = resource.getFile();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    
                    String fileName2 = timestamp.getTime() + StringUtils.stripAccents(fileName);
              
                    //String path = "public" + File.separator + "images" + File.separator + type + File.separator; 
                    //String path = "../client/src/vithea-kids/assets/images/animatedcharacter/";
                    String folderPath = "images" + File.separator + type + File.separator + fileName2;
                    //String folderPath = "images/animatedcharacter/" + fileName2;

                    boolean uploaded = false;
                    try {
                        FileUtils.moveFile(file, new File(path, fileName2   ));
                        uploaded = true;
                    } catch (IOException ioe) {
                        System.out.println("Problem operating on filesystem");
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

}