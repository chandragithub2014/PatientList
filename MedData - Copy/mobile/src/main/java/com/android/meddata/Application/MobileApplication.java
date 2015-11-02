package com.android.meddata.Application;

import android.app.Activity;
import android.app.Application;

/**
 * Created by 245742 on 9/8/2015.
 */
public class MobileApplication extends Application {
    private static MobileApplication singleton;
    private Activity activity;
    private String json;
    private String imageURL;
    private String propertiesJSON;
    private String message;
    private String patientList;
    private String reminderList;
    private String accountDetails;
    private String locationList;
    private String physicianList;
    private String billingList;
    private String dispositionList;
    private String gender;
    private String notesType;
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public static MobileApplication getInstance() {
        return singleton;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPropertiesJSON() {
        return propertiesJSON;
    }

    public void setPropertiesJSON(String propertiesJSON) {
        this.propertiesJSON = propertiesJSON;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPatientList() {
        return patientList;
    }

    public void setPatientList(String patientList) {
        this.patientList = patientList;
    }

    public String getReminderList() {
        return reminderList;
    }

    public void setReminderList(String reminderList) {
        this.reminderList = reminderList;
    }

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getLocationList() {
        return locationList;
    }

    public void setLocationList(String locationList) {
        this.locationList = locationList;
    }

    public String getPhysicianList() {
        return physicianList;
    }

    public void setPhysicianList(String physicianList) {
        this.physicianList = physicianList;
    }

    public String getBillingList() {
        return billingList;
    }

    public void setBillingList(String billingList) {
        this.billingList = billingList;
    }

    public String getDispositionList() {
        return dispositionList;
    }

    public void setDispositionList(String dispositionList) {
        this.dispositionList = dispositionList;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNotesType() {
        return notesType;
    }

    public void setNotesType(String notesType) {
        this.notesType = notesType;
    }
}
