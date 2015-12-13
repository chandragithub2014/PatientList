package com.android.meddata.interfaces;

import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by CHANDRASAIMOHAN on 12/12/2015.
 */
public class MyYAxisValueFormatter implements ValueFormatter {
    private DecimalFormat mFormat;

    public MyYAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }
    @Override
    public String getFormattedValue(float v) {
        return mFormat.format(v);
    }
}
