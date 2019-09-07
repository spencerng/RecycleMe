package com.pennapps.xx.recycleme.data;

import android.os.AsyncTask;
import android.util.Log;

import com.pennapps.xx.recycleme.models.RecycleCenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class RecycleCenterFinder extends AsyncTask<String, Void, ArrayList<RecycleCenter>> {

    public ArrayList<RecycleCenter> doInBackground(String... params) {
        try {
            String searchUrl = "https://search.earth911.com/?what=" + params[0] + "&where=" + params[1] + "&list_filter=all&max_distance=50";

            Document doc = Jsoup.connect(searchUrl).get();
            Log.i("tag", doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
