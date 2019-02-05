package com.example.labourondemand;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

class ReviewActivity extends CustomerMainActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_profile, null, false);
        drawerLayout.addView(view, 0);



    }
}
