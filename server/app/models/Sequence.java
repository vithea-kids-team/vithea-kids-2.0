package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.Logger;

@Entity
public class Sequence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    //@OneToMany( targetEntity = SequenceExercise.class, mappedBy = "sequence", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //@JoinColumn( name="sequence_id",nullable=true )
    //@ManyToMany(cascade = CascadeType.ALL)
    //@JoinTable(name = "sequence_exercise")
    @OneToMany(mappedBy = "sequence")
    //@OneToMany(targetEntity = SequenceExercise.class, mappedBy = "sequence")
    //@JoinColumn(name="sequence_id", referencedColumnName="id", nullable=true)
    private List<SequenceExercise> exerciseList;
    
    @ManyToMany(mappedBy="sequencesList", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<Child> childrenList;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Caregiver author;

    public static final Finder<Long, Sequence> find = new Finder<>(Sequence.class);
    
    public Sequence(String name, List<Long> exerciseIds, List<Long> order, List<Long> childrenIds, Caregiver author) {
        this.name = name;
        this.author = author;
        
        setSequenceExercisesById(exerciseIds, order);
        setSequenceChildrensById(childrenIds);
    }
   
    public void setSequenceExercisesById(List<Long> exerciseIds, List<Long> order){
        exerciseList.clear();
        exerciseIds.stream().map((d) -> Exercise.findExerciseById(d)).forEachOrdered((ex) -> {
            SequenceExercise sequenceExercise = new SequenceExercise();
            sequenceExercise.setExercise(ex);
            sequenceExercise.setExerciseOrder(0);
            sequenceExercise.setSequence(this);
            
            exerciseList.add(sequenceExercise);
            ex.addSequenceExercise(sequenceExercise);
            ex.save();
        });
        
    }
    
    public void setSequenceChildrensById(List<Long> childrenIds){
        childrenList.clear();
        childrenIds.stream().map((d) -> Child.findByChildId(d)).forEachOrdered((ch) -> {
            childrenList.add(ch);
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

    public List<SequenceExercise> getSequenceExercises() {
        return this.exerciseList;
    }

    public void setSequenceExercises(List<SequenceExercise> exerciseList) {
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
    
    public static Sequence findSequenceById(Long id) {
        Logger.debug("Looking for sequence with id: " + id);
        return find.where().eq("id", id).findUnique();
    }
    
    public static List<Sequence> findByAuthor(Caregiver caregiver) {
        Logger.debug("Looking for sequences from: " + caregiver.getCaregiverLogin().getUsername());
        return find.where().eq("author_id", caregiver.getCaregiverId()).findList();
    }
}
