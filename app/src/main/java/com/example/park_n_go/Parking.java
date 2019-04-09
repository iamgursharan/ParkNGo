/*
 * Author: Gursharan Singh
 * Description; This class lays the skeletal for holding values in the database
 */
package com.example.park_n_go;

import android.os.Parcelable;
import android.text.format.Time;

import java.io.Serializable;

public class Parking {

    // private variables
    public String address;
    public String hostType;
    public String id;
    public String latitude;
    public String longitude;
    public String name;
    public String notes;
    public String occupancy;
    public String occupied;
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

    public String getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(String occupancy) {
        this.occupancy = occupancy;
    }

    public String getOccupied() {
        return occupied;
    }

    public void setOccupied(String occupied) {
        this.occupied = occupied;
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
    public String getHostType() {
        return hostType;
    }

    public void setHostType(String hostType) {
        this.hostType = hostType;
    }

    public Parking() {
    }

    public Parking(String address,String hostType,
                   String id, String latitude, String longitude, String name, String notes, String occupancy, String occupied, String price, String time) {
        this.address = address;
        this.hostType=hostType;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.notes = notes;
        this.occupancy = occupancy;
        this.occupied = occupied;
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
                ", occupancy='" + occupancy + '\'' +
                ", occupied='" + occupied + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
