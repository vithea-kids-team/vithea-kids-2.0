package models;

import com.avaje.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

@Entity
public class SecurityQuestion extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private final String question;
    private final String answer;
    
    
    public SecurityQuestion(String question, String answer){
        this.question = question;
        this.answer = answer;
    }
    
    public String getQuestion(){
        return this.question;
    }
    public String getAnswer() {
        return this.answer;
    }
    
    public static final Model.Finder<Long, SecurityQuestion> find = new Model.Finder<>(SecurityQuestion.class);

}
