package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Child;
import models.Login;
import play.Logger;
import play.libs.Json;
import play.mvc.*;

@Security.Authenticated(Secured.class)
public class ChildAppCtrl extends Controller {
    
    public Result getChildApp() {
        Logger.debug("Hit ChildAppCtrl.getChildApp method");
        Login user = SecurityController.getUser();
        if (user == null) {
            return badRequest(buildJsonResponse("error", "User does not exist"));
        }
        Logger.debug("User exists.");
        Child loggedChild = Child.findByUsername(user.getUsername());

        if (loggedChild == null) {
            return badRequest(buildJsonResponse("error", "Child does not exist."));
        }
        Logger.debug(loggedChild.getChildLogin().getUsername() + " is logged in.");
        
        return ok(Json.toJson(loggedChild));
    }

    public Result getChildSequencesApp() {
        Logger.debug("Hit ChildAppCtrl.getChildSequencesApp method");
        Login user = SecurityController.getUser();
        if (user == null) {
            return badRequest(buildJsonResponse("error", "User does not exist"));
        }
        Logger.debug("User exists.");
        Child loggedChild = Child.findByUsername(user.getUsername());

        if (loggedChild == null) {
            return badRequest(buildJsonResponse("error", "Child does not exist."));
        }
        Logger.debug(loggedChild.getChildLogin().getUsername() + " is logged in.");

        System.out.println(Json.stringify(Json.toJson(loggedChild.toString1())));
        return ok(Json.toJson(loggedChild.toString1()));
    }
    
    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}
