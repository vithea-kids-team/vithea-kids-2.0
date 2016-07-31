package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.List;

import com.avaje.ebean.Model;

@Entity
public class Level extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long levelId;
	
	private String levelDescription;

	/**
	 * @return the levelId
	 */
	public Long getLevelId() {
		return levelId;
	}

	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

	/**
	 * @return the levelDescription
	 */
	public String getLevelDescription() {
		return levelDescription;
	}

	/**
	 * @param levelDescription the levelDescription to set
	 */
	public void setLevelDescription(String levelDescription) {
		this.levelDescription = levelDescription;
	}

	public static final Finder<Long, Level> find = new Finder<>(Level.class);

	public static List<Level> getAll() {
        return find		
		//.orderBy("level_description desc")
		.all();
    }
	
	public static Level findLevelById(Long levelId) {
        return find
		.where()
		.eq("level_id", levelId)
		.findUnique();
    }
	
}
