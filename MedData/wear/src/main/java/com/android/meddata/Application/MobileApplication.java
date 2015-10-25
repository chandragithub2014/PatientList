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
    private String patientList;
    private String reminderList;
    private String accountDetails;
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
}
