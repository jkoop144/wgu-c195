/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jkupidura
 */
public class customerMainController implements Initializable {
    
    @FXML private TableView<Customer> customerList;
    @FXML private TableColumn<Customer, String> customerNameCol;
    
    @FXML private static Customer currentSelection;
    
    public static Customer passCustomerIndex(){
        return currentSelection;
    }
    
    public void addButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("addCustomer.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    
    public void editButton(ActionEvent event) throws IOException{
        if(customerList.getSelectionModel().getSelectedItem() != null){
            currentSelection = customerList.getSelectionModel().getSelectedItem();
            
            Parent parent = FXMLLoader.load(getClass().getResource("editCustomer.fxml"));
            Scene scene = new Scene(parent);
        
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
            window.setScene(scene);
            window.show();
           
        } else{
            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setTitle("Error");
            error.setHeaderText("No Customer Selected");
            error.setContentText("Please select a customer to edit.");
            error.showAndWait();  
        }
        
    }
    
    public void deleteButton(ActionEvent event){
        if(customerList.getSelectionModel().getSelectedItem() != null){
            currentSelection = customerList.getSelectionModel().getSelectedItem();
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm");
            confirm.setHeaderText("Confirm Customer Deletion");
            confirm.setContentText("Are you sure you want to delete " + currentSelection.getName() + "?");
            confirm.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    Database.deleteCustomer(currentSelection.getId());
                    updateCustomerList();
                }
            }));
        } else{
            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setTitle("Error");
            error.setHeaderText("No Customer Selected");
            error.setContentText("Please select a customer to delete.");
            error.showAndWait();  
        }
    }
    
    
    
    public void backButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    public void updateCustomerList(){
        customerList.setItems(Database.getCustomerList());
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Database.getCustomerList();
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        customerList.setItems(Database.getCustomerList());
        
        
        
    }    
    
}
