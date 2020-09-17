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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jkupidura
 */
public class calendarMainController implements Initializable {

    @FXML RadioButton weekly;
    @FXML RadioButton monthly;
    
    @FXML TableView<Appointment> calendar;
    
    
    @FXML TableColumn location;
    @FXML TableColumn<Appointment, String> customer;
    @FXML TableColumn description;
    @FXML TableColumn<Appointment, String> date;
    @FXML TableColumn<Appointment, String> start;
    @FXML TableColumn<Appointment, String> end;
    
    private static Appointment currentSelection;
    
    public static Appointment passAppointmentIndex(){
        return currentSelection;
    } 
    
    private ToggleGroup viewSelected;
    
    public void addButton (ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("addAppointment.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    public void backButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    public void editButton(ActionEvent event) throws IOException{
        if(calendar.getSelectionModel().getSelectedItem() != null){
            currentSelection = calendar.getSelectionModel().getSelectedItem();
            
            Parent parent = FXMLLoader.load(getClass().getResource("editAppointment.fxml"));
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
        if(calendar.getSelectionModel().getSelectedItem() != null){
            currentSelection = calendar.getSelectionModel().getSelectedItem();
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm");
            confirm.setHeaderText("Confirm Appointment Deletion");
            confirm.setContentText("Are you sure you want to delete this appointment?");
            confirm.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    Database.deleteAppointment(currentSelection.getAppointmentId());
                    viewButtonSelected();
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
    
    public void viewButtonSelected(){
        if (this.viewSelected.getSelectedToggle().equals(this.weekly)){
            calendar.setItems(Database.getWeeklyAppointments());
        }else {
            calendar.setItems(Database.getMonthlyAppointments());
        }  
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewSelected = new ToggleGroup();
        this.weekly.setToggleGroup(viewSelected);
        this.monthly.setToggleGroup(viewSelected);
    
        calendar.setItems(Database.getWeeklyAppointments());
        
        customer.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerName();
        });
        
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        //This lambda allows the execution of a method when filling cell
        start.setCellValueFactory(cellData -> {
            return cellData.getValue().getStartDateTime();
        });
        
        //This lambda allows the execution of a method when filling cell
        end.setCellValueFactory(cellData -> {
            return cellData.getValue().getEndDateTime();
        });
        
        //This lambda allows the execution of a method when filling cell
        date.setCellValueFactory(cellData -> {
            return cellData.getValue().getDateString();
        });
        
    }    
    
}
