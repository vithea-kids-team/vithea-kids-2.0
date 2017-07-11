package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by silvi on 07/04/2017.
 */

public class PersonalMessage {
    @JsonProperty
    private long id;
    @JsonProperty
    private String message;
    @JsonProperty
    private String messageType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}

