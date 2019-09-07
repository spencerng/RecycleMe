package com.pennapps.xx.recycleme.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.data.RecycleCenterFinder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new RecycleCenterFinder().execute("calculator", "08902");
    }
}
