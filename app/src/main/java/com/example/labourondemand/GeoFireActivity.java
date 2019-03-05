package com.example.labourondemand;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
/*import com.koalap.geofirestore.GeoFire;
import com.koalap.geofirestore.GeoLocation;
import com.koalap.geofirestore.GeoQuery;
import com.koalap.geofirestore.GeoQueryEventListener;*/

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryEventListener;


public class GeoFireActivity extends AppCompatActivity {

    private CollectionReference geoFirestoreRef;
    private GeoFirestore geoFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fire);
/*
        CollectionReference ref = FirebaseFirestore.getInstance().collection("plumberLocation");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation("plumber1",new GeoLocation(12.9,79.5));
        geoFire.setLocation("plumber2",new GeoLocation(12.9,79.5));
        geoFire.setLocation("plumber3",new GeoLocation(12.9,79.5));
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(12.9233, -123.4059), 100);
        Log.d("tag","geoquery"+geoQuery.getCenter()+"!"+geoQuery.getRadius());

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(Exception exception) {
                System.err.println("There was an error with this query: " + exception.toString());
            }
        });*/

        geoFirestoreRef = FirebaseFirestore.getInstance().collection("carpenterLocation");
        geoFirestore = new GeoFirestore(geoFirestoreRef);
        /*geoFirestore.setLocation("carpenter1", new GeoPoint(12.95, 77.5));
        geoFirestore.setLocation("carpenter2", new GeoPoint(12.932, 77.321), new GeoFirestore.CompletionListener() {
            @Override
            public void onComplete(Exception exception) {
                if (exception == null){
                    System.out.println("Location saved on server successfully!");
                }
            }
        });*/

        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(12.92981, 77.51405), 500);
        Log.d("tag","geoquery"+geoQuery.getCenter()+"!"+geoQuery.getRadius());

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String documentID, GeoPoint location) {
                System.out.println(String.format("Document %s entered the search area at [%f,%f]", documentID, location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onKeyExited(String documentID) {
                System.out.println(String.format("Document %s is no longer in the search area", documentID));
            }

            @Override
            public void onKeyMoved(String documentID, GeoPoint location) {
                System.out.println(String.format("Document %s moved within the search area to [%f,%f]", documentID, location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(Exception exception) {
                System.err.println("There was an error with this query: " + exception.getLocalizedMessage());
            }
        });
    }
}
