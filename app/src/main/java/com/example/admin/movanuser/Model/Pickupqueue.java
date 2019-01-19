package com.example.admin.movanuser.Model;

/**
 * Created by Admin on 12/7/2018.
 */

public class Pickupqueue {

    public String Person_name;
    public String Person_mobileNo;
    public String Customer_status;
    public String Pick_lat;
    public String Pick_long;
    public String Drop_lat;
    public String Drop_long;
    public String Fare;
    public String Parentno;
    public String Journeyid;


    public Pickupqueue(String person_name, String person_mobileNo, String customer_status, String pick_lat, String pick_long, String drop_lat, String drop_long, String fare, String parentno, String journeyid) {
        Person_name = person_name;
        Person_mobileNo = person_mobileNo;
        Customer_status = customer_status;
        Pick_lat = pick_lat;
        Pick_long = pick_long;
        Drop_lat = drop_lat;
        Drop_long = drop_long;
        Fare = fare;
        Parentno = parentno;
        Journeyid = journeyid;
    }

    public String getJourneyid() {
        return Journeyid;
    }

    public void setJourneyid(String journeyid) {
        Journeyid = journeyid;
    }

    public String getPerson_name() {

        return Person_name;
    }

    public void setPerson_name(String person_name) {
        Person_name = person_name;
    }

    public String getPerson_mobileNo() {
        return Person_mobileNo;
    }

    public void setPerson_mobileNo(String person_mobileNo) {
        Person_mobileNo = person_mobileNo;
    }

    public String getCustomer_status() {
        return Customer_status;
    }

    public void setCustomer_status(String customer_status) {
        Customer_status = customer_status;
    }

    public String getPick_lat() {
        return Pick_lat;
    }

    public void setPick_lat(String pick_lat) {
        Pick_lat = pick_lat;
    }

    public String getPick_long() {
        return Pick_long;
    }

    public void setPick_long(String pick_long) {
        Pick_long = pick_long;
    }

    public String getParentno() {
        return Parentno;
    }

    public void setParentno(String parentno) {
        Parentno = parentno;
    }

    public String getDrop_lat() {
        return Drop_lat;
    }

    public void setDrop_lat(String drop_lat) {
        Drop_lat = drop_lat;
    }

    public String getDrop_long() {
        return Drop_long;
    }

    public void setDrop_long(String drop_long) {
        Drop_long = drop_long;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }
}
