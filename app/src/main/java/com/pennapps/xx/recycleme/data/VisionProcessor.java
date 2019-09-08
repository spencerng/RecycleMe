package com.pennapps.xx.recycleme.data;

import android.content.Context;
import android.net.Uri;

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


public class VisionProcessor {

    private Context c;
    private String imagePath;

    public VisionProcessor(Context context, String imagePath) {
        c = context;
        this.imagePath = imagePath;
    }

    public void process(final Callback cb) {
        FirebaseApp.initializeApp(c);
        final ArrayList<FirebaseVisionImageLabel> firebaseLabels = new ArrayList<>();

        try {
            FirebaseVisionImage image =
                    FirebaseVisionImage.fromFilePath(c, Uri.fromFile(new File(imagePath)));
            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                    .getCloudImageLabeler();

            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            String display = "";
                            for (FirebaseVisionImageLabel label : labels) {
                                display += label.getText() + ": " + label.getConfidence() + "\n";
                            }

                            cb.displayLabels(display);
                            cb.fetchItemLabels(labels);
                        }
                    });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public interface Callback {
        void displayLabels(String displayText);

        void fetchItemLabels(List<FirebaseVisionImageLabel> itemLabels);
    }

}
