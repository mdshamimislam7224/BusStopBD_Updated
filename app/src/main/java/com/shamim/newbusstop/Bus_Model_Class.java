package com.shamim.newbusstop;

public class Bus_Model_Class
{
    public String getBus_name() {
        return Bus_name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setBus_name(String bus_name) {
        Bus_name = bus_name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Bus_Model_Class(String bus_name, String email, String phone) {
        Bus_name = bus_name;
        Email = email;
        Phone = phone;
    }

    private String Bus_name,Email,Phone;

}