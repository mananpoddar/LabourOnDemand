package com.example.labourondemand;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class    CheckingActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private String TAG = CheckingActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(CheckingActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Log.d(TAG,"send to login : Checking");
            sendToLogin();
        } else {
            current_user_id = mAuth.getCurrentUser().getUid();
            Log.d(TAG,current_user_id);

            firebaseFirestore.collection("uid").document(current_user_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.get("type").equals("customer")){
                                Intent customer = new Intent(CheckingActivity.this, CustomerMainActivity.class);
                                Log.d(TAG,"customer");
                                startActivity(customer);
                                finish();
                            }else{
                                Log.d(TAG,"labourer");
                                Intent customer = new Intent(CheckingActivity.this, LabourerMainActivity.class);
                                startActivity(customer);
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"error : " + e.toString());
                        }
                    });
        }
    }
}
