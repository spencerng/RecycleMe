package com.pennapps.xx.recycleme.data;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import com.pennapps.xx.recycleme.models.RecyclableObject;
import com.pennapps.xx.recycleme.models.RecycleCenter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VisionProcessor {

    private static ArrayList<String> getItems(String imagePath) throws IOException {
        ArrayList<String> labels = new ArrayList<String>();
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // The path to the image file to annotate: somehow get this from the camera?
            String fileName = imagePath;

            // Reads the image file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return null;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    labels.add(annotation.getDescription());
                }
            }
        }
        return labels;
    }


    public static ArrayList<RecycleCenter> getSortedRecycleCenters(String imageToAnalyzePath, Object startLocation, Object endLocation) {
        ArrayList<RecyclableObject> items = new ArrayList<>();

        try {
            ArrayList<String> itemLabels = getItems(imageToAnalyzePath);


            // Extract this from start location later
            String zipCode = "08902";


            for (String itemLabel : itemLabels) {
                items.add(new RecyclableObject(itemLabel, new RecycleCenterFinder().execute(itemLabel, zipCode).get()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DistanceOptimizer.optimizeRecycleCenters(startLocation, endLocation, items);
    }
}
