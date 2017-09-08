/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import com.avaje.ebean.Model;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author soraiamenesesalarcao
 */
public class SequenceLog extends Model  {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sequenceLogId;
    
/*
- sequenceId
- childId
- timestampBegin
- timestampEnd
- numberExercisesCorrect
- numberExercisesSkipped
- numberExercises
- listExerciseLogs    
*/    
    
}
