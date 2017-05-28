package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import play.mvc.*;
import java.util.UUID;
import javax.persistence.Entity;

@Entity
public class Session extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String authToken;
        
    public Session() {
        this.authToken = UUID.randomUUID().toString();
    }

    public static final Finder<Long, Session> find = new Finder<>(Session.class);
    
    public static Session findByAuthToken(String authToken) {
        if (authToken == null) {
                return null;
        }

        return find.where().eq("authToken", authToken).findUnique();
    }

    String getAuthToken() {
        return this.authToken;
    }

}