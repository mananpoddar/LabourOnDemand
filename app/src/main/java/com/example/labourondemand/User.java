package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class User implements Parcelable {

    private String name,image,phone,skill,dob,workExperience,a1,a2,a3,city,state, avgerageRating, currentService;
    private GeoPoint currentLocation;
    private Boolean isBusy;

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Boolean getLabourer() {
        return isLabourer;
    }

    public void setLabourer(Boolean labourer) {
        isLabourer = labourer;
    }

    private Boolean isLabourer;

    private ArrayList<String> services;

    public User(ArrayList<String> services) {
        this.services = services;
    }

    protected User(Parcel in) {
        name = in.readString();
        image = in.readString();
        phone = in.readString();
        skill = in.readString();
        dob = in.readString();
        workExperience = in.readString();
        a1 = in.readString();
        a2 = in.readString();
        a3 = in.readString();
        city = in.readString();
        state = in.readString();
        avgerageRating = in.readString();
        currentService = in.readString();
        byte tmpIsBusy = in.readByte();
        isBusy = tmpIsBusy == 0 ? null : tmpIsBusy == 1;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getA3() {
        return a3;
    }

    public void setA3(String a3) {
        this.a3 = a3;
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

    public String getAvgerageRating() {
        return avgerageRating;
    }

    public void setAvgerageRating(String avgerageRating) {
        this.avgerageRating = avgerageRating;
    }

    public String getCurrentService() {
        return currentService;
    }

    public void setCurrentService(String currentService) {
        this.currentService = currentService;
    }

    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
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
        dest.writeString(phone);
        dest.writeString(skill);
        dest.writeString(dob);
        dest.writeString(workExperience);
        dest.writeString(a1);
        dest.writeString(a2);
        dest.writeString(a3);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(avgerageRating);
        dest.writeString(currentService);
        dest.writeByte((byte) (isBusy == null ? 0 : isBusy ? 1 : 2));
        dest.writeStringList(services);
    }
}
