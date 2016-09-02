package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Caregiver;
import models.Child;
import models.Login;

import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class ChildCtrl extends Controller {

	/*
	 * CHILD FORM
	 */

	public static class RegisterChild {
		@Constraints.Required
		@Constraints.MinLength(6)
		public String password;

		public String username;

		public String passwordcheck;

		public String firstname;

		public String lastname;

		public String gender;

		public String birthdate;
	}

	/**
	 * RegisterChild action
	 */
	@Inject
	FormFactory formFactory;

	public Result registerchild() {
		Form<RegisterChild> registerChildForm = formFactory.form(RegisterChild.class).bindFromRequest();

		if (registerChildForm.hasErrors()) {
			return badRequest(registerChildForm.errorsAsJson());
		}
		RegisterChild newUser = registerChildForm.get();
		Login existingUser = Login.findByUsername(newUser.username);
		if (existingUser != null) {
			return badRequest(buildJsonResponse("error", "User exists"));
		} else {
			Login user = new Login();
			user.setPassword(newUser.password);
			user.setUserName(newUser.username);
			user.setEnabled(true);
			user.setUserType(1);
			user.save();

			Child child = new Child();
			child.setChildLogin(user);
			child.setFirstName(newUser.firstname);
			child.setLastName(newUser.lastname);
			child.setGender(newUser.gender);
			child.setBirthDate(newUser.birthdate);

			child.save();

			Caregiver loggedCaregiver = Caregiver.findByUsername(session("username"));
			if (loggedCaregiver == null)
				return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
			Logger.debug(loggedCaregiver.getCaregiverLogin().getUserName() + " is logged in.");
			loggedCaregiver.addChild(child);
			Logger.debug(child.getChildLogin().getUserName() + " added to caregivers list.");
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
	 */
	

	public Result editchild(Long childId) {
		Form<EditChild> editChildForm = formFactory.form(EditChild.class).bindFromRequest();

		Logger.debug("DEBUG:" + editChildForm);

		EditChild newUser = editChildForm.get();
		Logger.debug("DEBUG:" + newUser.userName + " " + newUser.firstName + " " + newUser.lastName + " " + newUser.gender +" "+ newUser.birthDate);

		if (editChildForm.hasErrors()) {
			return badRequest(editChildForm.errorsAsJson());
		}

		Child child = Child.findByChildId(childId);

		if (child == null) {
			return badRequest(buildJsonResponse("error", "User doesn't exist"));
		} else {
			Logger.debug("Editing child with id " + childId + ": " + child.getChildLogin().getUserName());
			
			child.getChildLogin().setUserName(newUser.userName);
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

		if (child == null)
			return badRequest(buildJsonResponse("error", "User doesn't exist"));

		Caregiver loggedCaregiver = Caregiver.findByUsername(session("username"));
		if (loggedCaregiver == null)
			return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
		Logger.debug("Deleting " + loggedCaregiver.getCaregiverLogin().getUserName() + "'s' child: "+ child.getFirstName());
		
		loggedCaregiver.removeChild(child);
		loggedCaregiver.save();

		child.delete();

		return ok(buildJsonResponse("success", "User deleted successfully"));
	}

	/**
	 * GetChildren action
	 */
	public Result getChildren() {
		Caregiver loggedCaregiver = Caregiver.findByUsername("Caregiver");
		if (loggedCaregiver == null)
			return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
		Logger.debug(loggedCaregiver.getCaregiverLogin().getUserName() + " is logged in.");
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
