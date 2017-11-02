package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.util.Properties;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import models.Caregiver;
import models.Login;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;

public class SecurityController extends Controller {

    @Inject
    FormFactory formFactory;

    public final static String AUTH_TOKEN_HEADER = "Authorization";
    public static final String AUTH_TOKEN = "authToken";
    public AdminLogs adminLogs = new AdminLogs();

    public static Login getUser() {
        return (Login) Http.Context.current().args.get("user");
    }

    // returns an authToken
    public Result login() {
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

        if (user == null) {
            Logger.debug("\t \t Invalid user, returning unauthorized");
            return unauthorized("Invalid username or password");
        } else {
            String authToken = user.addSession();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(Http.Cookie.builder(AUTH_TOKEN, authToken).withSecure(ctx().request().secure()).build());

            Logger.debug("\t \t Returning authentication token.");
            
            //this.sendEmail("lua.svmac@gmail.com", "Teste", "Awesome! It works!");
            
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

    public Result signup() {
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
            Logger.debug("\t \t Creating user login...");
            Login userlogin = new Login(signUpForm.get("username"), signUpForm.get("password"));
            userlogin.setEnabled(true);
            userlogin.setUserType(0);
            userlogin.save();
            Logger.debug("\t \t Creating caregiver...");
            Caregiver user = new Caregiver();
            user.setCaregiverLogin(userlogin);
            user.setEmail(signUpForm.get("email"));
            user.setFirstName(signUpForm.get("firstname"));
            user.setLastName(signUpForm.get("lastname"));
            user.setGender(signUpForm.get("gender"));
            user.save();
            
            Boolean DEVELOPMENT = Boolean.parseBoolean(ConfigFactory.load().getString("development"));
            String path = "";
            
            // update path files
            Logger.debug("\t \t Creating caregiver log files");
            if (DEVELOPMENT) path = ConfigFactory.load().getString("vitheaRoot") + "/public/logs/";
            else path =  "public" + File.separator + "logs" + File.separator;
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
            Logger.debug("\t \t \t Returning ok");
            return ok("User created successfully");
        }
    }
    
        public void sendEmail(String to, String subject, String body){
        
        // Recipient's email ID needs to be mentioned.
        // Sender's email ID needs to be mentioned
        String from = "lua.svmac@gmail.com"; //admin@vithea-kids.com

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();
    
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}