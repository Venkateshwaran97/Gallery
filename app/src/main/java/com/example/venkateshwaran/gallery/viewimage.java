package com.example.venkateshwaran.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class viewimage extends Activity {
    ArrayList<image> f = new ArrayList<image>();
    ImageView y;
    private Button b1,b2;
    int position;
    Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewimage);
        Intent i = getIntent();
         position = i.getExtras().getInt("i");
         f = (ArrayList<image>)getIntent().getSerializableExtra("e");

       y = (touchimageview) findViewById(R.id.img);
        b2 = (Button) findViewById(R.id.button);

        b1 =(Button)findViewById(R.id.button2);



         bmp = BitmapFactory.decodeFile(f.get(position).getTitle());


        y.setImageBitmap(bmp);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position>0) position--;

                bmp = BitmapFactory.decodeFile(f.get(position).getTitle());


                y.setImageBitmap(bmp);

            }});
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<f.size())position++;

                bmp = BitmapFactory.decodeFile(f.get(position).getTitle());


                y.setImageBitmap(bmp);

            }});


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_viewimage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }







}
