package com.example.labourondemand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LabourerAdapter extends RecyclerView.Adapter<LabourerAdapter.MyViewHolder> {
    private Context context;
    private ServicesFinal servicesFinals;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CircleImageView photo;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_service_tv_name);
            photo = view.findViewById(R.id.item_service_civ_photo);
        }
    }


    public LabourerAdapter(Context context, ServicesFinal servicesFinals) {
        this.context = context;
        this.servicesFinals = servicesFinals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_labourer, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        LabourerFinal labourer = servicesFinals.getLabourers().get(position);
        holder.name.setText(labourer.getName());

        Glide.with(context).load(labourer.getImage()).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return this.servicesFinals.getLabourers().size();
    }

  /*  public void added(Services c){
        Log.d("added @ adapter", servicesArrayList.size()+"s");
        servicesArrayList.add(c);
        notifyItemInserted(servicesArrayList.indexOf(c));
    }

    public void addedFromCustomer(Labourer labourer){
        Log.d("addedFromCustomer ", labourers.size()+"s");
        labourers.add(labourer);
        notifyItemInserted(labourers.indexOf(labourer));
    }

    public void setServiceAndCustomer(Services service, Customer customer){
        this.service = service;
        this.customer = customer;
    }*/
}