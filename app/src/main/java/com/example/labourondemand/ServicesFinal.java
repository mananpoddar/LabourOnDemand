package com.example.labourondemand;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class ServicesFinal implements Serializable {

    private String skill, customerUID, description, feedback, serviceId, status;
    private Long numOfLabourers, customerAmount;
    private Double rating;
    private ArrayList<String> images, labourerUID;
    private CustomerFinal customer;
    private ArrayList<LabourerFinal> labourers, selectedLabourers;
    private GeoPoint destination;
    private String startTime, endTime;

    public ServicesFinal() {
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNumOfLabourers() {
        return numOfLabourers;
    }

    public void setNumOfLabourers(Long numOfLabourers) {
        this.numOfLabourers = numOfLabourers;
    }

    public Long getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(Long customerAmount) {
        this.customerAmount = customerAmount;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getLabourerUID() {
        return labourerUID;
    }

    public void setLabourerUID(ArrayList<String> labourerUID) {
        this.labourerUID = labourerUID;
    }

    public CustomerFinal getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerFinal customer) {
        this.customer = customer;
    }

    public ArrayList<LabourerFinal> getLabourers() {
        return labourers;
    }

    public void setLabourers(ArrayList<LabourerFinal> labourers) {
        this.labourers = labourers;
    }

    public ArrayList<LabourerFinal> getSelectedLabourers() {
        return selectedLabourers;
    }

    public void setSelectedLabourers(ArrayList<LabourerFinal> selectedLabourers) {
        this.selectedLabourers = selectedLabourers;
    }

    public GeoPoint getDestination() {
        return destination;
    }

    public void setDestination(GeoPoint destination) {
        this.destination = destination;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
