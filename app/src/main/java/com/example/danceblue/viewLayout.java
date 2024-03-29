//Created by Adrian Carideo, Joe Clements, Kendall Conley
//Copyright © 2019 DanceBlue. All rights reserved.
package com.example.danceblue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//This class handles displaying the db_floormap.png image.
// Layout can be found in activity_view_layout.xml
public class viewLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_layout);
    }

    //when the activity is left, end it to save resources
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
