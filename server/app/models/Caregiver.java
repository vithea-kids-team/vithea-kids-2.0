package models;

import com.avaje.ebean.Model;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import play.data.validation.Constraints;

@Entity
public class Caregiver extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(length = 255, unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    public String email;

    private String gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "caregiverlogin_id")
    private Login caregiverLogin;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "caregiver_child")
    private List<Child> childList;
    
    private String pathExercisesLog;
    private String pathSequencesLog;
    private String pathResourcesLog;
    private String pathTopicsLog;
    private String pathLevelsLog;
    private String pathPreferencesLog;
    private String pathChildrenLog;


    /**
     * @return the caregiverId
     */
    public Long getCaregiverId() {
        return id;
    }

    /**
     * @param caregiverId the caregiverId to set
     */
    public void setCaregiverId(Long caregiverId) {
        this.id = caregiverId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the caregiverLogin
     */
    public Login getCaregiverLogin() {
        return caregiverLogin;
    }

    /**
     * @param caregiverLogin the caregiverLogin to set
     */
    public void setCaregiverLogin(Login caregiverLogin) {
        this.caregiverLogin = caregiverLogin;
    }

    /**
     * @return the childList
     */
    public List<Child> getChildList() {
        return childList;
    }

    /**
     * @param childList the childList to set
     */
    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    /**
     * Adds a child
     *
     * @param child
     */
    public void addChild(Child child) {
        childList.add(child);
    }

    /**
     * Removes a child
     *
     * @param child
     */
    public void removeChild(Child child) {
        childList.remove(child);
    }
    
    
    public String getPathExercisesLog() {
        return pathExercisesLog;
    }

    public void setPathExercisesLog(String pathExercisesLog) {
        this.pathExercisesLog = pathExercisesLog;
    }

    public String getPathSequencesLog() {
        return pathSequencesLog;
    }

    public void setPathSequencesLog(String pathSequencesLog) {
        this.pathSequencesLog = pathSequencesLog;
    }

    public String getPathResourcesLog() {
        return pathResourcesLog;
    }

    public void setPathResourcesLog(String pathResourcesLog) {
        this.pathResourcesLog = pathResourcesLog;
    }

    public String getPathTopicsLog() {
        return pathTopicsLog;
    }

    public void setPathTopicsLog(String pathTopicsLog) {
        this.pathTopicsLog = pathTopicsLog;
    }

    public String getPathLevelsLog() {
        return pathLevelsLog;
    }

    public void setPathLevelsLog(String pathLevelsLog) {
        this.pathLevelsLog = pathLevelsLog;
    }

    public String getPathPreferencesLog() {
        return pathPreferencesLog;
    }

    public void setPathPreferencesLog(String pathPreferencesLog) {
        this.pathPreferencesLog = pathPreferencesLog;
    }
    
    public String getPathChildrenLog() {
        return pathChildrenLog;
    }

    public void setPathChildrenLog(String pathChildrenLog) {
        this.pathChildrenLog = pathChildrenLog;
    }

    public static final Finder<Long, Caregiver> find = new Finder<>(Caregiver.class);

    public static Caregiver findByEmail(String email) {
        return find.where().eq("email", email.toLowerCase()).findUnique();
    }

    public static Caregiver findByUsername(String username) {
        Login l = Login.findByUsername(username);
        return l != null ? find.where().eq("caregiverlogin_id", l.getLoginId()).findUnique() : null;
    }
}
