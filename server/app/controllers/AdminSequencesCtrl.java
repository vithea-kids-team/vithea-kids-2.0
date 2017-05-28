package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import static java.lang.Integer.parseInt;
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
        Sequence sequence = new Sequence(sequenceName);
        
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
     * GetSequences action
     */
    
    public Result getSequences() {
        return ok(Json.toJson(Sequence.getAll()));
    }
    
    /*
     * GetSequenceExercises action
     */
    
    public Result getSequence(Long sequenceId) {
        Caregiver loggedCaregiver = Caregiver.findByUsername(SecurityController.getUser().username);
        if (loggedCaregiver == null) {
            return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        }
        
        List<Child> children = loggedCaregiver.getChildList();
        Sequence sequence = Sequence.findById(sequenceId);
        
        boolean found = false;
        for(Child child : children) {
            if (child.getSequencesList().contains(sequence)) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            return badRequest(buildJsonResponse("error", "Invalid sequence ID."));
        }
        
        return  ok(Json.toJson(sequence));
    }
    
    public Result deleteSequence (Long sequenceId) {
        Sequence seq = Sequence.findById(sequenceId);
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