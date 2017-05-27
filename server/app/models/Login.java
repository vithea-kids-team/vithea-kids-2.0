package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import com.avaje.ebean.Model;
import java.util.List;
import java.util.Optional;
import play.Logger;

@Entity
public class Login extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
        @JsonIgnore
	public Long id;

	@Column(length = 255, unique = true, nullable = false)
	@Constraints.MaxLength(256)
	@Constraints.Required
	public String username;

	@Column(length = 64, nullable = false)
        @JsonIgnore
	private byte[] shaPassword;

	@Transient
	@Constraints.Required
	@Constraints.MinLength(6)
	@Constraints.MaxLength(256)
	@JsonIgnore
	private String password;

	@Column(nullable = false)
	private boolean enabled;

	@Column(nullable = false)
        @JsonIgnore
	private int userType;

	@Column(nullable = false, columnDefinition = "datetime")
	public Date createdUtc;
        
        @OneToMany(cascade = CascadeType.ALL)
        @JsonIgnore
	private List<Session> loginSessions;
        
        /* For binding */
        public Login() {}
        
	public Login(String username, String password) {
		setUsername(username);
		setPassword(password);
                this.createdUtc = new Date();
	}	

	public Long getLoginId() {
		return id;
	}

	public void setLoginId(Long loginId) {
		this.id = loginId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		shaPassword = getSha512(password);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public UserType getUserType() {
		return UserType.findById((long) this.userType);
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}
        
        public String addSession() {
            Session newSession = new Session();
            this.loginSessions.add(newSession);
            save();
            return newSession.getAuthToken();
        }
        
        
        public void removeSession(String token) {
            Optional<Session> session = this.loginSessions.stream()
                .filter(item -> item.getAuthToken().equals(token))
                .findFirst();
            
            if(session.isPresent()) {
                session.get().delete();
            }
        }
        
	public static final Finder<Long, Login> find = new Finder<>(Login.class);

	public static Login findByUsername(String username) {
            return find.where().eq("username", username).findUnique();
	}

	public static Login findByLoginId(Long loginId) {
            return find.where().eq("id", loginId).findUnique();
	}
        
        public static Login findByAuthToken(String authToken) {
            return find.fetch("loginSessions").where().eq("loginSessions.authToken", authToken).findUnique(); 
        }

	public static byte[] getSha512(String value) {
            try {
                return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
	}

	public static Login findByUsernameAndPassword(String username, String password) {
		return find.where().eq("username", username.toLowerCase()).eq("sha_password", getSha512(password)).findUnique();
	}
}