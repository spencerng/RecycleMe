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

        for (String itemLabel : itemLabels) {
            items.add(new RecyclableObject(itemLabel, RecycleCenterFinder.getRecycleCenters(itemLabel)));
        }

        return DistanceOptimizer.optimizeRecycleCenters(startLocation, endLocation, items);
    }
}
