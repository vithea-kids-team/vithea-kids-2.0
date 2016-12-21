package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import models.Caregiver;
import models.Child;
import models.Login;

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
public class ChildCtrl extends Controller {

    @Inject
    FormFactory formFactory;

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
            child.setFirstName(registerChildForm.get("firstname"));
            child.setLastName(registerChildForm.get("lastname"));
            child.setGender(registerChildForm.get("gender"));

            child.setBirthDate(registerChildForm.get("birthDate"));

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

        public String userName;

        public String firstName;

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
        Logger.debug("DEBUG:" + newUser.userName + " " + newUser.firstName + " " + newUser.lastName + " " + newUser.gender + " " + newUser.birthDate);

        if (editChildForm.hasErrors()) {
            return badRequest(editChildForm.errorsAsJson());
        }

        Child child = Child.findByChildId(childId);

        if (child == null) {
            return badRequest(buildJsonResponse("error", "User doesn't exist"));
        } else {
            Logger.debug("Editing child with id " + childId + ": " + child.getChildLogin().getUsername());

            child.getChildLogin().setUsername(newUser.userName);
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

    private static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}
