package com.example.labourondemand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ReviewActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private SessionManager session;
    private RatingBar ratingBar;
    private Button submitButton;
    private ImageView servicePhoto;
    private TextView title, endTime, amount;
    private ServicesFinal servicesFinal;
    private RecyclerView recyclerView;
    private LabourerAdapter labourerAdapter;
    private FirebaseFirestore firebaseFirestore;
    private TextInputLayout feedback;
    private ProgressBar progressBar;
    private CustomerFinal customerFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review2);

        servicesFinal = (ServicesFinal) getIntent().getExtras().getSerializable("service");
        customerFinal = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        toolbar = findViewById(R.id.review_tb);
        toolbar.setTitle("Please Review");
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        recyclerView = findViewById(R.id.review_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        labourerAdapter = new LabourerAdapter(this,servicesFinal);
        recyclerView.setAdapter(labourerAdapter);

        ratingBar = findViewById(R.id.review_rb);
        submitButton = findViewById(R.id.review_btn_submit);
        title = findViewById(R.id.review_title_tv);
        endTime = findViewById(R.id.review_end_time);
        amount = findViewById(R.id.review_cost_tv);
        servicePhoto = findViewById(R.id.review_iv);
        feedback = findViewById(R.id.review_feedback_til);
        progressBar = findViewById(R.id.review_pb);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_ATOP);

        if(servicesFinal.getSkill().equals("Carpenter")){
            //servicePhoto.setImageDrawable(R.drawable.ic_carpenter_tools_colour);
        }

        title.setText(servicesFinal.getTitle());
        amount.setText(String.valueOf(servicesFinal.getNumOfLabourers()*servicesFinal.getCustomerAmount()));
        endTime.setText(servicesFinal.getEndTime());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0.0)
                    //ratingTextView.setText("Please Enter Rating ");
                    ratingBar.setIsIndicator(false);
                else {
                    if (feedback.getEditText().getText().toString().compareTo("") == 0)
                        feedback.setError("Please enter text");
                    else if (feedback.getEditText().getText().toString().length() > 100)
                        feedback.setError("Length exceeding 100");
                    else {
                        feedback.setError(null);
                        //ratingTextView.setText("Ratings : " + ratingBar.getRating());
                        submitButton.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        submitReview();
                    }
                }
            }
        });

    }

    private void submitReview() {

        String feedbackString = feedback.getEditText().getText().toString();
        float ratingFloat = ratingBar.getRating();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("rating", ratingFloat);
        map.put("feedback",feedbackString);

        firebaseFirestore.collection("services").document(servicesFinal.getServiceId())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("success review","yes");
                        Intent intent = new Intent(ReviewActivity2.this,CustomerHomeActivity.class);
                        intent.putExtra("customer",customerFinal);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("failure",e.toString());
                    }
                });
    }
}
