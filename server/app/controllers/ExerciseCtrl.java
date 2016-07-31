package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.libs.Json;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;

import javax.inject.Inject;

import java.util.*;

import models.Topic;
import models.Level;
import models.Exercise;
import models.Caregiver;
import models.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;


public class ExerciseCtrl extends Controller {

    
    /*
	 * EXERCISE FORM
	 */

	public static class RegisterTextOnlyExercise {
		
		public Long level;

		public Long topic;

		public String question;

		public String answer;

        public List<String> distractors;

        public Long stimulus;
	}

	/**
	 * RegisterExercise action
	 */
	@Inject
	FormFactory formFactory;

    public Result registerExercise() {
        Form<RegisterTextOnlyExercise> registerExerciseForm = formFactory.form(RegisterTextOnlyExercise.class).bindFromRequest();

		if (registerExerciseForm.hasErrors()) {
			return badRequest(registerExerciseForm.errorsAsJson());
		}
		RegisterTextOnlyExercise newExercise = registerExerciseForm.get();

        Exercise exercise = new Exercise();
        exercise.setTopic(newExercise.topic);
        exercise.setLevel(newExercise.level);
        exercise.setQuestion(newExercise.question, newExercise.stimulus);
        exercise.setRightAnswer(newExercise.answer);
        exercise.setAnswers(newExercise.distractors);
        Caregiver loggedCaregiver = Caregiver.findByUsername(session("username"));
        if (loggedCaregiver == null)
			return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
        exercise.setAuthor(loggedCaregiver);

        exercise.save();

        return ok(Json.toJson(exercise));
    }

    public Result editExercise(long exerciseId) {
        return ok("yey");
    }

    public Result deleteExercise(long exerciseId) {
        return ok("yey");
    }

    public Result getExercises() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(session("username"));
        if (loggedCaregiver == null)
			return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
		Logger.debug(loggedCaregiver.getCaregiverLogin().getUserName() + " is logged in.");
        List<Exercise> exercises = Exercise.findByAuthor(loggedCaregiver);
		Logger.debug(exercises.size() + " exercises registered.");
		return ok(Json.toJson(exercises));
    }

    public Result getTopics() {
        return ok(Json.toJson(Topic.getAll()));
    }

    public Result getLevels() {
        return ok(Json.toJson(Level.getAll()));
    }

    public Result getResources() {
        Caregiver loggedCaregiver = Caregiver.findByUsername(session("username"));
        if (loggedCaregiver == null)
			return badRequest(buildJsonResponse("error", "Caregiver does not exist."));
		Logger.debug(loggedCaregiver.getCaregiverLogin().getUserName() + " is logged in.");
        return ok(Json.toJson(Resource.findByOwner(loggedCaregiver)));
    }

    public static ObjectNode buildJsonResponse(String type, String message) {
	  ObjectNode wrapper = Json.newObject();
	  ObjectNode msg = Json.newObject();
	  msg.put("message", message);
	  wrapper.set(type, msg);
	  return wrapper;
	}
}