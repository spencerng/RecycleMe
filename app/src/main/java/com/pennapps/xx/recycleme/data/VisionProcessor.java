package com.pennapps.xx.recycleme.data;

import com.pennapps.xx.recycleme.models.RecyclableObject;
import com.pennapps.xx.recycleme.models.RecycleCenter;

import java.util.ArrayList;

public class VisionProcessor {

    private static String[] getItems(Object imageToAnalyze) {
        return new String[]{};
    }

    public static ArrayList<RecycleCenter> getSortedRecycleCenters(Object imageToAnalyze, Object startLocation, Object endLocation) {

        String[] itemLabels = getItems(imageToAnalyze);

        ArrayList<RecyclableObject> items = new ArrayList<>();

        // Extract this from start location later
        String zipCode = "08902";

        try {

            for (String itemLabel : itemLabels) {
                items.add(new RecyclableObject(itemLabel, new RecycleCenterFinder().execute(itemLabel, zipCode).get()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DistanceOptimizer.optimizeRecycleCenters(startLocation, endLocation, items);
    }
}
