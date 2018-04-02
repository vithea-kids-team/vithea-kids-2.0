package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import javax.inject.Inject;
import models.Caregiver;
import models.Level;
import models.Login;
import models.SecurityQuestion;
import models.Topic;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import static play.mvc.Results.ok;

public class SecurityController extends Controller {

    @Inject
    FormFactory formFactory;

    public final static String AUTH_TOKEN_HEADER = "Authorization";
    public static final String AUTH_TOKEN = "authToken";
    public static final String SECURITY_QUESTION = "securityQuestion";
    public AdminLogs adminLogs = new AdminLogs();  
    //public AdminCaregiverCtrl adminCaregiver = new AdminCaregiverCtrl();


    public static Login getUser() {
        return (Login) Http.Context.current().args.get("user");
    }
    
    public Result signup(){
        Logger.debug("Hit SecurityController.Signup method");
        Logger.debug("\t Binding data from request");
        DynamicForm signUpForm = formFactory.form().bindFromRequest();
        
        if (signUpForm.hasErrors()) {
            Logger.debug("\t \t Form has errors, returning", signUpForm.errorsAsJson());
            return badRequest(signUpForm.errorsAsJson());
        }

        if (Caregiver.findByEmail(signUpForm.get("email")) != null) {
            Logger.debug("\t \t User already exists, returning bad request");
            return badRequest("User already exists");
        } else if (Login.findByUsername(signUpForm.get("username")) != null) {
            return badRequest("Username already exists");
        } else {
            String username     = signUpForm.get("username");
            String password     = signUpForm.get("password");
            String email        = signUpForm.get("email");
            String firstName    = signUpForm.get("firstName");
            String lastName     = signUpForm.get("lastName");
            String gender       = signUpForm.get("gender");
            String secQuestion  = signUpForm.get("securityQuestion");
            String secPassword  = signUpForm.get("securityPassword");
            
            Logger.debug("\t \t Creating caregiver...");
            Caregiver user = new Caregiver(email, gender,  firstName, lastName, secQuestion, secPassword);
            
            Logger.debug("\t \t Creating user login...");
            Login userlogin = new Login(username, password);
            userlogin.setEnabled(true);
            userlogin.setUserType(0);
            userlogin.save();
            user.setCaregiverLogin(userlogin);
            user.save();
                    
            Boolean DEVELOPMENT = Boolean.parseBoolean(ConfigFactory.load().getString("development"));
            String path = "";
            
            // update path files
            Logger.debug("\t \t Creating caregiver log files");
            if (DEVELOPMENT) path = "logs" + File.separator;
            else path = ConfigFactory.load().getString("vitheaLogs");
            Logger.debug(path);
           
            String pathExercises = path + "Log_caregiver_" +  user.getCaregiverId() + "_exercises.csv";
            user.setPathExercisesLog(pathExercises);
            adminLogs.createFile(pathExercises);
            adminLogs.writeToFile(pathExercises, "exerciseId,caregiverId,timestamp,type,action,#answers,resources,stimulus,default\n");
            
            String pathSequences = path + "Log_caregiver_" +  user.getCaregiverId() + "_sequences.csv";
            user.setPathSequencesLog(pathSequences);
            adminLogs.createFile(pathSequences);
            adminLogs.writeToFile(pathSequences, "sequenceId,caregiverId,timestamp,action,#exercises,exercises,#children,children\n");
            
            String pathResources = path + "Log_caregiver_" +  user.getCaregiverId() + "_resources.csv";
            user.setPathResourcesLog(pathResources);
            adminLogs.createFile(pathResources);
            adminLogs.writeToFile(pathResources, "resourceId,caregiverId,exerciseId,timestamp,type,action,default\n");
            
            String pathTopics = path + "Log_caregiver_" +  user.getCaregiverId() + "_topics.csv";
            user.setPathTopicsLog(pathTopics);
            adminLogs.createFile(pathTopics);
            adminLogs.writeToFile(pathTopics, "topicId,caregiverId,exerciseId,timestamp,action,description,default\n");
            
            String pathLevels = path + "Log_caregiver_" +  user.getCaregiverId() + "_levels.csv";
            user.setPathLevelsLog(pathLevels);
            adminLogs.createFile(pathLevels);
            adminLogs.writeToFile(pathLevels, "levelId,caregiverId,exerciseId,timestamp,action,description,default\n");
            
            String pathChildren = path + "Log_caregiver_" +  user.getCaregiverId() + "_children.csv";
            user.setPathChildrenLog(pathChildren);
            adminLogs.createFile(pathChildren);
            adminLogs.writeToFile(pathChildren, "childId,caregiverId,timestamp,action\n");
            
            String pathPrefs = path + "Log_caregiver_" +  user.getCaregiverId() + "_preferences.csv";
            user.setPathPreferencesLog(pathPrefs);
            adminLogs.createFile(pathPrefs);
            adminLogs.writeToFile(pathPrefs, "childId,caregiverId,timestamp,greeetingMessage,exerciseReinforcementMessage,"
                    + "sequenceReinforcementMessage,promptingStrat,promptingColor,promptingHide,promptingScratch,"
                    + "promptingSize,promptingRead,reinforcementStrat,resourceId,order,caps,emotions,animatedChar\n");
            user.save();
            
            //Insert default content
            createTopicsDefault(user);
            createLevelsDefault(user);
            //createExercisesDefault(user);
            
            Logger.debug("\t \t \t Returning ok");
            return ok("User created successfully");
        }
    }
    
