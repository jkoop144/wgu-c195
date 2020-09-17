/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import static jkupidu_c195.Database.getConnectionStatus;

/**
 * FXML Controller class
 *
 * @author jkupidura
 */
public class reportsController implements Initializable {


    @FXML TextArea reportArea;
    @FXML RadioButton monthly;
    @FXML RadioButton consultant;
    @FXML RadioButton customer;
    
    private ToggleGroup viewSelected;
    
    
    /**
     * Initializes the controller class.
     */
    
    
    public void reportSelected(){
        if (this.viewSelected.getSelectedToggle().equals(this.monthly)){
            try {
                Statement connect = getConnectionStatus().createStatement();
                String search = "SELECT description, MONTHNAME(start) as 'Month', COUNT(*) as 'Total' FROM appointment GROUP BY description, MONTH(start)";
                ResultSet result = connect.executeQuery(search);
                StringBuilder reportMonthly = new StringBuilder();
                reportMonthly.append(String.format("%1$-55s %2$-55s %3$s \n", "Month", "Appointment Type", "Total"));
                reportMonthly.append(String.join("", Collections.nCopies(110, "-")));
                reportMonthly.append("\n");
                while(result.next()) {
                    reportMonthly.append(String.format("%1$-55s %2$-60s %3$d \n", 
                    result.getString("Month"), result.getString("description"), result.getInt("Total")));
                }
                connect.close();
                reportArea.setText(reportMonthly.toString());
            } catch (SQLException error) {
                System.out.println("SQLException: " + error.getMessage());
            }
        }else if (this.viewSelected.getSelectedToggle().equals(this.consultant)){
            try {
                Statement connect = getConnectionStatus().createStatement();
                String search = "SELECT appointment.contact, appointment.description, customer.customerName, start, end " +
                    "FROM appointment JOIN customer ON customer.customerId = appointment.customerId " +
                    "GROUP BY appointment.contact, MONTH(start), start";
                ResultSet result = connect.executeQuery(search);
                StringBuilder consultReport = new StringBuilder();
                consultReport.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n", 
                    "Consultant", "Appointment", "Customer", "Start", "End"));
                consultReport.append(String.join("", Collections.nCopies(110, "-")));
                consultReport.append("\n");
                while(result.next()) {
                    consultReport.append(String.format("%1$-25s %2$-25s %3$-25s %4$-25s %5$s \n", 
                    result.getString("contact"), result.getString("description"), result.getString("customerName"),
                    result.getString("start"), result.getString("end")));
                }
                connect.close();
                reportArea.setText(consultReport.toString());
            } catch (SQLException error) {
                System.out.println("SQLException: " + error.getMessage());
            }
        }else {
            try {
                Statement connect = getConnectionStatus().createStatement();
                String search = "SELECT customer.customerName, COUNT(*) as 'Total' FROM customer JOIN appointment " +
                    "ON customer.customerId = appointment.customerId GROUP BY customerName";
                ResultSet result = connect.executeQuery(search);
                StringBuilder customerReport = new StringBuilder();
                customerReport.append(String.format("%1$-65s %2$-65s \n", "Customer", "Total Appointments"));
                customerReport.append(String.join("", Collections.nCopies(110, "-")));
                customerReport.append("\n");
                while(result.next()) {
                    customerReport.append(String.format("%1$s %2$65d \n", 
                    result.getString("customerName"), result.getInt("Total")));
                }
                connect.close();
                reportArea.setText(customerReport.toString());
            } catch (SQLException error) {
                System.out.println("SQLExcpetion: " + error.getMessage());
            } 
        }
    }
    
    public void backButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        viewSelected = new ToggleGroup();
        this.monthly.setToggleGroup(viewSelected);
        this.consultant.setToggleGroup(viewSelected);
        this.customer.setToggleGroup(viewSelected);
        
        
    }    
    
}
