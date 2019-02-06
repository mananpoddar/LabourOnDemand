package com.example.labourondemand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PersonalDetailsFragment.OnFragmentInteractionListener,
        WorkDetailsFragment.OnFragmentInteractionListener, AddressDetailsFragment.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private Services services = new Services();
    private Button submitButton;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
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

    private String TAG = ProfileActivity.class.getName();
    private Boolean isLabourer = false, isEditable;
    private Labourer labourer = new Labourer();
    private TextView name;
    private CircleImageView photo;
    private ProgressBar progressBar;
    private String type;
    private TextView emailid, phone, dob, address1, address2, address3, city, state, skill;
    private Button submit;
    private ImageView edit;
    private TabLayout tabLayout;

    private String userId;
    private Boolean server = true, isChanged = false;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.profile_tb);
        drawerLayout = findViewById(R.id.profile_dl);
        navigationView = findViewById(R.id.profile_nav);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(2);
        navigationView.setNavigationItemSelectedListener(this);

        name = findViewById(R.id.profile_et_name);
        photo = findViewById(R.id.profile_civ_photo);
        progressBar = findViewById(R.id.profile_pb);
        progressBar.setVisibility(View.VISIBLE);
        submit = findViewById(R.id.profile_btn_submit);
        submit.setVisibility(View.GONE);
        tabLayout = findViewById(R.id.profile_tl);
        edit = findViewById(R.id.profile_iv_edit);
        submit.setClickable(false);
        type = (String) getIntent().getExtras().get("type");
        Bundle bundle = new Bundle();

        if (type.equals("labourer")) {
            isLabourer = true;
            labourer = (Labourer) getIntent().getParcelableExtra("user");
            bundle.putParcelable("labourer", labourer);
            bundle.putString("type", "labourer");
            name.setText(labourer.getName());
            Glide.with(getApplicationContext())
                    .load(labourer.getImage())
                    .into(photo);
        } else {
            //
            customer = (Customer) getIntent().getParcelableExtra("user");
            bundle.putParcelable("customer", customer);
            bundle.putString("type", "customer");
            name.setText(customer.getName());
            Glide.with(getApplicationContext())
                    .load(customer.getImage())
                    .into(photo);
        }

        Log.d(TAG, "labourer" + labourer.getAddressLine1());
        //Toast.makeText(getApplicationContext(), "Hurray", Toast.LENGTH_LONG).show();

        viewPager = findViewById(R.id.profile_vp);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
        AddressDetailsFragment addressDetailsFragment = new AddressDetailsFragment();

        personalDetailsFragment.setArguments(bundle);
        addressDetailsFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(personalDetailsFragment, "Personal");
        viewPagerAdapter.addFragment(addressDetailsFragment, "Address");
        if (isLabourer) {
            viewPagerAdapter.addFragment(new WorkDetailsFragment(), "Work");
            viewPager.setOffscreenPageLimit(3);
        } else {
            viewPager.setOffscreenPageLimit(2);
        }

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        isEditable = false;

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setClickable(true);
                isEditable = true;
                submit.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                editfunction(v);
            }
        });

        progressBar.setVisibility(View.GONE);
    }


    public void editfunction(View v) {

        name = findViewById(R.id.profile_et_name);
        emailid = findViewById(R.id.personal_et_email);
        phone = findViewById(R.id.personal_et_phone);
        dob = findViewById(R.id.personal_et_dob);
        address1 = findViewById(R.id.address_details_et_a1);
        address2 = findViewById(R.id.address_details_et_a2);
        address3 = findViewById(R.id.address_details_et_a3);
        city = findViewById(R.id.address_details_et_city);
        state = findViewById(R.id.address_details_et_state);
        skill = findViewById(R.id.work_et_skill);
        //skill = null;
        if (isEditable) {

            name.setFocusableInTouchMode(true);
            emailid.setFocusableInTouchMode(true);
            phone.setFocusableInTouchMode(true);
            address1.setFocusableInTouchMode(true);
            address2.setFocusableInTouchMode(true);
            address3.setFocusableInTouchMode(true);
            city.setFocusableInTouchMode(true);
            state.setFocusableInTouchMode(true);
            /*if (isLabourer) skill.setFocusableInTouchMode(true);*/

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isEditable) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                //Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                            } else {

                                BringImagePicker();

                            }

                        } else {
                            BringImagePicker();
                        }
                    }
                }
            });

            dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEditable) {
                        selectDate(v);
                    }
                }
            });

            if (isLabourer) {
                skill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isEditable)
                            selectSkill(v);
                    }
                });
            }

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isInputRight = true;

                    if (!isValidEmail(emailid.getText().toString().trim())) {
                        emailid.setError("Invalid email id");
                        Toast.makeText(getApplicationContext(), "Error in PERSONAL!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }
                    if (!isValidPhone(phone.getText().toString().trim())) {
                        phone.setError("Invalid phone number");
                        Toast.makeText(getApplicationContext(), "Error in PERSONAL!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }
                    if(!isNotEmpty(name.getText().toString().trim()))  {
                        name.setError("Name cannot be empty");
                        Toast.makeText(getApplicationContext(), "Error in Name!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }
                    if(!isNotEmpty(address1.getText().toString().trim()))  {
                        address1.setError("Address Line 1 cannot be empty");
                        Toast.makeText(getApplicationContext(), "Error in ADDRESS!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }
                    if(!isNotEmpty(address2.getText().toString().trim()))  {
                        address2.setError("Address Line 2 cannot be empty");
                        Toast.makeText(getApplicationContext(), "Error in ADDRESS!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }
                    if(!isNotEmpty(address3.getText().toString().trim()))  {
                        address3.setError("Address Line 3 cannot be empty");
                        Toast.makeText(getApplicationContext(), "Error in ADDRESS!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }
                    if(!isNotEmpty(city.getText().toString().trim()))  {
                        city.setError("City cannot be empty");
                        Toast.makeText(getApplicationContext(), "Error in ADDRESS!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }
                    if(!isNotEmpty(state.getText().toString().trim()))  {
                        state.setError("State cannot be empty");
                        Toast.makeText(getApplicationContext(), "Error in ADDRESS!", Toast.LENGTH_SHORT).show();
                        isInputRight = false;
                    }



                    if (isInputRight) {
                        name.setFocusableInTouchMode(false);
                        name.setFocusable(false);
                        emailid.setFocusableInTouchMode(false);
                        emailid.setFocusable(false);
                        phone.setFocusableInTouchMode(false);
                        phone.setFocusable(false);
                        dob.setFocusableInTouchMode(false);
                        dob.setFocusable(false);
                        address1.setFocusableInTouchMode(false);
                        address1.setFocusable(false);
                        address2.setFocusableInTouchMode(false);
                        address2.setFocusable(false);
                        address3.setFocusableInTouchMode(false);
                        address3.setFocusable(false);
                        city.setFocusableInTouchMode(false);
                        city.setFocusable(false);
                        state.setFocusableInTouchMode(false);
                        state.setFocusable(false);
                        if (isLabourer) skill.setFocusableInTouchMode(false);
                        if (isLabourer) skill.setFocusable(false);
                        submit.setVisibility(View.GONE);
                        edit.setVisibility(View.VISIBLE);
                        isEditable = false;

                        sendToFirebase();
                    }
                }
            });
        }
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return (email.matches(emailPattern) && (email.length() > 0));
    }

    public boolean isValidPhone(String phone) {
        String phonePattern = "[0-9]{10}";
        return phone.matches(phonePattern);
    }

    public boolean isNotEmpty(String name) {
        if(name.length() == 0)
            return false;
        return true;
    }

    public void selectDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dob.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void selectSkill(View view) {

        final String[] mSkill = new String[1];
        final String list[] = getResources().getStringArray(R.array.arrays_skill);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Choose Your Skill");

        builder.setSingleChoiceItems(list, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSkill[0] = list[which];
                    }
                });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                skill.setText(mSkill[0]);
            }
        });
        builder.show();
    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(120, 160)
                .start(ProfileActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                photo.setImageURI(mainImageURI);
                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }

    private void sendToFirebase() {

        progressBar.setVisibility(View.VISIBLE);

        if (isChanged) {

            userId = firebaseAuth.getCurrentUser().getUid();

            File newImageFile = new File(mainImageURI.getPath());
            try {

                compressedImageFile = new Compressor(ProfileActivity.this)
                        .setMaxHeight(160)
                        .setMaxWidth(120)
                        .setQuality(50)
                        .compressToBitmap(newImageFile);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] thumbData = baos.toByteArray();

            final UploadTask image_path = storageReference.child("profile_images").child(userId + ".jpg").putBytes(thumbData);

            image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {

                        storageReference.child("profile_images").child(userId+".jpg").getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        storeFirestore(uri);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, "(IMAGE Error uri) : " + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(ProfileActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }
            });

        }else {
            storeFirestore(null);
        }
        /*else {
                        //GeoPoint location = loc
                        storeFirestore(null, user_name,phone, lo);

                    }*/

    }

    private void storeFirestore(Uri uri) {

        isChanged = false;

        Uri download_uri = uri;
        /*if(task != null) {

            download_uri = task.getResult().g

        } else {

            download_uri = mainImageURI;

        }*/


        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name.getText().toString());
        if(uri != null) userMap.put("image", download_uri.toString());
        userMap.put("phone", Long.valueOf(phone.getText().toString()));
        userMap.put("city", city.getText().toString());
        userMap.put("state", state.getText().toString());
        userMap.put("addressLine1", address1.getText().toString());
        userMap.put("addressLine2", address2.getText().toString());
        userMap.put("addressLine3", address3.getText().toString());
        userMap.put("dob", dob.getText().toString());

        if(type.equals("labourer")) {
            userMap.put("skill", skill.getText().toString());
            //userMap.put("workExperience", Long.valueOf(workExperience.getText().toString()));
        }
        ArrayList<String> h = new ArrayList<>();
        HashMap<String,Integer> m = new HashMap<>();

        userMap.put("services",h);

        firebaseFirestore.collection(type).document(userId).set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(SetupActivity.this, CheckingActivity.class));
                        //finish();
//                        if(type.equals("customer")){
//                            Intent intent = new Intent(ProfileActivity.this, CustomerMainActivity.class);
//                            startActivity(intent);
//                        }else{
//                            Intent intent = new Intent(ProfileActivity.this, LabourerMainActivity.class);
//                            startActivity(intent);
//                        }
//                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //String error = task.getException().getMessage();
                        Toast.makeText(ProfileActivity.this, "(FIRESTORE Error) : " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

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

        if (id == R.id.nav_dashboard) {
            Intent intent;
            if (type.equals("customer")) {
                intent = new Intent(this, CustomerMainActivity.class);
                intent.putExtra("customer", customer);
            } else {
                intent = new Intent(this, LabourerMainActivity.class);
                intent.putExtra("labourer", labourer);
            }
            startActivity(intent);

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this, PreviousActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_person) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
