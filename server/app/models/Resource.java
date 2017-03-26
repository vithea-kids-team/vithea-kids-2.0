package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.*;

import play.Logger;

import com.avaje.ebean.Model;

import models.Caregiver;
import models.ResourceArea;

@Entity
public class Resource extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String resourcePath;

	@ManyToOne
	private ResourceType resourceType;

	@ManyToOne
	private ResourceArea resourceArea;

	@ManyToOne
	private Caregiver owner;

	public void setResourceArea(String resourceArea) {
		this.resourceArea = ResourceArea.findByDescription(resourceArea);
	}

	public void setResourceArea(ResourceArea resourceArea) {
		this.resourceArea = resourceArea;
	}

	public ResourceArea getResourceArea() {
		return resourceArea;
	}

	public void setOwner(Caregiver owner) {
		this.owner = owner;
	}

	public Caregiver getOwner() {
		return this.owner;
	}

	/**
	 * @return the resourceId
	 */
	public Long getResourceId() {
		return id;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(Long resourceId) {
		this.id = resourceId;
	}

	/**
	 * @return the resourcePath
	 */
	public String getResourcePath() {
		return resourcePath;
	}

	/**
	 * @param resourcePath the resourcePath to set
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	/**
	 * @return the resourceType
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public static final Finder<Long, Resource> find = new Finder<>(Resource.class);

	public static List<Resource> findByOwner(Caregiver owner) {
		Logger.debug("Looking for exercises from: " + owner.getCaregiverLogin().getUsername());
		return find
		.where()
		.eq("owner_id", owner.getCaregiverLogin().getLoginId())
		.findList();
	}

	public static Resource findById(Long resourceId) {
		return find
		.where()
		.eq("id", resourceId)
		.findUnique();
	}
}
