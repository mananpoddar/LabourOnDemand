package com.example.labourondemand;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DetailServiceActivity extends LabourerMainActivity implements ServiceAmountFragment.OnFragmentInteractionListener,
                ServiceAddressFragment.OnFragmentInteractionListener,ServiceDescriptionFragment.OnFragmentInteractionListener{

    private Services services = new Services();
    private ViewPager viewPagerImages, viewPagerData;
    private TabLayout tabs;
    private Slide slide;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_detail_service, null, false);
        drawerLayout.addView(view, 0);

        services = getIntent().getParcelableExtra("service");
        viewPagerImages = view.findViewById(R.id.detail_service_vp_images);
        viewPagerData = view.findViewById(R.id.detail_service_vp_data);
        tabs = view.findViewById(R.id.detail_service_tl);

        slide = new Slide(this, services.getImages());
        viewPagerImages.setAdapter(slide);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putParcelable("labourer", services);

        ServiceDescriptionFragment serviceDescriptionFragment = new ServiceDescriptionFragment();
        ServiceAddressFragment serviceAddressFragment = new ServiceAddressFragment();
        ServiceAmountFragment serviceAmountFragment = new ServiceAmountFragment();

        serviceAddressFragment.setArguments(bundle);
        serviceAmountFragment.setArguments(bundle);
        serviceDescriptionFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(serviceDescriptionFragment,"Description");
        viewPagerAdapter.addFragment(serviceAddressFragment,"Location");
        viewPagerAdapter.addFragment(serviceAmountFragment,"Amount");
        viewPagerData.setAdapter(viewPagerAdapter);
        viewPagerData.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewPagerData);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
