package com.android.meddata.MedDataDTO;

/**
 * Created by CHANDRASAIMOHAN on 11/13/2015.
 */
public class HandOffResultDTO {
    private  String patientName;
    private  String hospitalLocation;
    private String primPhy;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPrimPhy() {
        return primPhy;
    }

    public void setPrimPhy(String primPhy) {
        this.primPhy = primPhy;
    }

    public String getHospitalLocation() {
        return hospitalLocation;
    }

    public void setHospitalLocation(String hospitalLocation) {
        this.hospitalLocation = hospitalLocation;
    }
}
