package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertnorthard on 13/02/16.
 */
public class Route implements Serializable {

    private Address startAddress;
    private Address endAddress;
    private double distance;
    private double estimateTravelTime;
    private List<Location> path = new ArrayList<>();

    public Route(){}

    public Route(Address startAddress, Address endAddress,
                 double distance, double estimateTravelTime, List<Location> path) {
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.distance = distance;
        this.estimateTravelTime = estimateTravelTime;
        this.path = path;
    }

    public Address getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(Address startAddress) {
        this.startAddress = startAddress;
    }

    public Address getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(Address endAddress) {
        this.endAddress = endAddress;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getEstimateTravelTime() {
        return estimateTravelTime;
    }

    public void setEstimateTravelTime(double estimateTravelTime) {
        this.estimateTravelTime = estimateTravelTime;
    }

    public List<Location> getPath() {
        return path;
    }

    public void setPath(List<Location> path) {
        this.path = path;
    }

    public List<LatLng> getLatLngPath(){
        List<LatLng> path = new ArrayList<>();

        for(Location l : this.path){
            path.add(new LatLng(l.getLatitude(), l.getLongitude()));
        }

        return path;
    }

    public void setLatLngPath(List<LatLng> path){
        for(LatLng latLng : path){
            this.path.add(new Location(latLng.latitude, latLng.longitude));
        }
    }
}
