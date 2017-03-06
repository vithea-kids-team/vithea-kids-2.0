package models;

import java.util.List;

import com.avaje.ebean.Model;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Sequence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequenceId;
    
    private String sequenceName;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Exercise> exerciseList;

    public static final Finder<Long, Sequence> find = new Finder<>(Sequence.class);
    
    public Sequence(String name) {
        this.sequenceName = name;
    }

    public Long getSequenceId() {
        return this.sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getSequenceName() {
        return this.sequenceName;
    }

    public void setSequenceName(String name) {
        this.sequenceName = name;
    }

    public List<Exercise> getSequenceExercises() {
        return this.exerciseList;
    }

    public void setSequenceExercises(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }
}
