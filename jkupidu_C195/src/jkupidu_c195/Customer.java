/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jkupidu_c195;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



/**
 *
 * @author jkupidura
 */
public final class Customer {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty address = new SimpleStringProperty();
    private final SimpleStringProperty city = new SimpleStringProperty();
    private final SimpleStringProperty zipcode = new SimpleStringProperty();
    private final SimpleStringProperty phoneNumber = new SimpleStringProperty();

    
    //Empty Constructor
    public Customer() {}
    
    //Filled Constructor
    
    public Customer(int id, String name, String address, String city, String phoneNumber, String zipcode){
        setId(id);
        setName(name);
        setAddress(address);
        setCity(city);
        setPhoneNumber(phoneNumber);
        setZipcode(zipcode);
    }
    //Getters

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getZipcode() {
        return zipcode.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }
    
    
    
    
    //Setters

    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setZipcode(String zipcode) {
        this.zipcode.set(zipcode);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }
    
    
    
    public static String validateCustomer(String name, String address, int city, String zipcode, String phone, String errorMessage){
        if(name.isEmpty()){
            errorMessage =  errorMessage + "You cannot leave the Name field blank. ";
        }
        if(address.isEmpty()){
            errorMessage =  errorMessage + "You cannot leave the Address field blank. ";
        }
        if(city == 0){
            errorMessage = errorMessage + "You must select a City. ";
        }
        if(zipcode.isEmpty()){
            errorMessage = errorMessage + "You cannot leave the ZipCode field blank. ";
        }
        if(phone.isEmpty()){
            errorMessage = errorMessage + "You cannot leave the Phone Number field blank. ";
        }
        return errorMessage;
    }
    
}
