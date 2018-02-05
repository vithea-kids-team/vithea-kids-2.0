package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import play.Logger;



@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Exercise extends Model {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(insertable=false, updatable=false)  //type to differenciate the kind of exercise
    public String dtype;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Caregiver author;
    
   // @Column(nullable = false)
  //  private ExerciseType type;
    
    private Boolean defaultExercise;
    
    @OneToMany(mappedBy = "exercise")
    @JsonManagedReference
    private List<SequenceExercise> sequencesExercise = new ArrayList<SequenceExercise>();
    
    public Exercise(String name, Caregiver author, Boolean defaultExercise) {
        this.name = name;
        this.author = author;
        this.defaultExercise = defaultExercise;
    }

    public Boolean getDefaultExercise() {
        return defaultExercise;
    }

    public void setDefaultExercise(Boolean defaultExercise) {
        this.defaultExercise = defaultExercise;
    }

    public void addSequenceExercise(SequenceExercise se) {
        this.sequencesExercise.add(se);
    }

    public List<SequenceExercise> getSequenceExercise() {
        return this.sequencesExercise;
    }

    public void setSequenceExercise(List<SequenceExercise> sequencesExercise) {
        this.sequencesExercise = sequencesExercise;
    }
    
    public String getExerciseName(){
        return name;
    }
    
    public void setExerciseId(Long exerciseId) {
        this.id = exerciseId;
    }
    public void setExerciseName(String name){
        this.name = name;
    }
    
    public Long getExerciseId() {
        return id;
    }
     public void setAuthor(Caregiver author) {
            this.author = author;
    }

    public static final Finder<Long, Exercise> find = new Finder<>(Exercise.class);
    
    public static List<Exercise> findByAuthor(Caregiver author) {
            Logger.debug("Looking for exercises from: " + author.getCaregiverLogin().getUsername());
            return find.where().eq("author_id", author.getCaregiverId()).findList();
    }
    
    public static Exercise findExerciseById(Long id) {
            Logger.debug("Looking for exercise " + id);
            return find.where().eq("id", id).findUnique();
    }
}