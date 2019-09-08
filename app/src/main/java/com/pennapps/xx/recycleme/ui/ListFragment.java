package com.pennapps.xx.recycleme.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.models.RecycleCenter;
import com.pennapps.xx.recycleme.ui.views.RCView;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    public ListFragment(){}


    ArrayList<RecycleCenter> centers;

    RelativeLayout centerContainer;

    public ListFragment(ArrayList<RecycleCenter> centersList) {
        centers = centersList;
    }


    public static ListFragment newInstance() {
        return new ListFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView rl = (ScrollView) inflater.inflate(R.layout.results_list, container, false);
        centerContainer = rl.findViewById(R.id.centerContainer);

        int lastId = 0;
        boolean firstTime = true;


        int num = 1;
        for (RecycleCenter center : centers) {
            RCView av = new RCView (getContext(), null);
            int currentId = View.generateViewId();
            av.setId(currentId);
            av.setRC(center);
            TextView name = av.findViewById(R.id.title);
            TextView address = av.findViewById(R.id.address);
            TextView items = av.findViewById(R.id.items);
            TextView distance = av.findViewById(R.id.distance);
            TextView number = av.findViewById(R.id.number);

            name.setText(center.getName());
            address.setText(center.getAddress());
            items.setText("Lightbulb, Cable");
            distance.setText(center.getDrivingDistance(MainActivity.currentLocation) + "mi");
            //distance.setText("2.0 mi");
            number.setText(Integer.toString(num++));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (!firstTime)
                params.addRule(RelativeLayout.BELOW, lastId);
            lastId = currentId;


            centerContainer.addView(av, params);


            firstTime = false;
        }

        return rl;

    }




}
