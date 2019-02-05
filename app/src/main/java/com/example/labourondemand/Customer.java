package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Customer implements Parcelable {

    private String name, image, dob, city, state, currentService, addressLine1, addressLine2, addressLine3, phone;
    private GeoPoint currentLocation;
    private ArrayList<String> servicesId;
    private ArrayList<Services> services;

    public Customer() {
    }

    public Customer(String name, String image, String dob, String city, String state, String currentService, String addressLine1, String addressLine2, String addressLine3, String phone, GeoPoint currentLocation, ArrayList<String> servicesId, ArrayList<Services> services) {
        this.name = name;
        this.image = image;
        this.dob = dob;
        this.city = city;
        this.state = state;
        this.currentService = currentService;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.phone = phone;
        this.currentLocation = currentLocation;
        this.servicesId = servicesId;
        this.services = services;
    }

    protected Customer(Parcel in) {
        name = in.readString();
        image = in.readString();
        dob = in.readString();
        city = in.readString();
        state = in.readString();
        currentService = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        addressLine3 = in.readString();
        phone = in.readString();
        servicesId = in.createStringArrayList();
        services = in.createTypedArrayList(Services.CREATOR);
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCurrentService() {
        return currentService;
    }

    public void setCurrentService(String currentService) {
        this.currentService = currentService;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public ArrayList<String> getServicesId() {
        return servicesId;
    }

    public void setServicesId(ArrayList<String> servicesId) {
        this.servicesId = servicesId;
    }

    public ArrayList<Services> getServices() {
        return services;
    }

    public void setServices(ArrayList<Services> services) {
        this.services = services;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(dob);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(currentService);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(addressLine3);
        dest.writeString(phone);
        dest.writeStringList(servicesId);
        dest.writeTypedList(services);
    }
}
