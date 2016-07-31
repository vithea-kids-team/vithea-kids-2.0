package app;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class Utils {

    public static ObjectNode buildJsonResponse(String type, String message) {
	  ObjectNode wrapper = Json.newObject();
	  ObjectNode msg = Json.newObject();
	  msg.put("message", message);
	  wrapper.set(type, msg);
	  return wrapper;
	}
}