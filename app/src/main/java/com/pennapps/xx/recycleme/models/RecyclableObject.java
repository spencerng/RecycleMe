package com.pennapps.xx.recycleme.models;

import java.util.ArrayList;

public class RecyclableObject {

    private String label;
    private ArrayList<RecycleCenter> potentialCenters;

    public RecyclableObject(String label, ArrayList<RecycleCenter> potentialCenters) {
        this.label = label;
        this.potentialCenters = potentialCenters;
    }
}
