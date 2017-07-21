package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import play.Logger;

@Entity
public class Child extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;

    @Column(columnDefinition = "datetime")
    private Date birthDate;

    private String gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "childlogin_id")
    @JsonUnwrapped
    private Login childLogin;

    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Sequence> sequencesList;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<PersonalMessage> personalMessagesList;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private AnimatedCharacter animatedCharacter;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Reinforcement reinforcement;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Prompting prompting;
    
    private Boolean emotions;

    
    public AnimatedCharacter getAnimatedCharacter() {
        return animatedCharacter;
    }
    public Long getChildId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public String getGender() {
        return gender;
    }
    public Login getChildLogin() {
        return childLogin;
    }
    public List<Sequence> getSequencesList() {
        return sequencesList;
    }
    public List<PersonalMessage> getPersonalMessagesList() {
        return personalMessagesList;
    }
    public Reinforcement getReinforcement() {
        return reinforcement;
    }
    public Prompting getPrompting() {
        return prompting;
    }
    public Boolean getEmotions() {
        return emotions;
    }

    public void setAnimatedCharacter(AnimatedCharacter animatedCharacter) {
        this.animatedCharacter = animatedCharacter;
    }
    public void setChildId(Long childId) {
        this.id = childId;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setBirthDate(String birthDate) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        TemporalAccessor accessor = timeFormatter.parse(birthDate);
        this.birthDate = Date.from(Instant.from(accessor));
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setChildLogin(Login childLogin) {
        this.childLogin = childLogin;
    }
    public void setSequencesList(List<Sequence> sequencesList) {
        this.sequencesList = sequencesList;
    }
    public void setPersonalMessagesList(List<PersonalMessage> personalMessagesList) {
        this.personalMessagesList = personalMessagesList;
    }
    public void setReinforcement(Reinforcement reinforcement) {
        this.reinforcement = reinforcement;
    }
    public void setPrompting(Prompting prompting) {
        this.prompting = prompting;
    }
    public void setEmotions(Boolean emotions) {
        this.emotions = emotions;
    }
    
    public static final Finder<Long, Child> find = new Finder<>(Child.class);
    public static Child findByUsername(String username) {
        Logger.debug("Looking for child with username: " + username);
        return find
                .where()
                .eq("childlogin_id", Login.findByUsername(username).getLoginId())
                .findUnique();
    }
    public static Child findByChildId(Long childId) {
        return find
                .where()
                .eq("id", childId)
                .findUnique();
    }
    
    @Override
    public String toString() {
        return "Child{" + "childId=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate + ", gender=" + gender + ", childLogin=" + childLogin + ", sequencesList=" + sequencesList + ", personalMessagesList=" + personalMessagesList + '}';
    }
}
