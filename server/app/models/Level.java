package models;

import com.avaje.ebean.Model;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Level extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;
        
        @ManyToOne
        private Caregiver author;

        public Level(String levelDesc, Caregiver author) {
            this.description = levelDesc;
            this.author = author;
        }

	/**
	 * @return the levelId
	 */
	public Long getLevelId() {
		return id;
	}

	/**
	 * @param levelId the levelId to set
	 */
	public void setLevelId(Long levelId) {
		this.id = levelId;
	}

	/**
	 * @return the levelDescription
	 */
	public String getLevelDescription() {
		return description;
	}

	/**
	 * @param levelDescription the levelDescription to set
	 */
	public void setLevelDescription(String levelDescription) {
		this.description = levelDescription;
	}

	public static final Finder<Long, Level> find = new Finder<>(Level.class);

	public static List<Level> getAll() {
            return find.all();
        }
	
	public static Level findLevelById(Long levelId) {
            return find.where().eq("id", levelId).findUnique();
        }
        
        public static Level findLevelByDescription(String levelDescription) {
            return find.where().eq("description", levelDescription).findUnique();
        }
        
        public static List<Level> findByAuthor(Caregiver caregiver) {
            return find.where().eq("author_id", caregiver.getCaregiverId()).findList();
        }
	
}
