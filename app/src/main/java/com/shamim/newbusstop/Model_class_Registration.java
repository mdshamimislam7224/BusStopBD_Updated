package com.shamim.newbusstop;

public class Model_class_Registration {

    //    public Map<String, Boolean> getStars() {
//        return stars;
//    }
//
//    public void setStars(Map<String, Boolean> stars) {
//        this.stars = stars;
//    }
    public Model_class_Registration(String Fullname, String Email, String Phone, String Password, String driving_License, String NID, String Bus_name, String userType, String profileImageurl) {
        this.Fullname = Fullname;
        this.Email = Email;
        this.Phone = Phone;
        this.Password = Password;
        this.driving_License = driving_License;
        this.NID = NID;
        this.Bus_name = Bus_name;
        this.userType=userType;
        this.profileImageurl=profileImageurl;
    }


    public Model_class_Registration(String Fullname, String Email, String Phone, String Password, String userType, String profileImageurl) {
        this.Fullname = Fullname;
        this.Email = Email;
        this.Phone = Phone;
        this.Password = Password;
        this.userType=userType;
        this.profileImageurl=profileImageurl;
    }


    public Model_class_Registration(String Fullname, String Email, String Phone, String Password, String road_permit_License, String NID, String Bus_name, String Companylicense, String userType, String profileImageurl) {
        this.Fullname = Fullname;
        this.Email = Email;
        this.Phone = Phone;
        this.Password = Password;
        this.road_permit_License = road_permit_License;
        this.NID = NID;
        this.Bus_name = Bus_name;
        this.trade_license_of_bus_Company =Companylicense;
        this.userType=userType;
        this.profileImageurl=profileImageurl;
    }


    public Model_class_Registration(String Fullname, String Email, String Phone, String Password, String NID, String Bus_name, String userType, String profileImageurl) {
        this.Fullname = Fullname;
        this.Email = Email;
        this.Phone = Phone;
        this.Password = Password;
        this.NID = NID;
        this.Bus_name = Bus_name;
        this.userType=userType;
        this.profileImageurl=profileImageurl;
    }


    public Model_class_Registration(String Email, String Phone, String profileImageurl) {
        this.Fullname = Fullname;
        this.Email = Email;
        this.Phone = Phone;
        this.Password = Password;
        this.driving_License = driving_License;
        this.NID = NID;
        this.Bus_name = Bus_name;
        this.userType=userType;
        this.profileImageurl=profileImageurl;
    }


     String Fullname;
     String Phone;
     String Email;
     String driving_License;
     String Password;
     String NID;
     String road_permit_License;
     String Bus_name;
    String trade_license_of_bus_Company;
     String profileImageurl;



    public String getProfileImageurl() {
        return profileImageurl;
    }

    public void setProfileImageurl(String profileImageurl) {
        this.profileImageurl = profileImageurl;
    }

    public String getFullname() {

        return Fullname;
    }


    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getNID() {
        return NID;
    }

    public void setNID(String nid) {
        NID =nid;
    }

    public String getDriving_License() {
        return driving_License;
    }

    public void setDriving_License(String License) {
        this.driving_License = License;
    }


    public String getBus_name() {
       return Bus_name;
    }

    public void setBus_name(String bus_name) {
        NID = bus_name;
    }



    public void setRoad_permit_License(String road_permit_License) {
        this.road_permit_License = road_permit_License;
    }

    public String getRoad_permit_License() {
        return road_permit_License;
    }




    public void setTrade_license_of_bus_Company(String trade_license_of_bus_Company) {
        this.trade_license_of_bus_Company = trade_license_of_bus_Company;
    }

    public String getTrade_license_of_bus_Company() {
        return trade_license_of_bus_Company;
    }





    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;
    //public Map<String, Boolean> stars = new HashMap<>();

    public Model_class_Registration() {
    }







    /*@Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Fullname", Fullname);
        result.put("Email", Email);
        result.put("Phone", Phone);
        result.put("Password", Password);
        result.put("gender", gender);
        return result;
    }*/
}