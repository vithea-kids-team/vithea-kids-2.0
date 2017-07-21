package com.l2f.vitheakids.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by silvi on 07/04/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class PersonalMessage {

    @JsonProperty private long id;
    @JsonProperty private String message;
    @JsonProperty private String messageType;

    public long getId() {
        return id;
    }
    public String getMessage() {
        return message;
    }
    public String getMessageType() {
        return messageType;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

}

