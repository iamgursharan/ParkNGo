/*
 * Author: Gursharan Singh
 * Description; This class lays the skeletal for holding values in the database
 */
package com.example.park_n_go;

import android.text.format.Time;

public class Parking {

    // private variables
    public String address;
    public String id;
    public String longitude;
    public String latitude;
    public String name;
    public String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Parking(String address, String id, String longitude, String latitude, String name, String price) {
        this.address = address;
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.price = price;
    }

    public Parking(){}

    @Override
    public String toString() {
        return "Parking{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
