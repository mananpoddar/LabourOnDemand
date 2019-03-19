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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class CustomerJobsAdapter extends RecyclerView.Adapter<CustomerJobsAdapter.MyViewHolder> {
    Context context;
    private ArrayList<LabourerFinal> labourers;
    private ServicesFinal service;
    private ArrayList<String> selectedLabourersUID;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    public CustomerJobsAdapter(Context context, ServicesFinal service){
        this.context = context;
        this.service = service;
        this.labourers = service.getLabourers();
        if(service.getSelectedLabourerUID() == null){
            selectedLabourersUID = new ArrayList<>();
        }else {
            selectedLabourersUID = service.getSelectedLabourerUID();
        }
    }

    public ServicesFinal getService() {
        return service;
    }

    public void setService(ServicesFinal service) {
        Log.d("set Service in ada",service.toString());
        this.service = service;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, rating, price, distance;
        public CircleImageView photo;
        public ConstraintLayout constraintLayout;
        public Button accept;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_service_tv_name);
            rating = view.findViewById(R.id.item_service_tv_tags);
            price = view.findViewById(R.id.item_service_tv_landmark);
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
        view = inflater.inflate(R.layout.item_customer_job_labourer, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerJobsAdapter.MyViewHolder holder, int position) {

        final LabourerFinal labourer = labourers.get(position);
        Glide.with(context).load(labourer.getImage()).into(holder.photo);
        holder.name.setText(labourer.getName());
        holder.price.setText( String.valueOf(service.getLabourerResponses().get(labourer.getId())));
        holder.accept.setText("Accept");
        if(labourer.getAverageRating() == null){
            holder.rating.setText("No Rating");
        }else {
            holder.rating.setText(String.valueOf(labourer.getAverageRating()));
        }
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLabourersUID = service.getSelectedLabourerUID();
                if(selectedLabourersUID.size() < service.getNumOfLabourers()) {
                    service.getSelectedLabourerUID().add(labourer.getId());

                    //selectedLabourersUID.add(labourer.getId());
                    //labourer.setCurrentServiceId(service.getServiceId());

                    db.collection("service").document(service.getServiceId())
                            .update("selectedLabourerUID", FieldValue.arrayUnion(service.getServiceId()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: Service successfully updated.");

                                    db.collection("labourer").document(labourer.getId())
                                            .update("currentServiceId",service.getServiceId())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "onSuccess: Labourer successfully written.");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "onFailure: Error writing labourer back.", e);
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "onFailure: Service not updated.", e);
                                }
                            });



                    //logic for labourer (goes in a batch)
                    if(selectedLabourersUID.size() == service.getNumOfLabourers()) {
                        //service.setSelectedLabourerUID(selectedLabourersUID);
                        service.setApplyable(false);

                        db.collection("service").document(service.getServiceId())
                                .update("isApplyable",false )
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Service successfully updated.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "onFailure: Service not updated.", e);
                                    }
                                });
                    }
                }
                else if(selectedLabourersUID.size() == service.getNumOfLabourers()) {
                    Toast.makeText(context, "You have already selected required number of labourers.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return labourers.size();
    }


    public void clear(){
        labourers.clear();
        notifyDataSetChanged();
    }

    public void addLabourer(LabourerFinal labourer){
        Log.d("addedFromCustomer ", labourers.size()+"s");
        labourers.add(labourer);
        notifyItemInserted(labourers.indexOf(labourer));
    }

    public ArrayList<LabourerFinal> getLabourers() {
        return labourers;
    }

    public void swapItems(int positionA, int positionB) {
        LabourerFinal labourerA = labourers.get(positionA);
        labourers.set(positionA, labourers.get(positionB));
        labourers.set(positionB, labourerA);
        notifyDataSetChanged(); //to trigger onBindViewHolder
    }

    public Boolean isDone(){
        return !service.getApplyable();
    }
}
