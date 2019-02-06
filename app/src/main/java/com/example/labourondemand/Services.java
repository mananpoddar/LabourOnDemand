package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Services implements Parcelable {

    private String skill, customerUID, description, feedback, labourUID;
    private long customerAmount;
    private ArrayList<String> images;
    private GeoPoint from, to;
    private String addressLine1, addressLine2, landmark, city, serviceID;
    private HashMap<String, Long> labourerResponses;
    private Customer customer;
    private Labourer labourer;

    public Services() {
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getCustomerUID() {
        return customerUID;
    }

    public void setCustomerUID(String customerUID) {
        this.customerUID = customerUID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getLabourUID() {
        return labourUID;
    }

    public void setLabourUID(String labourUID) {
        this.labourUID = labourUID;
    }

    public long getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(long customerAmount) {
        this.customerAmount = customerAmount;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public GeoPoint getFrom() {
        return from;
    }

    public void setFrom(GeoPoint from) {
        this.from = from;
    }

    public GeoPoint getTo() {
        return to;
    }

    public void setTo(GeoPoint to) {
        this.to = to;
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

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public HashMap<String, Long> getLabourerResponses() {
        return labourerResponses;
    }

    public void setLabourerResponses(HashMap<String, Long> labourerResponses) {
        this.labourerResponses = labourerResponses;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Labourer getLabourer() {
        return labourer;
    }

    public void setLabourer(Labourer labourer) {
        this.labourer = labourer;
    }

    protected Services(Parcel in) {
        skill = in.readString();
        customerUID = in.readString();
        description = in.readString();
        feedback = in.readString();
        labourUID = in.readString();
        customerAmount = in.readLong();
        images = in.createStringArrayList();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        landmark = in.readString();
        city = in.readString();
        serviceID = in.readString();
        customer = in.readParcelable(Customer.class.getClassLoader());
        labourer = in.readParcelable(Labourer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(skill);
        dest.writeString(customerUID);
        dest.writeString(description);
        dest.writeString(feedback);
        dest.writeString(labourUID);
        dest.writeLong(customerAmount);
        dest.writeStringList(images);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(landmark);
        dest.writeString(city);
        dest.writeString(serviceID);
        dest.writeParcelable(customer, flags);
        dest.writeParcelable(labourer, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Services> CREATOR = new Creator<Services>() {
        @Override
        public Services createFromParcel(Parcel in) {
            return new Services(in);
        }

        @Override
        public Services[] newArray(int size) {
            return new Services[size];
        }
    };
}
