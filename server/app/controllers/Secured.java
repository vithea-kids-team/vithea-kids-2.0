package controllers;

import models.Login;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Implements basic authentication
 */
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        if (ctx.request().headers().containsKey(SecurityController.AUTH_TOKEN_HEADER)) {
            String token = ctx.request().headers().get(SecurityController.AUTH_TOKEN_HEADER)[0];
            if (token != null) {
                Login user = Login.findByAuthToken(token);
                
                if (user != null) {
                    ctx.args.put("user", user);
                    return user.getUsername();
                }
                
                Logger.debug("Could not find user with auth token " + token);
            }
        }
        return null;
    }

    @Override
        public Result onUnauthorized(Context ctx) {
        return unauthorized();
    }
}
