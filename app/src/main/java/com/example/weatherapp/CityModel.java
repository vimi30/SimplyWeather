package com.example.weatherapp;

public class CityModel {

        private int distance;
        private String title;
        private String location_type;
        private int woeid;
        private String latt_long;

    public CityModel(int distance, String title, String location_type, int woeid, String latt_long) {
        this.distance = distance;
        this.title = title;
        this.location_type = location_type;
        this.woeid = woeid;
        this.latt_long = latt_long;
    }

    public CityModel(){

    }
    public int getDistance() {
        return distance;
    }

    public String getTitle() {
        return title;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public void setWoeid(int woeid) {
        this.woeid = woeid;
    }

    public void setLatt_long(String latt_long) {
        this.latt_long = latt_long;
    }

    @Override
    public String toString() {
        return
                "title='" + title + '\'' +
                ", location_type='" + location_type + '\'' +
                ", woeid=" + woeid +
                ", latt_long='" + latt_long;
    }

    public String getLocation_type() {
        return location_type;
    }

    public int getWoeid() {
        return woeid;
    }

    public String getLatt_long() {
        return latt_long;
    }
}
