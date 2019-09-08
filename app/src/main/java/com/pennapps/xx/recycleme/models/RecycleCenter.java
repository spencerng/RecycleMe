package com.pennapps.xx.recycleme.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    public Location getLocation(Context c) {
        Location loc = new Location("");
        loc.setLongitude(getLatLng(c).longitude);
        loc.setLatitude(getLatLng(c).latitude);
        return loc;
    }

    public double getDrivingDistance(Location fromThisLocation, Context c) {
        return getLocation(c).distanceTo(fromThisLocation) / 1609.344;
        //Return miles
    }

    public void deleteItems(ArrayList<String> itemNames){
        for (String name: itemNames){
            if (isRecyclableHere(name))
                itemNames.remove(name);
        }
    }

    public int numberOfCommonItems(ArrayList<String> itemNames){
        int count = 0;
        for (String name: itemNames){
            if (isRecyclableHere(name)){
                count++;
            }
        }
        return count;
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

    public LatLng getLatLng(Context context) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(this.address, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    //takes items from ArrayList of scanned things that this center takes and outputs as string
    public String userFacingString(ArrayList<String> items) {
        String output = "";

        for (String item : items) {
            output = output + item;
            if (items.indexOf(item) != items.size() - 1) { //if item is not last item
                output = output + ", ";
            }
        }
        return output;
    }



}
