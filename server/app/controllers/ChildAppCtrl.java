package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import models.Child;
import models.Exercise;
import models.Sequence;
import play.*;
import play.libs.Json;
import play.mvc.*;

//@Security.Authenticated(Secured.class)
public class ChildAppCtrl extends Controller {

    public Result getSequences() {
        Child loggedChild = Child.findByUsername("child");//SecurityController.getUser().getUsername());
        if (loggedChild == null) {
            return badRequest(buildJsonResponse("error", "Child does not exist."));
        }
        //Logger.debug(loggedChild.getChildLogin().getUsername() + " is logged in.");
        List<Sequence> sequences = loggedChild.getSequencesList();
        //Logger.debug(sequences.size() + " sequences registered.");
        return ok(Json.toJson(sequences));

    }

    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}
