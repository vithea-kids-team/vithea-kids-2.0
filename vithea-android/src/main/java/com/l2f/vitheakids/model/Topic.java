package com.l2f.vitheakids.model;

/**
 * Created by silvi on 20/04/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class Topic {

    private Long topicId;
    private String topicDescription;
    private  Boolean defaultTopic;

    public Long getTopicId() {
        return topicId;
    }
    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }
    public void setTopicId(Long id) {
        this.topicId = id;
    }

    public Boolean getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(Boolean defaultTopic) {
        this.defaultTopic = defaultTopic;
    }
}
