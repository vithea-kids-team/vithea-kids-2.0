package models;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;

import play.Logger;

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

	public static Child findByUsername(String username) {
		Logger.debug("Looking for child with username: " + username);
		return find
				.where()
				.eq("childlogin_id", Login.findByUsername(username).getLoginId())
				.findUnique();
	}
	
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

	public void setBirthDate(String birthDate) {
		
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        
		try {
			java.util.Date utilDate = sdf.parse(birthDate);
			Logger.debug(utilDate + "");
			this.birthDate =  new java.sql.Date(utilDate.getTime());
			Logger.debug(this.birthDate + "");
		}
		catch (ParseException e){
			e.printStackTrace();
		}
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
