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

        try {
            for (int i = 0; i < centers.size() - 1; i++) {
                LatLng startLatLng = centers.get(i).getLatLng(getContext());
                LatLng endLatLng = centers.get(i + 1).getLatLng(getContext());
                if (i == 0) {
                    startLatLng = new LatLng(start.getLatitude(), start.getLongitude());
                    endLatLng = centers.get(0).getLatLng(getContext());
                }
                if (i == centers.size() - 2) {
                    endLatLng = new LatLng(end.getLatitude(), end.getLongitude());
                }

                googleMap.addMarker(new MarkerOptions().position(startLatLng).title("Current Location"));
                googleMap.addMarker(new MarkerOptions().position(endLatLng).title("Final Destination"));


                GMapV2Direction md = new GMapV2Direction(startLatLng, endLatLng, GMapV2Direction.MODE_DRIVING);

                Document doc = md.execute().get();

                ArrayList<LatLng> directionPoint = md.getDirection(doc);
                PolylineOptions rectLine = new PolylineOptions().width(3).color(
                        Color.RED);

                for (int j = 0; j < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                Polyline polyline = googleMap.addPolyline(rectLine);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
