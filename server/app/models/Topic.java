package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@Entity
public class Topic extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long topicId;
	
	private String topicDescription;

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
	
	
}
