package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.inject.Key;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import play.Logger;

/**
 *
 * @author Claudia
 */
@Entity
public class PersonalMessage extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    @Column(nullable = false)
    private PersonalMessageType messageType;

    public PersonalMessage(String message, PersonalMessageType personalMessageType) {
        this.message = message;
        this.messageType = personalMessageType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PersonalMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(PersonalMessageType messageType) {
        this.messageType = messageType;
    }

    public static final Finder<Long, PersonalMessage> find = new Finder<>(PersonalMessage.class);

    public static PersonalMessage findByType(Long typeId) {
        return find
                .where()
                .eq("messagetype_id", typeId)
                .findUnique();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonalMessage)) {
            return false;
        }
        PersonalMessage other = (PersonalMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersonalMessage{" + "id=" + id + ", message=" + message + ", messageType=" + messageType + '}';
    }

}
