package com.example.labourondemand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LabourerHistoryRVAdapter extends RecyclerView.Adapter<LabourerHistoryRVAdapter.ViewHolder> {

    private static final String TAG = "LabourerHistoryRVAdapter";

    private Context context;
    private ArrayList<ServicesFinal> services;
//    private ServicesFinal services;
    public LabourerHistoryRVAdapter(Context context, ArrayList<ServicesFinal> services) {
        this.services = services;
        this.context = context;
    }

    @NonNull
    @Override
    public LabourerHistoryRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_labourer_history_item, parent, false);
        LabourerHistoryRVAdapter.ViewHolder holder = new LabourerHistoryRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LabourerHistoryRVAdapter.ViewHolder viewHolder, int position) {
        ServicesFinal service = services.get(viewHolder.getAdapterPosition());
        viewHolder.jobcost.setText(String.valueOf(service.getCustomerAmount()));
        viewHolder.date.setText(service.getEndTime());
//        viewHolder.jobTitle.setText(service.getJobTitle());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("labourer");
        Query query = databaseReference.child(service.getCustomerUID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Customer customer = snapshot.getValue(Customer.class);
                        Glide.with(context).load(customer.getImage()).into(viewHolder.labourerImage);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView jobImage, labourerImage;
        TextView day, date, time, jobcost, jobTitle;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobImage = itemView.findViewById(R.id.job_image);
            labourerImage = itemView.findViewById(R.id.labour_image);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            jobcost = itemView.findViewById(R.id.job_cost);
            jobTitle = itemView.findViewById(R.id.job_title);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
