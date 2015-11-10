package com.android.meddata.Fragments;

//http://stackoverflow.com/questions/17312435/change-divider-color-or-theme-of-android-datepicker-dialog

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.meddata.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandoffPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandoffPatientFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view = null;
    int mContainerId = -1;

    ImageButton back_float;
    EditText toDateVal,fromDateVal;
    SimpleDateFormat formatter;
    protected DatePickerDialog  dateMyAlert;

    boolean isDateClear = false;
    boolean isDateCancel = false;
    boolean isDateSet = false;
    private boolean isFromSelected = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandoffPatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandoffPatientFragment newInstance(String param1, String param2) {
        HandoffPatientFragment fragment = new HandoffPatientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HandoffPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view  =  inflater.inflate(R.layout.handoffs_current_patients_layout, container, false);
        mContainerId = container.getId();
        back_float = (ImageButton)view.findViewById(R.id.fab_back);
        back_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });
        Button search = (Button)view.findViewById(R.id.search_handoff);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, HandOffSearchResultFragment.newInstance("","")).addToBackStack(null).commit();
            }
        });

        fromDateVal = (EditText)view.findViewById(R.id.from_date_val);
        toDateVal  = (EditText)view.findViewById(R.id.to_date_val);

        formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Date dt = new Date();
        toDateVal.setText(formatter.format(dt));
        toDateVal.setTextColor(Color.parseColor("#999999"));
        toDateVal.setOnClickListener(this);
        fromDateVal.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.to_date_val:

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, DatePickerWearFragment.newInstance("","")).addToBackStack(null).commit();
              //  showDatePicker();
                //processDate(toDateVal.getText().toString());
                break;
            case R.id.from_date_val:
                isFromSelected = true;
                processDate(fromDateVal.getText().toString());
                break;
        }
    }

    private void showDatePicker(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View sortDialogView = inflater.inflate(R.layout.dialog_date_picker, null);
        dialogBuilder.setView(sortDialogView);
         AlertDialog  sortByDialog = dialogBuilder.create();
    //    sortByDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(sortByDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        sortByDialog.show();
       sortByDialog.getWindow().setAttributes(lp);
    }

//String dateInString  = toDateVal.getText().toString();
    private void processDate(String dateInString){
        formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        formatter.setLenient(false);

        try{
            Date date = null;
            if(TextUtils.isEmpty(dateInString) ||  dateInString.equalsIgnoreCase("Select"))
                date = new Date();
            else
                date = formatter.parse(dateInString);
            final Calendar c = Calendar
                    .getInstance();
            c.setTime(date);
            int year =  c.get(Calendar.YEAR);
            int month =  c.get(Calendar.MONTH);
            int day =   c.get(Calendar.DAY_OF_MONTH);

            dateMyAlert = new DatePickerDialog(
                    getActivity(),
                    dateSetListener,
                    year,
                    month,
                    day);
            dateMyAlert.getDatePicker().setMaxDate(System.currentTimeMillis());
            dateMyAlert.getDatePicker().setCalendarViewShown(false);

            dateMyAlert.setButton(DialogInterface.BUTTON_POSITIVE, "Set", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("TAG", "Date Picker SET.... Clicked");
                    //datePickerbutton.setText("Select");
                    isDateSet = true;
                    DatePicker datePicker = dateMyAlert.getDatePicker();
                    dateSetListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                }
            });
           /* dateMyAlert.setButton(DialogInterface.BUTTON_NEUTRAL, "Clear", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("TAG", "Date Picker CLEAR.... Clicked");
                    //datePickerbutton.setText("Select");
                    isDateClear = true;
                    DatePicker datePicker = dateMyAlert.getDatePicker();
                    dateSetListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                }
            });*/

            dateMyAlert.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("TAG", "Date Picker Cancel.... Cancel");
                    isDateCancel = true;
                    DatePicker datePicker = dateMyAlert.getDatePicker();
                    dateSetListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }

       /* WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dateMyAlert.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;*/
        dateMyAlert.show();
     //   dateMyAlert.getWindow().setAttributes(lp);
    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0,
                              int year,
                              int monthofyear,
                              int dayOfMonth) {
            String monthString;
            String dayString;
            int mYear = year;
            if (monthofyear + 1 < 10) {
                monthString = "0"
                        + (monthofyear+1);
            } else {
                monthString = ""
                        + (monthofyear + 1);
            }
            if (dayOfMonth < 10) {
                dayString = "0"
                        + dayOfMonth;
            } else {
                dayString = ""
                        + dayOfMonth;
            }
            if(isDateSet){
                isDateSet=false;
                String inputFomat_val = new StringBuilder()
                        .append(monthString)
                        .append("-")
                        .append(dayString)
                        .append("-")
                        .append(mYear)
                        .append(" ").toString();

                String outputFormat_val = getReadableDate(inputFomat_val, "MM-dd-yyyy", "MMM dd, yyyy");
if(isFromSelected) {
    fromDateVal.setText(outputFormat_val);
}else{
    toDateVal.setText(outputFormat_val);
}
            }/*else if(isDateClear){

                isDateClear = false;
              //  dateInString ="Select";
           *//*datePicker.setTag(dateInString);*//*
                if(isFromSelected) {
                    fromDateVal.setText("Select");
                }
                else{
                    toDateVal.setText("Select");
                }
                isDateCancel = false;
            }*/

        }
    };


    private String getReadableDate(String originalDate,String inputFormat,String outputFormat){
        String readableDate="";
        SimpleDateFormat input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat output = new SimpleDateFormat(outputFormat);
        try {
            Date oneWayTripDate = input.parse(originalDate); // parse input
            readableDate = (output.format(oneWayTripDate));
        } catch (ParseException e) {
            Log.e("TAG",
                    "ParseException. Error is:"+e.getMessage() +". Method[getReadableDate] paramaters are:originalDate["
                            + originalDate + "], inputFormat[" + inputFormat
                            + "],outputFormat[" + outputFormat + "] Error is:"+e.getMessage());
            e.printStackTrace();
        }
        return readableDate;
    }


}
