package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class Login extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loginId;
	
	private String userName;
	
	private byte[] password;
	
	private boolean enabled;
	
	//0 - Caregiver
	//1 - Child
	private int userType; 

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long id) {
		this.loginId = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public byte[] getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = getSha512(password);
	}


	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return the userType
	 */
	public int getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(int userType) {
		this.userType = userType;
	}

	public static final Finder<Long, Login> find = new Finder<>(Login.class);

	public static Login findByUsername(String username) {
        return find
	        .where()
	        .eq("user_name", username)
	        .findUnique();
    }
	
	public static Login findByLoginId(Long loginId) {
        return find
	        .where()
	        .eq("login_id", loginId)
	        .findUnique();
    }
	
	 public static byte[] getSha512(String value) {
		    try {
		      return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
		    }
		    catch (NoSuchAlgorithmException e) {
		      throw new RuntimeException(e);
		    }
		    catch (UnsupportedEncodingException e) {
		      throw new RuntimeException(e);
		    }
	 }
	 
	 public static Login findByUsernameAndPassword(String username, String password) {
		    return find
		        .where()
		        .eq("user_name", username.toLowerCase())
		        .eq("password", getSha512(password))
		        .findUnique();
	}
}