package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class ChildLogin extends Model {

	@Id
	private Long childId;
	
	private String childUserName;
	
	private String password;
	
	private boolean enabled;	
	
	private List<Caregiver> caregivers;
		
	private List<Sequence> sequenceList;

	public Long getChildId() {
		return childId;
	}

	public void setChildId(Long childId) {
		this.childId = childId;
	}

	public String getChildUserName() {
		return childUserName;
	}

	public void setChildUserName(String childUserName) {
		this.childUserName = childUserName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	 * @return the caregivers
	 */
	public List<Caregiver> getCaregivers() {
		return caregivers;
	}

	/**
	 * @param caregivers the caregivers to set
	 */
	public void setCaregivers(List<Caregiver> caregivers) {
		this.caregivers = caregivers;
	}

	/**
	 * @return the sequenceList
	 */
	public List<Sequence> getSequenceList() {
		return sequenceList;
	}

	/**
	 * @param sequenceList the sequenceList to set
	 */
	public void setSequenceList(List<Sequence> sequenceList) {
		this.sequenceList = sequenceList;
	}
	
	/**
	 * @param sequence the sequence to add to sequenceList
	 */
	public void addSequence(Sequence sequence) {
		this.sequenceList.add(sequence);
	}
	
	public static final Finder<Long, ChildLogin> find = new Finder<>(ChildLogin.class);

	public static ChildLogin findByUsername(String username) {
        return find
	        .where()
	        .eq("childUsername", username.toLowerCase())
	        .findUnique();
    }

}