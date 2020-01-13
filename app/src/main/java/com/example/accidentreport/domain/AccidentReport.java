package com.example.accidentreport.domain;

import java.io.Serializable;

public class AccidentReport implements Serializable {

    private String id;

    private String reasonAccident;
    private String location;

    private String nameA;
    private String surnamesA;
    private String phoneA;
    private String dniA;
    private String registrationA;

    private String nameB;
    private String surnamesB;
    private String phoneB;
    private String dniB;
    private String registrationB;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReasonAccident() {
        return reasonAccident;
    }

    public void setReasonAccident(String reasonAccident) {
        this.reasonAccident = reasonAccident;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getSurnamesA() {
        return surnamesA;
    }

    public void setSurnamesA(String surnamesA) {
        this.surnamesA = surnamesA;
    }

    public String getPhoneA() {
        return phoneA;
    }

    public void setPhoneA(String phoneA) {
        this.phoneA = phoneA;
    }

    public String getDniA() {
        return dniA;
    }

    public void setDniA(String dniA) {
        this.dniA = dniA;
    }

    public String getRegistrationA() {
        return registrationA;
    }

    public void setRegistrationA(String registrationA) {
        this.registrationA = registrationA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }

    public String getSurnamesB() {
        return surnamesB;
    }

    public void setSurnamesB(String surnamesB) {
        this.surnamesB = surnamesB;
    }

    public String getPhoneB() {
        return phoneB;
    }

    public void setPhoneB(String phoneB) {
        this.phoneB = phoneB;
    }

    public String getDniB() {
        return dniB;
    }

    public void setDniB(String dniB) {
        this.dniB = dniB;
    }

    public String getRegistrationB() {
        return registrationB;
    }

    public void setRegistrationB(String registrationB) {
        this.registrationB = registrationB;
    }
}
