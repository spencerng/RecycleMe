package com.pennapps.xx.recycleme.ui;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.data.GMapV2Direction;
import com.pennapps.xx.recycleme.models.RecycleCenter;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class PathFragment extends Fragment implements OnMapReadyCallback {

    private ArrayList<RecycleCenter> centers;
    private Location start, end;

    public PathFragment(Location start, Location end, ArrayList<RecycleCenter> centersList) {
        this.start = start;
        this.end = end;
        centers = centersList;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.results_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return layout;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        ArrayList<LatLng> locs = new ArrayList<>();
        LatLng startLatLng = new LatLng(start.getLatitude(), start.getLongitude());
        LatLng endLatLng = new LatLng(end.getLatitude(), end.getLongitude());
        locs.add(startLatLng);
        for (RecycleCenter center : centers) {
            locs.add(center.getLatLng(getContext()));
            googleMap.addMarker(new MarkerOptions().position(center.getLatLng(getContext())).title(center.getName()));
        }

        googleMap.addMarker(new MarkerOptions().position(startLatLng).title("Current Location"));
        googleMap.addMarker(new MarkerOptions().position(endLatLng).title("Work"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));
        try {
            for (int i = 0; i < locs.size() - 1; i++) {


                GMapV2Direction md = new GMapV2Direction(locs.get(i), locs.get(i + 1), GMapV2Direction.MODE_WALKING);

                Document doc = md.execute().get();

                ArrayList<LatLng> directionPoint = md.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(50).color(
                        Color.RED);

                rectLine.add(locs.get(i), locs.get(i + 1));


//                for (int j = 0; j < directionPoint.size(); j++) {
//                    rectLine.add(directionPoint.get(j));
//                }
                Polyline polyline = googleMap.addPolyline(rectLine);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
