package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.avaje.ebean.Model;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import play.Logger;

@Entity
public class Child extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childId;

    private String firstName;

    private String lastName;

    private Date birthDate;

    private String gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "childlogin_id")
    private Login childLogin;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Sequence> sequencesList;

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
                .eq("child_id", childId)
                .findUnique();
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {

        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        TemporalAccessor accessor = timeFormatter.parse(birthDate);
        this.birthDate = Date.from(Instant.from(accessor));

    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setChildLogin(Login childLogin) {
        this.childLogin = childLogin;
    }

    public Login getChildLogin() {
        return childLogin;
    }

    public List<Sequence> getSequencesList() {
        return sequencesList;
    }

    public void setSequencesList(List<Sequence> sequencesList) {
        this.sequencesList = sequencesList;
    }

}
