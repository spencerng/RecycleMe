package com.pennapps.xx.recycleme.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.data.RecycleCenterFinder;
import com.pennapps.xx.recycleme.data.VisionProcessor;
import com.pennapps.xx.recycleme.models.RecyclableObject;
import com.pennapps.xx.recycleme.models.RecycleCenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button btn;
    Button settings;
    public int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    String imageFilePath;
    TextView resultView;
    String zipCode;
    double latitude, longitude;
    Location currentLocation;
    List<FirebaseVisionImageLabel> labels;
    boolean locationFetched, labelsFetched;

    private static void verifyPermissions(Activity activity) {
        // Check if the app has write permission
        int[] permissions = new int[]{ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA), ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)};

        for (int permission : permissions) {
            if (permission != PackageManager.PERMISSION_GRANTED) {

                // App doesn't have permission so prompt the user
                int requestStorageCode = 1;
                String[] storagePermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};

                ActivityCompat.requestPermissions(activity, storagePermissions, requestStorageCode);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyPermissions(this);


        locationFetched = false;
        labelsFetched = false;

        btn = findViewById(R.id.button1);
        settings = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        resultView = findViewById(R.id.resultView);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                openCameraIntent();
            }
        });
        settings.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(myIntent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);
        imageView.setImageBitmap(bitmap);
        getLabels(imageFilePath);
        fetchCurrentLocation();
    }

    public void getLabels(String imageFilePath) {
        final ArrayList<RecyclableObject> items = new ArrayList<>();

        VisionProcessor vp = new VisionProcessor(getApplicationContext(), imageFilePath);

        vp.process(new VisionProcessor.Callback() {
            @Override
            public void displayLabels(final String displayText) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultView.setText(displayText);
                    }
                });
            }

            @Override
            public void fetchItemLabels(List<FirebaseVisionImageLabel> itemLabels) {
                try {
                    labels = itemLabels;
                    if (locationFetched) {
                        fetchRecycleCenters();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void fetchCurrentLocation() {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    zipCode = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getPostalCode();
                    currentLocation = location;
                    if (labelsFetched) {
                        fetchRecycleCenters();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchRecycleCenters() {
        ArrayList<String> itemLabels = filterItems();
        ArrayList<RecyclableObject> recyclableObjects = new ArrayList<>();
        for (String itemLabel : itemLabels) {
            RecycleCenterFinder rcf = new RecycleCenterFinder(itemLabel, currentLocation.getLatitude(), currentLocation.getLongitude());
            try {
                ArrayList<RecycleCenter> recycleCenters = rcf.execute().get();
                recyclableObjects.add(new RecyclableObject(itemLabel, recycleCenters));
            } catch (Exception e) {

            }
        }

        ArrayList<RecycleCenter> centersToPass = getMinDistance(consolidateCenters(recyclableObjects), currentLocation, currentLocation);

        // Create intent filter here

    }

    public ArrayList<String> filterItems() {
        ArrayList<String> detectedItems = new ArrayList<>();

        for (FirebaseVisionImageLabel label : labels) {

        }
        return new ArrayList<>();
    }

    public ArrayList<RecycleCenter> consolidateCenters(ArrayList<RecyclableObject> recyclableObjects) {
        return new ArrayList<>();
    }

    public ArrayList<RecycleCenter> getMinDistance(ArrayList<RecycleCenter> centers, Location startPoint, Location endPoint) {
        return new ArrayList<>();
    }



}
