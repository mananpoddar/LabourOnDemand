package com.example.labourondemand;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class LabourerDashboardAdapter extends RecyclerView.Adapter<LabourerDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Services> services;
    private ArrayList<Labourer> labourers;
    private LabourerMainActivity labourerMainActivity;
    private CustomerMainActivity customerMainActivity;
    private Integer type;

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


    public LabourerDashboardAdapter(Context context, ArrayList<Services> services, Integer type) {
        this.context = context;
        this.services = services;
        this.labourerMainActivity = (LabourerMainActivity)context;
        this.type = type;
    }

    public LabourerDashboardAdapter(Context context, Integer type, ArrayList<Labourer> labourers){
        this.context = context;
        this.type = type;
        this.labourers = labourers;
        this.customerMainActivity = (CustomerMainActivity) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_service, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(final LabourerDashboardAdapter.MyViewHolder holder, final int position) {

        if(type == 0) {
            holder.accept.setVisibility(View.GONE);
            final Services service = services.get(position);
            holder.name.setText(service.getCustomer().getName());
            holder.landmark.setText(service.getLandmark());
            Glide.with(context).load(service.getCustomer().getImage()).into(holder.photo);

            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailServiceActivity.class);
                    intent.putExtra("service", service);
                    context.startActivity(intent);
                }
            });

        }else{

            final Labourer labourer = labourers.get(position);
            Glide.with(context).load(labourer.getImage()).into(holder.photo);
            holder.name.setText(labourer.getName());
            holder.tags.setText("Price : "+ labourer.getCurrentServicePrice());

            holder.landmark.setVisibility(View.GONE);
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to Review Activity

                    Intent intent = new Intent(context,ReviewActivity.class);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(type == 0) {
            return services.size();
        }else{
            return labourers.size();
        }
    }

    public void added(Services c){
        Log.d("added @ adapter", services.size()+"s");
        services.add(c);
        notifyItemInserted(services.indexOf(c));
    }

    public void addedFromCustomer(Labourer labourer){
        Log.d("addedFromCustomer ", labourers.size()+"s");
        labourers.add(labourer);
        notifyItemInserted(labourers.indexOf(labourer));
    }
}