package com.l2f.vitheakids.model;

/**
 * Created by silvi on 20/04/2017.
 */

public class Topic {
    private Long topicId;
    private String topicDescription;

    public Long getId() {
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
}
