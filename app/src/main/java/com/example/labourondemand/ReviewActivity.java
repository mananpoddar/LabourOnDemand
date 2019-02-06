package com.example.labourondemand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ReviewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Services services = new Services();
    private Boolean isLabourer = false, isEditting;
    private Labourer labourer = new Labourer();
    private FirebaseFirestore firebaseFirestore;
    private TextInputLayout feedback;
    private RatingBar ratingBar;
    private Button submitButton;
    private TextView ratingTextView;
    private String TAG = ReviewActivity.class.getName();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        toolbar = findViewById(R.id.review_tb);
        drawerLayout =  findViewById(R.id.review_dl);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        services = (Services) getIntent().getExtras().get("service");
        ratingBar = findViewById(R.id.review_rb);
        submitButton = findViewById(R.id.review_btn_submit);
        ratingTextView = findViewById(R.id.rating_text_view);
        feedback = findViewById(R.id.feedback);
        progressBar = findViewById(R.id.progress_bar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_ATOP);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0.0)
                    ratingTextView.setText("Please Enter Rating ");
                else {
                    if (feedback.getEditText().getText().toString().compareTo("") == 0)
                        feedback.setError("Please enter text");
                    else if (feedback.getEditText().getText().toString().length() > 14)
                        feedback.setError("Length exceeding 14");
                    else {
                        feedback.setError(null);
                        ratingTextView.setText("Ratings : " + ratingBar.getRating());
                        submitButton.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        submitReview();
                    }
                }
            }
        });
    }

    public void submitReview() {

        String feedbackString = feedback.getEditText().getText().toString();
        float ratingFloat = ratingBar.getRating();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("rating", ratingFloat);
        map.put("feedback",feedbackString);

        if (user != null){

            firebaseFirestore.collection("services").document(services.getServiceID()).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "SUCCESS");

                            progressBar.setVisibility(View.GONE);
                            submitButton.setText("Thank You!");
                            submitButton.setVisibility(View.VISIBLE);
                            submitButton.setEnabled(false);
                            submitButton.setTextColor(getResources().getColor(R.color.black));
                            submitButton.setBackgroundColor(
                                    getResources().getColor(android.R.color.transparent));
                            //startActivity(new Intent(ReviewActivity.this,CustomerMainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@android.support.annotation.NonNull Exception e) {
                            //Log.d(TAG, e.toString());
                            Log.d(TAG, e.toString());
                            progressBar.setVisibility(View.GONE);
                            submitButton.setVisibility(View.VISIBLE);
                        }
                    });
        }

        }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
