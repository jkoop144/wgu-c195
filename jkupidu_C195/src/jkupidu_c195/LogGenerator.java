/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

/**
 *
 * @author jkupidura
 */
public class LogGenerator {
    
    private static final String file = "logs.txt";
    
    
    public LogGenerator(){
        
    }
    
    public static void logMe(String user, boolean status){
        try(FileWriter writeFile = new FileWriter(file, true);
            BufferedWriter buffWrite = new BufferedWriter(writeFile);
            PrintWriter printWrite = new PrintWriter(buffWrite)){
            printWrite.println(ZonedDateTime.now() +" " + user + (status ? " Successful":" Failed"));
        }
        catch (IOException error){
            System.out.println("Logging error");
        }
    }
}
