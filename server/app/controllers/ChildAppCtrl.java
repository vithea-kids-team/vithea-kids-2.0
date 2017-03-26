package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Child;
import models.Login;
import play.libs.Json;
import play.mvc.*;
import play.Logger;

public class ChildAppCtrl extends Controller {
    
    public Result getChildApp() {
        Login user = SecurityController.getUser();
        if (user == null) {
            return badRequest(buildJsonResponse("error", "User does not exist"));
        }
        Child loggedChild = Child.findByUsername(user.getUsername());
        if (loggedChild == null) {
            return badRequest(buildJsonResponse("error", "Child does not exist."));
        }
        Logger.debug(loggedChild.getChildLogin().getUsername() + " is logged in.");
        return ok(Json.toJson(loggedChild));
    }

    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}
