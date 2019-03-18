package com.example.labourondemand;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerJobsAdapter extends RecyclerView.Adapter<CustomerJobsAdapter.MyViewHolder> {
    Context context;
    private ArrayList<LabourerFinal> labourers;
    private ServicesFinal service;

    public CustomerJobsAdapter(Context context, ServicesFinal service){
        this.context = context;
        this.service = service;
        this.labourers = service.getLabourers();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, tags, landmark, distance;
        public CircleImageView photo;
        public ConstraintLayout constraintLayout;
        public Button accept;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_service_tv_name);
            tags = view.findViewById(R.id.item_service_tv_tags);
            landmark = view.findViewById(R.id.item_service_tv_landmark);
            distance = view.findViewById(R.id.item_service_tv_distance);
            photo = view.findViewById(R.id.item_service_civ_photo);
            constraintLayout = view.findViewById(R.id.item_service_cl);
            accept = view.findViewById(R.id.item_service_btn_accept);
        }
    }

    @NonNull
    @Override
    public CustomerJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_labourer, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerJobsAdapter.MyViewHolder holder, int position) {
        final LabourerFinal labourer = labourers.get(position);
        Glide.with(context).load(labourer.getImage()).into(holder.photo);
        holder.name.setText(labourer.getName());
        holder.landmark.setText( String.valueOf(service.getLabourerResponses().get(labourer.getId())));
        holder.accept.setText("Accept");
//        if(labourer.getAverageRating() == null){
//            holder.tags.setText("No Rating");
//        }else {
//            holder.tags.setText(String.valueOf(labourer.getAverageRating()));
//        }
    }

    @Override
    public int getItemCount() {
        return labourers.size();
    }
}
