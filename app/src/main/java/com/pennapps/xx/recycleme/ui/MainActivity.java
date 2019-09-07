package com.pennapps.xx.recycleme.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.pennapps.xx.recycleme.R;
import com.pennapps.xx.recycleme.data.RecycleCenterFinder;

public class MainActivity extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new RecycleCenterFinder().execute("calculator", "08902");
        btn=(Button)findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(i);
            }
        });
    }

}
