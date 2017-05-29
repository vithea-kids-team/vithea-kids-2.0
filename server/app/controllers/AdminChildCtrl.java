package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import models.AnimatedCharacter;

import models.Caregiver;
import models.Child;
import models.Login;
import models.PersonalMessage;
import models.PersonalMessageType;
import models.Prompting;
import models.Reinforcement;
import models.Sequence;

import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import play.data.DynamicForm;
import play.db.ebean.Transactional;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class AdminChildCtrl extends Controller {

    @Inject
    FormFactory formFactory;
    
    public Result getPersonalMessages(Long childId) {
        Child child = Child.findByChildId(childId);
        if (child == null) {
            return badRequest("Child does not exist.");
        }
    
        return ok(Json.toJson(child.getPersonalMessagesList()));
    }
    

    public Result setPersonalMessages(Long childId) {
        DynamicForm registerPreferencesForm = formFactory.form().bindFromRequest();
        
        if (registerPreferencesForm.hasErrors()) {
            return badRequest(registerPreferencesForm.errorsAsJson());
        }
        
        Child child = Child.findByChildId(childId);
        if (child == null) {
            return badRequest("Child does not exist.");
        }
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        if(loggedCaregiver.getChildList().contains(child)) {
            String greetingMessage = registerPreferencesForm.get("greetingMessage");
            String exerciseReinforcementMessage = registerPreferencesForm.get("exerciseReinforcementMessage");
            String sequenceReinforcementMessage = registerPreferencesForm.get("sequenceReinforcementMessage");
            
            List<PersonalMessage> messages = child.getPersonalMessagesList();
            
            Optional<PersonalMessage> oldGreetingMessage = messages.stream()
                .filter(item -> item.getMessageType() == PersonalMessageType.GREETING_MESSAGE)
                .findFirst();
            
            if (oldGreetingMessage.isPresent()) {
                oldGreetingMessage.get().setMessage(greetingMessage);
            } else {
                messages.add(new PersonalMessage(greetingMessage, PersonalMessageType.GREETING_MESSAGE));
            }
            
            Optional<PersonalMessage> oldExerciseReinforcementMessage = messages.stream()
                .filter(item -> item.getMessageType() == PersonalMessageType.EXERCISE_REINFORCEMENT)
                .findFirst();
            
            if (oldExerciseReinforcementMessage.isPresent()) {
                oldExerciseReinforcementMessage.get().setMessage(exerciseReinforcementMessage);
            } else {
                messages.add(new PersonalMessage(exerciseReinforcementMessage, PersonalMessageType.EXERCISE_REINFORCEMENT));
            }
            
            Optional<PersonalMessage> oldSequenceReinforcementMessage = messages.stream()
                .filter(item -> item.getMessageType() == PersonalMessageType.SEQUENCE_REINFORCEMENT)
                .findFirst();
            
            if (oldSequenceReinforcementMessage.isPresent()) {
                oldSequenceReinforcementMessage.get().setMessage(sequenceReinforcementMessage);
            } else {
                messages.add(new PersonalMessage(sequenceReinforcementMessage, PersonalMessageType.SEQUENCE_REINFORCEMENT));
            }
            
            
            child.save();
            
            return ok("New messages saved.");
        }
        
        return badRequest("No permission to change child data");
    }
    
    @Transactional
    public Result registerchild() {
        DynamicForm registerChildForm = formFactory.form().bindFromRequest();

        if (registerChildForm.hasErrors()) {
            return badRequest(registerChildForm.errorsAsJson());
        }
     
        Login existingUser = Login.findByUsername(registerChildForm.get("username"));
        if (existingUser != null) {
            return badRequest(buildJsonResponse("error", "User already exists."));
        } else {
            Login user = new Login(registerChildForm.get("username"), registerChildForm.get("password"));
            user.setEnabled(true);
            user.setUserType(1);
            user.save();

            Child child = new Child();
            child.setChildLogin(user);
            child.setFirstName(registerChildForm.get("firstName"));
            child.setLastName(registerChildForm.get("lastName"));
            child.setGender(registerChildForm.get("gender"));
            child.setBirthDate(registerChildForm.get("birthDate"));
            
            List<PersonalMessage> messages = child.getPersonalMessagesList();
            messages.add(new PersonalMessage("", PersonalMessageType.GREETING_MESSAGE));
            messages.add(new PersonalMessage("", PersonalMessageType.EXERCISE_REINFORCEMENT));
            messages.add(new PersonalMessage("", PersonalMessageType.SEQUENCE_REINFORCEMENT));
            
            AnimatedCharacter defaultChar = AnimatedCharacter.findByName("cat");
            if(defaultChar != null) {
                child.setAnimatedCharacter(defaultChar);
            }
            
            child.setReinforcement(new Reinforcement());
            child.setPrompting(new Prompting());
            child.setEmotions(false);
            
            child.save();

            Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
            if (loggedCaregiver == null) {
                return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
            }
            Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
            loggedCaregiver.addChild(child);
            Logger.debug(child.getChildLogin().getUsername() + " added to caregivers list.");
            loggedCaregiver.save();

            return ok(Json.toJson(child));
        }
    }

    /*
	 * EDIT FORM
     */
    public static class EditChild {

        public String username;

        public String firstName;
        
        public String password;

        public String lastName;

        public String birthDate;

        public String gender;
    }

    /**
     * EditChild action
     *
     * @param childId
     * @return
     */
    public Result editchild(Long childId) {
        Form<EditChild> editChildForm = formFactory.form(EditChild.class).bindFromRequest();

        Logger.debug("DEBUG:" + editChildForm);

        EditChild newUser = editChildForm.get();
        Logger.debug("DEBUG:" + newUser.username + " " + newUser.firstName + " " + newUser.lastName + " " + newUser.gender + " " + newUser.birthDate);

        if (editChildForm.hasErrors()) {
            return badRequest(editChildForm.errorsAsJson());
        }

        Child child = Child.findByChildId(childId);

        if (child == null) {
            return badRequest(buildJsonResponse("error", "User doesn't exist"));
        } else {
            Logger.debug("Editing child with id " + childId + ": " + child.getChildLogin().getUsername());

            child.getChildLogin().setUsername(newUser.username);
            
            if (!newUser.password.isEmpty()) {
                child.getChildLogin().setPassword(newUser.password);
            }
            
            child.setFirstName(newUser.firstName);
            child.setLastName(newUser.lastName);
            child.setGender(newUser.gender);
            child.setBirthDate(newUser.birthDate);

            child.save();
            return ok(Json.toJson(child));
        }
    }

    /**
     * DeleteChild action
     */
    public Result deleteChild(Long childId) {

        Child child = Child.findByChildId(childId);

        if (child == null) {
            return badRequest(buildJsonResponse("error", "User doesn't exist"));
        }

        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug("Deleting " + loggedCaregiver.getCaregiverLogin().getUsername() + "'s' child: " + child.getFirstName());

        loggedCaregiver.removeChild(child);
        loggedCaregiver.save();

        child.delete();

        return ok(buildJsonResponse("success", "User deleted successfully"));
    }

    /**
     * GetChildren action
     */
    public Result getChildren() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        Logger.debug(loggedCaregiver.getChildList().size() + " children registered.");
        return ok(Json.toJson(loggedCaregiver.getChildList()));
    }
    
    public Result getChild(Long childId) {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        List<Child> children = loggedCaregiver.getChildList();
        Child child = Child.findByChildId(childId);
        if (!children.contains(child)) {
            return badRequest(buildJsonResponse("error", "Invalid child id."));
        }
        
        return ok(Json.toJson(child));
    }
    
    /*
     * GetChildSequences action
     */
    
    public Result getChildSequences(Long childId) {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        List<Child> children = loggedCaregiver.getChildList();
        Child child = Child.findByChildId(childId);
        if (!children.contains(child)) {
            return badRequest(buildJsonResponse("error", "Invalid child id."));
        }
        
        return  ok(Json.toJson(child.getSequencesList()));
    }
    
    private static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}
