package com.pennapps.xx.recycleme.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.models.RecycleCenter;
import com.pennapps.xx.recycleme.ui.views.RCView;

import java.util.ArrayList;
import java.util.Arrays;

public class ListFragment extends Fragment {
    public ListFragment(){}
    ArrayList<String> items1 = new ArrayList<String>(Arrays.asList("Lightbulb", "Cable") );
    ArrayList<String> items2 = new ArrayList<String>(Arrays.asList("Shoe", "Electronics") );

    ArrayList<RecycleCenter> centers = new ArrayList<RecycleCenter>(Arrays.asList(new RecycleCenter
                    ("Center 1", "100 Technology Drive, Edison, NJ, 08837","Drop-Off", items1),
                new RecycleCenter("Center 2", "123 Ho Plaza, Ithaca, NY, 14853", "Drop-Off", items2)));
    RelativeLayout centerContainer;

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
        centerContainer = (RelativeLayout) rl.findViewById(R.id.centerContainer);

        int lastId = 0;
        boolean firstTime = true;
        Toast.makeText(getContext(), Integer.toString(centers.size()),Toast.LENGTH_SHORT).show();


        for (RecycleCenter center : centers) {
            RCView av = new RCView (getContext(), null);
            int currentId = View.generateViewId();
            av.setId(currentId);
            av.setRC(center);
            TextView name = (TextView) av.findViewById(R.id.title);
            TextView address = (TextView) av.findViewById(R.id.address);
            TextView items = (TextView) av.findViewById(R.id.items);
            TextView distance = (TextView) av.findViewById(R.id.distance);

            name.setText(center.getName());
            address.setText(center.getAddress());
            items.setText("Items List");
            //distance.setText(center.getDrivingDistance(new Object()) + "mi");
            distance.setText("2.0 mi");

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
