package com.android.meddata.MedDataUtils;


import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 245742 on 12/7/2015.
 */
public class MedDataUtil {


    private static MedDataUtil instance;
    Context ctx;

    private MedDataUtil(){

    }

    public static MedDataUtil getInstance(){
        if(instance == null){
            instance = new MedDataUtil();
        }
        return instance;
    }


    public String getJsonDateFormat(String javaDate){
        String jsonDate = "";
        if(!TextUtils.isEmpty(javaDate)){
			/* Date dt = new Date(javaDate);
			 Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					 cal.setTime(dt);*/
            //cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            //TimeZone.getTimeZone("GMT").getOffset(time)
            //SimpleDateFormat format = new SimpleDateFormat("Z");

            //String str = "\/Date(" + dt.getTime() + "-0400)\/";
            ///	format.setTimeZone(TimeZone.getTimeZone("GMT"));
            //jsonDate  ="/Date(" + String.valueOf(dt.getTime())+")/";

            try {
                SimpleDateFormat sdf  = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
                javaDate =javaDate +" "+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":"+
                        Calendar.getInstance().get(Calendar.MINUTE)+":"+
                        Calendar.getInstance().get(Calendar.SECOND);
                System.out.println("javaDate"+javaDate);
                long unixtime = sdf.parse(javaDate).getTime();

                jsonDate  ="/Date(" + String.valueOf(unixtime)+")/";
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return jsonDate;
    }


     //getAge(new Date(dateInString))
    public  int getAge(Date dateOfBirth) {
        Calendar calAgeReferenceDate = Calendar.getInstance();
        Calendar calDateOfBirth = Calendar.getInstance();
        int refDateMonth;
        int dobDateMonth;
        int age;

        //  calAgeReferenceDate.setTime(ageReferenceDate);

        calDateOfBirth.setTime(dateOfBirth);
        age = calAgeReferenceDate.get(Calendar.YEAR) - calDateOfBirth.get(Calendar.YEAR);

        refDateMonth = Integer.parseInt(String.format("%02d", calAgeReferenceDate.get(Calendar.MONTH)) + String.format("%02d", calAgeReferenceDate.get(Calendar.DAY_OF_MONTH)));
        dobDateMonth = Integer.parseInt(String.format("%02d", calDateOfBirth.get(Calendar.MONTH)) + String.format("%02d", calDateOfBirth.get(Calendar.DAY_OF_MONTH)));

        if (refDateMonth < dobDateMonth) {
            age--;
        }

        return age;
    }
}
