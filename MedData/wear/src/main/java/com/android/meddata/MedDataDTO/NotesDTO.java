package com.android.meddata.MedDataDTO;

/**
 * Created by 245742 on 12/3/2015.
 */
public class NotesDTO {
    private String notesType;
    private String patientNotes;
    private String updatedDate;
    private String physician;

    public String getNotesType() {
        return notesType;
    }

    public void setNotesType(String notesType) {
        this.notesType = notesType;
    }

    public String getPhysician() {
        return physician;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getPatientNotes() {
        return patientNotes;
    }

    public void setPatientNotes(String patientNotes) {
        this.patientNotes = patientNotes;
    }
}
