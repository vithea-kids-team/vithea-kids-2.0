package models;

import com.avaje.ebean.Model;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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