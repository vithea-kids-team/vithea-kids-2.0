/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author soraiamenesesalarcao
 */
public class AdminLogs {
   
    FileWriter fileWriter;
    
    public Boolean createFile(String path){
        
        File file = new File(path);
        
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                return created;
            } catch (IOException ex) {
                Logger.getLogger(AdminLogs.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        else return false;
    }
     
    public Boolean writeToFile(String path, String content){
        try {
            System.out.println("Path: " + path);
            fileWriter = new FileWriter(path, true);
            fileWriter.append(content);
            fileWriter.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(AdminLogs.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
