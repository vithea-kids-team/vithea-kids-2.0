package models;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

@Entity
public class Child extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long childId;
	
	private String firstName;
	
	private String lastName;
	
	private Date birthDate;
	
	private String gender;
	
	@OneToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="childlogin_id")
	private Login childLogin;
	
	public static final Finder<Long, Child> find = new Finder<>(Child.class);

	public static Child findByChildId(Long childId) {
        return find
	        .where()
	        .eq("child_id", childId)
	        .findUnique();
    }
	
	/**
	 * @return the childId
	 */
	public Long getChildId() {
		return childId;
	}

	/**
	 * @param childId the childId to set
	 */
	public void setChildId(Long childId) {
		this.childId = childId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}
	
	/**
	 * 
	 * @return the date in the format yyyy-mm-dd
	 */
	public String getBirthDateFormatted() {
		return birthDate.toString();
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @param childLogin the childLogin to set
	 */
	public void setChildLogin(Login childLogin) {
		this.childLogin = childLogin;
	}

	/**
	 * @return the childLogin
	 */
	public Login getChildLogin() {
		return childLogin;
	}	

	
}
