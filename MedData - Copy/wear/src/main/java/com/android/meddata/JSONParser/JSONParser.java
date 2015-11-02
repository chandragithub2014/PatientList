package com.android.meddata.JSONParser;

import android.content.Context;

import com.android.meddata.MedDataDTO.LocationDTO;
import com.android.meddata.MedDataDTO.PhysicianDTO;
import com.android.meddata.MedDataDTO.RemindersDTO;
import com.android.meddata.MedDataDTO.WorkListDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                workList.add(temp);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return workList;
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




}