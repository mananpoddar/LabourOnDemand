package com.example.labourondemand;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

class HorizontalScrollViewAdapter extends RecyclerView.Adapter<HorizontalScrollViewAdapter.MyViewHolder> {
    private Context context;
    private HashMap<String, Integer> icons;
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CircleImageView photo;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.item_skill_hsv_cv);
            name = view.findViewById(R.id.item_skill_hsv_tv);
            photo = view.findViewById(R.id.item_skill_hsv_civ);
        }
    }

    public HorizontalScrollViewAdapter(Context context, HashMap<String, Integer> icons, RecyclerView recyclerView, ArrayList<String> skills, GoogleMap map) {
        this.context = context;
        this.skills = skills;
        this.icons = icons;
        this.recyclerView = recyclerView;
        this.last = 0;
        this.map = map;
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
        //holder.photo.setImageResource(icons.get(skills.get(position)));
        holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.dummy));


        /*StateListDrawable states = new StateListDrawable();
        states.addState(new int[] {android.R.attr.state_pressed},
                context.getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
        states.addState(new int[] {android.R.attr.state_focused},
                context.getResources().getDrawable(R.drawable.ic_history_black_24dp));
        holder.photo.setImageDrawable(states);*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != last && skill != null) {
                    presentView = recyclerView.getLayoutManager().findViewByPosition(position);
                    lastView = recyclerView.getLayoutManager().findViewByPosition(last);
                    Log.d("position", position + "!" + last);
                    deSelectedView(lastView);
                    selectedView(presentView);

                    last = position;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void selectedView(View v) {
        TextView skillName = v.findViewById(R.id.item_skill_hsv_tv);
        skillName.setTypeface(skillName.getTypeface(), Typeface.BOLD);
        CircleImageView skillPic = v.findViewById(R.id.item_skill_hsv_civ);
        CardView cardView = v.findViewById(R.id.item_skill_hsv_cv);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.whiteTransparentHalf));
        cardView.setElevation(16);
        skill = skillName.getText().toString();
        //skillPic.setEnabled(false);
        /*skillPic.setPressed(true);*/
        geoFirestoreRef = FirebaseFirestore.getInstance().collection(skill + "Location");
        geoFirestore = new GeoFirestore(geoFirestoreRef);
        geoQuery = geoFirestore.queryAtLocation(new GeoPoint(location.getLatitude(), location.getLongitude()), 500);
        addListener();
    }

    public void deSelectedView(View v) {
        TextView skillName = v.findViewById(R.id.item_skill_hsv_tv);
        skillName.setTypeface(null, Typeface.NORMAL);
        CircleImageView skillPic = v.findViewById(R.id.item_skill_hsv_civ);
        CardView cardView = v.findViewById(R.id.item_skill_hsv_cv);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        cardView.setElevation(0);
        /*skillPic.setPressed(false);*/
        removeListener();

        //TODO remove marker from map
    }

    public void removeListener() {
        geoQuery.removeAllListeners();
        labourersLocation.clear();
        map.clear();
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
                    Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
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
        return skill + "Location";
    }

    public void locationChanged(Location location) {

        Log.d("locationChanged", location.toString());
        if (skill == null) {
            skill = "carpenter";

        }

        geoFirestoreRef = FirebaseFirestore.getInstance().collection(skill + "Location");
        geoFirestore = new GeoFirestore(geoFirestoreRef);
        this.location = location;

        geoQuery = geoFirestore.queryAtLocation(new GeoPoint(location.getLatitude(), location.getLongitude()), 500);
        Log.d("locationChanged", geoQuery.toString());
        addListener();

    }


}
