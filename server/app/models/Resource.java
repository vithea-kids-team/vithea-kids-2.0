package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.*;

import play.Logger;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.persistence.Column;

@Entity
public class Resource extends Model {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourcePath;

    @ManyToOne
    @JsonUnwrapped
    private ResourceType resourceType;

    @Column(nullable = false)
    private ResourceArea resourceArea;

    @ManyToOne
    @JsonIgnore
    private Caregiver owner;
    
    public Resource(Caregiver owner, String path, ResourceArea resourceArea) {
        this.owner = owner;
        this.resourceType = new ResourceType("img");
        this.resourcePath = path;
        this.resourceArea = resourceArea;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public ResourceArea getResourceArea() {
        return resourceArea;
    }

    public Caregiver getOwner() {
        return owner;
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