    public void createTopicsDefault(Caregiver loggedCaregiver){
        Topic topic1 = new Topic("Animais", loggedCaregiver, true);
        topic1.save();
        Topic topic2 = new Topic("Alimentos", loggedCaregiver, true);
        topic2.save();
        Topic topic3 = new Topic("Objectos", loggedCaregiver, true);
        topic3.save();        
    }        

    public void createLevelsDefault(Caregiver loggedCaregiver){
        Level level1 = new Level("Fácil", loggedCaregiver, true);
        level1.save();
        Level level2 = new Level("Intermédio", loggedCaregiver, true);
        level2.save();
        Level level3 = new Level("Difícil", loggedCaregiver, true);
        level3.save();
    }
    
    // returns an authToken
    public Result login(String type) {
        Logger.debug("Hit SecurityController.Login method");
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            Logger.debug("\t Login form has errors, returning badRequest", loginForm.errorsAsJson());
            return badRequest(loginForm.errorsAsJson());
        }

        Login login = loginForm.get();
        Logger.debug("\t Username:" + login.getUsername());
        Logger.debug("\t Password:" + login.getPassword());
        Login user = Login.findByUsernameAndPassword(login.getUsername(), login.getPassword());
        
        if (user == null || (user.getUserType() == 0 && type.equals("child")) || (user.getUserType() == 1 && type.equals("caregiver"))) {
            Logger.debug("\t \t Invalid user, returning unauthorized");
            return unauthorized("Invalid username or password");
        } else {
            String authToken = user.addSession();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(Http.Cookie.builder(AUTH_TOKEN, authToken).withSecure(ctx().request().secure()).build());

            Logger.debug("\t \t Returning authentication token.");
                        
            return ok(authTokenJson);
        }
    }

    @Security.Authenticated(Secured.class)
    public Result logout() {
        Logger.debug("Hit SecurityController.Logout method");
        Logger.debug("\t Deleting tokens...");
        response().discardCookie(AUTH_TOKEN);
        String token = Http.Context.current().request().headers().get(AUTH_TOKEN_HEADER)[0];
        getUser().removeSession(token);
        Logger.debug("\t Redirecting...");
        return redirect("/");
    }
    
    public Result getQuestion(){
        Logger.debug("Hit SecurityController.getQuestion method");
        
        DynamicForm questionForm = formFactory.form().bindFromRequest();
        String username = questionForm.get("username");
        
        Caregiver caregiver = Caregiver.findByUsername(username);
        
        if(caregiver == null){
            return badRequest("Username does not exist");
        }
        else {
            SecurityQuestion securityQuestion = caregiver.getSecurityQuestion();
            if(securityQuestion == null){ 
                return badRequest("Security Question does not exist"); 
            }
            String question = securityQuestion.getQuestion();
            ObjectNode questionJson = Json.newObject();
            questionJson.put(SECURITY_QUESTION, question);
            return ok(questionJson);
        }
    }
    
    public Result recoverPassword() {
        Logger.debug("Hit SecurityController.recoverPassword method");  
      
        DynamicForm recoverPasswordForm = formFactory.form().bindFromRequest();
        String username = recoverPasswordForm.get("username");
        String password = recoverPasswordForm.get("password");
        String securityAnswer = recoverPasswordForm.get("securityAnswer");
        
        Caregiver caregiver = Caregiver.findByUsername(username);
        String caregiverSecurityAnswer = caregiver.getSecurityQuestion().getAnswer();
        
        if(caregiverSecurityAnswer.equals(securityAnswer)) {
            Login findByUsername = Login.findByUsername(username);
            findByUsername.setPassword(password);
            findByUsername.setEnabled(true);
            findByUsername.save();
        }
        else return badRequest("Wrong security answer");
        
    return ok("Password changed successfully");
    }

}