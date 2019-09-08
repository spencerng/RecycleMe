package com.pennapps.xx.recycleme.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RecycleCenter implements Parcelable {

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

    protected RecycleCenter(Parcel in) {
        address = in.readString();
        materialTypes = in.createStringArrayList();
        name = in.readString();
        centerType = in.readString();
    }

    public static final Creator<RecycleCenter> CREATOR = new Creator<RecycleCenter>() {
        @Override
        public RecycleCenter createFromParcel(Parcel in) {
            return new RecycleCenter(in);
        }

        @Override
        public RecycleCenter[] newArray(int size) {
            return new RecycleCenter[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public double getDrivingDistance(Location fromThisLocation) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeStringList(materialTypes);
        parcel.writeString(name);
        parcel.writeString(centerType);
    }
}
