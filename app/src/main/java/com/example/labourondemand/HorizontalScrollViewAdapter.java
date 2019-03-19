package com.example.labourondemand;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.*;

import de.hdodenhof.circleimageview.CircleImageView;

class HorizontalScrollViewAdapter extends RecyclerView.Adapter<HorizontalScrollViewAdapter.MyViewHolder> {
    private Context context;
/*
    private ArrayList<String> icons;
*/
    private ArrayList<String> skills;
    private RecyclerView recyclerView;
    private View lastView, presentView;
    private int last;
    private String skill;
    private GeoQuery geoQuery;
    private CollectionReference geoFirestoreRef;
    private GeoFirestore geoFirestore;
    private Location location;
    private HashMap<String, Marker> labourersLocation = new HashMap<>();
    private HashMap<String, GeoPoint> newQueries = new HashMap<>();
    private GoogleMap map;
    private BitmapDescriptor icon;
    private Bitmap bitmap;

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public HashMap<String, Marker> getLabourersLocation() {
        return labourersLocation;
    }

    public void setLabourersLocation(HashMap<String, Marker> labourersLocation) {
        this.labourersLocation = labourersLocation;
    }

    public HashMap<String, GeoPoint> getNewQueries() {
        return newQueries;
    }

    public void setNewQueries(HashMap<String, GeoPoint> newQueries) {
        this.newQueries = newQueries;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView photo;
        public CardView cardView;
        public ConstraintLayout constraintLayout;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.item_skill_hsv_cv);
            name = view.findViewById(R.id.item_skill_hsv_tv);
            photo = view.findViewById(R.id.item_skill_hsv_civ);
            constraintLayout = view.findViewById(R.id.item_skill_hsv_cl);
        }
    }

    public HorizontalScrollViewAdapter(Context context, RecyclerView recyclerView, GoogleMap map, String skill, Location destination,
                                       HashMap<String, Marker> labourersLocation                                       ) {
        this.context = context;
        this.skills = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.arrays_skill)));
        this.recyclerView = recyclerView;
        this.last = 0;
        this.map = map;
        icon = getBitmapDescriptor(R.drawable.ic_carpenter);
        this.skill = skill;
        this.location = destination;
        this.labourersLocation = labourersLocation;
        //bitmap = getBitmapFromVectorDrawable(context,R.drawable.ic_carpenter);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_skill_hsv, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final HorizontalScrollViewAdapter.MyViewHolder holder, final int position) {

        holder.name.setText(skills.get(position));
        Log.d("item", position + "!");
        //holder.photo.setImageResource(icons.get(skills.get(position)));
        if (skills.get(position).equals("Carpenter")) {
            if (skill.equals("Carpenter")) {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_carpenter_tools_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
            } else {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_carpenter_tools_no_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }

        } else if (skills.get(position).equals("Plumber")) {

            if (skill.equals("Plumber")) {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plumber_tools));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
            } else {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plumber_tools_no_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        } else if (skills.get(position).equals("Electrician")) {
            if (skill.equals("Electrician")) {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_electric_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
            } else {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_electric_no_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        } else if (skills.get(position).equals("Painter")) {
            if (skill.equals("Painter")) {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_paint_roller));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
            } else {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_paint_roller__no_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        } else if (skills.get(position).equals("Constructor")) {
            if (skill.equals("Constructor")) {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_construction_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
            } else {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_constuction_no_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        } else if (skills.get(position).equals("Chef")) {
            if (skill.equals("Chef")) {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cooking_no_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
            } else {
                holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cooking_colour));
                holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }

        /*StateListDrawable states = new StateListDrawable();
        states.addState(new int[] {android.R.attr.state_pressed},
                context.getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
        states.addState(new int[] {android.R.attr.state_focused},
                context.getResources().getDrawable(R.drawable.ic_history_black_24dp));
        holder.photo.setImageDrawable(states);*/

       /* holder.cardView.setOnClickListener(mCorkyListener);
        holder.photo.setOnClickListener(mCorkyListener);*/
        //holder.constraintLayout.setOnClickListener(mCorkyListener);

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("pos", position + "!!!!");
                if (position != last && skill != null) {
                    presentView = recyclerView.getLayoutManager().findViewByPosition(position);
                    lastView = recyclerView.getLayoutManager().findViewByPosition(last);
                    Log.d("views", (boolean) (presentView == null) + "!" + (boolean) (lastView == null));
                    Log.d("position", position + "!" + last);
                    if (presentView != null && lastView != null) {
                        deSelectedView(lastView);
                        selectedView(presentView);
                        last = position;
                    }
                }/*else if(position == last && skill != null)*/

            }
        });

    }

    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
        }
    };

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void selectedView(View v) {
        TextView skillName = v.findViewById(R.id.item_skill_hsv_tv);
        skillName.setTypeface(skillName.getTypeface(), Typeface.BOLD);
        ImageView skillPic = v.findViewById(R.id.item_skill_hsv_civ);
        ConstraintLayout constraintLayout = v.findViewById(R.id.item_skill_hsv_cl);
        constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));
        skill = skillName.getText().toString();
        skillPic.setBackgroundColor(context.getResources().getColor(R.color.lightBlue));

        if (skill.equals("Carpenter")) {
            icon = getBitmapDescriptor(R.drawable.ic_carpenter);
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_carpenter_tools_colour));
        } else if (skill.equals("Plumber")) {
            //icon = bitmapDescriptorFromVector(context, R.drawable.ic_plumber);
            icon = getBitmapDescriptor(R.drawable.ic_plumber);
            //bitmap = getBitmapFromVectorDrawable(context,R.drawable.ic_plumber);
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plumber_tools));
        } else if (skill.equals("Electrician")) {
            icon = getBitmapDescriptor(R.drawable.ic_electrician);
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_electric_colour));
        } else if (skill.equals("Painter")) {
            icon = getBitmapDescriptor(R.drawable.ic_painter);
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_paint_roller));
        } else if (skill.equals("Constructor")) {
            icon = getBitmapDescriptor(R.drawable.ic_constructer);
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_construction_colour));
        } else if (skill.equals("Chef")) {
            icon = getBitmapDescriptor(R.drawable.ic_chef);
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cooking_colour));
        }

       /* geoFirestoreRef = FirebaseFirestore.getInstance().collection("Location").whereEqualTo("type",skill);
        geoFirestore = new GeoFirestore((CollectionReference)geoFirestoreRef);*/

        geoFirestoreRef = FirebaseFirestore.getInstance().collection(skill + "Location");
        geoFirestore = new GeoFirestore(geoFirestoreRef);
        geoQuery = geoFirestore.queryAtLocation(new GeoPoint(location.getLatitude(), location.getLongitude()), 20);
        addListener();
    }

    public void deSelectedView(View v) {

        TextView skillName = v.findViewById(R.id.item_skill_hsv_tv);
        skillName.setTypeface(null, Typeface.NORMAL);
        ImageView skillPic = v.findViewById(R.id.item_skill_hsv_civ);
        ConstraintLayout constraintLayout = v.findViewById(R.id.item_skill_hsv_cl);
        constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        skillPic.setBackgroundColor(context.getResources().getColor(R.color.white));
        if (skill.equals("Carpenter")) {
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_carpenter_tools_no_colour));
        } else if (skill.equals("Plumber")) {
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plumber_tools_no_colour));
        } else if (skill.equals("Electrician")) {
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_electric_no_colour));
        } else if (skill.equals("Painter")) {
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_paint_roller__no_colour));
        } else if (skill.equals("Constructor")) {
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_constuction_no_colour));
        } else if (skill.equals("Chef")) {
            skillPic.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cooking_no_colour));
        }

        removeListener();

        //TODO remove marker from map
    }

    public void removeListener() {
        try {
            geoQuery.removeAllListeners();
            labourersLocation.clear();
            map.clear();
        } catch (NullPointerException e) {
            Log.d("exception", e.toString());
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_launcher_background);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = context.getDrawable(id);
       /* int h = ((int) Utils.convertDpToPixel(42, context));
        int w = ((int) Utils.convertDpToPixel(25, context));*/
        int h = ((int) dpToPx(28));
        int w = ((int) dpToPx(28));
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void addListener() {

        newQueries.clear();
        Log.d("addlistener", "!");
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String documentID, GeoPoint location) {

                newQueries.put(documentID, location);
                if (labourersLocation.containsKey(documentID)) {
                    animateMarker(documentID, location);
                    //TODO: animate the movement of already present icon
                } else {
                   /* MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
                    labourersLocation.put(documentID, location);*/
                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_carpenter);
                    Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(icon));
                    labourersLocation.put(documentID, marker);

                }
                System.out.println(String.format("Document %s entered the search area at [%f,%f]", documentID, location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onKeyExited(String documentID) {
                System.out.println(String.format("Document %s is no longer in the search area", documentID));
                labourersLocation.get(documentID).remove();
                labourersLocation.remove(documentID);

            }

            @Override
            public void onKeyMoved(String documentID, GeoPoint location) {

                //TODO: animate the movement of already present icon
                animateMarker(documentID, location);
                System.out.println(String.format("Document %s moved within the search area to [%f,%f]", documentID, location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");

                for (String s : labourersLocation.keySet()) {
                    if (!newQueries.containsKey(s)) {
                        labourersLocation.get(s).remove();
                        labourersLocation.remove(s);
                    }
                }
            }

            @Override
            public void onGeoQueryError(Exception exception) {
                System.err.println("There was an error with this query: " + exception.getLocalizedMessage());
            }
        });

    }

    public void animateMarker(final String documentId, GeoPoint newLocation) {
        Marker marker = labourersLocation.get(documentId);
        double[] startValues = new double[]{marker.getPosition().latitude, marker.getPosition().longitude};
        double[] endValues = new double[]{newLocation.getLatitude(), newLocation.getLongitude()};
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
        latLngAnimator.setDuration(600);
        latLngAnimator.setInterpolator(new DecelerateInterpolator());
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                double[] animatedValue = (double[]) animation.getAnimatedValue();
                labourersLocation.get(documentId).setPosition(new LatLng(animatedValue[0], animatedValue[1]));//marker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
            }
        });
        latLngAnimator.start();

    }

    public String getSkill() {
        return skill;
    }

    public void locationChanged(Location location) {

        Log.d("locationChanged", location.toString());
        if (skill == null) {
            skill = "Carpenter";
        }
        //removeListener();
        /*geoFirestoreRef = FirebaseFirestore.getInstance().collection("location").whereEqualTo("type",skill);
        geoFirestore = new GeoFirestore((CollectionReference)geoFirestoreRef);*/
        this.location = location;

        geoFirestoreRef = FirebaseFirestore.getInstance().collection(skill + "Location");
        geoFirestore = new GeoFirestore(geoFirestoreRef);
        geoQuery = geoFirestore.queryAtLocation(new GeoPoint(location.getLatitude(), location.getLongitude()), 20);
        Log.d("locationChanged", geoQuery.toString());
        addListener();


    }


}
