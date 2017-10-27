package models;

import com.avaje.ebean.Model;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Reinforcement extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private ReinforcementStrategy reinforcementStrategy;
    
    @Column(nullable = true)
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Resource reinforcementResource;

    public Reinforcement() {
        this.reinforcementStrategy = ReinforcementStrategy.OFF;
    }

    public ReinforcementStrategy getReinforcementStrategy() {
        return reinforcementStrategy;
    }

    public void setReinforcementStrategy(ReinforcementStrategy reinforcementStrategy) {
        this.reinforcementStrategy = reinforcementStrategy;
    }

    public Resource getReinforcementResource() {
        return reinforcementResource;
    }

    public void setReinforcementResource(Resource reinforcementResource) {
        this.reinforcementResource = reinforcementResource;
    }
}
