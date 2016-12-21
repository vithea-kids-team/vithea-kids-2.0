package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import com.avaje.ebean.Model;

@Entity
public class Login extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long loginId;

	private String authToken;

	@Column(length = 256, unique = true, nullable = false)
	@Constraints.MaxLength(256)
	@Constraints.Required
	public String username;

	@Column(length = 64, nullable = false)
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

	@Column(nullable = false)
	public Date createdUtc;
        
        /* For binding */
        public Login() {}
        
	public Login(String username, String password) {
		setUsername(username);
		setPassword(password);
                this.createdUtc = new Date();
	}	

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
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

	public String createToken() {
		authToken = UUID.randomUUID().toString();
		save();
		return authToken;
	}

	public void deleteAuthToken() {
		authToken = null;
		save();
	}

	public static final Finder<Long, Login> find = new Finder<>(Login.class);

	public static Login findByUsername(String username) {
		return find.where().eq("username", username).findUnique();
	}

	public static Login findByLoginId(Long loginId) {
		return find.where().eq("login_id", loginId).findUnique();
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

	public static Login findByAuthToken(String authToken) {
		if (authToken == null) {
			return null;
		}
		
		return find.where().eq("authToken", authToken).findUnique();
	}

}