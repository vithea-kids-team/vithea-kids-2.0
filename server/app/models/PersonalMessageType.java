package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Claudia
 */
@Entity
public class PersonalMessageType extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public static final Finder<Long, PersonalMessageType> find = new Finder<>(PersonalMessageType.class);
    
     public static PersonalMessageType find(String type) {
        return find
                .where()
                .eq("type", type)
                .findUnique();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonalMessageType)) {
            return false;
        }
        PersonalMessageType other = (PersonalMessageType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PersonalMessageType{" + "id=" + id + ", type=" + type + '}';
    }


    
}
