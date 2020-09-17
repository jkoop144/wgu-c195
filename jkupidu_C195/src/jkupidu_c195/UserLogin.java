/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author jkupidura
 */
public class UserLogin {
    private static User userLogin;
    
    public static User getLogin(){
        return userLogin;
    }
    
    public static Boolean login(String user, String pass) {
        try {
            Statement statement = Database.getConnectionStatus().createStatement();
            String query = "SELECT * FROM user WHERE userName='" + user + "' AND password='" + pass + "';";
            ResultSet results = statement.executeQuery(query);
            if(results.next()) {
                userLogin = new User();
                userLogin.setUsername(results.getString("userName"));
                statement.close();
                LogGenerator.logMe(user, true);
                return true;
            } else {
                LogGenerator.logMe(user, false);
                return false;
            }
        } catch (SQLException error) {
            System.out.println("SQLException: " + error.getMessage());
            return false;
        }
    }
    
}
