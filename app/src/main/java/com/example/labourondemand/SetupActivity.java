package com.example.labourondemand;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class SetupActivity extends AppCompatActivity /*implements DetailsSetupFragment.OnFragmentInteractionListener*/{

    private ViewPager viewPager;
    private TabLayout tabs;
    Fragment detailsSetupFragment = new DetailsSetupFragment();
    Fragment addressSetupFragment = new AddressSetupFragment();
    private Button submit;
    private ImageView photo;
    private Uri mainImageURI = null;
    private ProgressBar progressBar;
    private EditText name, phone, dob, skill, workExperience;
    private TextInputLayout date;
    private EditText a1, a2, a3, state, city;
    private Boolean server = true, isChanged = false;
    private FirebaseAuth firebaseAuth;
    private Bitmap compressedImageFile;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private String userId, type;
    private TextInputLayout skillTil, workTil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        type = getIntent().getExtras().getString("type");

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        /*viewPager = findViewById(R.id.setup_vp);
        tabs = findViewById(R.id.setup_tl);*/

        submit = findViewById(R.id.setup2_btn_submit);
        photo = findViewById(R.id.setup_iv_photo);
        name = findViewById(R.id.setup_details_et_name);
        phone = findViewById(R.id.setup_details_et_phone);
        dob = findViewById(R.id.setup_details_et_dob);
        date = findViewById(R.id.setup_details_til_dob);
        skill = findViewById(R.id.setup_details_et_skill);
        workExperience = findViewById(R.id.setup_details_et_work_experience);
        skillTil = findViewById(R.id.setup_tip_skill);
        workTil = findViewById(R.id.setup_tip_work);

        a1 = findViewById(R.id.setup_address_et_address_line_1);
        a2 = findViewById(R.id.setup_address_et_address_line_2);
        a3 = findViewById(R.id.setup_address_et_address_line_3);
        city = findViewById(R.id.setup_address_et_city);
        state = findViewById(R.id.setup_address_et_state);
        progressBar = findViewById(R.id.setup_pb);

        progressBar.setVisibility(View.GONE);

        if(type.equals("customer")){
            skillTil.setVisibility(View.GONE);
            workTil.setVisibility(View.GONE);
            skill.setVisibility(View.GONE);
            workExperience.setVisibility(View.GONE);
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(v);
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(v);
            }
        });

        skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSkill(v);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server = true;
                if(TextUtils.isEmpty(a1.getText())){
                    a1.setError("error");
                    server = false;
                }
                if(TextUtils.isEmpty(a2.getText())){
                    a2.setError("error");
                    server = false;
                }
                if(TextUtils.isEmpty(a3.getText())){
                    a3.setError("error");
                    server = false;
                }
                if(TextUtils.isEmpty(city.getText())){
                    city.setError("error");
                    server = false;
                }
                if(TextUtils.isEmpty(state.getText())){
                    state.setError("error");
                    server = false;
                }
                if(TextUtils.isEmpty(name.getText())){
                    name.setError("error");
                    server = false;
                }
                if(TextUtils.isEmpty(phone.getText())){
                    phone.setError("error");
                    server = false;
                }
                if(TextUtils.isEmpty(dob.getText())){
                    dob.setError("error");
                    server = false;
                }
                if(type.equals("labourer")) {
                    if (TextUtils.isEmpty(skill.getText())) {
                        a1.setError("error");
                        server = false;
                    }
                    if (TextUtils.isEmpty(workExperience.getText())) {
                        a1.setError("error");
                        server = false;
                    }
                }
                if(server){
                    sendToFirebase();
                }else{
                    Toast.makeText(getApplicationContext(),"Fill",Toast.LENGTH_SHORT).show();
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        //Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {
                    BringImagePicker();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            BringImagePicker();
        }
    }

    private void sendToFirebase() {

        progressBar.setVisibility(View.VISIBLE);
        if (isChanged) {

            userId = firebaseAuth.getCurrentUser().getUid();

            File newImageFile = new File(mainImageURI.getPath());
            try {

                compressedImageFile = new Compressor(SetupActivity.this)
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
                                        Toast.makeText(SetupActivity.this, "(IMAGE Error uri) : " + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
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

        Uri download_uri = uri;
        /*if(task != null) {

            download_uri = task.getResult().g

        } else {

            download_uri = mainImageURI;

        }*/


        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name.getText().toString());
        userMap.put("image", download_uri.toString());
        userMap.put("phone", Long.valueOf(phone.getText().toString()));
        userMap.put("city", city.getText().toString());
        userMap.put("state", state.getText().toString());
        userMap.put("addressLine1", a1.getText().toString());
        userMap.put("addressLine2", a2.getText().toString());
        userMap.put("addressLine3", a3.getText().toString());
        userMap.put("dob", dob.getText().toString());

        if(type.equals("labourer")) {
            userMap.put("skill", skill.getText().toString());
            userMap.put("workExperience", Long.valueOf(workExperience.getText().toString()));
        }
        ArrayList<String> h = new ArrayList<>();
        HashMap<String,Integer> m = new HashMap<>();

        userMap.put("services",h);

        firebaseFirestore.collection(type).document(userId).set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SetupActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(SetupActivity.this, CheckingActivity.class));
                        //finish();
                        if(type.equals("customer")){
                            Intent intent = new Intent(SetupActivity.this, CustomerMainActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(SetupActivity.this, LabourerMainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //String error = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "(FIRESTORE Error) : " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void selectDate(View view){
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

    public void selectSkill(View view){

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
                .start(SetupActivity.this);

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

    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(detailsSetupFragment, "Personal");
        adapter.addFragment(addressSetupFragment, "Address");
        viewPager.setAdapter(adapter);


    }

    /*@Override
    public void onRadioButtonChoice(int choice) {

    }*/

    //Custom adapter class provides fragments required for the view pager
    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        Adapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
