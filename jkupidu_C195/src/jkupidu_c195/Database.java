/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jkupidura
 */
public class Database {
    
    //Database values
    private static final String dbName = "U04zhj";
    private static String driver = "com.mysql.jdbc.Driver";
    private static String userName = "U04zhj";
    private static String userPass = "53688391261";
    private static String url = "jdbc:mysql://3.227.166.251/" + dbName;
    private static Connection connection;
    
    //Customer List
    private static ObservableList<Customer> listAllCustomers = FXCollections.observableArrayList();
    
    
    public Database(){
        
    }
    
    public static void connectSQL(){
        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, userPass);
        }
        catch(ClassNotFoundException exception){
            System.out.println("Class error" + exception.getMessage());
        }
        catch (SQLException exception){
            System.out.println("SQL error" + exception.getMessage());
        }
    }
    
    public static void disconnectSQL(){
        try{
            connection.close();
        }
        catch (SQLException exception){
        System.out.println("SQL error" + exception.getMessage());
        }
    }
    
    public static Connection getConnectionStatus() {
        return connection;
    }
    
    
    //
    //Customer data
    //
    
    
    
    public static StringProperty getCustomerName(int byId){
        try{
            Statement connect = getConnectionStatus().createStatement();
            String search = "SELECT * FROM customer WHERE customerId='" +byId + "';";
            ResultSet result = connect.executeQuery(search);
            if(result.next()){
                StringProperty customerName = new SimpleStringProperty(result.getString("customerName"));
                connect.close();
                return customerName;
            }
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
        }
        return null;
    }
    
    public static Customer getCustomerObject(int byId){
        try{
            Statement connect = getConnectionStatus().createStatement();
            String search = "SELECT * FROM customer WHERE customerId='" +byId + "';";
            ResultSet result = connect.executeQuery(search);
            if(result.next()){
                Customer customer = new Customer();
                customer.setName(result.getString("customerName"));
                connect.close();
                return customer;
            }
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
        }
        return null;
    }
    
    
    public static ObservableList<Customer> getCustomerList() {
        listAllCustomers.clear();
        try{
            Statement connect = getConnectionStatus().createStatement();
            String search = "SELECT customer.customerId, customer.customerName, address.address, address.phone, address.postalCode, city.city FROM customer INNER JOIN address ON customer.addressId = address.addressID INNER JOIN city ON address.cityId = city.cityID;";
            ResultSet result = connect.executeQuery(search);
            while(result.next()){
                Customer customerHolder = new Customer(result.getInt("customerId"),result.getString("customerName"),result.getString("address"),result.getString("city"),result.getString("phone"),result.getString("postalCode"));
                listAllCustomers.add(customerHolder);
            }
            connect.close();
            return listAllCustomers;
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
            return null;
        }
    }
  
    
    
    
    public static boolean createCustomer(String name, String address, int city, String zipcode, String phoneNumber){
        try{
            Statement connect = getConnectionStatus().createStatement();
            String insert = "INSERT INTO address SET address='" + address + "', phone='" + phoneNumber + "', postalCode='" + zipcode + "', cityId=" + city + ";";
            int checkOne = connect.executeUpdate(insert);
            if (checkOne == 1){
                String addressIdLookup = "SELECT * FROM address WHERE address='" + address + "'AND phone='" + phoneNumber + "'AND postalCode='" + zipcode + "'AND cityId=" + city + ";";
                ResultSet result = connect.executeQuery(addressIdLookup);
                System.out.println(result.next());
                int addressId = result.getInt("addressId");
                String insertAgain = "INSERT INTO customer SET customerName='" + name + "', addressId=" + addressId + ";";
                int checkTwo = connect.executeUpdate(insertAgain);
                if (checkTwo == 1){
                    return true;
                }
            }
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
        }
        return false;
    }
    
    public static boolean editCustomer(int id, String name, String address, int cityId, String zip, String phone) {
        try {
            Statement connect = getConnectionStatus().createStatement();
            String searchOne = "UPDATE customer SET customerName='" + name + "' WHERE customerId=" + id + ";";
            int updateOne = connect.executeUpdate(searchOne);
            if(updateOne == 1) {
                String addressIdLookup = "SELECT * FROM customer WHERE customerId=" + id + ";";
                ResultSet result = connect.executeQuery(addressIdLookup);
                System.out.println(result.next());
                int addressId = result.getInt("addressId");
                String searchTwo = "UPDATE address SET address='" + address + "', cityId=" + cityId + ", postalCode='" + zip + "', phone='" + phone + "' WHERE addressId=" + addressId + ";";
                int updateTwo = connect.executeUpdate(searchTwo);
                if(updateTwo == 1) {
                    return true;
                }
            }
        } catch(SQLException error) {
            System.out.println("SQLException: " + error.getMessage());
        }
        return false;
    }
    
    public static boolean deleteCustomer(int id){
        try{
            Statement connect = getConnectionStatus().createStatement();
            String addressIdLookup = "SELECT * FROM customer WHERE customerId=" + id + ";";
            ResultSet result = connect.executeQuery(addressIdLookup);
            System.out.println(result.next());
            int addressId = result.getInt("addressId");
            String remove = "DELETE FROM customer WHERE customerId=" + id;
            int checkOne = connect.executeUpdate(remove);
            if(checkOne == 1){
                String removeAgain = "DELETE FROM address WHERE addressId=" + addressId;
                int checkTwo = connect.executeUpdate(removeAgain);
                if(checkTwo == 1){
                    return true;
                }
            }
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
        }
        return false;
    }
    
    
    
    //
    //Appointment fetches and changes
    //
    
    
    
    public static ObservableList<Appointment> getMonthlyAppointments (){
        ObservableList<Appointment> mAppointments = FXCollections.observableArrayList();
        Appointment holder;
        LocalDate setStart = LocalDate.now();
        LocalDate setEnd = LocalDate.now().plusMonths(1);
        try {
            Statement connect = getConnectionStatus().createStatement();
            String search = "SELECT * FROM appointment WHERE start >= '" + setStart + "' AND start <= '" + setEnd + "';";
            ResultSet result = connect.executeQuery(search);
            while (result.next()){
                holder = new Appointment();
                holder.setAppointmentId(result.getInt("appointmentId"));
                holder.setCustomerId(result.getInt("customerId"));
                holder.setStart(result.getString("start"));
                holder.setEnd(result.getString("end"));
                holder.setTitle(result.getString("title"));
                holder.setDescription(result.getString("description"));
                holder.setLocation(result.getString("location"));
                holder.setContact(result.getString("contact"));
                mAppointments.add(holder);
            }
            connect.close();
            return mAppointments;
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
        }
        return null;
    }
    
    public static ObservableList<Appointment> getWeeklyAppointments (){
        ObservableList<Appointment> wAppointments = FXCollections.observableArrayList();
        Appointment holder;
        LocalDate setStart = LocalDate.now();
        LocalDate setEnd = LocalDate.now().plusWeeks(1);
        try {
            Statement connect = getConnectionStatus().createStatement();
            String search = "SELECT * FROM appointment WHERE start >= '" + setStart + "' AND start <= '" + setEnd + "';";
            ResultSet result = connect.executeQuery(search);
            while (result.next()){
                holder = new Appointment();
                holder.setAppointmentId(result.getInt("appointmentId"));
                holder.setCustomerId(result.getInt("customerId"));
                holder.setStart(result.getString("start"));
                holder.setEnd(result.getString("end"));
                holder.setTitle(result.getString("title"));
                holder.setDescription(result.getString("description"));
                holder.setLocation(result.getString("location"));
                holder.setContact(result.getString("contact"));
                wAppointments.add(holder);
            }
            connect.close();
            return wAppointments;
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
        }
        return null;
    }
    
    public static Appointment upcomingAppointment() {
        Appointment holder;
        LocalDateTime current = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDT = current.atZone(zoneId);
        LocalDateTime localStart = zonedDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime localEnd = localStart.plusMinutes(15);
        System.out.println(localStart);
        System.out.println(localEnd);
        try {
            Statement connect = getConnectionStatus().createStatement();
            String search = "SELECT * FROM appointment WHERE start BETWEEN '" + localStart + "' AND '" + localEnd + "';";
            ResultSet result = connect.executeQuery(search);
            if(result.next()) {
                holder = new Appointment();
                holder.setAppointmentId(result.getInt("appointmentId"));
                holder.setCustomerId(result.getInt("customerId"));
                holder.setStart(result.getString("start"));
                holder.setEnd(result.getString("end"));
                holder.setTitle(result.getString("title"));
                holder.setDescription(result.getString("description"));
                holder.setLocation(result.getString("location"));
                holder.setContact(result.getString("contact"));
                return holder;
            }
        } catch (SQLException error) {
            System.out.println("SQL error " + error.getMessage());
        }    
        return null;
    }
    
    public static void addAppointment(int id, String type, String contact, String location, String date, String time) {
       String title = type.split("-")[0];
       String descript = type.split("-")[1]; 
       String startTime = timeStampCreate(date, time, location, true);
       String endTime = timeStampCreate(date, time, location, false);
       try {
           Statement connect = getConnectionStatus().createStatement();
           String search = "INSERT INTO appointment SET customerId='" + id + "', title='" + title + "', description='" + descript + "', contact='" + contact + "', location='" + location + "', start='" + startTime + "', end='" + endTime + "';";
           connect.executeUpdate(search);
       } catch (SQLException error) {
           System.out.println("SQL error " + error.getMessage());
       }
    }
    
    
    public static boolean editAppointment(int id, String type, String contact, String location, String date, String time, int customerId) {
       String title = type.split("-")[0];
       String descript = type.split("-")[1]; 
       String startTime = timeStampCreate(date, time, location, true);
       String endTime = timeStampCreate(date, time, location, false);
       try {
           Statement connect = getConnectionStatus().createStatement();
           String search = "UPDATE appointment SET title='" + title + "', description='" + descript + "', contact='" + contact + "', location='" + location + "', start='" + startTime + "', end='" + endTime + "', customerId=" + customerId +" WHERE appointmentId='" + id + "';";
           int success = connect.executeUpdate(search);
           if (success == 1) {
               return true;
           }
       } catch (SQLException error) {
           System.out.println("SQL error " + error.getMessage());
       } return false;
    }
    
    
    public static boolean deleteAppointment(int id) {
        try {
            Statement connect = getConnectionStatus().createStatement();
            String search = "DELETE FROM appointment WHERE appointmentID='" + id + "'";
            int success = connect.executeUpdate(search);
            if (success == 1) {
                return true;
            }
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
        } return false;
    }
    
    
    public static boolean scheduleCheck(int id, String location, String date, String time) {
        String startTime = timeStampCreate(date, time, location, true);
        try {
            Statement connect = getConnectionStatus().createStatement();
            String search = "SELECT * FROM appointment WHERE start='" + startTime + "' AND location ='" + location + "'";
            ResultSet result = connect.executeQuery(search);
            if (result.next()) {
                if (result.getInt("appointmentId") == id){
                    connect.close();
                    return false;
                }
                connect.close();
               return true;
            } else {
                connect.close();
                return false;
            }
        } catch (SQLException error){
            System.out.println("SQL error " + error.getMessage());
            return true;
        }
    }
    
    
    
    
    //
    //Timestamp formatting
    //
    
    public static String timeStampCreate(String date, String time, String location, boolean startOrEnd) {
        String hour = time.split(":")[0];
        int convertH = Integer.parseInt(hour);
        if(convertH < 7) {
            convertH += 12;
        }
        if(!startOrEnd) {
            convertH += 1;
        }
        String rawD = String.format("%s %02d:%s", date, convertH, "00");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        LocalDateTime ldt = LocalDateTime.parse(rawD, df);
        ZoneId zid;
        if(location.equals("New York")) {
            zid = ZoneId.of("America/New_York");
        } else if(location.equals("Phoenix")) {
            zid = ZoneId.of("America/Phoenix");
        } else {
            zid = ZoneId.of("Europe/London");
        }
        
        ZonedDateTime zdt = ldt.atZone(zid);
        ZonedDateTime utcDate = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        ldt = utcDate.toLocalDateTime();
        Timestamp ts = Timestamp.valueOf(ldt);
        return ts.toString();
    }
    
}
