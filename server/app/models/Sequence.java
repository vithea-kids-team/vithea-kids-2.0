package models;

import java.util.List;

import com.avaje.ebean.Model;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

    public static final Finder<Long, Sequence> find = new Finder<>(Sequence.class);
    
    public Sequence(String name) {
        this.name = name;
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
    
    public static List<Sequence> getAll() {
        return find.all();
    }
    
    public static Sequence findById(Integer id) {
        Logger.debug("Looking for sequence with id: " + id);
        return find
        .where()
        .eq("id", id)
        .findUnique();
    }
}
