package com.pennapps.xx.recycleme.models;

public class RecycleCenter {

    private String address;
    private String[] objectTypes;

    public RecycleCenter(String address, String[] objectTypes) {
        this.address = address;
        this.objectTypes = objectTypes;
    }

    public String getAddress() {
        return address;
    }

    public double getDrivingDistance(Object fromThisLocation) {
        return 0.0;
    }

    public boolean isRecyclableHere(String objectType) {
        return false;
    }
}
