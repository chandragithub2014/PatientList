package com.android.meddata.MedDataDTO;

/**
 * Created by CHANDRASAIMOHAN on 10/21/2015.
 */
public class RemindersDTO {
    private String day;
    private String dateOfDay;
    private String patientName;
    private String primaryPhysician;
    private String secPhysician;
    private String hospitalName;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDateOfDay() {
        return dateOfDay;
    }

    public void setDateOfDay(String dateOfDay) {
        this.dateOfDay = dateOfDay;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSecPhysician() {
        return secPhysician;
    }

    public void setSecPhysician(String secPhysician) {
        this.secPhysician = secPhysician;
    }

    public String getPrimaryPhysician() {
        return primaryPhysician;
    }

    public void setPrimaryPhysician(String primaryPhysician) {
        this.primaryPhysician = primaryPhysician;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
