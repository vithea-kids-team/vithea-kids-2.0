package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.List;
import javax.persistence.OneToOne;


@Entity
public class AnimatedCharacter extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @OneToOne
    @JsonUnwrapped
    private Resource avatar;

    public AnimatedCharacter(String path, String name) {
        Resource avatar = new Resource(path, ResourceArea.ANIMATEDCHARACTER);
        avatar.save();
        
        this.avatar = avatar;
        this.name = name;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }   

    public Resource getAvatar() {
        return avatar;
    }

    public void setAvatar(Resource avatar) {
        this.avatar = avatar;
    }
    
    public static final Finder<Long, AnimatedCharacter> find = new Finder<>(AnimatedCharacter.class);

    public static List<AnimatedCharacter> findAll() {
        return find.all();
    }
    
    public static AnimatedCharacter findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }
}