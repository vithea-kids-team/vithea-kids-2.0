package controllers;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Caregiver;

import models.Login;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;

public class SecurityController extends Controller {

    @Inject
    FormFactory formFactory;

    public final static String AUTH_TOKEN_HEADER = "Authorization";
    public static final String AUTH_TOKEN = "authToken";

    public static Login getUser() {
        return (Login) Http.Context.current().args.get("user");
    }

    // returns an authToken
    public Result login() {
        Logger.debug("Hit SecurityController.Login method");
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            Logger.debug("\t Login form has errors, returning badRequest");
            return badRequest(loginForm.errorsAsJson());
        }

        Login login = loginForm.get();
        Logger.debug("\t Username:" + login.getUsername());
        Logger.debug("\t Password:" + login.getPassword());
        Login user = Login.findByUsernameAndPassword(login.getUsername(), login.getPassword());

        if (user == null) {
            Logger.debug("\t \t Invalid user, returning unauthorized");
            return unauthorized();
        } else {
            String authToken = user.addSession();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response()
                    .setCookie(Http.Cookie.builder(AUTH_TOKEN, authToken).withSecure(ctx().request().secure()).build());

            Logger.debug("\t \t Returning authentication token.");
            return ok(authTokenJson);
        }
    }

    @Security.Authenticated(Secured.class)
    public Result logout() {
        Logger.debug("Hit SecurityController.Logout method");
        Logger.debug("\t Deleting tokens...");
        response().discardCookie(AUTH_TOKEN);
        String token = Http.Context.current().request().headers().get(AUTH_TOKEN_HEADER)[0];
        getUser().removeSession(token);
        Logger.debug("\t Redirecting...");
        return redirect("/");
    }

    public Result signup() {
        Logger.debug("Hit SecurityController.Signup method");
        Logger.debug("\t Binding data from request");
        DynamicForm signUpForm = formFactory.form().bindFromRequest();
        
        if (signUpForm.hasErrors()) {
            Logger.debug("\t \t Form has errors, returning");
            return badRequest(signUpForm.errorsAsJson());
        }

        Caregiver existingUser = Caregiver.findByEmail(signUpForm.get("email"));
        if (existingUser != null) {
            Logger.debug("\t \t User already exists, returning bad request");
            return badRequest("User already exists");
        } else {
            Logger.debug("\t \t Creating user login...");
            Login userlogin = new Login(signUpForm.get("username"), signUpForm.get("password"));
            userlogin.setEnabled(true);
            userlogin.setUserType(0);
            userlogin.save();
            Logger.debug("\t \t Creating caregiver...");
            Caregiver user = new Caregiver();
            user.setCaregiverLogin(userlogin);
            user.setEmail(signUpForm.get("email"));
            user.setFirstName(signUpForm.get("firstname"));
            user.setLastName(signUpForm.get("lastname"));
            user.setGender(signUpForm.get("gender"));
            user.save();
            Logger.debug("\t \t \t Returning ok");
            return ok("User created successfully");
        }
    }

}