/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jkupidura
 */
public class addAppointmentController implements Initializable {
    
    
    @FXML private TableView<Customer> customerNames;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TextField location;
    @FXML private ComboBox contact;
    @FXML private ComboBox time;
    @FXML private ComboBox type;
    @FXML private DatePicker date;
    
    private final ObservableList<String> contactList = FXCollections.observableArrayList("Norman Yankee", "Phoebe Arlinghouse","Landen Engles");
    private final ObservableList<String> timeList = FXCollections.observableArrayList("9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM");
    private final ObservableList<String> typeList = FXCollections.observableArrayList("Introduction-Meet and Greet","Planning-Investments","Consultation-Fiances");
    
    public static Customer selectedCustomer;
       
    public void saveButton(ActionEvent event) throws IOException {
        int contactSelected = contact.getSelectionModel().getSelectedIndex();
        int typeSelected = type.getSelectionModel().getSelectedIndex();
        int timeSelected = time.getSelectionModel().getSelectedIndex();
        LocalDate dateSelected = date.getValue();
        int customerId;
        if (customerNames.getSelectionModel().getSelectedItem() == null){
            customerId = 0;
        }else{
            selectedCustomer = customerNames.getSelectionModel().getSelectedItem();
            customerId = selectedCustomer.getId();
        }
        String messageHolder = new String();
        try {
           String errorMessage = Appointment.validateContact(contactSelected, typeSelected, timeSelected, dateSelected, customerId, messageHolder);
           if (errorMessage.length() > 0){
                Alert error = new Alert(Alert.AlertType.INFORMATION);
                error.setTitle("Error");
                error.setHeaderText("Cannot Add Appointment");
                error.setContentText(errorMessage);
                error.showAndWait();
            } else if (Database.scheduleCheck(-1, location.getText(), dateSelected.toString(), timeList.get(timeSelected))) {
                Alert error = new Alert(Alert.AlertType.INFORMATION);
                error.setTitle("Error");
                error.setHeaderText("Scheduling Conflict!");
                error.setContentText("There is a conflicting appointment, please choose a different time/date.");
                error.showAndWait();  
           } else {
                System.out.println("Got to executing");
                Database.addAppointment(customerId, typeList.get(typeSelected), contactList.get(contactSelected), location.getText(), dateSelected.toString(), timeList.get(timeSelected));
                
                Parent saveButtonParent = FXMLLoader.load(getClass().getResource("calendarMain.fxml"));
                Scene saveButtonScene = new Scene(saveButtonParent);
        
                Stage saveButtonWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
                saveButtonWindow.setScene(saveButtonScene);
                saveButtonWindow.show();
            }
        } catch (IOException error){
            System.out.println("Error " + error.getMessage());
        
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
            Parent cancelButtonParent = FXMLLoader.load(getClass().getResource("calendarMain.fxml"));
            Scene cancelButtonScene = new Scene(cancelButtonParent);
        
            Stage cancelButtonWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
            cancelButtonWindow.setScene(cancelButtonScene);
            cancelButtonWindow.show();
        }
    }
    
    public void setLocation() {
        switch (this.contact.getSelectionModel().getSelectedItem().toString()) {
            case "Landen Engles":
                {
                   location.setText("London");
                   break;
                }
            case "Norman Yankee":
                {
                   location.setText("New York");
                   break;
                }
            default:
                {
                   location.setText("Phoenix");
                   break;
                }
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        contact.setItems(contactList);
        time.setItems(timeList);
        type.setItems(typeList);
        location.setEditable(false);
        
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerNames.setItems(Database.getCustomerList());
        
        //This Lamba expression allows me to easy block out dates on the date picker
        
        date.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(
                    empty || 
                    date.getDayOfWeek() == DayOfWeek.SATURDAY || 
                    date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                    date.isBefore(LocalDate.now()));
                if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || date.isBefore(LocalDate.now())) {
                    setStyle("-fx-background-color: #ffc4c4;");
                }
            }
        });
        
    }    
    
}
