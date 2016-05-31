package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Caregiver;
import models.Login;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains application common logic
 */
public class ApplicationCtrl extends Controller {

	/**
     * FORMS
     */
	public static class SignUp {
	 @Constraints.Required
	 @Constraints.MinLength(6)
	 public String password;
	 
	 public String username;
	 
	 public String passwordcheck;
	 
	 public String firstname;
	 
	 public String lastname;
	 
	 public String gender;
	 
	 @Constraints.Required
	 @Constraints.Email
	 public String email;
	}

	public static class LoginForm {
		public String username;
		
		@Constraints.Required
		public String password;
	}
	 
	

    /**
     * Signup action
     */
	@Inject FormFactory formFactory;
    public Result signup() {
	 Form<SignUp> signUpForm = formFactory.form(SignUp.class).bindFromRequest();

	 if ( signUpForm.hasErrors()) {
	   return badRequest(signUpForm.errorsAsJson());
	 }
	 SignUp newUser =  signUpForm.get();
	 Caregiver existingUser = Caregiver.findByEmail(newUser.email);
	 if(existingUser != null) {
	   return badRequest(buildJsonResponse("error", "User exists"));
	 } else {
	   Caregiver user = new Caregiver();
	   user.setEmail(newUser.email);	  
	   user.setFirstName(newUser.firstname);
	   user.setLastName(newUser.lastname);
	   user.setGender(newUser.gender);
	   user.save();
	   
	   Login userlogin = new Login();
	   userlogin.setPassword(newUser.password);
	   userlogin.setUserName(newUser.username);
	   userlogin.setEnabled(true);
	   userlogin.setUserType(0);
	   userlogin.save();
	   
	   session().clear();
	   session("username", newUser.email);

	   return ok(buildJsonResponse("success", "User created successfully"));
	 }
	}

	/**
     * Login action
     */
    @Inject FormFactory formFactory1;
	public Result login() {
	 Form<LoginForm> loginForm = formFactory1.form(LoginForm.class).bindFromRequest();
	 if (loginForm.hasErrors()) {
	   return badRequest(loginForm.errorsAsJson());
	 }
	 LoginForm loggingInUser = loginForm.get();
	 Login user = Login.findByUsernameAndPassword(loggingInUser.username, loggingInUser.password);
	 if(user == null) {
	   return badRequest(buildJsonResponse("error", "Incorrect email or password"));
	 } else {
	   session().clear();
	   session("username", loggingInUser.username);

	   ObjectNode wrapper = Json.newObject();
	   ObjectNode msg = Json.newObject();
	   msg.put("message", "Logged in successfully");
	   msg.put("user", loggingInUser.username);
	   wrapper.set("success", msg);
	   return ok(wrapper);
	 }
	}

	/**
     * Logout action
     */
	public Result logout() {
	 session().clear();
	 return ok(buildJsonResponse("success", "Logged out successfully"));
	}

	public Result isAuthenticated() {
	 if(session().get("username") == null) {
	   return unauthorized();
	 } else {
	   ObjectNode wrapper = Json.newObject();
	   ObjectNode msg = Json.newObject();
	   msg.put("message", "User is logged in already");
	   msg.put("user", session().get("username"));
	   wrapper.set("success", msg);
	   return ok(wrapper);
	 }
	}	
	
	private static ObjectNode buildJsonResponse(String type, String message) {
	  ObjectNode wrapper = Json.newObject();
	  ObjectNode msg = Json.newObject();
	  msg.put("message", message);
	  wrapper.set(type, msg);
	  return wrapper;
	}
}