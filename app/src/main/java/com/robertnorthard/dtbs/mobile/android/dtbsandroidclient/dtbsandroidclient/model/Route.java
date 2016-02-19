package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by robertnorthard on 13/02/16.
 */
public class Route implements Serializable {

    private Address startAddress;
    private Address endAddress;
    private double distance;
    private double estimateTravelTime;
    private List<Location> path;

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
}
