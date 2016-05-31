package controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Child;
import models.Login;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class ChildCtrl extends Controller {
	
	/*
	 * CHILD FORM
	 * */
	
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
	@Inject FormFactory formFactory;
    public Result registerchild() {
	 Form<RegisterChild> registerChildForm = formFactory.form(RegisterChild.class).bindFromRequest();

	 if ( registerChildForm.hasErrors()) {
	   return badRequest(registerChildForm.errorsAsJson());
	 }
	 RegisterChild newUser =  registerChildForm.get();
	 Login existingUser = Login.findByUsername(newUser.username);
	 if(existingUser != null) {
	   return badRequest(buildJsonResponse("error", "User exists"));
	 } else {
	   Login user = new Login();
	   user.setPassword(newUser.password);
	   user.setUserName(newUser.username);
	   user.setEnabled(true);
	   user.setUserType(1);
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
    
    /**
     * DeleteChild action
     */
    public Result deleteChild(Long childId) {
    
       Child child = Child.findByChildId(childId);
      	
       if (child == null)
    	   return badRequest(buildJsonResponse("error", "User doesn't exist"));
       
       child.delete();
       
   	   return ok(buildJsonResponse("success", "User deleted successfully"));
   	}

    /**
     * GetChildren action
     */
    public Result getChildren() {
   	 return ok(Json.toJson(Child.find.findList()));
   	}
    
    private static ObjectNode buildJsonResponse(String type, String message) {
  	  ObjectNode wrapper = Json.newObject();
  	  ObjectNode msg = Json.newObject();
  	  msg.put("message", message);
  	  wrapper.set(type, msg);
  	  return wrapper;
  	}
}
