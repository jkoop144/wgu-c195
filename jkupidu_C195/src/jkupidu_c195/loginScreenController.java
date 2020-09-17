/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jkupidura
 */
public class loginScreenController implements Initializable {

    //GUI items
    
    @FXML Label calendarScheduler;
    @FXML Label message;
    @FXML Label usernameL;
    @FXML Label passwordL;
    @FXML TextField usernameTF;
    @FXML PasswordField passwordTF;
    @FXML Button login;
    @FXML Button exit;
    
    

    private String errorTitle;
    private String errorWarningMessage;
    private String exitTitle;
    private String exitWarningMessage;
    
    
    
    //login button
    
    public void loginButton(ActionEvent event) throws IOException {
        String user = usernameTF.getText();
        String pass = passwordTF.getText();
        boolean loginSuccess = UserLogin.login(user, pass);
        
        if (loginSuccess){
            Appointment in15Mins = Database.upcomingAppointment();
        
            if (in15Mins != null){
                String customerName = Database.getCustomerObject(in15Mins.getCustomerId()).getName();
                Alert message = new Alert(Alert.AlertType.INFORMATION);
                message.setTitle("Upcoming Appointment");
                message.setHeaderText("Appointment Reminder");
                message.setContentText("You have an appointment with " + customerName + " at " + in15Mins.getUpcomingApp() + " today");
                message.showAndWait();
            }
            
            
            
            Parent loginParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene loginScene = new Scene(loginParent);
        
            Stage loginWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
            loginWindow.setScene(loginScene);
            loginWindow.show();
        }
        else {
            Alert failed = new Alert(Alert.AlertType.ERROR);
            failed.setTitle(errorTitle);
            failed.setHeaderText(errorTitle);
            failed.setContentText(errorWarningMessage);
            failed.showAndWait();
        }
    }
    
    
    //exit button
    public void exitButton(ActionEvent event)
    {
        Alert exitButton = new Alert(Alert.AlertType.CONFIRMATION);
        exitButton.initModality(Modality.NONE);
        exitButton.setTitle(exitTitle);
        exitButton.setHeaderText(exitTitle);
        exitButton.setContentText(exitWarningMessage);
        Optional<ButtonType> answer = exitButton.showAndWait();
        if (answer.get() == ButtonType.OK){
            System.exit(0);
        }
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale location = Locale.getDefault();
        ResourceBundle langBundle = ResourceBundle.getBundle("Languages/lang", location);
        
        usernameL.setText(langBundle.getString("username"));
        passwordL.setText(langBundle.getString("password"));
        login.setText(langBundle.getString("login"));
        exit.setText(langBundle.getString("exit"));
        message.setText(langBundle.getString("message"));
        errorTitle = langBundle.getString("errorTitle");
        errorWarningMessage = langBundle.getString("errorWarningMessage");
        exitTitle = langBundle.getString("exitTitle");
        exitWarningMessage = langBundle.getString("exitWarningMessage");

    }
}