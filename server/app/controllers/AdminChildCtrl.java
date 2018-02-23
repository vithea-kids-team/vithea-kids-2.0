package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import models.AnimatedCharacter;
import models.Caregiver;
import models.Child;
import models.Login;
import models.PersonalMessage;
import models.PersonalMessageType;
import models.Prompting;
import models.PromptingStrategy;
import models.Reinforcement;
import models.ReinforcementStrategy;
import models.Resource;
import models.Sequence;
import models.SequenceExercisesCapitalization;
import models.SequenceExercisesOrder;
import models.SequencePreferences;
import org.json.JSONException;
import org.json.JSONObject;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;


@Security.Authenticated(Secured.class)
public class AdminChildCtrl extends Controller {

    @Inject
    FormFactory formFactory;
    
    public AdminLogs adminLogs = new AdminLogs();
        
    public Result getPersonalMessages(Long childId) {
        Child child = Child.findByChildId(childId);
        if (child == null) {
            return badRequest("Child does not exist.");
        }
    
        return ok(Json.toJson(child.getPersonalMessagesList()));
    }
    
    @Transactional
    public Result registerchild() {
        DynamicForm registerChildForm = formFactory.form().bindFromRequest();

        if (registerChildForm.hasErrors()) {
            return badRequest(registerChildForm.errorsAsJson());
        }
     
        Login existingUser = Login.findByUsername(registerChildForm.get("username"));
        if (existingUser != null) {
            return badRequest("Username already exists");
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
            messages.add(new PersonalMessage("Bem-vindo!", PersonalMessageType.GREETING_MESSAGE));
            messages.add(new PersonalMessage("Muito bem!", PersonalMessageType.EXERCISE_REINFORCEMENT));
            messages.add(new PersonalMessage("Parabéns!", PersonalMessageType.SEQUENCE_REINFORCEMENT));
            
            AnimatedCharacter defaultChar = AnimatedCharacter.findByName("cat");
            if(defaultChar != null) {
                child.setAnimatedCharacter(defaultChar);
            }
            
            child.setReinforcement(new Reinforcement());
            child.setPrompting(new Prompting());
            child.setSequenceExercisesPreferences(new SequencePreferences());
            child.setEmotions(false);
            
            child.save();
            
            Boolean DEVELOPMENT = Boolean.parseBoolean(ConfigFactory.load().getString("development"));
            String path = "";
            
            // update path files
            Logger.debug("\t \t Creating child log files");
            if (DEVELOPMENT) path = "logs" + File.separator;
            else path = ConfigFactory.load().getString("vitheaLogs");
            Logger.debug(path);

            Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
            if (loggedCaregiver == null) {
                return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
            }
            Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
            loggedCaregiver.addChild(child);
            Logger.debug(child.getChildLogin().getUsername() + " added to caregivers list.");
            loggedCaregiver.save();
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String content = child.getChildId() + "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + "," + "create\n";
            String pathChildren = loggedCaregiver.getPathChildrenLog();
            adminLogs.writeToFile(pathChildren, content);
            
            String pathAndroidSequences = path + "Log_child_" +  child.getChildId()+ "_androidSequences.csv";
            child.setPathAndroidSequencesLog(pathAndroidSequences);
            adminLogs.createFile(pathAndroidSequences);
            adminLogs.writeToFile(pathAndroidSequences, "sequenceId,timestampBeginSequence,timestampEndSequence,childId,numberOfExercises,"
                    + "correctExercises,distractorHitsAvg\n");
            
            String pathAndroidSequencesExercises = path + "Log_child_" +  child.getChildId() + "_androidSequencesExercises.csv";
            child.setPathAndroidSequencesExercisesLog(pathAndroidSequencesExercises);
            adminLogs.createFile(pathAndroidSequencesExercises);
            adminLogs.writeToFile(pathAndroidSequencesExercises, "sequenceId,timestampBeginSequence,timestampEndSequence,childId,exerciseId,"
                    + "promptingStrategy,reinforcementStrategy,timestampBeginExercise,timestampEndExercise,numberOfDistractorHits,correct,skipped\n");            
            
            child.save();
            
            return ok(Json.toJson(child));
        }
    }

