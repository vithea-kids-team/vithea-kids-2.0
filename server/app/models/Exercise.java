package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.Logger;



@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class Exercise extends Model {
	
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    
    @Column(insertable=false, updatable=false)
    public String dtype; //type to differ the kind of exercise
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Caregiver author;
    
   // @Column(nullable = false)
  //  private ExerciseType type;
    
    private Boolean defaultExercise;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @Column(nullable = true)
    private Topic topic;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @Column(nullable = true)
    private Level level;
    
    @OneToMany(mappedBy = "exercise")
    @JsonManagedReference
    private List<SequenceExercise> sequencesExercise = new ArrayList<SequenceExercise>();
    
    public Exercise(String name, Caregiver author, Boolean defaultExercise, Long topic, Long level) {
        this.name = name;
        this.author = author;
        this.defaultExercise = defaultExercise;
        
        if (topic != -1) {
            this.topic = Topic.findTopicById(topic);
        }
        
        if (level != -1) {
            this.level = Level.findLevelById(level);
        }
        
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
     
    public void setTopic(Long topicId) {
        Topic topic = Topic.findTopicById(topicId);
        if (topic == null) throw new NullPointerException("Topic does not exist");
        Logger.debug("New exercise :: setTopic: " + topic.getTopicDescription());
        this.topic = topic;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public void setLevel(Long levelId) {
        Level level = Level.findLevelById(levelId);
        if (level == null) throw new NullPointerException("Level does not exist");
        Logger.debug("New exercise :: setLevel: " + level.getLevelDescription());
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public Topic getTopic() {
        return topic;
    }

    public Level getLevel() {
        return level;
    }

    public List<SequenceExercise> getSequencesExercise() {
        return sequencesExercise;
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