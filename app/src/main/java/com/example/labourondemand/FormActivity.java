package com.example.labourondemand;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class FormActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Services services = new Services();

    private EditText description, addressLine1, addressLine2, landmark, city;
    private Button submitButton;
    private ViewPager viewPager;
    private Uri filePath;
    private FloatingActionButton floatingActionButton;
    private FirebaseStorage storage;
    private Uri mainImageURI;
    private ArrayList<Uri> pictures = new ArrayList<>();
    private Customer customer;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private Slide slide;
    private Bitmap compressedImageFile;
    private EditText amount;
    private Button submit;
    private String TAG = FormActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        toolbar = findViewById(R.id.form_tl);
        drawerLayout =  findViewById(R.id.form_dl);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        navigationView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.activity_form_vp);
        floatingActionButton = findViewById(R.id.activity_form_fab);
        description = findViewById(R.id.activity_form_et_description);
        addressLine1 = findViewById(R.id.activity_form_et_address1);
        addressLine2 = findViewById(R.id.activity_form_et_address2);
        landmark = findViewById(R.id.activity_form_et_landmark);
        city = findViewById(R.id.activity_form_et_city);
        submit = findViewById(R.id.activity_form_btn_submit);
        amount = findViewById(R.id.activity_form_et_amount);

        navigationView.setNavigationItemSelectedListener(this);
        services.setSkill(getIntent().getExtras().getString("skill"));
        customer = (Customer) getIntent().getExtras().get("customer");



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        slide = new Slide(this, new ArrayList<String>() );
        viewPager.setAdapter(slide);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_description(v);
            }
        });
    }

    public void save_description(View view) {

        String problem_description = description.getText().toString();
        String address_line_1_string = addressLine1.getText().toString();
        String address_line_2_string = addressLine2.getText().toString();
        String landmark_string = landmark.getText().toString();
        String city_string = city.getText().toString();
        String amonut_string = amount.getText().toString();

        if (problem_description.length() == 0 || address_line_1_string.length() == 0 || address_line_2_string.length() == 0 || landmark_string.length() == 0 || city_string.length() == 0 || amonut_string.length() == 0) {
            if (problem_description.length() == 0) {
                description.setError("Please enter a description before submitting");
            }
            if (address_line_1_string.length() == 0) {
                addressLine1.setError("Please enter an address before submitting");
            }
            if (address_line_2_string.length() == 0) {
                addressLine2.setError("Please enter an address before submitting");
            }
            if (landmark_string.length() == 0) {
                landmark.setError("Please enter a landmark before submitting");
            }
            if (city_string.length() == 0) {
                city.setError("Please enter a city before submitting");
            }
            if (amonut_string.length() == 0) {
                city.setError("Please enter amount before submitting");
            }
        } else {
            services.setServiceID(firebaseAuth.getUid()+"+"+String.valueOf(System.currentTimeMillis()));
            services.setSkill(getIntent().getExtras().getString("skill"));
            services.setAddressLine1(address_line_1_string);
            services.setAddressLine2(address_line_2_string);
            services.setDescription(problem_description);
            services.setCity(city_string);
            services.setLandmark(landmark_string);
            services.setCustomerAmount(Long.valueOf(amonut_string));
            sendToFirebase();
        }

    }

    private void sendToFirebase() {

        final ArrayList<String> uris = new ArrayList<>();
        final HashMap<String, Object> map = new HashMap<>();

        map.put("labourUID","");
        map.put("customerUID",firebaseAuth.getUid());
        map.put("customerAmount",services.getCustomerAmount());
        map.put("description",services.getDescription());
        map.put("feedback","");
        map.put("skill",services.getSkill());
        //map.put("images", pictures);
        map.put("labourResponses", new HashMap<>());
        map.put("addressLine1",services.getAddressLine1());
        map.put("addressLine2",services.getAddressLine2());
        map.put("city",services.getCity());
        map.put("landmark",services.getLandmark());

        if(pictures.size()>0){
            for(final Uri uri : pictures){
                File newImageFile = new File(uri.getPath());
                try {

                    compressedImageFile = new Compressor(FormActivity.this)
                            .setMaxHeight(200)
                            .setMaxWidth(400)
                            .setQuality(50)
                            .compressToBitmap(newImageFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumbData = baos.toByteArray();

                final UploadTask image_path = storageReference.child("services").child(pictures.indexOf(uri) + services.getServiceID() + ".jpg").putBytes(thumbData);

                image_path.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.d("inside fi", taskSnapshot.toString());
                        storageReference.child("services").child(pictures.indexOf(uri) + services.getServiceID() + ".jpg").getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        uris.add(uri.toString());
                                        Log.d("uri",uri.toString()+"!"+uris.size());

                                        if(uris.size() == pictures.size()){
                                            HashMap<String, Object> images = new HashMap<>();
                                            images.put("images",uris);
                                            firebaseFirestore.collection("services").document(services.getServiceID()).set(images, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("done",uris.toString());
                                                            ///////////
                                                            firebaseFirestore.collection("services").document(services.getServiceID()).set(map,SetOptions.merge())
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            HashMap<String , String> m = new HashMap<>();
                                                                            m.put("currentService",services.getServiceID());
                                                                            firebaseFirestore.collection("customer").document(firebaseAuth.getUid()).set(m,SetOptions.merge())
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Log.d("success","!");

                                                                                            //TODO: directly go to DashboardActivity
                                                                                            Intent intent = new Intent(FormActivity.this,CustomerMainActivity.class);
                                                                                            intent.putExtra("currentService",services.getServiceID());
                                                                                            intent.putExtra("services",services);
                                                                                            intent.putExtra("customer",customer);
                                                                                            startActivity(intent);
                                                                                            finish();
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.d("error",e.toString());
                                                                                        }
                                                                                    });



                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.d(TAG,"error : "+e.toString());
                                                                        }
                                                                    });

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("failure 1", e.toString());
                                                        }
                                                    });
                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("failure 2", e.toString());

                                        Toast.makeText(FormActivity.this, "(IMAGE Error uri) : " + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("failure 3", e.toString());
                        Toast.makeText(FormActivity.this ,"(IMAGE Error) : " + e, Toast.LENGTH_LONG).show();
                    }
                });

            }

        }else {
            map.put("images", new ArrayList<String>());
        }


    }

    private void chooseImage() {

       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(FormActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(FormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {

                BringImagePicker();

            }
        } else {

            BringImagePicker();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            BringImagePicker();
        }
    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(400, 200)
                .start(FormActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                pictures.add(mainImageURI);
                slide.added(mainImageURI.toString());
                viewPager.setCurrentItem(pictures.size()-1);
                //viewPager.setAdapter(new Slide(getApplicationContext(), pictures));
                //photo.setImageURI(mainImageURI);

                //isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
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
        getMenuInflater().inflate(R.menu.form2, menu);
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
