package models;

import com.avaje.ebean.Model.Finder;
import javax.inject.Inject;

public enum UserType{

	CAREGIVER(0), 
	CHILD(1);
	
	private final Long id;
           
        @Inject
	UserType(long id) {
		this.id = id;
	}
	
	public static Finder<Long, UserType> find = new Finder<>(UserType.class);
	
	public static UserType findById(Long id) {
		return find.where().eq("id", id).findUnique();
	}
}
