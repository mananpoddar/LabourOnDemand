package com.example.labourondemand;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class LabourerPreviousActivity extends LabourerMainActivity{


    //vars (demo)
    private ArrayList<String> mCustomerNames = new ArrayList<>();
    private ArrayList<String> mFroms = new ArrayList<>();
    private ArrayList<String> mTos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labourer_previous);

        initText();
        initRecyclerView();

    }

    private void initText() {
        //Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mCustomerNames.add("Shanthanu");
        mCustomerNames.add("Varun");
        mCustomerNames.add("Narayan");
        mCustomerNames.add("Prajwal");
        mCustomerNames.add("Manan");
        mCustomerNames.add("Srivatsan");
        mCustomerNames.add("dummy 1");
        mCustomerNames.add("dummy 2");
        mCustomerNames.add("dummy 3");

        mFroms.add("Bombay");
        mFroms.add("Delhi");
        mFroms.add("Udupi");

        mTos.add("Udupi");
        mTos.add("Delhi");
        mTos.add("Bombay");

        for(int i = 0; i < 6; i++) {
            mFroms.add("location" + i);
            mTos.add("location" + (9+i));
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.labourerPrevious_rec_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mCustomerNames, mFroms, mTos);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
