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
	private Long topicId;
	
	private String topicDescription;

	public Topic(String topicDescription) {
		this.topicDescription = topicDescription;
	}

	/**
	 * @return the topicId
	 */
	public Long getTopicId() {
		return topicId;
	}

	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	/**
	 * @return the topicDescription
	 */
	public String getTopicDescription() {
		return topicDescription;
	}

	/**
	 * @param topicDescription the topicDescription to set
	 */
	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}
	
	public static final Finder<Long, Topic> find = new Finder<>(Topic.class);

	public static List<Topic> getAll() {
        return find		
		//.orderBy("topic_description desc")
		.all();
    }

	public static Topic findTopicById(Long topicId) {
        return find
		.where()
		.eq("topic_id", topicId)
		.findUnique();
    }
	
	
}
