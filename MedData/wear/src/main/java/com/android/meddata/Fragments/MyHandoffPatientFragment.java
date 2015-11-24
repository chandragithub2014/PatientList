package com.android.meddata.Fragments;

//http://stackoverflow.com/questions/17312435/change-divider-color-or-theme-of-android-datepicker-dialog

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.LocationDTO;
import com.android.meddata.MedDataDTO.PhysicianDTO;
import com.android.meddata.MedDataUtils.MedDataConstants;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.R;
import com.android.meddata.custom.CustomSpinner;
import com.android.meddata.interfaces.SendDataDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyHandoffPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyHandoffPatientFragment extends Fragment implements View.OnClickListener,SendDataDialogListener {
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
    String selctedLocation="";
    String selectedPhy="";



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandoffPatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyHandoffPatientFragment newInstance(String param1, String param2) {
        MyHandoffPatientFragment fragment = new MyHandoffPatientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyHandoffPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);
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
                postForSearchResults();

         /*       FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, HandOffSearchResultFragment.newInstance("","")).addToBackStack(null).commit();*/
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

        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dt);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", dt); //Jun
        String intMonth = (String) android.text.format.DateFormat.format("MM", dt); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", dt); //2013
        String day = (String) android.text.format.DateFormat.format("dd", dt); //20

        Log.d("TAG","Day Of Week:::"+dayOfTheWeek);
        Log.d("TAG","Day Of stringMonth:::"+stringMonth);
        Log.d("TAG","Day Of intMonth:::"+intMonth);
        Log.d("TAG","Day Of year:::"+year);
        Log.d("TAG","Day Of day:::"+day);
        initSpinners(view);
        return view;
    }

    private void initSpinners(View v) {
        Spinner locationSpinner = (Spinner) v.findViewById(R.id.loc_spinner);
        Spinner physicianSpinner = (Spinner) v.findViewById(R.id.pr_phy);

        List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getLocationList());
        List<String> hospitalList = new ArrayList<String>();
        if (locList != null && locList.size() > 0) {
            for (int i = 0; i < locList.size(); i++) {
                hospitalList.add(locList.get(i).getListDesc());
            }
            if (hospitalList != null && hospitalList.size() > 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                locationSpinner.setAdapter(adapter);
            }

        }

        if (!TextUtils.isEmpty(MobileApplication.getInstance().getPhysicianList())) {
            List<PhysicianDTO> prPhyList = JSONParser.getInstance().getPhysicianList(MobileApplication.getInstance().getPhysicianList());
            List<String> phyList = new ArrayList<String>();
            if (prPhyList != null && prPhyList.size() > 0) {
                for (int i = 0; i < prPhyList.size(); i++) {
                    phyList.add(prPhyList.get(i).getPhyDesc());
                }
                if (phyList != null && phyList.size() > 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, phyList);
                    physicianSpinner.setAdapter(adapter);

                   /* if(!TextUtils.isEmpty(tempWorkListDto.getPhysicianName())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getPhysicianName());
                        physicianSpinner.setSelection(spinnerPosition);
                    }*/
                }
            }
        }
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selctedLocation = parent.getItemAtPosition(position)
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        physicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPhy = parent.getItemAtPosition(position)
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.to_date_val:

               /* FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, DatePickerWearFragment.newInstance("","")).addToBackStack(null).commit();*/

              /*  DialogFragment newFragment = new DatePickerDialogFragment();
                newFragment.show(getFragmentManager(), "datePicker");*/

                FragmentManager fm = getFragmentManager();
                DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment ();
                Bundle args = new Bundle();
                args.putString("date", toDateVal.getText().toString());
                args.putSerializable("type","to");
                dialogFragment.setArguments(args);
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(fm, "Date Dialog Fragment");
              //  showDatePicker();
                //processDate(toDateVal.getText().toString());
                break;
            case R.id.from_date_val:
                formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date dt = new Date();

                isFromSelected = true;
                FragmentManager fm1 = getFragmentManager();
                DatePickerDialogFragment dialogFragment1 = new DatePickerDialogFragment ();
                Bundle args1 = new Bundle();
                args1.putString("date",formatter.format(dt));
                args1.putSerializable("type", "from");
                dialogFragment1.setArguments(args1);
                dialogFragment1.setTargetFragment(this, 0);
                dialogFragment1.show(fm1, "Date Dialog Fragment");

               /* FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, CustomDatePickerFragment.newInstance("","")).addToBackStack(null).commit();*/
              //  processDate(fromDateVal.getText().toString());
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

    @Override
    public void onFinishDialog(String inputText,String type) {
        if(!TextUtils.isEmpty(inputText)) {
            //Oct 1,2015
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MMM dd, yyyy", Locale.ENGLISH);
           // Date myDate;
            try {
                Date   myDate = dateFormat.parse(inputText);
                if(type.equalsIgnoreCase("to")) {
                    if (toDateVal != null) {
                        toDateVal.setText(dateFormat.format(myDate));
                    }
                }
                if(type.equalsIgnoreCase("from")){
                    if (fromDateVal != null) {
                        fromDateVal.setText(dateFormat.format(myDate));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }



    public void postForSearchResults(){
        JSONObject handOffSearchJSON = prepareHandsOffSearchJSON();
        if(handOffSearchJSON!=null){
            Log.d("TAG", "Account Details Update JSON:::" + handOffSearchJSON);
            MobileApplication.getInstance().setHandOffSearchJSON(""+handOffSearchJSON);
            MessageService.getInstance().startMessageService(getActivity(), "handoffSearch");
        }
    }


    private JSONObject prepareHandsOffSearchJSON(){
        String globalJSON = MobileApplication.getInstance().getPropertiesJSON();
        String key = "";
        try {
            JSONObject jsonObject = new JSONObject(globalJSON);
            key = jsonObject.getString("Key");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        JSONObject updateJSON = null;
        JSONObject    requestJsonObject = null;
        try {
            updateJSON = new JSONObject();
            if(!TextUtils.isEmpty(selectedPhy)){
                updateJSON.put("PrimaryPhysician", selectedPhy);
            }else {
                updateJSON.put("PrimaryPhysician", "Tyy Dr"); //Sue Chilson
            }
            updateJSON.put("ToDate", getJsonDateFormat(toDateVal.getText().toString()));
            if(!fromDateVal.getText().toString().equalsIgnoreCase("Select")){
                updateJSON.put("FromDate", getJsonDateFormat(fromDateVal.getText().toString()));
            }
            updateJSON.put("MedicalRecordNo", "");
            updateJSON.put("Key",key);
            updateJSON.put("Login_Id", MedDataConstants.LOGIN_ID);
            updateJSON.put("TransactionType", "R");
            updateJSON.put("FirstName", "");
            updateJSON.put("LocationId", selctedLocation);
            updateJSON.put("AdmissionNo", "");
            updateJSON.put("FinancialNo", "");
            updateJSON.put("DispositionId", "");
         /*   if(clickedLocationId==0){
                updateJSON.put("LocationId", locationId);
            }else {
                updateJSON.put("LocationId", "" + clickedLocationId);
            }*/

            requestJsonObject = new JSONObject();
            requestJsonObject.put("request", updateJSON);

        }catch (JSONException e){
            e.printStackTrace();
        }


        return requestJsonObject;
    }



    private String getJsonDateFormat(String javaDate){
        Log.d("TAG","javaDate :::::"+javaDate);
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
            } catch(java.text.ParseException e){
                e.printStackTrace();
            }
        }

        return jsonDate;
    }


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  String message = intent.getStringExtra("message");
            //Log.v("myTag", "Main activity received message: " + message);

            try {
                //
                if (intent.hasExtra("result")) {
                    String result_data = intent.getStringExtra("result");
                    if (result_data.equalsIgnoreCase("handoffSearch")) {
                        Log.d("TAG", "handoffSearchResponse::::" + MobileApplication.getInstance().getHandsOffSearchResponse());
                        if(MobileApplication.getInstance().getHandsOffSearchResponse().equalsIgnoreCase("No")){
                            Toast.makeText(getActivity(), "No Records Found", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d("TAG","Search Response:::"+MobileApplication.getInstance().getHandsOffSearchResponse());
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.framelayout, HandsOffPatientSearchResultFragment.newInstance("revert","")).addToBackStack(null)
                                    .commit();
                        }
                        //Toast.makeText(getActivity(), MobileApplication.getInstance().getHandsOffSearchResponse(), Toast.LENGTH_SHORT).show();
                        //      Toast.makeText(getActivity(),MobileApplication.getInstance().getBulkUpdateResponse(),Toast.LENGTH_LONG).show();

                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
