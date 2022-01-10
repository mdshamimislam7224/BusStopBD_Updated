package com.shamim.newbusstop;

import android.net.Uri;

import java.io.Serializable;

public class model_class_buss implements Serializable {


    public String busStand_Name;
    public String bus_Stand_Rent;
    int bus_total_seat_number;
    int Total_Amount_Buses;
    public String BusStand_Name_and_Rent;
    private String adminUser_uid;
    private String name_of_bus_company;
    private String road_permit_license;
    private String bus_stand_start;
    private String bus_stand_end;

    public int getTotal_Amount_Buses() {
        return Total_Amount_Buses;
    }

    public void setTotal_Amount_Buses(int total_Amount_Buses) {
        Total_Amount_Buses = total_Amount_Buses;
    }



    public void setName_of_bus_company(String name_of_bus_company) {
        this.name_of_bus_company = name_of_bus_company;
    }



    public String getBuses_name_from_admin() {
        return buses_name_from_admin;
    }

    public void setBuses_name_from_admin(String buses_name_from_admin) {
        this.buses_name_from_admin = buses_name_from_admin;
    }

    public String getBuses_ImageUrl() {
        return buses_ImageUrl;
    }

    public void setBuses_ImageUrl(String buses_ImageUrl) {
        this.buses_ImageUrl = buses_ImageUrl;
    }

    private String buses_name_from_admin;
    private String buses_ImageUrl;


    public model_class_buss(String busStand_Name_and_Rent) {
        BusStand_Name_and_Rent = busStand_Name_and_Rent;
    }




    public String getAdminUser_uid() {
        return adminUser_uid;
    }

    public void setAdminUser_uid(String adminUser_uid) {
        this.adminUser_uid = adminUser_uid;
    }

    public String getName_of_bus_company() {
        return name_of_bus_company;
    }



    public String getRoad_permit_license() {
        return road_permit_license;
    }

    public void setRoad_permit_license(String road_permit_license) {
        this.road_permit_license = road_permit_license;
    }

    public String getBus_stand_start() {
        return bus_stand_start;
    }

    public void setBus_stand_start(String bus_stand_start) {
        this.bus_stand_start = bus_stand_start;
    }

    public String getBus_stand_end() {
        return bus_stand_end;
    }

    public void setBus_stand_end(String bus_stand_end) {
        this.bus_stand_end = bus_stand_end;
    }

    public int getBus_total_seat_number() {
        return bus_total_seat_number;
    }








    public String getBusStand_Name() {
        return busStand_Name;
    }

    public void setBusStand_Name(String busStand_Name) {
        this.busStand_Name = busStand_Name;
    }

    public String getBus_Stand_Rent() {
        return bus_Stand_Rent;
    }

    public void setBus_Stand_Rent(String bus_Stand_Rent) {
        this.bus_Stand_Rent = bus_Stand_Rent;
    }



    public model_class_buss() {

    }

    public model_class_buss(String BusStandName, String BusStandRent) {
        this.busStand_Name = BusStandName;
        this.bus_Stand_Rent = BusStandRent;
    }


}
