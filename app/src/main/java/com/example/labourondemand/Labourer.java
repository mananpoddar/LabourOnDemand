package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Labourer implements Parcelable {

    private String name, image, dob, city, state, currentService, addressLine1, addressLine2, addressLine3;
    private Long currentServicePrice, phone;
    private Double averageRating;
    private GeoPoint currentLocation;
    private Boolean isBusy;
    private ArrayList<String> servicesId, skill;
    private ArrayList<Services> services;

    public Labourer() {
    }

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

    public ArrayList<String> getSkill() {
        return skill;
    }

    public void setSkill(ArrayList skill) {
        this.skill = skill;
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

    public Long getCurrentServicePrice() {
        return currentServicePrice;
    }

    public void setCurrentServicePrice(Long currentServicePrice) {
        this.currentServicePrice = currentServicePrice;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
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

    protected Labourer(Parcel in) {
        name = in.readString();
        image = in.readString();
        skill = in.createStringArrayList();
        dob = in.readString();
        city = in.readString();
        state = in.readString();
        currentService = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        addressLine3 = in.readString();
        if (in.readByte() == 0) {
            currentServicePrice = null;
        } else {
            currentServicePrice = in.readLong();
        }
        if (in.readByte() == 0) {
            phone = null;
        } else {
            phone = in.readLong();
        }
        if (in.readByte() == 0) {
            averageRating = null;
        } else {
            averageRating = in.readDouble();
        }
        byte tmpIsBusy = in.readByte();
        isBusy = tmpIsBusy == 0 ? null : tmpIsBusy == 1;
        servicesId = in.createStringArrayList();
        services = in.createTypedArrayList(Services.CREATOR);
    }

    public static final Creator<Labourer> CREATOR = new Creator<Labourer>() {
        @Override
        public Labourer createFromParcel(Parcel in) {
            return new Labourer(in);
        }

        @Override
        public Labourer[] newArray(int size) {
            return new Labourer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeStringList(skill);
        dest.writeString(dob);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(currentService);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(addressLine3);
        if (currentServicePrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(currentServicePrice);
        }
        if (phone == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(phone);
        }
        if (averageRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(averageRating);
        }
        dest.writeByte((byte) (isBusy == null ? 0 : isBusy ? 1 : 2));
        dest.writeStringList(servicesId);
        dest.writeTypedList(services);
    }
}
