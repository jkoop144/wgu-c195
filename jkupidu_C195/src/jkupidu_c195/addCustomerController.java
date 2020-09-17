/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jkupidura
 */
public class addCustomerController implements Initializable {
    
    @FXML TextField name;
    @FXML TextField address;
    @FXML ComboBox city;
    @FXML TextField country;
    @FXML TextField zipcode;
    @FXML TextField phoneNumber;
    
    
    private ObservableList<String> cityList = FXCollections.observableArrayList("New York","Pheonix","London");
    private ObservableList<String> countryList = FXCollections.observableArrayList("US","UK");
    
    
    public void saveButton (ActionEvent event) throws IOException{
        try {
            String messageHolder = new String();
            int cityIdSelected = city.getSelectionModel().getSelectedIndex() + 1;
            String errorMessage = Customer.validateCustomer(name.getText(), address.getText(), cityIdSelected, zipcode.getText(), phoneNumber.getText(), messageHolder);
            if(errorMessage.length() > 0){
                Alert error = new Alert(Alert.AlertType.INFORMATION);
                error.setTitle("Error");
                error.setHeaderText("Cannot Add Customer");
                error.setContentText(errorMessage);
                error.showAndWait();
            } else{
                Database.createCustomer(name.getText(),address.getText(),cityIdSelected,zipcode.getText(),phoneNumber.getText());
                
                Parent saveButtonParent = FXMLLoader.load(getClass().getResource("customerMain.fxml"));
                Scene saveButtonScene = new Scene(saveButtonParent);
        
                Stage saveButtonWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
                saveButtonWindow.setScene(saveButtonScene);
                saveButtonWindow.show();
            }
        } catch(IOException error){
            System.out.println("Error saving customer....");
        }
    }
    
    
    
    
    
    public void cancelButton(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Confirmation");
        alert.setHeaderText("Cancel Confirmation");
        alert.setContentText("Are you sure you would like to cancel? All changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {        
            Parent cancelButtonParent = FXMLLoader.load(getClass().getResource("customerMain.fxml"));
            Scene cancelButtonScene = new Scene(cancelButtonParent);
        
            Stage cancelButtonWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
            cancelButtonWindow.setScene(cancelButtonScene);
            cancelButtonWindow.show();
        }
    }
    
    public void setLocation() {
        switch (this.city.getSelectionModel().getSelectedItem().toString()) {
            case "New York":
                {
                   country.setText("US");
                   break;
                }
            case "Pheonix":
                {
                   country.setText("US");
                   break;
                }
            default:
                {
                   country.setText("UK");
                   break;
                }
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        city.setItems(cityList);
        country.setEditable(false);
        
    }    
    
}