    /**
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
    public Result editChild(Long childId) {
        
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
        
        Sequence.find.all().forEach((s) -> {
            child.getSequencesList().remove(s);
            s.save();
        });
        
        child.delete();
            
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = child.getChildId() + "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + "," + "delete\n";
        String pathChildren = loggedCaregiver.getPathChildrenLog();
        adminLogs.writeToFile(pathChildren, content);

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
    
    /**
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
   
        return ok(Json.toJson(child.getSequencesList()));
    }
    
    
    public static class UpdatePreferences {
        public String greetingMessage;
        public String exerciseReinforcementMessage;
        public String sequenceReinforcementMessage;
	public String animatedCharacterResourceId;
        public String animatedCharacterResourcePath;
	public String promptingStrategy;
	public String promptingColor;
	public String promptingSize;
	public String promptingScratch;
	public String promptingHide;
        public String promptingRead;
        public String sequenceExercisesOrder;
        public String sequenceExercisesCapitalization;
	public String reinforcementStrategy;
	public String reinforcementResourceId;
        public String reinforcementResourcePath;
	public String emotions;
    }
    
    public Result updatePreferences(Long childId) {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        List<Child> children = loggedCaregiver.getChildList();
        Child child = Child.findByChildId(childId);
        if (!children.contains(child)) {
            return badRequest(buildJsonResponse("error", "Invalid child id."));
        }
        
        
        DynamicForm prefs = formFactory.form().bindFromRequest();
        Logger.debug("DEBUG:" + prefs);
        
        //Form<UpdatePreferences> updatePreferencesForm = formFactory.form(UpdatePreferences.class).bindFromRequest();
        
        if (prefs.hasErrors()) {
            return badRequest(prefs.errorsAsJson());
        }
        
        //UpdatePreferences prefs = updatePreferencesForm.get();
        
        String greetingMessage = child.getPersonalMessagesList().get(0).getMessage();
        String exerciseReinforcementMessage = child.getPersonalMessagesList().get(1).getMessage();
        String sequenceReinforcementMessage = child.getPersonalMessagesList().get(2).getMessage();
                
        this.setPersonalMessages(child, prefs.get("greetingMessage"), prefs.get("exerciseReinforcementMessage"), 
                prefs.get("sequenceReinforcementMessage"));
        Logger.debug("Greeting message:" + prefs.get("greetingMessage"));
        Logger.debug("Exercise message:" + prefs.get("exerciseReinforcementMessage"));
        Logger.debug("Sequence message:" + prefs.get("sequenceReinforcementMessage"));
        
        Boolean greeting = !greetingMessage.equals(prefs.get("greetingMessage"));
        Boolean exerciseReinforcement = !exerciseReinforcementMessage.equals(prefs.get("exerciseReinforcementMessage"));
        Boolean sequenceReinforcement = !sequenceReinforcementMessage.equals(prefs.get("sequenceReinforcementMessage"));
        
        int animatedCharResourceId;
        try {
            animatedCharResourceId = Integer.parseInt(prefs.get("animatedCharacterResourceId"));
            this.setAnimatedCharacter(child, (long)animatedCharResourceId);
        }
        catch (Exception e) {
            animatedCharResourceId = -1;
        }
        
        Prompting p = child.getPrompting();
        p.setPromptingStrategy(PromptingStrategy.valueOf(prefs.get("promptingStrategy")));
        p.setPromptingColor(Boolean.parseBoolean(prefs.get("promptingColor")));
        p.setPromptingHide(Boolean.parseBoolean(prefs.get("promptingHide")));
        p.setPromptingScratch(Boolean.parseBoolean(prefs.get("promptingScratch")));
        p.setPromptingSize(Boolean.parseBoolean(prefs.get("promptingSize")));
        p.setPromptingRead(Boolean.parseBoolean(prefs.get("promptingRead")));
        p.save();
         
        Logger.debug("Prompting strategy:" + prefs.get("promptingStrategy"));
        Logger.debug("Prompting color:" + prefs.get("promptingColor"));
        Logger.debug("Prompting size:" + prefs.get("promptingSize"));
        Logger.debug("Prompting scratch:" + prefs.get("promptingScratch"));
        Logger.debug("Prompting hide:" + prefs.get("promptingHide"));
        Logger.debug("Prompting read:" + prefs.get("promptingRead"));
       
        int reinforcementResourceId;
        try {
            reinforcementResourceId = Integer.parseInt(prefs.get("reinforcementResourceId"));
        }
        catch (Exception e) {
            reinforcementResourceId = -1;
        }
        
        Logger.debug("Reinforcement strategy:" + prefs.get("reinforcementStrategy"));
        
        Reinforcement r = child.getReinforcement();
        r.setReinforcementResource(Resource.findById((long)reinforcementResourceId));
        r.setReinforcementStrategy(ReinforcementStrategy.valueOf(prefs.get("reinforcementStrategy")));
        r.save();
       
        SequencePreferences sq = child.getSequenceExercisesPreferences();
        Logger.debug("Sequence Exercises Order:" + prefs.get("sequenceExercisesOrder"));
        sq.setSequenceExercisesOrder(SequenceExercisesOrder.valueOf(prefs.get("sequenceExercisesOrder")));    
        Logger.debug("Sequence Exercises Capitalization:" + prefs.get("sequenceExercisesCapitalization"));
        sq.setSequenceExercisesCapitalization(SequenceExercisesCapitalization.valueOf(prefs.get("sequenceExercisesCapitalization")));
        sq.save();
        
        child.setEmotions(Boolean.parseBoolean(prefs.get("emotions")));
        
        Logger.debug("Emotions:" + prefs.get("emotions"));
        
        child.save();
        
        Reinforcement rchild = child.getReinforcement();
        Prompting pchild = child.getPrompting();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = childId + "," + loggedCaregiver.getCaregiverId() + "," + timestamp + "," +
                greeting + "," + exerciseReinforcement + "," + sequenceReinforcement + "," + 
                pchild.getPromptingStrategy().name() + "," + pchild.getPromptingColor() + "," + pchild.getPromptingHide() + "," + 
                pchild.getPromptingScratch() + "," + pchild.getPromptingSize() + "," + pchild.getPromptingRead() + ",";
        
        if(rchild.getReinforcementStrategy().name().equals("OFF")) content += rchild.getReinforcementStrategy().name() + ",,";
        
        else content += rchild.getReinforcementStrategy().name() + "," + rchild.getReinforcementResource().getResourceId() + ",";
        
        content += sq.getSequenceExercisesOrder() + "," + sq.getSequenceExercisesCapitalization() + "," + 
                child.getEmotions() + ",";
        
        if(child.getAnimatedCharacter() == null) content += ",\n";
        else content += child.getAnimatedCharacter().getAvatar().getResourceId() + "\n";
           
        String pathPreferences = loggedCaregiver.getPathPreferencesLog();
        adminLogs.writeToFile(pathPreferences, content);
        
        return ok();
    }
    
    public void setPersonalMessages(Child child, String greetingMessage, String exerciseReinforcementMessage, String sequenceReinforcementMessage) {
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
    }
    public void setAnimatedCharacter(Child child, Long animatedCharacterResourceId) {
        AnimatedCharacter character = AnimatedCharacter.findByResourceId(animatedCharacterResourceId);
        child.setAnimatedCharacter(character);
        
        Logger.debug("Animated char:" + character.getName());
    }
    
    private static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
    
    public Result saveLogsChild() {
        
        Child loggedChild= Child.findByUsername(SecurityController.getUser().username);
        if (loggedChild == null) {
            System.err.println("Child does not exist.");
            return badRequest(buildJsonResponse("error", "Child does not exist."));
        }
        
       DynamicForm sequenceLogsForm = formFactory.form().bindFromRequest();
       
        if (sequenceLogsForm.hasErrors()) {
            System.err.println("Form with errors.");
            return badRequest(sequenceLogsForm.errorsAsJson());
        }
        
        Logger.debug("DEBUG:" + sequenceLogsForm);
        
        String SEQUENCE_LOG_TAG = sequenceLogsForm.get("sequence_log");
        
        try {
            JSONObject json = new JSONObject(SEQUENCE_LOG_TAG);
            
            String sequenceId = json.get("sequenceID").toString();
            String childId = json.get("childID").toString();
            String timestampBeginSequence = json.get("timestampBeginSequence").toString();
            String timestampEndSequence = json.get("timestampEndSequence").toString();
            String numberOfExercises = json.get("numberOfExercises").toString();
            String correctExercises = json.get("correctExercises").toString();
            String exercisesLogs = json.get("exercisesLogs").toString();
            String distractorHitsAvg = json.get("distractorHitsAvg").toString();
            
            String content = sequenceId + "," + timestampBeginSequence + "," + 
                    timestampEndSequence + "," + childId + "," + 
                    numberOfExercises + "," + correctExercises + "," + 
                    distractorHitsAvg;
            
            exercisesLogs = exercisesLogs.substring(1, exercisesLogs.length() - 1);
            String[] split = exercisesLogs.split("\\},");
            int numberExercises =  split.length;
            
            JSONObject json2;
            String content2 = "";
            
            // para cada exercise
            for (int i = 0; i < numberExercises; i++){
                split[i] += "}";
                json2 = new JSONObject(split[i]);
            
                String exerciseId = json2.get("exerciseID").toString();
                String promptingStrategy = json2.get("promptingStrategy").toString();
                String reinforcementStrategy = json2.get("reinforcementStrategy").toString();
                String timestampBeginExercise = json2.get("timestampBeginExercise").toString();
                String timestampEndExercise = json2.get("timestampEndExercise").toString();
                String numberOfDistractorHits = json2.get("numberOfDistractorHits").toString();
                String correct = json2.get("correct").toString();
                String skipped = json2.get("skipped").toString();
            
                content2 += sequenceId + "," + timestampBeginSequence + "," + 
                    timestampEndSequence + "," + childId + "," + exerciseId + "," + 
                    promptingStrategy + "," + reinforcementStrategy + "," + 
                    timestampBeginExercise + "," + timestampEndExercise + "," + 
                    numberOfDistractorHits + "," + correct + "," + skipped + "\n";          
            }
            
            System.out.println(loggedChild.getFirstName() + " " + loggedChild.getLastName());
            
            String pathAndroidSequences = loggedChild.getPathAndroidSequencesLog();
            System.out.println(loggedChild.getPathAndroidSequencesLog());
            adminLogs.writeToFile(pathAndroidSequences, content);
            
            String pathAndroidSequencesExercises = loggedChild.getPathAndroidSequencesExercisesLog();
            adminLogs.writeToFile(pathAndroidSequencesExercises, content2);
            
           
            return ok();
        
            
        } catch (JSONException ex) {
            return badRequest(sequenceLogsForm.errorsAsJson());
        }
    }
}