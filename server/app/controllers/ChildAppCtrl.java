package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import models.Sequence;
import play.libs.Json;
import play.mvc.*;

public class ChildAppCtrl extends Controller {
    
    public Result getSequences() {
        List<Sequence> sequences = Sequence.find.all();
        return ok(Json.toJson(sequences));
    }
    
    public Result getGreetingMessage() {
        return ok(Json.toJson("Bom dia!"));
    }

    public static ObjectNode buildJsonResponse(String type, String message) {
        ObjectNode wrapper = Json.newObject();
        ObjectNode msg = Json.newObject();
        msg.put("message", message);
        wrapper.set(type, msg);
        return wrapper;
    }
}
