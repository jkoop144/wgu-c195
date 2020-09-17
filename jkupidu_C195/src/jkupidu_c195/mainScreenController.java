/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jkupidura
 */
public class mainScreenController implements Initializable {

    public void calendarButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("calendarMain.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    public void customerButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("customerMain.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    public void reportsButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("reports.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    public void logButton(ActionEvent event) throws IOException{
       File logFile = new File("logs.txt");
       if (logFile.exists()){
           if(Desktop.isDesktopSupported()){
               try{
                   Desktop.getDesktop().open(logFile);
               } catch (IOException error){
                   System.out.println("Error opening log file");
               }
           }else {
               Alert error = new Alert(Alert.AlertType.INFORMATION);
                error.setTitle("Error");
                error.setHeaderText("Desktop Not Supported");
                error.setContentText("Sorry, but your OS does not support viewing this file.");
                error.showAndWait();
           }
       }
    }
    
    public void exitButton(ActionEvent event)
    {
        Alert exitButton = new Alert(Alert.AlertType.CONFIRMATION);
        exitButton.initModality(Modality.NONE);
        exitButton.setTitle("Exit Confirmation");
        exitButton.setHeaderText("Exit Confirmation");
        exitButton.setContentText("Are you sure you want to exit the program?");
        Optional<ButtonType> answer = exitButton.showAndWait();
        if (answer.get() == ButtonType.OK){
            System.exit(0);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    
    
}
