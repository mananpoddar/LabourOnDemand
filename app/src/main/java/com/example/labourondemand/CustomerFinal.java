package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerFinal extends User implements Serializable {

    private ArrayList<ServicesFinal> historyServices, currentServices, incomingServices;
    private ArrayList<String> currentServicesId;
    private GeoPoint destination;
    private HashMap<String, Marker> labourersLocation;

    @Override
    public String toString() {
        return "CustomerFinal{" +
                "historyServices=" + historyServices +
                ", currentServices=" + currentServices +
                ", incomingServices=" + incomingServices +
                ", destination=" + destination +
                ", labourersLocation=" + labourersLocation +
                '}';
    }

    public ArrayList<String> getCurrentServicesId() {
        return currentServicesId;
    }

    public void setCurrentServicesId(ArrayList<String> currentServicesId) {
        this.currentServicesId = currentServicesId;
    }

    public CustomerFinal() {

    }

    public CustomerFinal(ArrayList<ServicesFinal> historyServices, ArrayList<ServicesFinal> currentServices, ArrayList<ServicesFinal> incomingServices,
                         GeoPoint destination, HashMap<String, Marker> labourersLocation) {
        this.historyServices = historyServices;
        this.currentServices = currentServices;
        this.incomingServices = incomingServices;
        this.destination = destination;
        this.labourersLocation = labourersLocation;
    }

    public CustomerFinal(String id, String name, String image, String dob, String city, String state, String addressLine1, String addressLine2,
                         String addressLine3, Long phone, Long wallet, ArrayList<String> services, ArrayList<ServicesFinal> historyServices,
                         ArrayList<ServicesFinal> currentServices, ArrayList<ServicesFinal> incomingServices, GeoPoint destination, HashMap<String,
            Marker> labourersLocation) {
        super(id, name, image, dob, city, state, addressLine1, addressLine2, addressLine3, phone, wallet, services);
        this.historyServices = historyServices;
        this.currentServices = currentServices;
        this.incomingServices = incomingServices;
        this.destination = destination;
        this.labourersLocation = labourersLocation;
    }


    public CustomerFinal(String id, String name, String image, String dob, String city, String state, String addressLine1, String addressLine2, String addressLine3,
                         Long phone, Long wallet, ArrayList<String> services) {
        super(id, name, image, dob, city, state, addressLine1, addressLine2, addressLine3, phone, wallet, services);
        this.historyServices = new ArrayList<ServicesFinal>();
        this.currentServices = new ArrayList<ServicesFinal>();
        this.incomingServices = new ArrayList<ServicesFinal>();

    }

    public ArrayList<ServicesFinal> getHistoryServices() {
        return historyServices;
    }

    public void setHistoryServices(ArrayList<ServicesFinal> historyServices) {
        this.historyServices = historyServices;
    }

    public ArrayList<ServicesFinal> getCurrentServices() {
        return currentServices;
    }

    public void setCurrentServices(ArrayList<ServicesFinal> currentServices) {
        this.currentServices = currentServices;
    }

    public ArrayList<ServicesFinal> getIncomingServices() {
        return incomingServices;
    }

    public void setIncomingServices(ArrayList<ServicesFinal> incomingServices) {
        this.incomingServices = incomingServices;
    }

    public GeoPoint getDestination() {
        return destination;
    }

    public void setDestination(GeoPoint destination) {
        this.destination = destination;
    }

    public HashMap<String, Marker> getLabourersLocation() {
        return labourersLocation;
    }

    public void setLabourersLocation(HashMap<String, Marker> labourersLocation) {
        this.labourersLocation = labourersLocation;
    }



}
