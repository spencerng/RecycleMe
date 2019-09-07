package com.pennapps.xx.recycleme.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.data.DistanceOptimizer;
import com.pennapps.xx.recycleme.data.RecycleCenterFinder;
import com.pennapps.xx.recycleme.data.VisionProcessor;
import com.pennapps.xx.recycleme.models.RecyclableObject;
import com.pennapps.xx.recycleme.models.RecycleCenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btn;
    public int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    String imageFilePath;

    private static void verifyPermissions(Activity activity) {
        // Check if the app has write permission
        int[] permissions = new int[]{ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)};

        for (int permission : permissions) {
            if (permission != PackageManager.PERMISSION_GRANTED) {

                // App doesn't have permission so prompt the user
                int requestStorageCode = 1;
                String[] storagePermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

                ActivityCompat.requestPermissions(activity, storagePermissions, requestStorageCode);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyPermissions(this);

        new RecycleCenterFinder().execute("calculator", "08902");
        btn = findViewById(R.id.button1);
        imageView = findViewById(R.id.imageView);
        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                openCameraIntent();

            }
        });
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.pennapps.xx.recycleme.fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = new File(storageDir, "scan.jpg");

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public ArrayList<RecycleCenter> getSortedRecycleCenters(String imageFilePath, Object startLocation, Object endLocation) {
        ArrayList<RecyclableObject> items = new ArrayList<>();

        try {
            ArrayList<String> itemLabels = new VisionProcessor(getApplicationContext(), imageFilePath).execute().get();
            for (String label : itemLabels) {
                Log.i("item", label);
            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);
        imageView.setImageBitmap(bitmap);
        getSortedRecycleCenters(imageFilePath, null, null);
    }

}
