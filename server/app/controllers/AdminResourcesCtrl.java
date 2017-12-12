package controllers;

import com.typesafe.config.ConfigFactory;
import static controllers.ChildAppCtrl.buildJsonResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import models.AnimatedCharacter;
import models.Caregiver;
import models.Resource;
import models.ResourceArea;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import play.*;
import play.libs.Json;
import play.mvc.*;
import static play.mvc.Controller.request;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

@Security.Authenticated(Secured.class)
public class AdminResourcesCtrl extends Controller {

    public AdminLogs adminLogs = new AdminLogs();
    
    public String getExtension(String filename){
        int i = filename.lastIndexOf('.');
        
        String extension;
        
        if (i > 0) extension = filename.substring(i+1);
        else extension = "error";
        
        if(extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || 
           extension.equals("jpg")  || extension.equals("bmp") || extension.equals("jpe")) {
            return extension;
        }
        else return "error";
    }
    
    public Result uploadResources(String type) {
        
        Logger.debug("Uploading " + type);
        MultipartFormData<File> body = request().body().asMultipartFormData();
        Logger.debug("body -> " + body);
        List<FilePart<File>> resources = body.getFiles();

        String path = ConfigFactory.load().getString("vitheaImages");
        Logger.debug(path);
        
        Boolean DEVELOPMENT = Boolean.parseBoolean(ConfigFactory.load().getString("development"));
        
        try {
            for (FilePart<File> resource : resources) {
                if (resource != null) {
                    
                    // Resize
                    File file = resource.getFile();
                    BufferedImage originalImage = ImageIO.read(file);
                    
                    String fileName = resource.getFilename();
                    String extension = this.getExtension(fileName);
                    
                    // validate extension
                    if (extension.equals("error")) return badRequest("Invalid image format");
                    
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
                    String folderPath = "";
                    
                    boolean uploaded = false;
                    
                    try {
                        if (DEVELOPMENT) folderPath = createImageDevelopment(fileName2, extension, file, new_width, new_height);
                        else folderPath = createImageProduction(fileName2, path, file);
                        uploaded = true;
                    } catch (IOException ioe) {
                        System.out.println("Problem operating on filesystem" + ioe.getMessage());
                        uploaded = false;
                    }
                    
                    // add animated character
                    if (uploaded) { 
                         Logger.debug("Uploaded: " + uploaded);
                        
                        if (type.contains("animatedcharacter")){
                            CharSequence name = type.subSequence(18, type.length());
                            AnimatedCharacter newChar = new AnimatedCharacter(folderPath, (String) name);
                            newChar.save();
                        
                            return ok(Json.toJson(newChar));
                        }
                        else{
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
                            Resource res = new Resource(loggedCaregiver, folderPath, resourceArea, false);
                            res.save();

                            String content = res.getResourceId() + "," + loggedCaregiver.getCaregiverId() + "," + "," + timestamp.toLocalDateTime() + "," + 
                                    type + "," + "create" + "," + "false" + "\n";
                            String pathResource = loggedCaregiver.getPathResourcesLog();
                            adminLogs.writeToFile(pathResource, content);
                        
                            return  ok(Json.toJson(res));
                        }  
                    }
                    return badRequest("Not possible to upload");
                }
            }            
        } catch (Exception e) {
            return badRequest(e.getLocalizedMessage());
        }
        return null;
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
            String resourcePath = resource.getResourcePath();
            System.out.println(resourcePath);
            
            Boolean DEVELOPMENT = Boolean.parseBoolean(ConfigFactory.load().getString("development"));
            String pathVitheaImages = ConfigFactory.load().getString("vitheaImages");
            String path;
            
            if(DEVELOPMENT) path = "public/" + resource.getResourcePath();             
            else path = resourcePath.replace("vkimg", pathVitheaImages);
            
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            return badRequest(buildJsonResponse("error", "Problem deleting resource " + resource.getResourcePath()));
        }
      
        resource.delete();
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = resource.getResourceId() + "," + loggedCaregiver.getCaregiverId() + "," + "," + timestamp.toLocalDateTime() + "," + 
                "," + "delete" + "," + false + "\n";
        String pathResource = loggedCaregiver.getPathResourcesLog();
        adminLogs.writeToFile(pathResource, content);
        
        return ok(buildJsonResponse("success", "Resource deleted successfully"));
     }

    private String createImageDevelopment(String fileName2, String extension, File file, int new_width, int new_height) throws IOException {
        
        String pathClient = ".." + File.separator + "client" + File.separator + "src" + File.separator + "vithea-kids" +
                File.separator + "assets" + File.separator + "images";          // path to the client
        Logger.debug(pathClient);
        File fileDestinationClient = new File(pathClient, fileName2);
        if(!extension.equals("gif")) Thumbnails.of(file.getAbsolutePath()).size(new_width, new_height).imageType(BufferedImage.TYPE_INT_ARGB).toFile(fileDestinationClient);
        else FileUtils.copyFile(file, fileDestinationClient);
        
        String pathServer = "public" + File.separator + "images";                // path to the server
        File fileDestinationServer = new File(pathServer, fileName2);
        if(!extension.equals("gif")) Thumbnails.of(file.getAbsolutePath()).size(new_width, new_height).imageType(BufferedImage.TYPE_INT_ARGB).toFile(fileDestinationServer);
        else FileUtils.copyFile(file, fileDestinationServer);
        
        return "images/" + fileName2;
    }
    private String createImageProduction(String fileName2, String path, File file) throws IOException {
        
        Logger.debug("Creating " + fileName2 + " on " + path);
        File file2 = new File(path, fileName2);
        FileUtils.moveFile(file, file2);
        setPermission(path + "/" + fileName2);
        
        return "vkimg/" + fileName2;
    }
    public void setPermission(String path) throws IOException{
        Set<PosixFilePermission> perms = new HashSet<>();
        
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OTHERS_READ); 
        perms.add(PosixFilePermission.GROUP_READ);

        Files.setPosixFilePermissions(Paths.get(path), perms);
    }

    public Result getResources() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        return ok(Json.toJson(Resource.findByOwner(loggedCaregiver)));
    }

}