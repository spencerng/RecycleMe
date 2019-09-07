package com.pennapps.xx.recycleme.models;

import java.util.ArrayList;

public class RecycleCenter {

    private String address;
    private ArrayList<String> materialTypes;
    private String name;
    private String centerType;

    public RecycleCenter(String name, String address, String centerType, ArrayList<String> materialTypes) {
        this.address = address;
        this.materialTypes = materialTypes;
        this.name = name;
        this.centerType = centerType;
    }

    public String getAddress() {
        return address;
    }

    public double getDrivingDistance(Object fromThisLocation) {
        return 0.0;
    }

    public boolean isRecyclableHere(String objectType) {
        return materialTypes.contains(objectType);
    }

    public String getName() {
        return name;
    }

    public String getCenterType() {
        return centerType;
    }
}
