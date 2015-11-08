package com.android.meddata.Fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.android.meddata.R;

import java.lang.reflect.Field;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomDatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomDatePickerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View mRootView=null;

    TextView mDialogTitle;
    NumberPicker mYearPicker,mDayPicker, mMonthPicker;
    Button mCancelButton,mSetButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomDatePickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomDatePickerFragment newInstance(String param1, String param2) {
        CustomDatePickerFragment fragment = new CustomDatePickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CustomDatePickerFragment() {
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
        mRootView = inflater.inflate(R.layout.fragment_custom_date_picker, container, false);
        mYearPicker = (NumberPicker) mRootView.findViewById(R.id.year);
        mYearPicker.setMaxValue(99);
        mYearPicker.setMinValue(0);
        
        mYearPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        mDayPicker = (NumberPicker) mRootView.findViewById(R.id.day);
        mDayPicker.setMaxValue(31);
        mDayPicker.setMinValue(01);
        mDayPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });


        mMonthPicker = (NumberPicker) mRootView.findViewById(R.id.month);
        mMonthPicker.setMaxValue(12);
        mMonthPicker.setMinValue(01);

        mMonthPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
    //    mCancelButton = (Button) mRootView.findViewById(R.id.cancelButton);
  //      mSetButton = (Button) mRootView.findViewById(R.id.setButton);
    //    mDialogTitle = (TextView) mRootView.findViewById(R.id.dateDialogTitle);
       // setListeners(); // Or set listeners here
   //    prepareDialog();
        // Inflate the layout for this fragment

        return mRootView;
    }

    /**
     * Using reflection to access private variables for the number picker in the datepicker
     */
   /*private void prepareDialog() {
      //  mDialogTitle.setText(mTitle);
        try {
            Field datePickerFields[] = mDatePicker.getClass().getDeclaredFields();
            for (Field field : datePickerFields) {
                if ("mSpinners".equals(field.getName())) {
                    field.setAccessible(true);
                    Object spinnersObj = new Object();
                    spinnersObj = field.get(mDatePicker);
                    LinearLayout mSpinners = (LinearLayout) spinnersObj;
                    mDatePicker = (NumberPicker) mSpinners.getChildAt(0);
               *//*     dayPicker = (NumberPicker) mSpinners.getChildAt(1);
                    yearPicker = (NumberPicker) mSpinners.getChildAt(2);
                    setDividerColor(monthPicker);
                    setDividerColor(dayPicker);
                    setDividerColor(yearPicker);*//*
                    break;
                }
            }
        } catch (Exception ex) {
            Log.v("TAG", "Unable to change date dialog style");
        }
    }
*/

   /* private void setDividerColor(NumberPicker picker) {
        Field[] numberPickerFields = NumberPicker.class.getDeclaredFields();
        for (Field field : numberPickerFields) {
            if (field.getName().equals("mSelectionDivider")) {
                field.setAccessible(true);
                try {
                    field.set(picker, getResources().getDrawable(R.drawable.c1_drawable));
                } catch (IllegalArgumentException e) {
                    Log.v("TAG", "Illegal Argument Exception");
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    Log.v("TAG", "Resources NotFound");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.v("TAG", "Illegal Access Exception");
                    e.printStackTrace();
                }
                break;
            }
        }
    }*/
}
