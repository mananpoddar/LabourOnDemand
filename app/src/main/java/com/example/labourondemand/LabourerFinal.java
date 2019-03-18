package com.example.labourondemand;


import java.io.Serializable;
import java.util.ArrayList;

public class LabourerFinal extends User implements Serializable{

    private Services currentService;
    private Boolean isBusy;
    private ArrayList<String> skill;
    private Long workExperience;
    private ArrayList<String> applied;
    private ArrayList<Services> incomingServices, historyServices;

    public LabourerFinal() {
    }

    public LabourerFinal(Services currentService, Boolean isBusy, ArrayList<String> skill, Long workExperience, ArrayList<Services> incomingServices, ArrayList<Services> historyServices) {
        this.currentService = currentService;
        this.isBusy = isBusy;
        this.skill = skill;
        this.workExperience = workExperience;
        this.incomingServices = incomingServices;
        this.historyServices = historyServices;
    }

    public LabourerFinal(String id, String name, String image, String dob, String city, String state, String addressLine1, String addressLine2, String addressLine3, Long phone, Long wallet, ArrayList<String> services, Services currentService, Boolean isBusy, ArrayList<String> skill, Long workExperience, ArrayList<Services> incomingServices, ArrayList<Services> historyServices) {
        super(id, name, image, dob, city, state, addressLine1, addressLine2, addressLine3, phone, wallet, services);
        this.currentService = currentService;
        this.isBusy = isBusy;
        this.skill = skill;
        this.workExperience = workExperience;
        this.incomingServices = incomingServices;
        this.historyServices = historyServices;
    }

    public Services getCurrentService() {
        return currentService;
    }

    public void setCurrentService(Services currentService) {
        this.currentService = currentService;
    }

    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }

    public ArrayList<String> getSkill() {
        return skill;
    }

    public void setSkill(ArrayList<String> skill) {
        this.skill = skill;
    }

    public Long getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Long workExperience) {
        this.workExperience = workExperience;
    }

    public ArrayList<Services> getIncomingServices() {
        return incomingServices;
    }

    public void setIncomingServices(ArrayList<Services> incomingServices) {
        this.incomingServices = incomingServices;
    }

    public ArrayList<Services> getHistoryServices() {
        return historyServices;
    }

    public void setHistoryServices(ArrayList<Services> historyServices) {
        this.historyServices = historyServices;
    }
}
