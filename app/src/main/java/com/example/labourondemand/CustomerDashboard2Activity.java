package com.example.labourondemand;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerDashboard2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private android.support.design.widget.NavigationView navigationView;
    private EditText description, addressLine1, addressLine2, landmark, city;
    private Button submitButton;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Uri filePath;
    private FloatingActionButton floatingActionButton;
    private FirebaseStorage storage;
    private Uri mainImageURI;
    private ArrayList<Uri> pictures = new ArrayList<>();
    private StorageReference storageReference;
    private Slide slide;
    private Bitmap compressedImageFile;
    private EditText amount;
    private Button submit;
    private String TAG = ProfileActivity.class.getName();
    private Boolean isLabourer = false, isEditting;
    private Labourer labourer = new Labourer();
    private TextView name;
    private CircleImageView photo;
    private ProgressBar progressBar;
    private String type;
    private Customer customer;
    private RecyclerView recyclerView;
    private DashboardAdapter customerDashboardAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Services services = new Services();
    private TextView noResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard2);

        toolbar = findViewById(R.id.customer_dashboard2_tb);
        drawerLayout =  findViewById(R.id.customer_dashboard2_dl);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.customer_dashboard2_nav);
        navigationView.setNavigationItemSelectedListener(this);
        noResponse = findViewById(R.id.customer_dashboard2_tv_no_response);
        customer = (Customer) getIntent().getExtras().get("customer");
        //services = (Services) getIntent().getExtras().get("services");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.customer_dashboard2_rv);
        customerDashboardAdapter = new DashboardAdapter(getApplicationContext(),1,new ArrayList<Labourer>(), services);
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

                        Log.d("service in dashboard22", customer.getCurrentService());
                        services = documentSnapshot.toObject(Services.class);
                        services.setServiceID(customer.getCurrentService());
                        Log.d("service in dashboard22", services.getAddressLine1()+"!");
                        if(services.getLabourerResponses() != null) {
                            noResponse.setVisibility(View.GONE);
                             customerDashboardAdapter.setServiceAndCustomer(services, customer);
                            Log.d("customerboardAdapter",services.getLabourerResponses().toString());
                            for (final String s : services.getLabourerResponses().keySet()) {
                                firebaseFirestore.collection("labourer").document(s)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Labourer labourer = new Labourer();
                                        labourer = documentSnapshot.toObject(Labourer.class);
                                        labourer.setCurrentServicePrice(services.getLabourerResponses().get(s));
                                        customerDashboardAdapter.addedFromCustomer(labourer);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }

                        }else{
                            noResponse.setText("No Response from any Labourers");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifications) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle menu_bottom_navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, PreviousActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            /*Bundle bundle = new Bundle();
            bundle.putParcelable("labourer",labourer);*/
            intent.putExtra("user", customer);
            intent.putExtra("type","customer");
            //Log.d(tag, "labourer : " + labourer.getAddressLine1());
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if(id == R.id.nav_logout){

            firebaseAuth.signOut();
            Intent intent = new Intent(CustomerDashboard2Activity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_jobs) {
            Intent intent = new Intent(this, CustomerJobsActivity.class);
            intent.putExtra("user", customer);
            intent.putExtra("type","customer");
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
