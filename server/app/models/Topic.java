package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import java.util.List;

@Entity
public class Topic extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String description;

	public Topic(String topicDescription) {
            this.description = topicDescription;
	}

	/**
	 * @return the topicId
	 */
	public Long getTopicId() {
            return id;
	}

	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(Long topicId) {
            this.id = topicId;
	}

	/**
	 * @return the topicDescription
	 */
	public String getTopicDescription() {
            return description;
	}

	/**
	 * @param topicDescription the topicDescription to set
	 */
	public void setTopicDescription(String topicDescription) {
            this.description = topicDescription;
	}
	
	public static final Finder<Long, Topic> find = new Finder<>(Topic.class);

	public static List<Topic> getAll() {
        return find.all();
    }

	public static Topic findTopicById(Long topicId) {
        return find
		.where()
		.eq("id", topicId)
		.findUnique();
    }
	
	
}
