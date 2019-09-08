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

    protected RecycleCenter(Parcel in) {
        address = in.readString();
        materialTypes = in.createStringArrayList();
        name = in.readString();
        centerType = in.readString();
    }

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

   //takes items from ArrayList of scanned things that this center takes and outputs as string
    public String userFacingString(ArrayList<String> items){
        String output = "";

        for (String item:items){
            output = output + item;
            if (items.indexOf(item)!=items.size()-1){ //if item is not last item
                output = output + ", ";
            }
        }
        return output;
    }



}
