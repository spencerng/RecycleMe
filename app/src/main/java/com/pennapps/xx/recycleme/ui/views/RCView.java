package com.pennapps.xx.recycleme.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.models.RecycleCenter;

public class RCView extends RelativeLayout {
    RecycleCenter center;



    public RCView (Context c, AttributeSet attrs){
        super(c, attrs);

        initializeViews(c);

    }

    public RecycleCenter getRC() {
        return center;
    }

    public void setRC(RecycleCenter rc) {
        this.center = rc;
    }

    public void populateView(){
    }



    private void initializeViews(Context c){
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.rc_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }
}
