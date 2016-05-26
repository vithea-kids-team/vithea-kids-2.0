package controllers;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Caregiver;
import models.Child;
import models.ChildLogin;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains application common logic
 */
public class Application extends Controller {

	/**
     * FORMS
     */
	public static class UserForm {
	 @Constraints.Required
	 @Constraints.Email
	 public String email;
	}

	public static class SignUp extends UserForm {
	 @Constraints.Required
	 @Constraints.MinLength(6)
	 public String password;
	 
	 public String username;
	 
	 public String passwordcheck;
	 
	 public String firstname;
	 
	 public String lastname;
	 
	 public String gender;
	}

	public static class Login extends UserForm {
	 @Constraints.Required
	 public String password;
	}
	
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
	   user.setPassword(newUser.password);
	   user.setUserName(newUser.username);
	   user.setFirstName(newUser.firstname);
	   user.setLastName(newUser.lastname);
	   user.setGender(newUser.gender);
	   user.save();
	   session().clear();
	   session("username", newUser.email);

	   return ok(buildJsonResponse("success", "User created successfully"));
	 }
	}

	/**
     * Login action
     */
	public Result login() {
	 Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
	 if (loginForm.hasErrors()) {
	   return badRequest(loginForm.errorsAsJson());
	 }
	 Login loggingInUser = loginForm.get();
	 Caregiver user = Caregiver.findByEmailAndPassword(loggingInUser.email, loggingInUser.password);
	 if(user == null) {
	   return badRequest(buildJsonResponse("error", "Incorrect email or password"));
	 } else {
	   session().clear();
	   session("username", loggingInUser.email);

	   ObjectNode wrapper = Json.newObject();
	   ObjectNode msg = Json.newObject();
	   msg.put("message", "Logged in successfully");
	   msg.put("user", loggingInUser.email);
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
	
	/**
     * RegisterChild action
     */
	@Inject FormFactory formFactory1;
    public Result registerchild() {
	 Form<RegisterChild> registerChildForm = formFactory.form(RegisterChild.class).bindFromRequest();

	 if ( registerChildForm.hasErrors()) {
	   return badRequest(registerChildForm.errorsAsJson());
	 }
	 RegisterChild newUser =  registerChildForm.get();
	 ChildLogin existingUser = ChildLogin.findByUsername(newUser.username);
	 if(existingUser != null) {
	   return badRequest(buildJsonResponse("error", "User exists"));
	 } else {
	   ChildLogin user = new ChildLogin();
	   user.setPassword(newUser.password);
	   user.setChildUserName(newUser.username);
	   user.save();
	   
	   Child userInfo = new Child();
	   userInfo.setChildLogin(user);
	   userInfo.setFirstName(newUser.firstname);
	   userInfo.setLastName(newUser.lastname);
	   userInfo.setGender(newUser.gender);
	   
	   DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	   try {
		   java.util.Date utilDate = format.parse(newUser.birthdate);
		   java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			userInfo.setBirthDate(sqlDate );
	   } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }
	   userInfo.save();

	   return ok(buildJsonResponse("success", "User created successfully"));
	 }
	}

	private static ObjectNode buildJsonResponse(String type, String message) {
	  ObjectNode wrapper = Json.newObject();
	  ObjectNode msg = Json.newObject();
	  msg.put("message", message);
	  wrapper.put(type, msg);
	  return wrapper;
	}
}