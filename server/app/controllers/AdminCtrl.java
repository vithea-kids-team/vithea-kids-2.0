package controllers;

import java.io.File;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import models.AnimatedCharacter;
import org.apache.commons.lang3.StringUtils;

import play.*;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.*;

@Security.Authenticated(Secured.class)
public class AdminCtrl extends Controller {

    public Result getAnimatedCharacters() {
        return ok(Json.toJson(AnimatedCharacter.findAll()));
    }

    public Result uploadAnimatedCharacter(String name) {
        Logger.debug("Uploading animated character");
        MultipartFormData<File> body = request().body().asMultipartFormData();
        List<FilePart<File>> resources = body.getFiles();

        try {
            for(Iterator<FilePart<File>> i = resources.iterator(); i.hasNext(); ) {
                FilePart<File> resource = i.next();
                if (resource != null) {
                    String fileName = resource.getFilename();
                    File file = resource.getFile();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String folderPath = "images/animatedcharacter/" + timestamp.getTime() + StringUtils.stripAccents(fileName);
                    String path = "../client/src/" + folderPath;
                    
                    Boolean uploaded = file.renameTo(new File(path));
                    
                    Logger.debug(folderPath + " " + uploaded);
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