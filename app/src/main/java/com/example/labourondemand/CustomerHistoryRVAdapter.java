package com.example.labourondemand;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerHistoryRVAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "CustomerHistoryRVAdapter";

    private ArrayList<String> mDays = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayList<String> mTimes = new ArrayList<>();
    private ArrayList<String> cost = new ArrayList<>();
    private ArrayList<String> jobTitles = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();

    public CustomerHistoryRVAdapter(ArrayList<String> mDays, ArrayList<String> mDates, ArrayList<String> mTimes, ArrayList<String> cost, ArrayList<String> jobTitles, ArrayList<String> mImages) {
        this.mDays = mDays;
        this.mDates = mDates;
        this.mTimes = mTimes;
        this.cost = cost;
        this.jobTitles = jobTitles;
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView jobImage,labourerImage;
        TextView day,date,time,jobcost;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobImage = itemView.findViewById(R.id.job_image);
            labourerImage = itemView.findViewById(R.id.labour_image);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            jobcost = itemView.findViewById(R.id.job_cost);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
