package com.example.labourondemand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class LabourerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CardVIewJobs.OnFragmentInteractionListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String tag = LabourerHomeActivity.class.getName();
    private BottomNavigationView navigation;
    private LabourerFinal labourerFinal;
    private ArrayList<Bundle> bundles = new ArrayList<>();
    private ArrayList<CardVIewJobs> cardViewJobs = new ArrayList<CardVIewJobs>();
    private ViewPagerAdapterLabourer viewPagerAdapterLabourer; //= new ViewPagerAdapter(getSupportFragmentManager());
    private ViewPager viewPager;
    private TabLayout tabLayout;

    static LabourerHomeActivity instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static LabourerHomeActivity getInstance() {
        return instance;
    }
    TextView textView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labourer_home);

        instance = this;

        textView = findViewById(R.id.labourer_home_no_response_tv);

        /*Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(LabourerHomeActivity.this, "you nsna", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();*/

        this.startService(new Intent(this,MyLocationService .class));

        toolbar = findViewById(R.id.labourer_home_tb);
        drawerLayout = findViewById(R.id.labourer_home_dl);
        navigationView = findViewById(R.id.labourer_home_nv);
        navigation = findViewById(R.id.bottom_nav_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(2);
        navigationView.setNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tabLayout = findViewById(R.id.labourer_home_tl);
        viewPager = findViewById(R.id.labourer_home_vp);
        viewPagerAdapterLabourer = new ViewPagerAdapterLabourer(getSupportFragmentManager());
        CardVIewJobs c = new CardVIewJobs();

        viewPagerAdapterLabourer.addFragment(c, "deddescs");
        viewPagerAdapterLabourer.addFragment(new CardVIewJobs(), "cdc");
        viewPager.setAdapter(viewPagerAdapterLabourer);
        tabLayout.setupWithViewPager(viewPager);

        labourerFinal = (LabourerFinal) getIntent().getExtras().get("labourer");
        Log.d("labourerHome", labourerFinal.toString());
//
//        if (labourerFinal.getCurrentService() == null) {
//            Log.d("tagggg",labourerFinal.getSkill()+"!");
//            fetchServices();
//        }

        fetchFromFirebase();
//        if (labourerFinal.getName() == null) {
//            fetchFromFirebase();
//        } else {
//            fetchServices();
//        }

/*
        for (int i = 0; i < 5; i++) {
            bundles.add(new Bundle());
            bundles.get(i).putString("key", Integer.toString(i));
        }


        for (int i = 0; i < 5; i++) {
            cardViewJobs.add(new CardVIewJobs());
            cardViewJobs.get(i).setArguments(bundles.get(i));
        }


        //viewPagerAdapter.addFragment(hello1, "Hello1");
        for (int i = 0; i < 5; i++) {
            viewPagerAdapter.addFragment(cardViewJobs.get(i), "hello" + i);
        }
        //viewPagerAdapter.addFragment(hello2, "Hello2");*/

        //viewPager.setAdapter(viewPagerAdapterLabourer);

    }

    private void updateLocation() {
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
        
    }

    public void update(final String value)
    {

        LabourerHomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("location brodcast ",value+"!");
                textView.setText(value);
            }
        });
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this,MyLocation.class);
        intent.setAction(MyLocation.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10f);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:

                    return true;
                case R.id.bottom_navigation_history:

                    return true;
                case R.id.bottom_navigation_jobs:

                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle menu_bottom_navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_history) {
            //Toast.makeText(this,"History yet to be Developed",)
            Intent intent = new Intent(this, PreviousActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_jobs) {

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            /*Bundle bundle = new Bundle();
            bundle.putParcelable("labourer",labourer);*//*
            intent.putExtra("user", labourer);
            intent.putExtra("type","labourer");
            Log.d(tag, "labourer : " + labourer.getAddressLine1());*/
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            //Intent settings = new Intent(LabourerMainActivity.this,SettingsActivity.class);
            //startActivity(settings);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {

            firebaseAuth.signOut();
            Intent intent = new Intent(LabourerHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_wallet) {
            /*Intent intent = new Intent(CustomerHistoryActivity.this, NAME.class);
            startActivity(intent);*/
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void fetchServices() {

        for (String skill : labourerFinal.getSkill())
            //add isApplyable later
            firebaseFirestore.collection("services").whereEqualTo("skill", skill).whereEqualTo("status", "incoming").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                Log.d("tag", labourerFinal.getSkill() + "!" + documentSnapshot.get("skill") + "!" + documentSnapshot.getData().toString());
                                Log.d("service fetched", documentSnapshot.getString("serviceId"));
                                ServicesFinal servicesFinal = documentSnapshot.toObject(ServicesFinal.class);
                                //servicesFinal.setCustomerUID(documentSnapshot.getString("customerUID"));
                                Log.d("I don't know", "+" + servicesFinal.getCustomerUID() + "!");
                                //final ServicesFinal finalServices = servicesFinal;
                                //ServicesFinal finalServicesFinal = servicesFinal;
                                //firebaseFirestore.collection("customer").document(servicesFinal.getCustomerUID())
                                firebaseFirestore.collection("customer").document(servicesFinal.getCustomerUID()).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                // To add code to add to viewPager
                                               /* bundles.add(new Bundle());
                                                bundles.get(j).putString("key", servicesFinal.getCustomerUID());
                                                cardViewJobs.add(new CardVIewJobs());
                                                cardViewJobs.get(j).setArguments(bundles.get(j);
                                                viewPagerAdapter.addFragment(cardViewJobs.get(j), "hello" + j);
                                                j = j +1;*/
                                                //
                                                //finalServices.setCustomer(documentSnapshot.toObject(Customer.class));
                                                //dashboardAdapter.added(finalServices);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(tag, "error fetchService2 : " + e.toString());
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(tag, "error fetchService1 : " + e.toString());
                        }
                    });
    }

    //Later to be deleted

    private void fetchFromFirebase() {

        fetchServices();

       /* firebaseFirestore.collection("labourer").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //labourer = new Labourer();
                        if (documentSnapshot.getData() != null) {
                            labourerFinal = documentSnapshot.toObject(LabourerFinal.class);
                            Log.d("Labourer info fetched", firebaseAuth.getUid() + "!");

                            if (labourerFinal.getCurrentService() == null) {
                                Log.d("tagggg", labourerFinal.getSkill() + "!");
                                fetchServices();
                            } else {

                            }

                        } else {
                            Log.d(tag, "null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/
    }

}

