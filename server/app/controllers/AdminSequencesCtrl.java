package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import static java.lang.Integer.parseInt;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import models.Caregiver;
import models.Child;
import models.Sequence;
import models.SequenceExercise;
import play.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.*;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

@Security.Authenticated(Secured.class)
public class AdminSequencesCtrl extends Controller {

    public AdminLogs adminLogs = new AdminLogs();

    
    @Inject
    FormFactory formFactory;

    @Transactional
    public Result registerSequence() {
        DynamicForm registerSequenceForm = formFactory.form().bindFromRequest();

        if (registerSequenceForm.hasErrors()) {
            return badRequest(registerSequenceForm.errorsAsJson());
        }
     
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        String sequenceName = registerSequenceForm.get("sequenceName");
        
        List<Long> exerciseIds = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        int i = 0;
        while(true) {
            String key = "exercisesToAdd[" + i + "]";
            if (registerSequenceForm.data().containsKey(key)) {
                int answerId;
                try {
                    answerId = parseInt(registerSequenceForm.data().get(key));
                } catch (NumberFormatException e) {
                    answerId = -1;
                }

                exerciseIds.add((long)answerId);
                int orderNumber = i+1;
                order.add(orderNumber);
            } else {
                break;
            }
            i++;
        }
        int exercises = exerciseIds.size();
        
        List<Long> childrenIds = new ArrayList<>();
        int j = 0;
        while(true) {
            String key = "childrenToAssign[" + j + "]";
            if (registerSequenceForm.data().containsKey(key)) {
                int childId;
                try {
                    childId = parseInt(registerSequenceForm.data().get(key));
                } catch (NumberFormatException e) {
                    childId = -1;
                }

                childrenIds.add((long)childId);
            } else {
                break;
            }
            j++;
        }
        int children = childrenIds.size();
        
        Sequence sequence = new Sequence(sequenceName, exerciseIds, order, childrenIds, Caregiver.findByUsername(SecurityController.getUser().username));
        sequence.save();
        sequence.setSequenceExercisesById(exerciseIds, order);
        sequence.setSequenceChildrensById(childrenIds);
        sequence.save();
        
        Logger.debug("Sequence:" + Json.toJson(sequence));
        
        childrenIds.forEach((id) -> {
            Child currentChild = Child.findByChildId((long)id);
            if (currentChild != null) {
                currentChild.getSequencesList().add(sequence);
                currentChild.save();
            } else Logger.debug("Child " + id + " does not exist!");
        });
        
        String childId = registerSequenceForm.get("childId");
        if(childId != null && !"".equals(childId)) {
            int child;
            try {
                child = parseInt(childId);
                Logger.debug("Adding sequence to child " + child);
                Child currentChild = Child.findByChildId((long)child);
                if (currentChild != null) {
                    currentChild.getSequencesList().add(sequence);
                    currentChild.save();
                } else Logger.debug("Child " + childId + " does not exist!");
            } catch (NumberFormatException e) {
                Logger.debug("ERROR: " + e);
            }
        }
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = sequence.getSequenceId()+ "," + loggedCaregiver.getCaregiverId() + "," + 
                timestamp.toLocalDateTime() + "," + "create" + "," + exercises + "," + children + "," + "\n";
        String pathSequence = loggedCaregiver.getPathSequencesLog();
        adminLogs.writeToFile(pathSequence, content);
        

        return ok(Json.toJson(sequence));
    }
    
     /*
     * EditSequence action
     */
    public Result editSequence(Long sequenceId){
        
        DynamicForm editSequenceForm = formFactory.form().bindFromRequest();

        if (editSequenceForm.hasErrors()) {
            return badRequest(editSequenceForm.errorsAsJson());
        }
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
               
        Sequence sequence = Sequence.findSequenceById(sequenceId);
        
        if (sequence == null) {
            return badRequest(buildJsonResponse("error", "Sequence doesn't exist"));
        } else {
            Logger.debug("Editing sequence with id " + sequenceId);
        
            String sequenceName = editSequenceForm.get("sequenceName");
            sequence.setSequenceName(sequenceName);
             
            List<Integer> order = new ArrayList<>();
            List<Long> exerciseIds = new ArrayList<>();
            int i = 0;
            while(true) {
                String key = "exercisesToAdd[" + i + "]";
                if (editSequenceForm.data().containsKey(key)) {
                    int answerId;
                    try {
                        answerId = parseInt(editSequenceForm.data().get(key));
                    } catch (NumberFormatException e) {
                        answerId = -1;
                    }

                    exerciseIds.add((long)answerId);
                    order.add(i+1);
                } else {
                    break;
                }
                i++;
            }
            sequence.setSequenceExercisesById(exerciseIds, order);
            int exercises = exerciseIds.size();
        
            List<Long> childrenIds = new ArrayList<>();
            int j = 0;
            while(true) {
                String key = "childrenToAssign[" + j + "]";
                if (editSequenceForm.data().containsKey(key)) {
                    int childId;
                    try {
                        childId = parseInt(editSequenceForm.data().get(key));
                    } catch (NumberFormatException e) {
                        childId = -1;
                    }

                    childrenIds.add((long)childId);
                } else {
                    break;
                }
                j++;
            }
            sequence.setSequenceChildrensById(childrenIds);
            int children = childrenIds.size();
        
            sequence.save();
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String content = sequence.getSequenceId()+ "," + loggedCaregiver.getCaregiverId() + "," + 
                timestamp.toLocalDateTime() + "," + "edit" + "," + exercises + "," + children + "\n";
            String pathSequence = loggedCaregiver.getPathSequencesLog();
            adminLogs.writeToFile(pathSequence, content);
        
            return ok(Json.toJson(sequence));
        }
    }

    /*
     * GetSequences action
     */
    public Result getSequences() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().getUsername());
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        Logger.debug(loggedCaregiver.getCaregiverLogin().getUsername() + " is logged in.");
        List<Sequence> sequences = Sequence.findByAuthor(loggedCaregiver);
        Logger.debug(sequences.size() + " sequences registered.");
        
        return ok(Json.toJson(sequences));
    }
    
    /*
     * GetSequence action
     */
    public Result getSequence(Long sequenceId) {
        //TODO check if the loggedin caregiver can see this sequence
        Sequence sequence = Sequence.findSequenceById(sequenceId);
        return ok(Json.toJson(sequence));
    }
    
    /*
     * DeleteSequence action
     */
    public Result deleteSequence (Long sequenceId) {
        
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        Sequence seq = Sequence.findSequenceById(sequenceId);
        Child.find.all().forEach((c) -> {
            c.getSequencesList().remove(seq);
            c.save();
        });
        
        for(SequenceExercise seqEx: seq.getSequenceExercisesList()) {
            seqEx.delete();
        }
        
        seq.getSequenceExercisesList().clear();
        
        seq.delete();
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String content = seq.getSequenceId()+ "," + loggedCaregiver.getCaregiverId() + "," + timestamp.toLocalDateTime() + "," + "delete\n";
        String pathSequence = loggedCaregiver.getPathSequencesLog();
        adminLogs.writeToFile(pathSequence, content);
                        
        
        return ok("Sequence deleted");
    }

    private static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}