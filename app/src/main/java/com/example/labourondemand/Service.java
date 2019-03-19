package com.example.labourondemand;

public class Service {
    int customerAmount;
    String customerUID;
    String jobTitle;
    double rating;
    String startTime;
    String endTime;

    public Service() {
    }

    public Service(int customerAmount, String customerUID, String jobTitle, double rating, String startTime, String endTime) {
        this.customerAmount = customerAmount;
        this.customerUID = customerUID;
        this.jobTitle = jobTitle;
        this.rating = rating;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getCustomerAmount() {
        return customerAmount;
    }

    public void setCustomerAmount(int customerAmount) {
        this.customerAmount = customerAmount;
    }

    public String getCustomerUID() {
        return customerUID;
    }

    public void setCustomerUID(String customerUID) {
        this.customerUID = customerUID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
