package com.pennapps.xx.recycleme.data;

import android.os.AsyncTask;
import android.util.Log;

import com.pennapps.xx.recycleme.models.RecyclableObject;
import com.pennapps.xx.recycleme.models.RecycleCenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecycleCenterFinder extends AsyncTask<Void, Void, ArrayList<RecycleCenter>> {

    String zipCode;
    double latitude, longitude;

    String item;

    public RecycleCenterFinder(String item, String zipCode) {
        this.zipCode = zipCode;
        this.item = item;
    }

    public RecycleCenterFinder(String item, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.item = item;
    }

    public ArrayList<RecycleCenter> doInBackground(Void... params) {

        ArrayList<RecycleCenter> locations = new ArrayList<>();
        String baseUrl = "https://search.earth911.com/";

        String searchUrl = baseUrl + "?what=" + item;

        try {

            if (zipCode != null) {
                searchUrl += "&where=" + zipCode + "&list_filter=all&max_distance=50";
            } else {
                searchUrl += "&latitude=" + latitude + "&longitude=" + longitude + "&list_filter=all&max_distance=50";
            }

            Document doc = Jsoup.connect(searchUrl).get();
            for (Element resultItem : doc.getElementsByClass("result-item")) {
                ArrayList<String> collectedMaterials = new ArrayList<>();

                if (!resultItem.hasClass("program")) {
                    String name = resultItem.getElementsByClass("title").first().child(0).html();
                    String address = resultItem.getElementsByClass("address1").first().html() + ", " + resultItem.getElementsByClass("address3").html();
                    String resultUrl = baseUrl + resultItem.getElementsByClass("title").first().child(0).attr("href");

                    Document locationPage = Jsoup.connect(resultUrl).get();

                    for (Element material : locationPage.getElementsByClass("material")) {
                        collectedMaterials.add(material.html());
                    }

                    Log.i("tag", item + ": " + address);

                    locations.add(new RecycleCenter(name, address, "location", collectedMaterials));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locations;
    }


    public static ArrayList<RecycleCenter> commonCenters(ArrayList<RecyclableObject> items){
        ArrayList<String> itemNames = new ArrayList<>();
        ArrayList<RecycleCenter> allCenters = new ArrayList<>();
        ArrayList<Integer> correspondingNumbers = new ArrayList<>();
        ArrayList<RecycleCenter> finalList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++){
            RecyclableObject obj = items.get(i);
            String item = obj.getLabel();
            itemNames.add(item);
        }
        for (int i = 0; i < items.size(); i++) {
            RecyclableObject obj = items.get(i);
            ArrayList<RecycleCenter> threeCenters = obj.getCenters();
            for (int j = 0; j < 3; j++){
                RecycleCenter checkingCenter = threeCenters.get(j);
                allCenters.add(checkingCenter);
                int count = checkingCenter.numberOfCommonItems(itemNames);
                correspondingNumbers.add(count);
            }
        }
        int highest = 0;
        RecycleCenter most = null;
        for (int i = 0; i < correspondingNumbers.size(); i++){
            if (correspondingNumbers.get(i) > highest){
                highest = correspondingNumbers.get(i);
                most = allCenters.get(i);
            }
        }
        finalList.add(most);
        for (int i = 0; i < itemNames.size(); i++){
            if (!(most.isRecyclableHere(itemNames.get(i)))){
                finalList.add(allCenters.get(i*3));
            }
        }

        //find highest count in correspondingnumbers
        //find associated objects
        //delete associated objects from itemnames
        //see if remaining objects are in any locations together
        //else put top locations for each individual object
    return finalList;
    }

}
