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
    public String latitude;
    public String longitude;
    public String name;
    public String notes;
    public String price;
    public String time;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Parking() {
    }

    public Parking(String address, String id, String latitude, String longitude, String name, String notes, String price, String time) {
        this.address = address;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
