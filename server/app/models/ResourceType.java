package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class ResourceType extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long resourceTypeId;
	
	private String resourceTypeDescription;

	/**
	 * @return the resourceTypeId
	 */
	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	/**
	 * @param resourceTypeId the resourceTypeId to set
	 */
	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * @return the resourceTypeDescription
	 */
	public String getResourceTypeDescription() {
		return resourceTypeDescription;
	}

	/**
	 * @param resourceTypeDescription the resourceTypeDescription to set
	 */
	public void setResourceTypeDescription(String resourceTypeDescription) {
		this.resourceTypeDescription = resourceTypeDescription;
	}
	
}
