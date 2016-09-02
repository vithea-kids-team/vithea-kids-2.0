package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class ResourceArea extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long resourceAreaId;
	
	private String resourceAreaDescription;

	/**
	 * @return the resourceAreaId
	 */
	public Long getResourceAreaId() {
		return resourceAreaId;
	}

	/**
	 * @param resourceAreaId the resourceAreaId to set
	 */
	public void setResourceAreaId(Long resourceAreaId) {
		this.resourceAreaId = resourceAreaId;
	}

	/**
	 * @return the resourceAreaDescription
	 */
	public String getResourceAreaDescription() {
		return resourceAreaDescription;
	}

	/**
	 * @param resourceAreaDescription the resourceAreaDescription to set
	 */
	public void setResourceAreaDescription(String resourceAreaDescription) {
		this.resourceAreaDescription = resourceAreaDescription;
	}

	public static final Finder<Long, ResourceArea> find = new Finder<>(ResourceArea.class);

	public static ResourceArea findByDescription(String description) {
		return find
		.where()
		.eq("resource_area_description", description)
		.findUnique();
	}
	
}
