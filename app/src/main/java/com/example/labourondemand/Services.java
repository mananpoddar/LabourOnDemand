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
    private List<String> images= new ArrayList<>();
    private GeoPoint from, to;
    private String a1, a2, landmark, city;
    private HashMap<String, Long> labourerResponses;

    public Services(String skill, String customerUID, String description, String feedback, String labourUID,
                    long customerAmount, List<String> images, GeoPoint from, GeoPoint to, String a1, String a2,

                    String landmark, String city, HashMap<String, Long> labourerResponses) {
        this.skill = skill;
        this.customerUID = customerUID;
        this.description = description;
        this.feedback = feedback;
        this.labourUID = labourUID;
        this.customerAmount = customerAmount;
        this.images = images;
        this.from = from;
        this.to = to;
        this.a1 = a1;
        this.a2 = a2;
        this.landmark = landmark;
        this.city = city;
        this.labourerResponses = labourerResponses;
    }

    protected Services(Parcel in) {
        skill = in.readString();
        customerUID = in.readString();
        description = in.readString();
        feedback = in.readString();
        labourUID = in.readString();
        customerAmount = in.readLong();
        images = in.createStringArrayList();
        a1 = in.readString();
        a2 = in.readString();
        landmark = in.readString();
        city = in.readString();
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
        dest.writeString(a1);
        dest.writeString(a2);
        dest.writeString(landmark);
        dest.writeString(city);
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
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

    public HashMap<String, Long> getLabourerResponses() {
        return labourerResponses;
    }

    public void setLabourerResponses(HashMap<String, Long> labourerResponses) {
        this.labourerResponses = labourerResponses;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getA1() {
        return a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
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
}
