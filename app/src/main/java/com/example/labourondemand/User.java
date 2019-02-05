package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class User implements Parcelable {

    private String name, image, skill, dob, workExperience, city, state, currentService, addressLine1, addressLine2, addressLine3;
    private String phone;
    private Double averageRating;
    private GeoPoint currentLocation;
    private Boolean isBusy, isLabourer;
    private ArrayList<String> services;

    public User() {
    }

    public User(String name, String image, String skill, String dob, String workExperience, String city, String state, String currentService, String addressLine1, String addressLine2, String addressLine3, String phone, Double averageRating, GeoPoint currentLocation, Boolean isBusy, Boolean isLabourer, ArrayList<String> services) {
        this.name = name;
        this.image = image;
        this.skill = skill;
        this.dob = dob;
        this.workExperience = workExperience;
        this.city = city;
        this.state = state;
        this.currentService = currentService;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.phone = phone;
        this.averageRating = averageRating;
        this.currentLocation = currentLocation;
        this.isBusy = isBusy;
        this.isLabourer = isLabourer;
        this.services = services;
    }

    protected User(Parcel in) {
        name = in.readString();
        image = in.readString();
        skill = in.readString();
        dob = in.readString();
        workExperience = in.readString();
        city = in.readString();
        state = in.readString();
        currentService = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        addressLine3 = in.readString();
        phone = in.readString();
        if (in.readByte() == 0) {
            averageRating = null;
        } else {
            averageRating = in.readDouble();
        }
        byte tmpIsBusy = in.readByte();
        isBusy = tmpIsBusy == 0 ? null : tmpIsBusy == 1;
        byte tmpIsLabourer = in.readByte();
        isLabourer = tmpIsLabourer == 0 ? null : tmpIsLabourer == 1;
        services = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }

    public Boolean getLabourer() {
        return isLabourer;
    }

    public void setLabourer(Boolean labourer) {
        isLabourer = labourer;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
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
        dest.writeString(skill);
        dest.writeString(dob);
        dest.writeString(workExperience);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(currentService);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(addressLine3);
        dest.writeString(phone);
        if (averageRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(averageRating);
        }
        dest.writeByte((byte) (isBusy == null ? 0 : isBusy ? 1 : 2));
        dest.writeByte((byte) (isLabourer == null ? 0 : isLabourer ? 1 : 2));
        dest.writeStringList(services);
    }
}
