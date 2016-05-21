package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

import play.data.validation.Constraints;

import com.avaje.ebean.Model;

@Entity
public class Caregiver extends Model {
	
	@Id
	private Long caregiverId;
	
	private String userName;
	
	public byte[] password;
	
	private String firstName;
	
	private String lastName;
	
	@Column(length = 255, unique = true, nullable = false)
	@Constraints.MaxLength(255)
	@Constraints.Required
	@Constraints.Email
	public String email;

	private String gender;
	
	private boolean active;

	private List<ChildLogin> childList;

	
	/**
	 * @return the caregiverId
	 */
	public Long getCaregiverId() {
		return caregiverId;
	}

	/**
	 * @param caregiverId the caregiverId to set
	 */
	public void setCaregiverId(Long caregiverId) {
		this.caregiverId = caregiverId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Adds a child
	 * @param child
	 */
	public void addChild(ChildLogin child) {
		childList.add(child);
	}	

	public static final Finder<Long, Caregiver> find = new Finder<>(Caregiver.class);

	public static Caregiver findByEmail(String email) {
        return find
	        .where()
	        .eq("email", email.toLowerCase())
	        .findUnique();
    }

    public static Caregiver findByEmailAndPassword(String email, String password) {
	    return find
	        .where()
	        .eq("email", email.toLowerCase())
	        .eq("password", getSha512(password))
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

}