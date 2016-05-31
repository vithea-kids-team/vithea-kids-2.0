package models;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;

import play.data.validation.Constraints;

import com.avaje.ebean.Model;

@Entity
public class Caregiver extends Model {
	
	@Id
	private Long caregiverId;
	
	private String firstName;
	
	private String lastName;
	
	@Column(length = 255, unique = true, nullable = false)
	@Constraints.MaxLength(255)
	@Constraints.Required
	@Constraints.Email
	public String email;

	private String gender;
	
	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="caregiverlogin_id")
	private Login caregiverLogin;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "CaregiverChild")
	private List<Login> childList;

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
	 * @return the caregiverLogin
	 */
	public Login getCaregiverLogin() {
		return caregiverLogin;
	}

	/**
	 * @param caregiverLogin the caregiverLogin to set
	 */
	public void setCaregiverLogin(Login caregiverLogin) {
		this.caregiverLogin = caregiverLogin;
	}

	/**
	 * @return the childList
	 */
	public List<Login> getChildList() {
		return childList;
	}

	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<Login> childList) {
		this.childList = childList;
	}

	/**
	 * Adds a child
	 * @param child
	 */
	public void addChild(Login child) {
		childList.add(child);
	}	

	public static final Finder<Long, Caregiver> find = new Finder<>(Caregiver.class);

	public static Caregiver findByEmail(String email) {
        return find
	        .where()
	        .eq("email", email.toLowerCase())
	        .findUnique();
    }
}