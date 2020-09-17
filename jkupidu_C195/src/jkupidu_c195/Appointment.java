/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author jkupidura
 */
public class Appointment {
    private int appointmentId;
    private int customerId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String start;
    private String end;

    
    
    public Appointment(){}

    
    //Getters

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }
    
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getContact() {
        return contact;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
    
    //Setters

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }
    
    public StringProperty getCustomerName(){
        StringProperty customerName = Database.getCustomerName(customerId);
        return customerName;
    }
    
    
    public StringProperty getEndDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime localFormat = LocalDateTime.parse(this.end, format);
        ZonedDateTime zoneFormat = localFormat.atZone(ZoneId.of("UTC"));
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcFormat = zoneFormat.withZoneSameInstant(zoneId);
        //StringProperty endDate = new SimpleStringProperty(utcFormat.toLocalDateTime().toString());
        
        LocalTime lt = utcFormat.toLocalTime();
        int rawH = Integer.parseInt(lt.toString().split(":")[0]);
        String amPm;
        if(rawH < 12) {
            amPm = "AM";
        } else {
            amPm = "PM";
        }
        if(rawH > 12) {
            rawH -= 12;
        }
        StringProperty endDate = new SimpleStringProperty(rawH + ":00 " + amPm);
        
        return endDate;
    }
    
    
    public StringProperty getStartDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime localFormat = LocalDateTime.parse(this.start, format);
        ZonedDateTime zoneFormat = localFormat.atZone(ZoneId.of("UTC"));
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcFormat = zoneFormat.withZoneSameInstant(zoneId);
        
        LocalTime lt = utcFormat.toLocalTime();
        int rawH = Integer.parseInt(lt.toString().split(":")[0]);
        String amPm;
        if(rawH < 12) {
            amPm = "AM";
        } else {
            amPm = "PM";
        }
        if(rawH > 12) {
            rawH -= 12;
        }
        StringProperty startDate = new SimpleStringProperty(rawH + ":00 " + amPm);
        
        return startDate;
    }

    public String getUpcomingApp(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime localFormat = LocalDateTime.parse(this.start, format);
        ZonedDateTime zoneFormat = localFormat.atZone(ZoneId.of("UTC"));
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcFormat = zoneFormat.withZoneSameInstant(zoneId);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("kk:mm");
        LocalTime upcomingApp = LocalTime.parse(utcFormat.toString().substring(11,16), timeFormat);
        
        int rawH = Integer.parseInt(upcomingApp.toString().split(":")[0]);
        String amPm;
        if(rawH < 12) {
            amPm = "AM";
        } else {
            amPm = "PM";
        }
        if(rawH > 12) {
            rawH -= 12;
        }
        String upcomingAppTime = rawH + ":00 " + amPm;
        
        return upcomingAppTime;
        
        
    }
    
    
    public String getTimeOnlyString() {
        Timestamp ts = Timestamp.valueOf(this.start);
        ZonedDateTime zdt;
        ZoneId zid;
        LocalTime lt;
        if(this.location.equals("New York")) {
            zid = ZoneId.of("America/New_York");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().minusHours(4);
        } else if(this.location.equals("Phoenix")) {
            zid = ZoneId.of("America/Phoenix");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().minusHours(7);
        } else {
            zid = ZoneId.of("Europe/London");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().plusHours(1);
        }
       
        
        
        int rawH = Integer.parseInt(lt.toString().split(":")[0]);
        String amPm;
        if(rawH < 12) {
            amPm = "AM";
        } else {
            amPm = "PM";
        }
        if(rawH > 12) {
            rawH -= 12;
        }
        String time = rawH + ":00 " + amPm;
        return time;
    }
 
    
    public StringProperty getDateString() {
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime localFormat = LocalDateTime.parse(this.start, format);
        ZonedDateTime zoneFormat = localFormat.atZone(ZoneId.of("UTC"));
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime utcFormat = zoneFormat.withZoneSameInstant(zoneId);
        LocalDate dateOnly = utcFormat.toLocalDate();
        StringProperty date = new SimpleStringProperty(dateOnly.toString());
        //StringProperty startDate = new SimpleStringProperty(utcFormat.toLocalDateTime().toString());
        return date;
    }
    
    public LocalDate getDateInFormat() {
        Timestamp timeStamp = Timestamp.valueOf(this.start);
        ZonedDateTime zoneDT;
        ZoneId zoneId;
        LocalDate localDate;
        if(this.location.equals("New York")) {
            zoneId = ZoneId.of("America/New_York");
        } else if(this.location.equals("Phoenix")) {
            zoneId = ZoneId.of("America/Phoenix");
        } else {
            zoneId = ZoneId.of("Europe/London");
        }
        zoneDT = timeStamp.toLocalDateTime().atZone(zoneId);
        localDate = zoneDT.toLocalDate();
        return localDate;
    }
    
    //
    //Validation Checking
    //
    
    public static String validateContact(int appContact, int appType, int appTime, LocalDate appDate, int customerId, String errorMessage){
        if(appContact == -1){
            errorMessage =  errorMessage + "You cannot leave the Contact field blank. ";
        }
        if(appType == -1){
            errorMessage =  errorMessage + "You cannot leave the type of appointment blank. ";
        }
        if(appTime == -1){
            errorMessage = errorMessage + "You must select a time. ";
        }
        if(appDate == null){
            errorMessage = errorMessage + "You must select a date. ";
        }
        if(customerId < 1){
            errorMessage = errorMessage + "You must select a customer ";
        }
        return errorMessage;
    }
    
}
