package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import models.Caregiver;
import models.Child;
import models.Sequence;
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
            } else {
                break;
            }
            i++;
        }
        
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
        
        Sequence sequence = new Sequence(sequenceName, exerciseIds, Caregiver.findByUsername(SecurityController.getUser().username));
        sequence.save();
        
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

        return ok(Json.toJson(sequence));
    }
    
    
     /*
     * EditSequence action
     */
    public Result editSequence(Long sequenceId){
        return ok("Ainda não está implementado");
    }
    
    /*
     * GetSequenceChildren action
     */
    public Result getSequenceChildren(Long sequenceId){
        
        Sequence sequence = Sequence.findById(sequenceId);
        List<Child> sequenceChildren = sequence.getSequenceChildren();
        
        return ok(Json.toJson(sequenceChildren));
    }
    
    /*
     * GetSequences action
     */
    public Result getSequences() {
        
        List<Sequence> findByAuthor = Sequence.findByAuthor(Caregiver.findByUsername(SecurityController.getUser().getUsername()));
        
        return ok(Json.toJson(findByAuthor));
    }
    
    /*
     * GetSequenceExercises action
     */
    public Result getSequence(Long sequenceId) {
        //TODO check if the loggedin caregiver can see this sequence
        Sequence sequence = Sequence.findById(sequenceId); 
        return  ok(Json.toJson(sequence));
    }
    
    public Result deleteSequence (Long sequenceId) {
        Sequence seq = Sequence.findById(sequenceId);
        Child.find.all().forEach((c) -> {
            c.getSequencesList().remove(seq);
            c.save();
        });
        seq.delete();
        
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