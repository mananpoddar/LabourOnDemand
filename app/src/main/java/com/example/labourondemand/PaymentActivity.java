package com.example.labourondemand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ServicesFinal servicesFinal;
    private Button pay;
    private FirebaseFirestore firebaseFirestore;
    private CustomerFinal customerFinal;
    private RecyclerView recyclerView;
    private LabourerAdapter labourerAdapter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = findViewById(R.id.customer_wallet_tb);
        toolbar.setTitle("Make Payment");
        setSupportActionBar(toolbar);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        firebaseFirestore = FirebaseFirestore.getInstance();
        servicesFinal = (ServicesFinal) getIntent().getExtras().getSerializable("services");
        customerFinal = (CustomerFinal) getIntent().getExtras().getSerializable("customer");

        recyclerView = findViewById(R.id.review_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        labourerAdapter = new LabourerAdapter(this,servicesFinal);
        recyclerView.setAdapter(labourerAdapter);


        pay = findViewById(R.id.payment_pay_btn);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseFirestore.collection("customer").document(customerFinal.getId())
                        .update("wallet", customerFinal.getWallet() - (servicesFinal.getNumOfLabourers() * servicesFinal.getCustomerAmount()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                customerFinal.setWallet(customerFinal.getWallet() - (servicesFinal.getNumOfLabourers() * servicesFinal.getCustomerAmount()));

                                for (LabourerFinal labourerFinal : servicesFinal.getLabourers()) {
                                    firebaseFirestore.collection("labourer").document(labourerFinal.getId())
                                            .update("wallet", labourerFinal.getWallet() + servicesFinal.getCustomerAmount())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    if(servicesFinal.getLabourers().indexOf(labourerFinal) == servicesFinal.getLabourers().size()-1)
                                                    {   progressBar.setVisibility(View.GONE);
                                                        Intent intent = new Intent(PaymentActivity.this, ReviewActivity.class);
                                                        intent.putExtra("customer",customerFinal);
                                                        intent.putExtra("services",servicesFinal);
                                                        startActivity(intent);
                                                        finish();
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
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
            }
        });

    }
}
