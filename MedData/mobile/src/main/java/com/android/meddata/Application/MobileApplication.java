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
    private String bulkUpdatedList;
    private String bulkUpdateResponse;
    private String updatedAccountDetails;
    private String account_update_response;
    private String handOffSearchJSON;
    private String handsOffSearchResponse;
    private String handOffPatientJSON;
    private String patientHandOffResponse;
    private String patientRevertJSON;
    private String patientRevertResponse;
    private int reminderCount;
    private int workListCount;
    private String patientNotes;

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

    public String getBulkUpdatedList() {
        return bulkUpdatedList;
    }

    public void setBulkUpdatedList(String bulkUpdatedList) {
        this.bulkUpdatedList = bulkUpdatedList;
    }

    public String getBulkUpdateResponse() {
        return bulkUpdateResponse;
    }

    public void setBulkUpdateResponse(String bulkUpdateResponse) {
        this.bulkUpdateResponse = bulkUpdateResponse;
    }

    public String getUpdatedAccountDetails() {
        return updatedAccountDetails;
    }

    public void setUpdatedAccountDetails(String updatedAccountDetails) {
        this.updatedAccountDetails = updatedAccountDetails;
    }

    public String getAccount_update_response() {
        return account_update_response;
    }

    public void setAccount_update_response(String account_update_response) {
        this.account_update_response = account_update_response;
    }

    public String getHandOffSearchJSON() {
        return handOffSearchJSON;
    }

    public void setHandOffSearchJSON(String handOffSearchJSON) {
        this.handOffSearchJSON = handOffSearchJSON;
    }

    public String getHandsOffSearchResponse() {
        return handsOffSearchResponse;
    }

    public void setHandsOffSearchResponse(String handsOffSearchResponse) {
        this.handsOffSearchResponse = handsOffSearchResponse;
    }

    public String getHandOffPatientJSON() {
        return handOffPatientJSON;
    }

    public void setHandOffPatientJSON(String handOffPatientJSON) {
        this.handOffPatientJSON = handOffPatientJSON;
    }

    public String getPatientHandOffResponse() {
        return patientHandOffResponse;
    }

    public void setPatientHandOffResponse(String patientHandOffResponse) {
        this.patientHandOffResponse = patientHandOffResponse;
    }

    public String getPatientRevertJSON() {
        return patientRevertJSON;
    }

    public void setPatientRevertJSON(String patientRevertJSON) {
        this.patientRevertJSON = patientRevertJSON;
    }

    public String getPatientRevertResponse() {
        return patientRevertResponse;
    }

    public void setPatientRevertResponse(String patientRevertResponse) {
        this.patientRevertResponse = patientRevertResponse;
    }

    public int getReminderCount() {
        return reminderCount;
    }

    public void setReminderCount(int reminderCount) {
        this.reminderCount = reminderCount;
    }

    public int getWorkListCount() {
        return workListCount;
    }

    public void setWorkListCount(int workListCount) {
        this.workListCount = workListCount;
    }

    public String getPatientNotes() {
        return patientNotes;
    }

    public void setPatientNotes(String patientNotes) {
        this.patientNotes = patientNotes;
    }
}
