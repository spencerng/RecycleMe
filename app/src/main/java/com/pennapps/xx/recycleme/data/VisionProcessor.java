package com.pennapps.xx.recycleme.data;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VisionProcessor extends AsyncTask<Void, Void, ArrayList<String>> {

    private Context c;
    private String imagePath;

    public VisionProcessor(Context context, String imagePath) {
        c = context;
        this.imagePath = imagePath;
    }

    public ArrayList<String> doInBackground(Void... params) {
        FirebaseApp.initializeApp(c);
        ArrayList<String> labels = new ArrayList<>();

        try {
            FirebaseVisionImage image =
                    FirebaseVisionImage.fromFilePath(c, Uri.fromFile(new File(imagePath)));
            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                    .getCloudImageLabeler();

            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            for (FirebaseVisionImageLabel label : labels) {
                                String text = label.getText();
                                String entityId = label.getEntityId();
                                float confidence = label.getConfidence();
                                Log.i("item", text + ": " + confidence);
                            }
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return labels;
    }

}
