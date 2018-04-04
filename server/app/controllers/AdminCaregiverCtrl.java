package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.inject.Inject;
import models.Caregiver;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import play.mvc.Security;


@Security.Authenticated(Secured.class)
public class AdminCaregiverCtrl extends Controller {

    @Inject
    FormFactory formFactory;            
   
  
    public Result getCaregiver(String username) {
        Logger.debug("Hit AdminCaregiverCtrl.getCaregiver method");
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        else return ok(Json.toJson(loggedCaregiver));
    }
    
    private static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
    
    public Result editCaregiver(String username) {
        Logger.debug("Hit AdminCaregiverCtrl.editCaregiver method");
        
        DynamicForm editCaregiverForm = formFactory.form().bindFromRequest();

        Logger.debug("DEBUG:" + editCaregiverForm);

        if (editCaregiverForm.hasErrors()) {
            return badRequest(editCaregiverForm.errorsAsJson());
        }
        Caregiver caregiver = Caregiver.findByUsername(username);
        
        if (caregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver doesn't exist"));
        } else {
            Logger.debug("Editing caregiver: " + username);

            caregiver.setFirstName(editCaregiverForm.get("firstName"));
            caregiver.setLastName(editCaregiverForm.get("lastName"));
            caregiver.setEmail(editCaregiverForm.get("email"));
            caregiver.setGender(editCaregiverForm.get("gender"));
            
            caregiver.save();
            
            /*child.getChildLogin().setUsername(editChildForm.get("username"));
            child.setFirstName(editChildForm.get("firstName"));
            child.setLastName(editChildForm.get("lastName"));
            child.setGender(editChildForm.get("gender"));
            child.setBirthDate(editChildForm.get("birthDate"));
            
            child.save();
            
            Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
            if (loggedCaregiver == null) {
                return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
            }
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String content = child.getChildId() + "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + "," + "edit\n";
            String pathChildren = loggedCaregiver.getPathChildrenLog();
            adminLogs.writeToFile(pathChildren, content);
            */
            return ok(Json.toJson(caregiver));
        }
    }
    
    
    /**
     * EditChild action
     *
     * @param childId
     * @return
     */
    /*public Result editChild(Long childId) {
        
        DynamicForm editChildForm = formFactory.form().bindFromRequest();

        Logger.debug("DEBUG:" + editChildForm);

        if (editChildForm.hasErrors()) {
            return badRequest(editChildForm.errorsAsJson());
        }
        Child child = Child.findByChildId(childId);

        if (child == null) {
            return badRequest(buildJsonResponse("error", "User doesn't exist"));
        } else {
            Logger.debug("Editing child with id " + childId + ": " + child.getChildLogin().getUsername());

            child.getChildLogin().setUsername(editChildForm.get("username"));
            child.setFirstName(editChildForm.get("firstName"));
            child.setLastName(editChildForm.get("lastName"));
            child.setGender(editChildForm.get("gender"));
            child.setBirthDate(editChildForm.get("birthDate"));
            
            child.save();
            
            Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
            if (loggedCaregiver == null) {
                return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
            }
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String content = child.getChildId() + "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + "," + "edit\n";
            String pathChildren = loggedCaregiver.getPathChildrenLog();
            adminLogs.writeToFile(pathChildren, content);
            
            return ok(Json.toJson(child));
        }
    }*/

}