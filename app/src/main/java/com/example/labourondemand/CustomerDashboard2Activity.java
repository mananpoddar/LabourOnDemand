package com.example.labourondemand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustomerDashboard2Activity extends CustomerMainActivity {

    private Customer customer;
    private RecyclerView recyclerView;
    private LabourerDashboardAdapter customerDashboardAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Services services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_customer_dashboard2, null, false);
        drawerLayout.addView(view, 0);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        customer = (Customer) getIntent().getExtras().get("customer");
        recyclerView = view.findViewById(R.id.customer_dashboard2_rv);
        customerDashboardAdapter = new LabourerDashboardAdapter(getApplicationContext(),1,new ArrayList<Labourer>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(customerDashboardAdapter);
        recyclerView.setHasFixedSize(false);
        fetchLabourResponses();
    }

    private void fetchLabourResponses() {

        firebaseFirestore.collection("services").document(customer.getCurrentService()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        services = documentSnapshot.toObject(Services.class);
                        if(services.getLabourerResponses() != null) {

                            for (final String s : services.getLabourerResponses().keySet()) {

                                firebaseFirestore.collection("labourer").document(s)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Labourer labourer = new Labourer();
                                        labourer.setCurrentServicePrice(services.getLabourerResponses().get(s));
                                        labourer = documentSnapshot.toObject(Labourer.class);
                                        customerDashboardAdapter.addedFromCustomer(labourer);

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        }else{

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
