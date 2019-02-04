package com.example.labourondemand;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends LabourerMainActivity implements PersonalDetailsFragment.OnFragmentInteractionListener,
        WorkDetailsFragment.OnFragmentInteractionListener,AddressDetailsFragment.OnFragmentInteractionListener{

    private Boolean isLabourer = false, isEditting;
    private User user = new User();
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private String TAG = ProfileActivity.class.getName();
    private TextView name;
    private CircleImageView photo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_profile, null, false);
        drawerLayout.addView(view, 2);

        name = view.findViewById(R.id.profile_et_name);
        photo = view.findViewById(R.id.profile_civ_photo);
        progressBar = view.findViewById(R.id.profile_pb);
        progressBar.setVisibility(View.VISIBLE);

        /*Bundle bundle = getIntent().getExtras().getBundle("user");*//*
        user = bundle.getParcelable("labourer");*/
        user = (User) getIntent().getParcelableExtra("labourer");
        Log.d(TAG,"user"+user.getAddressLine1());
        Toast.makeText(getApplicationContext(),"Hurray",Toast.LENGTH_LONG).show();

        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        ImageView editButton = view.findViewById(R.id.profile_iv_edit);


        name.setText(user.getName());
        Glide.with(getApplicationContext())
                .load(user.getImage())
                .into(photo);
        //tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        //tabLayout.addTab(tabLayout.newTab().setText("Address"));
        //if(isLabourer) tabLayout.addTab(tabLayout.newTab().setText("Work"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Bundle bundle = new Bundle();
        bundle.putParcelable("labourer", user);

        viewPager = view.findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
        AddressDetailsFragment addressDetailsFragment = new AddressDetailsFragment();

        personalDetailsFragment.setArguments(bundle);
        addressDetailsFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(personalDetailsFragment, "Personal");
        viewPagerAdapter.addFragment(addressDetailsFragment, "Address");
        if(user.getLabourer()) {
            viewPagerAdapter.addFragment(new WorkDetailsFragment(), "Work");
        }

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        /*viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        isEditting = false;
        
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(v);
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    public void edit(View v){
        /*isEditting = !isEditting;
        name = findViewById(R.id.name);
        emailid = findViewById(R.id.edit_emailid);
        phone = findViewById(R.id.edit_phone_number);
        dob = findViewById(R.id.edit_dob);
        address1 = findViewById(R.id.address1);
        address2 = findViewById(R.id.address2);
        address3 = findViewById(R.id.address3);
        pincode = findViewById(R.id.edit_pincode);
        if(isLabourer) job = findViewById(R.id.edit_job);
        if(isEditting) {
            name.setFocusableInTouchMode(true);
            emailid.setFocusableInTouchMode(true);
            phone.setFocusableInTouchMode(true);
            dob.setFocusableInTouchMode(true);
            address1.setFocusableInTouchMode(true);
            address2.setFocusableInTouchMode(true);
            address3.setFocusableInTouchMode(true);
            pincode.setFocusableInTouchMode(true);
            if(isLabourer) job.setFocusableInTouchMode(true);
        }
        else {

            if(!isValidEmail(emailid.getText().toString().trim())) {
                emailid.setError("Invalid email id");
                isEditting = true;
                Toast.makeText(getApplicationContext(), "Error in Personal!", Toast.LENGTH_SHORT).show();
            }

            if(!isValidPhone(phone.getText().toString().trim())) {
                phone.setError("Invalid phone number");
                isEditting = true;
                Toast.makeText(getApplicationContext(), "Error in Personal!", Toast.LENGTH_SHORT).show();
            }

            if(!isValidDate(dob.getText().toString().trim())) {
                dob.setError("Invalid date");
                isEditting = true;
                Toast.makeText(getApplicationContext(), "Error in Personal!", Toast.LENGTH_SHORT).show();
            }

            if(!isValidPincode(pincode.getText().toString().trim())) {
                pincode.setError("Invalid pincode");
                isEditting = true;
                Toast.makeText(getApplicationContext(), "Error in Address!", Toast.LENGTH_SHORT).show();
            }

            if(!isEditting) {
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
                pincode.setFocusableInTouchMode(false);
                pincode.setFocusable(false);
                if(isLabourer) job.setFocusableInTouchMode(false);
                if(isLabourer) job.setFocusable(false);
            }
        }*/
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    public boolean isValidPhone(String phone) {
        String phonePattern = "[0-9]{10}";
        return phone.matches(phonePattern);
    }

    public boolean isValidDate(String date) {
        String datePattern="^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]";
        return date.matches(datePattern);
    }

    public boolean isValidPincode(String pincode) {
        String pincodePattern = "[0-9]{6}";
        return pincode.matches(pincodePattern);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
