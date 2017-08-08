package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import play.Logger;

@Entity
public class Sequence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "sequence_exercise")
    private List<Exercise> exerciseList;
    
    @JsonIgnore
    //@ManyToMany(mappedBy = "sequencesList")
    //@LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Child> childrenList;

    
    @ManyToOne
    private Caregiver author;

    public static final Finder<Long, Sequence> find = new Finder<>(Sequence.class);
    
    public Sequence(String name, List<Long> exerciseIds, Caregiver author) {
        this.name = name;
        this.author = author;
        
        exerciseIds.stream().map((d) -> Exercise.findExerciseById(d)).forEachOrdered((ex) -> {
            exerciseList.add(ex);
        });
    }
    
    public Caregiver getAuthorCaregiver(){
        return this.author;
    }
    
    public void setAuthorCaregiver(Caregiver author){
        this.author = author;
    }

    public Long getSequenceId() {
        return this.id;
    }

    public void setSequenceId(Long sequenceId) {
        this.id = sequenceId;
    }

    public String getSequenceName() {
        return this.name;
    }

    public void setSequenceName(String name) {
        this.name = name;
    }

    public List<Exercise> getSequenceExercises() {
        return this.exerciseList;
    }

    public void setSequenceExercises(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }
    
     public List<Child> getSequenceChildren() {
        return this.childrenList;
    }

    public void setSequenceChildren(List<Child> childrenList) {
        this.childrenList = childrenList;
    }
    
    public static List<Sequence> getAll() {
        return find.all();
    }
    
    public static Sequence findById(Long id) {
        Logger.debug("Looking for sequence with id: " + id);
        return find
        .where()
        .eq("id", id)
        .findUnique();
    }
    
    public static List<Sequence> findByAuthor(Caregiver caregiver) {
        return find.where().eq("author_id", caregiver.getCaregiverId()).findList();
    }
}
