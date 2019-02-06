package com.example.labourondemand;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Slide extends PagerAdapter {

    private ArrayList<String> pictures ;
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public Slide(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.pictures = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        if(pictures == null){
            return 0;
        }else {
            return pictures.size();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_image_slide, view, false);

        assert imageLayout != null;
        final ImageView picture = (ImageView) imageLayout
                .findViewById(R.id.custom_layout_iv_pictures);

        Glide.with(context).load(pictures.get(position)).into(picture);
        //imageView.setImageResource(IMAGES.get(position));
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public void added(String s){
        pictures.add(s);
        notifyDataSetChanged();
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

