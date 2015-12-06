package com.android.meddata.JSONParser;

import android.content.Context;
import android.text.TextUtils;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.MedDataDTO.LocationDTO;
import com.android.meddata.MedDataDTO.NotesDTO;
import com.android.meddata.MedDataDTO.PhysicianDTO;
import com.android.meddata.MedDataDTO.RemindersDTO;
import com.android.meddata.MedDataDTO.WorkListDTO;
import com.android.meddata.MedDataUtils.MedDataConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/24/2015.
 */
public class JSONParser {


    private static JSONParser instance;
    Context ctx;

    private JSONParser(){

    }

    public static JSONParser getInstance(){
        if(instance == null){
            instance = new JSONParser();
        }
        return instance;
    }

    public List<WorkListDTO> getWorkList(String jsonArray){
        List<WorkListDTO> workList = new ArrayList<WorkListDTO>();
        HashMap<String,WorkListDTO> workListHash = new HashMap<String,WorkListDTO>();
        try{
            JSONArray jsonArray1 = new JSONArray(jsonArray);
            for(int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                WorkListDTO temp = new WorkListDTO();
                temp.setDay(jsonObject.getString("EncWeekDay"));
                temp.setPhysicianName("Pr." + jsonObject.getString("PrimaryPhysician"));
                temp.setPatientName(jsonObject.getString("Patientname"));
                temp.setHospitalName(jsonObject.getString("LocationId"));
                temp.setBillingStatus(jsonObject.getString("DispositionId"));
                temp.setRoomNum(jsonObject.getString("RoomNumber"));
                String encounterDate = jsonObject.getString("FormattedEncDate");
                SimpleDateFormat input = new SimpleDateFormat("yy/MM/dd");
                SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
                try {
                    Date oneWayTripDate = input.parse(encounterDate);                 // parse input
                    String readableDate = (output.format(oneWayTripDate));
                    temp.setDate(readableDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                temp.setEncounterId(jsonObject.getString("EncounterId"));
                temp.setMrn(jsonObject.getString("MedicalRecordNo"));
                temp.setAge(jsonObject.getString("Age"));
                temp.setBillingType(jsonObject.getString("BillingType"));
                if(jsonObject.has("gender") && jsonObject.getString("gender")!=null){
                    temp.setGender(jsonObject.getString("gender"));
                }
                if(jsonObject.getString("DOB")!=null){
                    temp.setDob(jsonObject.getString("DOB"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("NotesType"))){
                    temp.setNotesType(jsonObject.getString("NotesType"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("SecondaryPhysician"))){
                    temp.setSecPhysician(jsonObject.getString("SecondaryPhysician"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("FinancialNo"))){
                    temp.setFinancialNum(jsonObject.getString("FinancialNo"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("AdmissionNo"))){
                    temp.setAdminNum(jsonObject.getString("AdmissionNo"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("MiddleName"))){
                    temp.setMiddleName(jsonObject.getString("MiddleName"));
                }

                if(!TextUtils.isEmpty(jsonObject.getString("LastName"))){
                  temp.setLastName(jsonObject.getString("LastName"));
                }
/*

                private String unFormattedEncDate;
                private String recNotesDate;
                private String firstName;
                private String dispostionID;
*/
                if(!TextUtils.isEmpty(jsonObject.getString("Encounter_Date"))){
                    temp.setUnFormattedEncDate(jsonObject.getString("Encounter_Date"));
                }

                if(!TextUtils.isEmpty(jsonObject.getString("RecNotesDate"))){
                    temp.setRecNotesDate(jsonObject.getString("RecNotesDate"));
                }

                if(!TextUtils.isEmpty(jsonObject.getString("FirstName"))){
                    temp.setFirstName(jsonObject.getString("FirstName"));
                }

                if(!TextUtils.isEmpty(jsonObject.getString("DispositionId"))){
                    temp.setDispostionID(jsonObject.getString("DispositionId"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("EncounterId"))){
                    workListHash.put(jsonObject.getString("EncounterId"), temp);
                }
                if(MobileApplication.getInstance().getWorkListDTOHashMap()!=null && MobileApplication.getInstance().getWorkListDTOHashMap().size()>0){
               //     MobileApplication.getInstance().getWorkListDTOHashMap().clear();
                    HashMap<String,WorkListDTO> tempHash = MobileApplication.getInstance().getWorkListDTOHashMap();
                    tempHash.put(jsonObject.getString("EncounterId"), temp);
                    MobileApplication.getInstance().setWorkListDTOHashMap(tempHash);
                }else{
                    MobileApplication.getInstance().setWorkListDTOHashMap(workListHash);
                }


                workList.add(temp);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return workList;
    }



    public List<NotesDTO> getNotesList(String jsonArray){
        List<NotesDTO> notesList = new ArrayList<NotesDTO>();
        try{
            JSONArray jsonArray1 = new JSONArray(jsonArray);
            for(int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                NotesDTO temp = new NotesDTO();
                temp.setPatientNotes(jsonObject.getString("Notes"));
                temp.setNotesType(jsonObject.getString("NotesType"));
                temp.setPhysician(jsonObject.getString("Physician"));
                temp.setUpdatedDate(jsonObject.getString("UpdatedDate"));


                notesList.add(temp);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return notesList;
    }


    /*
"DispositionId" : "Signed Off",
      "Login_Id" : "test1",
      "PrimaryPhysician" : "Phy444",
      "EncounterId" : 199,
      "Key" : "9C4A6Z:l",
      "TransactionType" : "WA"
 */
    public JSONObject  getUpdatedPatientList(String depositionId){
        JSONObject requestJsonObject = null;
        JSONArray updatedJSONArray = new JSONArray();
        JSONObject updatedJSON = new JSONObject();
        String globalJSON = MobileApplication.getInstance().getPropertiesJSON();
        String key = "";
        try {
            JSONObject jsonObject = new JSONObject(globalJSON);
            key = jsonObject.getString("Key");
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        String jsonArray = MobileApplication.getInstance().getPatientList();
        try{
            JSONArray jsonArray1 = new JSONArray(jsonArray);
            for(int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);

                updatedJSON.put("DispositionId",depositionId);
                updatedJSON.put("Login_Id", MedDataConstants.LOGIN_ID);
                updatedJSON.put("PrimaryPhysician", jsonObject.getString("PrimaryPhysician"));
                updatedJSON.put("EncounterId", jsonObject.getString("EncounterId"));
                updatedJSON.put("Key", key);
                updatedJSON.put("TransactionType", "WA");

                updatedJSONArray.put(updatedJSON);


            }
            requestJsonObject = new JSONObject();
            requestJsonObject.put("request", updatedJSONArray);

        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return  requestJsonObject;
    }
    public WorkListDTO getPatientDetail(String encounterId){
        WorkListDTO temp  = new WorkListDTO();
        try {
            JSONArray workList = new JSONArray(MobileApplication.getInstance().getPatientList());
            for(int i=0;i<workList.length();i++){
                JSONObject jsonObject = workList.getJSONObject(i);
                if(jsonObject.getString("EncounterId").equalsIgnoreCase(encounterId)){
                    temp.setPatientName(jsonObject.getString("Patientname"));
                    temp.setRoomNum(jsonObject.getString("RoomNumber"));
                    String encounterDate = jsonObject.getString("FormattedEncDate");
                    SimpleDateFormat input = new SimpleDateFormat("yy/MM/dd");
                    SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
                    try {
                        Date oneWayTripDate = input.parse(encounterDate);                 // parse input
                        String readableDate = (output.format(oneWayTripDate));
                        temp.setDate(readableDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    temp.setHospitalName(jsonObject.getString("LocationId"));
                    temp.setPhysicianName(jsonObject.getString("PrimaryPhysician"));
                    temp.setMrn(jsonObject.getString("MedicalRecordNo"));
                    temp.setAge(jsonObject.getString("Age"));
                    temp.setBillingType(jsonObject.getString("BillingType"));
                    temp.setBillingStatus(jsonObject.getString("DispositionId"));
                }else{
                    continue;
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return temp;
    }
    public List<RemindersDTO> getRemindersList(String jsonArray){
        List<RemindersDTO> reminderList = new ArrayList<RemindersDTO>();

        try{
            JSONArray jsonArray1 = new JSONArray(jsonArray);
            for(int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                RemindersDTO temp = new RemindersDTO();
                temp.setDay(jsonObject.getString("EncWeekDay"));
                String encounterDate = jsonObject.getString("FormattedEncDate");
                SimpleDateFormat input = new SimpleDateFormat("yy/MM/dd");
                SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
                try {
                    Date oneWayTripDate = input.parse(encounterDate);                 // parse input
                    String readableDate = (output.format(oneWayTripDate));
                    temp.setDateOfDay(readableDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                temp.setPatientName(jsonObject.getString("Patientname"));
                temp.setPrimaryPhysician("Pr." + jsonObject.getString("PrimaryPhysician"));
                temp.setSecPhysician("Sec." + jsonObject.getString("SecondaryPhysician"));
                temp.setHospitalName(jsonObject.getString("LocationId"));


                temp.setEncounterId(jsonObject.getString("EncounterId"));
                temp.setMrn(jsonObject.getString("MedicalRecordNo"));
                temp.setAge(jsonObject.getString("Age"));
                temp.setBillingType(jsonObject.getString("BillingType"));
                if(jsonObject.has("gender") && jsonObject.getString("gender")!=null){
                    temp.setGender(jsonObject.getString("gender"));
                }
                if(jsonObject.getString("DOB")!=null){
                    temp.setDob(jsonObject.getString("DOB"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("NotesType"))){
                    temp.setNotesType(jsonObject.getString("NotesType"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("SecondaryPhysician"))){
                    temp.setSecPhysician("Sec."+jsonObject.getString("SecondaryPhysician"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("FinancialNo"))){
                    temp.setFinancialNum(jsonObject.getString("FinancialNo"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("AdmissionNo"))){
                    temp.setAdminNum(jsonObject.getString("AdmissionNo"));
                }
                if(!TextUtils.isEmpty(jsonObject.getString("RoomNumber"))) {
                    temp.setRoomNum(jsonObject.getString("RoomNumber"));
                }
                reminderList.add(temp);
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return reminderList;
    }


    public List<LocationDTO> getLocationList(String jsonArray){
        List<LocationDTO> locationList = new ArrayList<LocationDTO>();
        try{
            JSONArray jsonArray1 = new JSONArray(jsonArray);
            for(int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                LocationDTO temp = new LocationDTO();
                temp.setCategory(jsonObject.getString("Category"));
                temp.setListDesc(jsonObject.getString("ListDesc"));
                temp.setListId(jsonObject.getString("ListId"));
                temp.setLocId(jsonObject.getString("LocID"));
                locationList.add(temp);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return locationList;
    }

    public List<PhysicianDTO> getPhysicianList(String jsonArray){
        List<PhysicianDTO> physicianList = new ArrayList<PhysicianDTO>();
        try{
            JSONArray jsonArray1 = new JSONArray(jsonArray);
            for(int i=0;i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                PhysicianDTO temp = new PhysicianDTO();
                temp.setCategory(jsonObject.getString("Category"));
                temp.setPhyDesc(jsonObject.getString("ListDesc"));
                temp.setListId(jsonObject.getString("ListId"));
                temp.setLocId(jsonObject.getString("LocID"));
                physicianList.add(temp);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return physicianList;
    }

    public JSONObject getPatientUpdateJSON(String encounterId,String billingType,String location,String primaryPhysician){
        JSONObject  requestJsonObject = null;
        JSONObject updatedJSON = new JSONObject();
        if(MobileApplication.getInstance().getWorkListDTOHashMap()!=null && MobileApplication.getInstance().getWorkListDTOHashMap().size()>0) {
            HashMap<String, WorkListDTO> workListHash = MobileApplication.getInstance().getWorkListDTOHashMap();
            WorkListDTO temp = workListHash.get(encounterId);


          try {
              updatedJSON.put("Login_Id",MedDataConstants.LOGIN_ID);
              String globalJSON = MobileApplication.getInstance().getPropertiesJSON();
              String key = "";
              try {
                  JSONObject jsonObject = new JSONObject(globalJSON);
                  key = jsonObject.getString("Key");
              }
              catch (JSONException e){
                  e.printStackTrace();
              }
              updatedJSON.put("Key",key);
              updatedJSON.put("EncounterId",encounterId);
              updatedJSON.put("EncWeekDay", temp.getDay());
              updatedJSON.put("RecNotesDate", temp.getRecNotesDate());
              updatedJSON.put("MissedEncounters","0");
              /*

                SimpleDateFormat input = new SimpleDateFormat("yy/MM/dd");
                SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
                try {
                    Date oneWayTripDate = input.parse(encounterDate);                 // parse input
                    String readableDate = (output.format(oneWayTripDate));
                    temp.setDate(readableDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
               */
              updatedJSON.put("PatientId","0");
              updatedJSON.put("EntityID","-1");
              updatedJSON.put("IsContinuing","0");
              updatedJSON.put("transferredDoc","");
              updatedJSON.put("ImageID","");
              String formattedDate = temp.getDate();
              SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
              SimpleDateFormat input  = new SimpleDateFormat("MMM dd, yyyy");
              try {
                  Date oneWayTripDate = input.parse(formattedDate);                 // parse input
                  String readableDate = (output.format(oneWayTripDate));
                //  readableDate = readableDate.replace("/", "\"\"/"/");
                  updatedJSON.put("FormattedEncDate",readableDate);
              } catch (ParseException e) {
                  e.printStackTrace();
              }
              updatedJSON.put("Encounter_Date",temp.getUnFormattedEncDate());
              updatedJSON.put("MissedEncounters","0");
              updatedJSON.put("Patientname",temp.getPatientName());
              updatedJSON.put("LocationId",location);
              updatedJSON.put("FirstName",temp.getFirstName());
              updatedJSON.put("MiddleName",temp.getMiddleName());
              updatedJSON.put("LastName",temp.getLastName());
              updatedJSON.put("MedicalRecordNo",temp.getMrn());
              updatedJSON.put("SecondaryPhysician",temp.getSecPhysician());
              updatedJSON.put("PrimaryPhysician",primaryPhysician);
              updatedJSON.put("AdmissionNo",temp.getAdminNum());
              updatedJSON.put("FinancialNo",temp.getFinancialNum());
              updatedJSON.put("Notes","");
              updatedJSON.put("DispositionId",temp.getDispostionID());
              updatedJSON.put("TransactionType","WU");
              updatedJSON.put( "ImageRef1","");
              updatedJSON.put( "ImageRef2","");
              updatedJSON.put( "ImageRef3","");
              updatedJSON.put("NotesType",temp.getNotesType());
              updatedJSON.put("BillingType",billingType);
              updatedJSON.put("RoomNumber",temp.getRoomNum());
              updatedJSON.put("Age",temp.getAge());
              updatedJSON.put("Gender",temp.getGender());
              updatedJSON.put("NotesType",temp.getNotesType());


                requestJsonObject = new JSONObject();
              requestJsonObject.put("request", updatedJSON);


          }catch (JSONException e){
              e.printStackTrace();
          }
        }
        return  requestJsonObject;


    }

}