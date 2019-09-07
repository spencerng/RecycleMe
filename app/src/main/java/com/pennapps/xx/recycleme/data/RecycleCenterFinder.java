package com.pennapps.xx.recycleme.data;

import android.os.AsyncTask;

import com.pennapps.xx.recycleme.models.RecycleCenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class RecycleCenterFinder extends AsyncTask<String, Void, ArrayList<RecycleCenter>> {

    public ArrayList<RecycleCenter> doInBackground(String... params) {

        ArrayList<RecycleCenter> locations = new ArrayList<>();
        String baseUrl = "https://search.earth911.com/";

        try {
            String searchUrl = baseUrl + "?what=" + params[0] + "&where=" + params[1] + "&list_filter=all&max_distance=50";

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

                    locations.add(new RecycleCenter(name, address, collectedMaterials));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locations;
    }

}
