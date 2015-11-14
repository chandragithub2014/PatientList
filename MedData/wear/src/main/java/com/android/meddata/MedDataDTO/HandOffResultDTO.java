package com.android.meddata.MedDataDTO;

/**
 * Created by CHANDRASAIMOHAN on 11/13/2015.
 */
public class HandOffResultDTO {
    private  String patientName;
    private  String hospitalLocation;
    private String primPhy;
    private int patientId;
    private int encounterId;
    private String encounterDate;

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

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(int encounterId) {
        this.encounterId = encounterId;
    }

    public String getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(String encounterDate) {
        this.encounterDate = encounterDate;
    }
}
