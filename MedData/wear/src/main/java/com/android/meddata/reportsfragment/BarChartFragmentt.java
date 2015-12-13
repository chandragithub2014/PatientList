/*
 * Copyright (C) 2014 - Cognizant Technology Solutions.
 * This file is a part of OneMobileStudio
 * Licensed under the OneMobileStudio, Cognizant Technology Solutions, 
 * Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.cognizant.com/
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*

package com.android.meddata.reportsfragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.MedDataUtils.MedDataUtil;
import com.android.meddata.R;
import com.android.meddata.interfaces.OMSReceiveListener;
import com.android.meddata.reports.BarChartView;
import com.android.meddata.reportsDTO.ChartDTO;
import com.android.meddata.reportsDTO.FontStyleDTO;
import com.android.meddata.reportsDTO.ReportDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


*/
/**
 * TabletBarChartFragment : Displays Bar Chart type Report.
 * 
 * @author 280779
 *//*

@SuppressWarnings("unchecked")
public class BarChartFragmentt extends Fragment implements
		OnTouchListener, OMSReceiveListener {
	private final String TAG = this.getClass().getSimpleName();
	private BarChartView mBarChartView = null;
	private List<String> mYColumnNameList;
	private List<String> mYColumnLabelList;
	private String mTransTableName;
	private Integer mDateScale;
	private List<String> mIsDateList;
	private String mXColumnName = null;
	private String mXLabelName = null;
	private String mainFilterName = null;
	private String subFilterName = null;
	private String mSelectedFact = null;
	private int mSelectedFactPos = 0;

	private ReportHelper rHelper;
	private Context mContext = null;
	private List<String> subFilterList = new ArrayList<String>();

	private String screenId = null;

	private LinearLayout mainLayout = null;
	private RelativeLayout toolBarLayout = null;
	private LinearLayout chartLayout = null;
	private LinearLayout legendLL = null;
	private LinearLayout reportTableDataLL = null;

	private ReportDTO[] chartMonthlyData = null;
	private ProgressDialog dialog = null;

	*/
/* Styles Declarations *//*

	private String highlightedBarColor = null;
	private String highlightedColumnColor = null;

	private FontStyleDTO highlightedLabelStyle = null;
	private FontStyleDTO axisLabelsStyle = null;
	private FontStyleDTO axisColNameStyle = null;
	private String toolBarColor = null;

	private OMSStyleGuideHelper styleGuideHelper = null;
	private List<String> barColors = null;
	private String reportBGColor = null;
	private String reportBGImage = null;

	private FontStyleDTO tableHeaderStyle = null;
	private String tableHeaderBgColor = null;
	private FontStyleDTO tableRowTextStyle = null;
	private String tableRowAlternateColor1 = null;
	private String tableRowAlternateColor2 = null;

	private View tabletView = null;

	private String chartTitle = null;
	private int viewTypeSelectedIndex = 0;

	private static final int VIEW_TYPE_REPORT = 0;
	private static final int VIEW_TYPE_TABLE = 1;
	private static final int VIEW_TYPE_REPORT_WITH_TABLE = 2;

	private static final String[] DEFAULT_BAR_COLOR_CODE = new String[] {
			"#0675cb", "#ff9900", "#ec008c", "#3c9307", "#681ea6" };

	private boolean isSumRequired = true;
	private static final String SUM_OF = "sum(";
	private static final String AVG_OF = "avg(";

	private List<String> tableHeaderLabelList = null;
	private List<String> tableDataColList = null;

	private int isChangedFirstTime = 0;
	private boolean screenMode = true;

	// launch BL Variable
	private String launchBL = "";
	private int configDBAppId;
	private int mContainerId = -1;
	public List<String> trnsUsidList = new ArrayList<String>();
	private boolean isViewCreated = false;
	private int customContainerId = -1;
	private String uniqueId;
	private boolean IS_MEDDATA_APP = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MedDataUtil.getInstance().setFontScale(getActivity());
		Bundle args = getArguments();
		*/
/*if (args != null) {
			uniqueId = args.getString(OMSMessages.UNIQUE_ID.getValue());
			this.screenId = args.getString(OMSMessages.SCREENID.getValue());
			screenMode = args.getBoolean(OMSMessages.SCREEN_MODE.getValue(),
					true);
			configDBAppId = args.getInt(OMSMessages.CONFIGAPP_ID.getValue());
			customContainerId = args.getInt(OMSMessages.CUSTOM_CONTAINERID
					.getValue());
			this.mContext = getActivity();
		}*//*

		rHelper = new ReportHelper(this.mContext);
	   styleGuideHelper = new OMSStyleGuideHelper(mContext);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mContext = null;
	}

	public static BarChartFragmentt newInstance() {
		BarChartFragmentt barChartFragment = new BarChartFragmentt();
		return barChartFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (container != null) {
			mContainerId = container.getId();
		}
		isChangedFirstTime = 0;

		tabletView = inflater.inflate(R.layout.chart_bar_layout, container,
				false);
	//	OMSApplication.getInstance().setScreenMode(screenMode);

		final ChartDTO chartDataVO = ChartDTO.getChartDataInstance();
	//	chartDataVO.filters = rHelper.getMainFilters(screenId);
//{Year=[All, 2011, 2012, 2013, 2014, 2015]}
		Map<String, List<String>> filterList = new HashMap<String, List<String>>();
		List<String> yearList = new ArrayList<String>();
		yearList.add("All");
		yearList.add("2011");
		yearList.add("2012");
		yearList.add("2013");
		yearList.add("2014");
		yearList.add("2015");
		filterList.put("Year", yearList);
		chartDataVO.filters = filterList;




		if (chartDataVO.filters.isEmpty()) {
			tabletView.findViewById(R.id.NoData).setVisibility(View.VISIBLE);

			tabletView.findViewById(R.id.ToolBarLayout)
					.setVisibility(View.GONE);

			return tabletView;
		} else {
			tabletView.setOnTouchListener(this);

			mainLayout = (LinearLayout) tabletView
					.findViewById(R.id.main_layout);
			toolBarLayout = (RelativeLayout) tabletView
					.findViewById(R.id.ToolBarLayout);
			chartLayout = (LinearLayout) tabletView
					.findViewById(R.id.chart_layout);
			legendLL = (LinearLayout) tabletView
					.findViewById(R.id.report_legend_layout);
			reportTableDataLL = (LinearLayout) tabletView
					.findViewById(R.id.report_table_data);

			dialog = new ProgressDialog(mContext);
			dialog.setCancelable(false);
			//dialog.setTitle(getResources().getString(R.string.fetch_data));
			dialog.setMessage("Loading...");

			// fetch config data
			fetchConfigData();

			if (mYColumnNameList == null || mYColumnNameList.size() == 0) {
				tabletView.findViewById(R.id.NoData)
						.setVisibility(View.VISIBLE);
				tabletView.findViewById(R.id.ToolBarLayout).setVisibility(
						View.GONE);
				return tabletView;
			}

			// apply initial setting like setting title name, styling for
			// toolbar and background
			init();

			// Y- Axis Filter
			Spinner yAxisFilterSpinner = (Spinner) tabletView
					.findViewById(R.id.yAxisFilter);
			if(IS_MEDDATA_APP)
				yAxisFilterSpinner.setVisibility(View.GONE);
			final List<String> yAxisList = new ArrayList<String>();
			yAxisList.add("All");
			yAxisList.addAll(mYColumnLabelList);

			ArrayAdapter<String> yAxisFilterAdapter = new ArrayAdapter<String>(
					mContext, R.layout.reports_spinner_style, yAxisList);

			yAxisFilterAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			yAxisFilterSpinner.setAdapter(yAxisFilterAdapter);
			yAxisFilterSpinner.setPrompt("Choose Y-Axis");
			yAxisFilterSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int pos, long arg3) {
							if (pos < yAxisList.size()) {
								Log.d(TAG, "Selected Fact index1:" + pos
										+ " fact size:" + yAxisList.size());

								mSelectedFact = yAxisList.get(pos);
								mSelectedFactPos = pos;
								if (isChangedFirstTime != 0) {
									new BDFetchAsyncTask()
											.execute("0");
								}
								isChangedFirstTime = 1;
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							Toast.makeText(
									mContext,
									"Nothing Selected",
									Toast.LENGTH_LONG).show();
						}

					});

			// Main Filter Spinner
			Spinner mainFilterSpinner = (Spinner) tabletView
					.findViewById(R.id.mainFilter);
			final Spinner subFilterSpinner = (Spinner) tabletView
					.findViewById(R.id.subFilter);

			ArrayAdapter<String> mainFilterAdapter = new ArrayAdapter<String>(
					mContext, R.layout.reports_spinner_style);
			mainFilterAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			final ArrayAdapter<String> subFilterAdapter = new ArrayAdapter<String>(
					mContext, R.layout.reports_spinner_style);
			subFilterAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			final ArrayList<String> mainFilterList = new ArrayList<String>();
			Iterator<?> iterator = chartDataVO.filters.keySet().iterator();
			while (iterator.hasNext()) {
				mainFilterList.add((String) iterator.next());
			}
			mainFilterAdapter.addAll(mainFilterList);

			mainFilterName = mainFilterList.get(0);
			subFilterName = ChartDTO.getChartDataInstance().filters.get(
					mainFilterName).get(0);

			mainFilterSpinner.setPrompt("Choose Filter");
			mainFilterSpinner.setAdapter(mainFilterAdapter);
			mainFilterSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							mainFilterName = mainFilterList.get(position);

							subFilterList = chartDataVO.filters
									.get(mainFilterName);

							subFilterAdapter.clear();
							subFilterAdapter.addAll(subFilterList);
							subFilterSpinner.setAdapter(subFilterAdapter);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							Toast.makeText(
									mContext,
									"Nothing Selected",
									Toast.LENGTH_LONG).show();
						}

					});

			// Sub Filter Spinner
			subFilterSpinner.setPrompt("choose_subfilter");

			subFilterSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							subFilterName = subFilterList.get(position);

							subFilterSpinner.setSelection(position);

							new BDFetchAsyncTask()
									.execute("0");
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							Toast.makeText(
									mContext,
									"Nothing Selected",
									Toast.LENGTH_LONG).show();
						}

					});
			if (!TextUtils.isEmpty(launchBL) && !isViewCreated) {
				executeLauchBL();
			}
			return tabletView;
		}
	}

	*/
/* Background Asynchronous Service to fetch the Report Data *//*

	private class BDFetchAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			if (!dialog.isShowing()) {
				dialog.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			if (mYColumnNameList != null && !mYColumnNameList.isEmpty()) {
				ReportDTO[] reportData = null;
				if (subFilterName.equalsIgnoreCase("all")) {
					if(IS_MEDDATA_APP)
						mXLabelName = "Year";
					reportData = getAllWeeklyData();
				} else {
					if(IS_MEDDATA_APP)
					{
						mXLabelName = "Month";
						reportData = getAllWeeklyDataForMedData();
					}
					*/
/*else
						reportData = getEachWeeklyData();*//*

					
				}

				if (reportData != null && reportData.length > 0) {
					chartMonthlyData = reportData;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mContext != null) {
				if (mYColumnNameList.isEmpty() || chartMonthlyData == null) {
					((TextView) tabletView.findViewById(R.id.NoData))
							.setVisibility(View.VISIBLE);
					Log.d(TAG, "Data Not Found");
					dialog.dismiss();
				} else {
					isTableDataLoaded = false;
					mBarChartView = getBarChartView();
					selectView(viewTypeSelectedIndex);
				}
			}
		}

		*/
/**
		 * Returns Report data from specific Filter.
		 * 
		 * @return ArrayList<PieItem>[]
		 *//*

		*/
/*private ReportDTO[] getEachWeeklyData() {
			Double startDate = 0.0;
			Double endDate;
			int k = 0;
			Cursor cursor = null;

			ReportDTO[] barChartDataArray = new ReportDTO[mYColumnNameList
					.size()];
			ArrayList<String>[] segmentList = new ArrayList[mYColumnNameList
					.size()];
			ArrayList<String>[] segmentLabelList = new ArrayList[mYColumnNameList
					.size()];
			for (int i = 0; i < mYColumnNameList.size(); i++) {
				segmentList[i] = new ArrayList<String>();
				segmentLabelList[i] = new ArrayList<String>();
			}

			String[] chartsArray = new String[mYColumnNameList.size()];

			for (int i2 = 0; i2 < mYColumnNameList.size(); i2++) {
				if (isSumRequired) {
					chartsArray[i2] = SUM_OF + mYColumnNameList.get(i2)
							+ ")";
				} else {
					chartsArray[i2] = AVG_OF + mYColumnNameList.get(i2)
							+ ")";
				}
			}

			if (mIsDateList.get(0).equalsIgnoreCase("1")) {
				List<Double> dateList = rHelper.getChartEndStartDate(
						mTransTableName, new String[] { mXColumnName },
						mainFilterName, subFilterName);
				if (dateList != null && dateList.size() > 0) {
					Collections.sort(dateList);
					startDate = dateList.get(0);
					endDate = dateList.get(dateList.size() - 1);

					while (endDate - startDate > 0) {
						cursor = rHelper.getFilteredChartDataByDate(
								mTransTableName, chartsArray, mainFilterName,
								subFilterName, String.valueOf(startDate),
								String.valueOf((startDate + mDateScale)),
								mXColumnName);

						if (cursor != null && cursor.moveToFirst()) {
							do {
								for (int i = 0; i < mYColumnNameList.size(); i++) {
									segmentList[i].add(String.valueOf(cursor
											.getInt(i)));
									segmentLabelList[i]
											.add(OMSJulianDateGenerator
													.getDateFromJulian(startDate));
								}
							} while (cursor.moveToNext());
							cursor.close();
						}

						startDate = startDate + mDateScale;
					}
					for (int i = 0; i < mYColumnNameList.size(); i++) {
						barChartDataArray[i] = new ReportDTO();
						barChartDataArray[i].setSegValue(segmentList[i]);
						barChartDataArray[i].setSegLabel(segmentLabelList[i]);
					}
				} else {
					Log.e(TAG, "dateList is null for table[" + mTransTableName
							+ "] column[" + mXColumnName + "]");
					return null;
				}
			} else {
				k = 0;
				String distinctColumnName = null;
				Cursor dataCursor = rHelper.getAllWeeklyChartDataWithOutDate(
						mTransTableName, mXColumnName, mainFilterName,
						subFilterName);

				if (dataCursor != null && dataCursor.moveToFirst()) {
					while (k < dataCursor.getCount()) {
						distinctColumnName = dataCursor.getString(0);
						for (int i2 = 0; i2 < mYColumnNameList.size(); i2++) {
							if (isSumRequired) {
								chartsArray[i2] = SUM_OF
										+ mYColumnNameList.get(i2)
										+ ")";
							} else {
								chartsArray[i2] = AVG_OF
										+ mYColumnNameList.get(i2)
										+ ")";
							}
						}

						cursor = rHelper
								.getFilteredWeeklyChartDataWithOutDate(
										mTransTableName, chartsArray,
										mainFilterName, subFilterName,
										mXColumnName, distinctColumnName);
						if (cursor != null && cursor.moveToFirst()) {
							float value = 0;
							do {
								for (int i = 0; i < mYColumnNameList.size(); i++) {

									value = 0;
									if (cursor.getString(i) != null)
										value = Float.parseFloat(cursor
												.getString(i));
									segmentList[i].add(String.valueOf(value));
									segmentLabelList[i].add(distinctColumnName);
								}
							} while (cursor.moveToNext());
							cursor.close();
						}

						startDate = startDate + mDateScale;
						k++;
						dataCursor.moveToNext();
					}
					for (int i = 0; i < mYColumnNameList.size(); i++) {
						barChartDataArray[i] = new ReportDTO();
						barChartDataArray[i].setSegValue(segmentList[i]);
						barChartDataArray[i].setSegLabel(segmentLabelList[i]);
					}
					dataCursor.close();
				} else {
					return null;
				}
			}
			return barChartDataArray;
		}*//*


		*/
/**
		 * Returns Report data from 'All' Filters
		 * 
		 * @return ArrayList<PieItem>[]
		 *//*

		private ReportDTO[] getAllWeeklyData() {
			Double startDate = 0.0;
			Double endDate;
			int k = 0;
			Cursor cursor = null;
			ReportDTO[] barChartDataArray = new ReportDTO[mYColumnNameList
					.size()];
			ArrayList<String>[] totalList = new ArrayList[mYColumnNameList
					.size()];
			ArrayList<String>[] segmentLabelList = new ArrayList[mYColumnNameList
					.size()];
			for (int i = 0; i < mYColumnNameList.size(); i++) {
				segmentLabelList[i] = new ArrayList<String>();
				totalList[i] = new ArrayList<String>();
			}
			String[] chartsArray = new String[mYColumnNameList.size()];
			for (int i2 = 0; i2 < mYColumnNameList.size(); i2++) {
				if (isSumRequired) {
					chartsArray[i2] = SUM_OF + mYColumnNameList.get(i2)
							+ ")";
				} else {
					chartsArray[i2] = AVG_OF + mYColumnNameList.get(i2)
							+ ")";
				}
			}

			if (mIsDateList.get(0).equalsIgnoreCase("1")) {
				List<Double> dateList = rHelper.getChartEndStartDate(
						mTransTableName, new String[] { mXColumnName },
						mainFilterName, null);
				if (dateList != null && dateList.size() > 0) {
					Collections.sort(dateList);
					startDate = dateList.get(0);
					endDate = dateList.get(dateList.size() - 1);

					while (endDate - startDate > 0) {
						cursor = rHelper
								.getAllWeeklyFilteredDataByDateGroupBymainFilter(
										mTransTableName,
										chartsArray,
										mainFilterName,
										String.valueOf(startDate),
										String.valueOf((startDate + mDateScale)),
										mXColumnName);
						int[] toatlValue = new int[mYColumnNameList.size()];
						float[] recCount = new float[mYColumnNameList.size()];
						if (cursor != null && cursor.moveToFirst()) {
							float value = 0;
							do {
								for (int i = 0; i < mYColumnNameList.size(); i++) {
									value = 0;
									if (!TextUtils.isEmpty(cursor.getString(i))) {
										value = Float.parseFloat(cursor
												.getString(i));
										recCount[i] += 1;
									}
									toatlValue[i] += value;
								}
							} while (cursor.moveToNext());
							cursor.close();
						}

						for (int i = 0; i < mYColumnNameList.size(); i++) {
							if (isSumRequired) {
								totalList[i].add(String.valueOf(toatlValue[i]));
							} else {
								totalList[i].add(String.valueOf(toatlValue[i]
										/ recCount[i]));
							}
							segmentLabelList[i].add(OMSJulianDateGenerator
									.getDateFromJulian(startDate));
						}

						startDate = startDate + mDateScale;
					}
					for (int i = 0; i < mYColumnNameList.size(); i++) {
						barChartDataArray[i] = new ReportDTO();
						barChartDataArray[i].setSegValue(totalList[i]);
						barChartDataArray[i].setSegLabel(segmentLabelList[0]);
					}
				} else {
					return null;
				}
			} else {
				k = 0;
				Cursor dataCursor = rHelper.getAllWeeklyChartDataWithOutDate(
						mTransTableName, mXColumnName, null, null);
				if (dataCursor != null && dataCursor.moveToFirst()) {
					while (k < dataCursor.getCount()) {
						String distinctColumnName = dataCursor.getString(0);
						for (int i2 = 0; i2 < mYColumnNameList.size(); i2++) {
							if (isSumRequired) {
								chartsArray[i2] = SUM_OF
										+ mYColumnNameList.get(i2)
										+ OMSMessages.CLOSE_BRACES.getValue();
							} else {
								chartsArray[i2] = AVG_OF
										+ mYColumnNameList.get(i2)
										+ OMSMessages.CLOSE_BRACES.getValue();
							}
						}

						cursor = rHelper
								.getFilteredAllWeeklyChartDataWithOutDate(
										mTransTableName, chartsArray,
										mXColumnName, distinctColumnName,
										mainFilterName);
						int[] toatlValue = new int[mYColumnNameList.size()];
						float[] recCount = new float[mYColumnNameList.size()];
						if (cursor != null && cursor.moveToFirst()) {
							int value = 0;
							do {
								for (int i = 0; i < mYColumnNameList.size(); i++) {

									value = 0;
									if (!TextUtils.isEmpty(cursor.getString(i))) {
										value = Integer.parseInt(cursor
												.getString(i));
										recCount[i] += 1;
									}
									toatlValue[i] += value;
								}
							} while (cursor.moveToNext());
							cursor.close();
						}
						for (int i = 0; i < mYColumnNameList.size(); i++) {
							if (isSumRequired) {
								totalList[i].add(String.valueOf(toatlValue[i]));
							} else {
								totalList[i].add(String.valueOf(toatlValue[i]
										/ recCount[i]));
							}
							segmentLabelList[i].add(distinctColumnName);
						}
						startDate = startDate + mDateScale;
						k++;
						dataCursor.moveToNext();
					}
					dataCursor.close();
					for (int i = 0; i < mYColumnNameList.size(); i++) {
						barChartDataArray[i] = new ReportDTO();
						barChartDataArray[i].setSegValue(totalList[i]);
						barChartDataArray[i].setSegLabel(segmentLabelList[0]);
					}
				} else {
					return null;
				}
			}
			return barChartDataArray;

		}
		
		private ReportDTO[] getAllWeeklyDataForMedData() {
			Double startDate = 0.0;
			Double endDate;
			int k = 0;
			Cursor cursor = null;
			ReportDTO[] barChartDataArray = new ReportDTO[mYColumnNameList
					.size()];
			ArrayList<String>[] totalList = new ArrayList[mYColumnNameList
					.size()];
			ArrayList<String>[] segmentLabelList = new ArrayList[mYColumnNameList
					.size()];
			for (int i = 0; i < mYColumnNameList.size(); i++) {
				segmentLabelList[i] = new ArrayList<String>();
				totalList[i] = new ArrayList<String>();
			}
			String[] chartsArray = new String[mYColumnNameList.size()];
			for (int i2 = 0; i2 < mYColumnNameList.size(); i2++) {
				if (isSumRequired) {
					chartsArray[i2] = SUM_OF + mYColumnNameList.get(i2)
							+ OMSMessages.CLOSE_BRACES.getValue();
				} else {
					chartsArray[i2] = AVG_OF + mYColumnNameList.get(i2)
							+ OMSMessages.CLOSE_BRACES.getValue();
				}
			}

			if (mIsDateList.get(0).equalsIgnoreCase(OMSConstants.IS_DATE)) {
				List<Double> dateList = rHelper.getChartEndStartDate(
						mTransTableName, new String[] { mXColumnName },
						mainFilterName, null);
				if (dateList != null && dateList.size() > 0) {
					Collections.sort(dateList);
					startDate = dateList.get(0);
					endDate = dateList.get(dateList.size() - 1);

					while (endDate - startDate > 0) {
						cursor = rHelper
								.getAllWeeklyFilteredDataByDateGroupBymainFilter(
										mTransTableName,
										chartsArray,
										mainFilterName,
										String.valueOf(startDate),
										String.valueOf((startDate + mDateScale)),
										mXColumnName);
						int[] toatlValue = new int[mYColumnNameList.size()];
						float[] recCount = new float[mYColumnNameList.size()];
						if (cursor != null && cursor.moveToFirst()) {
							int value = 0;
							do {
								for (int i = 0; i < mYColumnNameList.size(); i++) {
									value = 0;
									if (!TextUtils.isEmpty(cursor.getString(i))) {
										value = (int) Integer.parseInt(cursor
												.getString(i));
										recCount[i] += 1;
									}
									toatlValue[i] += value;
								}
							} while (cursor.moveToNext());
							cursor.close();
						}

						for (int i = 0; i < mYColumnNameList.size(); i++) {
							if (isSumRequired) {
								totalList[i].add(String.valueOf(toatlValue[i]));
							} else {
								totalList[i].add(String.valueOf(toatlValue[i]
										/ recCount[i]));
							}
							segmentLabelList[i].add(OMSJulianDateGenerator
									.getDateFromJulian(startDate));
						}

						startDate = startDate + mDateScale;
					}
					for (int i = 0; i < mYColumnNameList.size(); i++) {
						barChartDataArray[i] = new ReportDTO();
						barChartDataArray[i].setSegValue(totalList[i]);
						barChartDataArray[i].setSegLabel(segmentLabelList[0]);
					}
				} else {
					return null;
				}
			} else {
				k = 0;
				//mXColumnName = "Month";
				Cursor dataCursor = rHelper.getAllWeeklyChartDataWithOutDate(
						mTransTableName, "Month", null, null);
				if (dataCursor != null && dataCursor.moveToFirst()) {
					while (k < dataCursor.getCount()) {
						String distinctColumnName = dataCursor.getString(0);
						for (int i2 = 0; i2 < mYColumnNameList.size(); i2++) {
							if (isSumRequired) {
								chartsArray[i2] = SUM_OF
										+ mYColumnNameList.get(i2)
										+ OMSMessages.CLOSE_BRACES.getValue();
							} else {
								chartsArray[i2] = AVG_OF
										+ mYColumnNameList.get(i2)
										+ OMSMessages.CLOSE_BRACES.getValue();
							}
						}

						cursor = rHelper
								.getFilteredAllWeeklyChartDataWithOutDateMedData(
										mTransTableName, chartsArray,
										"Month", distinctColumnName,
										mainFilterName,subFilterName);
						int[] toatlValue = new int[mYColumnNameList.size()];
						float[] recCount = new float[mYColumnNameList.size()];
						if (cursor != null && cursor.moveToFirst()) {
							int value = 0;
							do {
								for (int i = 0; i < mYColumnNameList.size(); i++) {

									value = 0;
									if (!TextUtils.isEmpty(cursor.getString(i))) {
										value = Integer.parseInt(cursor
												.getString(i));
										recCount[i] += 1;
									}
									toatlValue[i] += value;
								}
							} while (cursor.moveToNext());
							cursor.close();
						}
						for (int i = 0; i < mYColumnNameList.size(); i++) {
							if (isSumRequired) {
								totalList[i].add(String.valueOf(toatlValue[i]));
							} else {
								totalList[i].add(String.valueOf(toatlValue[i]
										/ recCount[i]));
							}
							segmentLabelList[i].add(distinctColumnName);
						}
						startDate = startDate + mDateScale;
						k++;
						dataCursor.moveToNext();
					}
					dataCursor.close();
					for (int i = 0; i < mYColumnNameList.size(); i++) {
						barChartDataArray[i] = new ReportDTO();
						barChartDataArray[i].setSegValue(totalList[i]);
						barChartDataArray[i].setSegLabel(segmentLabelList[0]);
					}
				} else {
					return null;
				}
			}
			return barChartDataArray;

		}
	}

	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		isViewCreated = true;
	}
	
	*/
/**
	 * Returns Bar chart Type view which contains all Y-Axis bars
	 * 
	 * @param viewChild
	 * @return BarChartView
	 *//*

	private BarChartView getBarChartView() {
		BarChartView barChart = new BarChartView(this.mContext);
		*/
/**
		 * get chart data 0 - All, Rest for Fact
		 *//*

		ReportDTO[] chartData = new ReportDTO[1];
		switch (mSelectedFactPos) {
		case 0:
			chartData = chartMonthlyData;
			if(IS_MEDDATA_APP)
				barChart.setYAxisLable(mYColumnLabelList.get(0));
			barChart.barColors = Arrays.asList(DEFAULT_BAR_COLOR_CODE);
			break;
		default:
			chartData[0] = chartMonthlyData[mSelectedFactPos - 1]; // -1 because
																	// first
																	// element
																	// is "All"
			barChart.barColors = Arrays
					.asList(DEFAULT_BAR_COLOR_CODE[mSelectedFactPos - 1]);

			barChart.setYAxisLable(mYColumnLabelList.get(mSelectedFactPos - 1));
			break;
		}

		barChart.setXAxisLable(mXLabelName);
		barChart.setChartData(chartData);

*/
/*		if (!TextUtils.isEmpty(highlightedBarColor)) {
			barChart.highlightedBarColor = Color
					.parseColor(OMSConstants.MEDDATA_BLUE);
		}
		barChart.highlightedLabelStyle = highlightedLabelStyle;
		barChart.axisLabelsStyle = axisLabelsStyle;
		barChart.colNameStyle = axisColNameStyle;*//*

		barChart.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		barChart.setBarType(BarChartView.BAR_TYPE.VERTICAL);
		return barChart;
	}

	*/
/**
	 * @param
	 * @param
	 *//*

	private void displayChart() {
		chartLayout.setVisibility(View.VISIBLE);
		chartLayout.removeAllViews();

		chartLayout.addView(mBarChartView, new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		displayLegend();
	}

	private boolean isTableDataLoaded = false;
	private Cursor tableDataCursor = null;

	private void displayTable() {

		Log.d(TAG, "isTableDataLoaded:" + isTableDataLoaded);

		if (!isTableDataLoaded) {
			String[] columnNames = new String[tableDataColList.size()];
			int count = 0;

			for (String colName : tableDataColList) {
				columnNames[count++] = colName;
			}
			tableDataCursor = rHelper.getTableData(mTransTableName,
					columnNames, mainFilterName, subFilterName);

			isTableDataLoaded = true;
		}
		generateTableLayout(tableDataCursor);
	}

	*/
/**
	 * Generate Report Trans Data in Table View along with data.
	 * 
	 * @param tableDataCursor
	 *//*

	private void generateTableLayout(final Cursor tableDataCursor) {

		if (!dialog.isShowing()) {
			dialog.setCancelable(true);
			dialog.setTitle(getResources().getString(R.string.fetch_data));
			dialog.setMessage(getResources().getString(
					R.string.linechart_message));
			dialog.show();
		}

		reportTableDataLL.setVisibility(View.VISIBLE);
		reportTableDataLL.removeAllViews();

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int layoutWidth = size.x;

		final int cellWidth = layoutWidth / (tableDataColList.size()) - 1;
		final ScrollView scroll = new ScrollView(mContext);
		scroll.setBackgroundColor(Color.TRANSPARENT);
		scroll.setLayoutParams(new LinearLayout.LayoutParams(layoutWidth,
				LinearLayout.LayoutParams.MATCH_PARENT));

		final TableLayout table = new TableLayout(mContext);
		table.setScrollbarFadingEnabled(true);
		table.setPadding(2, 0, 2, 70);

		TableLayout headerTable = new TableLayout(mContext);
		headerTable.setScrollbarFadingEnabled(true);
		headerTable.setPadding(1, 0, 1, 0);

		// column labels
		TableRow headerRow = new TableRow(mContext);

		final TableRow.LayoutParams params = new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.MATCH_PARENT);

		TextView columnLables = null;
		TableRow.LayoutParams headerParams = new TableRow.LayoutParams(
				cellWidth, TableRow.LayoutParams.MATCH_PARENT);
		headerParams.setMargins(0, 0, 1, 0);
		params.setMargins(0, 0, 1, 0);

		for (int i = 0; i < tableHeaderLabelList.size(); i++) {
			columnLables = new TextView(mContext);

			if (tableHeaderStyle != null) {
				if (tableHeaderStyle.getFontTypeFace() != null)
					columnLables
							.setTypeface(tableHeaderStyle.getFontTypeFace());
				columnLables.setTextColor(tableHeaderStyle.getFontColor());
				columnLables.setTextSize(TypedValue.COMPLEX_UNIT_SP,
						tableHeaderStyle.getFontSize());
			} else {
				columnLables.setTypeface(Typeface.DEFAULT_BOLD);
				columnLables.setTextColor(Color.WHITE);
			}

			if (!TextUtils.isEmpty(highlightedColumnColor)
					&& (mXColumnName.equals(tableDataColList.get(i)) || mSelectedFact
							.equals(tableDataColList.get(i)))) {
				columnLables.setTextColor(Color
						.parseColor(highlightedColumnColor));
			}

			if (!TextUtils.isEmpty(tableHeaderBgColor))
				columnLables.setBackgroundColor(Color
						.parseColor(tableHeaderBgColor));
			columnLables.setText(tableHeaderLabelList.get(i));
			columnLables.setTextScaleX(1f);
			columnLables.setGravity(Gravity.CENTER);
			columnLables.setPadding(1, 1, 1, 1);

			headerRow.setPadding(1, 1, 1, 1);
			headerRow.addView(columnLables, headerParams);
		}

		headerTable.addView(headerRow);
		reportTableDataLL.addView(headerTable);

		*/
/* Background Asynchronous Service to fetch the Report Data *//*

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				Log.d(TAG, "generate table data - Start");
			}

			@Override
			protected Void doInBackground(Void... input) {

				// set data
				if (tableDataCursor != null) {
					if (tableDataCursor.moveToFirst()) {
						TableRow tableRow = null;
						int rowCount = OMSDefaultValues.MIN_INDEX_INT
								.getValue();

						do {
							tableRow = new TableRow(mContext);
							TextView rowValue = null;

							for (int i = 0; i < tableDataCursor
									.getColumnCount(); i++) {
								rowValue = new TextView(mContext);
								if (mIsDateList.get(0).equalsIgnoreCase(
										OMSConstants.IS_DATE)
										&& tableDataCursor.getColumnName(i)
												.equalsIgnoreCase(mXColumnName)) {
									rowValue.setText(OMSJulianDateGenerator
											.getDateFromJulian(tableDataCursor
													.getDouble(i)));
								} else {
									rowValue.setText(tableDataCursor
											.getString(i));
								}

								if (tableRowTextStyle != null) {
									if (tableHeaderStyle.getFontTypeFace() != null)
										rowValue.setTypeface(tableHeaderStyle
												.getFontTypeFace());
									rowValue.setTextColor(tableRowTextStyle
											.getFontColor());
									rowValue.setTextSize(tableRowTextStyle
											.getFontSize());
								} else {
									rowValue.setTypeface(Typeface.DEFAULT_BOLD);
									rowValue.setTextColor(Color.BLACK);
								}

								if (!TextUtils.isEmpty(highlightedColumnColor)
										&& (mXColumnName.equals(tableDataCursor
												.getColumnName(i)) || mSelectedFact
												.equals(tableDataCursor
														.getColumnName(i)))) {
									rowValue.setTextColor(Color
											.parseColor(highlightedColumnColor));
								}

								if (rowCount % 2 == 0) {
									if (!TextUtils
											.isEmpty(tableRowAlternateColor1)) {
										rowValue.setBackgroundColor(Color
												.parseColor(tableRowAlternateColor1));
									}
								} else {
									if (!TextUtils
											.isEmpty(tableRowAlternateColor2)) {
										rowValue.setBackgroundColor(Color
												.parseColor(tableRowAlternateColor2));
									}
								}

								rowValue.setGravity(Gravity.CENTER_HORIZONTAL);
								rowValue.setPadding(1, 1, 0, 1);
								rowValue.setWidth(cellWidth);

								tableRow.setPadding(1, 0, 1, 1);
								tableRow.addView(rowValue, params);
							}
							rowCount = rowCount + 1;
							table.addView(tableRow);

						} while (tableDataCursor.moveToNext());
					}
					table.addView(new TableRow(mContext)); // empty
					scroll.setPadding(0, 0, 0, 0);
					scroll.addView(table);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				reportTableDataLL.addView(scroll);
				Log.d(TAG, "generate table data - End");
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}.execute();
	}

	*/
/**
	 * Populates Chart data from Config DB
	 * 
	 * @param chartCursor
	 *//*

	private void fetchConfigData() {
		List<String> mXAxisLableList;

		String tempTableHeaderLabel = null;
		String tempTableColumnName = null;

		mYColumnNameList = new ArrayList<String>();
		mYColumnLabelList = new ArrayList<String>();
		mIsDateList = new ArrayList<String>();
		mXAxisLableList = new ArrayList<String>();

		tableHeaderLabelList = new ArrayList<String>();
		tableDataColList = new ArrayList<String>();

		Cursor chartCursor = rHelper.getChartInfo(
				OMSConstants.MOBILE_BI_TABLE_NAME_IN_CONFIG,
				OMSConstants.BAR_CHART_TYPE, screenId);

		chartCursor.moveToFirst();
		mYColumnNameList.clear();

		while (chartCursor.isAfterLast() == false) {

			if (!TextUtils.isEmpty(chartCursor.getString(chartCursor
					.getColumnIndex(OMSDatabaseConstants.LAUNCH_BL)))) {
				launchBL = chartCursor.getString(chartCursor
						.getColumnIndex(OMSDatabaseConstants.LAUNCH_BL));
			}
			chartTitle = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.CHART_TITLE));
			mTransTableName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_DATA_TABLE_NAME));
			mDateScale = chartCursor.getInt(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_DATE_SCALE));

			if (chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL1)) != null
					&& !chartCursor
							.getString(
									chartCursor
											.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL1))
							.trim().equals(OMSConstants.EMPTY_STRING)) {
				mYColumnNameList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YCOLUMN1)));
				mYColumnLabelList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL1)));
			}
			if (chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL2)) != null
					&& !chartCursor
							.getString(
									chartCursor
											.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL2))
							.trim().equals(OMSConstants.EMPTY_STRING)) {
				mYColumnNameList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YCOLUMN2)));
				mYColumnLabelList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL2)));
			}
			if (chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL3)) != null
					&& !chartCursor
							.getString(
									chartCursor
											.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL3))
							.trim().equals(OMSConstants.EMPTY_STRING)) {
				mYColumnNameList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YCOLUMN3)));
				mYColumnLabelList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL3)));
			}
			if (chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL4)) != null
					&& !chartCursor
							.getString(
									chartCursor
											.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL4))
							.trim().equals(OMSConstants.EMPTY_STRING)) {
				mYColumnNameList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YCOLUMN4)));
				mYColumnLabelList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL4)));
			}
			if (chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL5)) != null
					&& !chartCursor
							.getString(
									chartCursor
											.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL5))
							.trim().equals(OMSConstants.EMPTY_STRING)) {
				mYColumnNameList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YCOLUMN5)));
				mYColumnLabelList.add(chartCursor.getString(chartCursor
						.getColumnIndex(OMSConstants.MOBILE_BI_YAXISLABEL5)));
			}
			if (chartCursor.getString(chartCursor
					.getColumnIndex(OMSDatabaseConstants.REPORT_STYLE_NAME)) != null
					&& !chartCursor
							.getString(
									chartCursor
											.getColumnIndex(OMSDatabaseConstants.REPORT_STYLE_NAME))
							.trim().equals(OMSConstants.EMPTY_STRING)) {
				Cursor chartStyleCursor = styleGuideHelper
						.getChartStyleDetails(
								OMSDatabaseConstants.REPORT_STYLE_TABLE_NAME,
								chartCursor
										.getString(
												chartCursor
														.getColumnIndex(OMSDatabaseConstants.REPORT_STYLE_NAME))
										.trim());
				if (chartStyleCursor != null) {
					barColors = new ArrayList<String>(mYColumnNameList.size());
					getChartStyles(chartStyleCursor);
				} else {
					barColors = Arrays.asList(DEFAULT_BAR_COLOR_CODE);
				}
			} else {
				barColors = Arrays.asList(DEFAULT_BAR_COLOR_CODE);
			}

			mIsDateList.add(chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_IS_DATE)));
			mXAxisLableList.add(chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_XAXISLABEL)));
			mXColumnName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_XAXIS_COLUMN_NAME));
			mXLabelName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_XAXISLABEL));

			// retrieve table header label and trans column name
			// max 5 columns are allowed
			// Table 1st column
			tempTableHeaderLabel = chartCursor
					.getString(chartCursor
							.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_HEADER_LABEL_1));
			tempTableColumnName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_COLUMN_1));

			if (tempTableHeaderLabel != null && tempTableColumnName != null
					&& !tempTableHeaderLabel.equals(OMSConstants.EMPTY_STRING)
					&& !tempTableColumnName.equals(OMSConstants.EMPTY_STRING)) {

				tableHeaderLabelList.add(tempTableHeaderLabel);
				tableDataColList.add(tempTableColumnName);
			}

			// Table 2nd column
			tempTableHeaderLabel = chartCursor
					.getString(chartCursor
							.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_HEADER_LABEL_2));
			tempTableColumnName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_COLUMN_2));

			if (tempTableHeaderLabel != null && tempTableColumnName != null
					&& !tempTableHeaderLabel.equals(OMSConstants.EMPTY_STRING)
					&& !tempTableColumnName.equals(OMSConstants.EMPTY_STRING)) {

				tableHeaderLabelList.add(tempTableHeaderLabel);
				tableDataColList.add(tempTableColumnName);
			}

			// Table 3rd column
			tempTableHeaderLabel = chartCursor
					.getString(chartCursor
							.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_HEADER_LABEL_3));
			tempTableColumnName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_COLUMN_3));

			if (tempTableHeaderLabel != null && tempTableColumnName != null
					&& !tempTableHeaderLabel.equals(OMSConstants.EMPTY_STRING)
					&& !tempTableColumnName.equals(OMSConstants.EMPTY_STRING)) {

				tableHeaderLabelList.add(tempTableHeaderLabel);
				tableDataColList.add(tempTableColumnName);
			}

			// Table 4th column
			tempTableHeaderLabel = chartCursor
					.getString(chartCursor
							.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_HEADER_LABEL_4));
			tempTableColumnName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_COLUMN_4));

			if (tempTableHeaderLabel != null && tempTableColumnName != null
					&& !tempTableHeaderLabel.equals(OMSConstants.EMPTY_STRING)
					&& !tempTableColumnName.equals(OMSConstants.EMPTY_STRING)) {

				tableHeaderLabelList.add(tempTableHeaderLabel);
				tableDataColList.add(tempTableColumnName);
			}

			// Table 5th column
			tempTableHeaderLabel = chartCursor
					.getString(chartCursor
							.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_HEADER_LABEL_5));
			tempTableColumnName = chartCursor.getString(chartCursor
					.getColumnIndex(OMSConstants.MOBILE_BI_TABLE_COLUMN_5));

			if (tempTableHeaderLabel != null && tempTableColumnName != null
					&& !tempTableHeaderLabel.equals(OMSConstants.EMPTY_STRING)
					&& !tempTableColumnName.equals(OMSConstants.EMPTY_STRING)) {

				tableHeaderLabelList.add(tempTableHeaderLabel);
				tableDataColList.add(tempTableColumnName);
			}

			chartCursor.moveToNext();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public void receiveResult(String result) {
		(new BDFetchAsyncTask()).execute(OMSMessages.MIN_INDEX_STR.getValue());
	}

	*/
/**
	 * Retrieves Custom Styles from DB
	 * 
	 * @param chartStyleCursor
	 *//*

	private void getChartStyles(Cursor chartStyleCursor) {

		barColors = Arrays.asList(DEFAULT_BAR_COLOR_CODE);

		if (chartStyleCursor.moveToFirst()) {
			Log.d(TAG, "Fetching Custom Styles from DB");
			reportBGColor = chartStyleCursor
					.getString(chartStyleCursor
							.getColumnIndex(OMSDatabaseConstants.REPORT_BACKGROUND_COLOR));

			reportBGImage = chartStyleCursor
					.getString(chartStyleCursor
							.getColumnIndex(OMSDatabaseConstants.REPORT_BACKGROUND_IMAGE));

			highlightedBarColor = chartStyleCursor
					.getString(chartStyleCursor
							.getColumnIndex(OMSDatabaseConstants.REPORT_HIGHLIGHTED_BAR_COLOR));
			try {
				highlightedLabelStyle = styleGuideHelper
						.getFontStyle(chartStyleCursor.getString(chartStyleCursor
								.getColumnIndex(OMSDatabaseConstants.REPORT_HIGHLIGHTED_BAR_STYLE)));
			} catch (Exception e) {
				Log.e(TAG,
						"Exception while getting table font details from DB.:"
								+ e.getMessage());
			}
			try {
				axisLabelsStyle = styleGuideHelper
						.getFontStyle(chartStyleCursor.getString(chartStyleCursor
								.getColumnIndex(OMSDatabaseConstants.REPORT_AXIS_LABELS_FONT_STYLE)));
			} catch (Exception e) {
				Log.e(TAG,
						"Exception while getting table font details from DB.:"
								+ e.getMessage());
			}
			try {
				axisColNameStyle = styleGuideHelper
						.getFontStyle(chartStyleCursor.getString(chartStyleCursor
								.getColumnIndex(OMSDatabaseConstants.REPORT_AXIS_COLNAME_FONT_STYLE)));
			} catch (Exception e) {
				Log.e(TAG,
						"Exception while getting table font details from DB.:"
								+ e.getMessage());
			}

			String barColor = null;
			barColor = chartStyleCursor
					.getString(chartStyleCursor
							.getColumnIndex(OMSDatabaseConstants.REPORT_TOOLBAR_BACKGROUND_COLOR));
			if (barColor != null && !barColor.isEmpty()) {
				toolBarColor = barColor;
			} else {
				toolBarColor = "#FFFFFF";
			}
			barColor = chartStyleCursor.getString(chartStyleCursor
					.getColumnIndex(OMSDatabaseConstants.CHART_BAR_COLOR1));
			if (barColor != null && !barColor.isEmpty()) {
				barColors.set(0, barColor);
			} else {
				barColors.set(0, barColors.get(0));
			}

			barColor = chartStyleCursor.getString(chartStyleCursor
					.getColumnIndex(OMSDatabaseConstants.CHART_BAR_COLOR2));
			if (barColor != null && !barColor.isEmpty()) {
				barColors.set(1, barColor);
			} else {
				barColors.set(1, barColors.get(1));
			}

			barColor = chartStyleCursor.getString(chartStyleCursor
					.getColumnIndex(OMSDatabaseConstants.CHART_BAR_COLOR3));
			if (barColor != null && !barColor.isEmpty()) {
				barColors.set(2, barColor);
			} else {
				barColors.set(2, barColors.get(2));
			}

			barColor = chartStyleCursor.getString(chartStyleCursor
					.getColumnIndex(OMSDatabaseConstants.CHART_BAR_COLOR4));
			if (barColor != null && !barColor.isEmpty()) {
				barColors.set(3, barColor);
			} else {
				barColors.set(3, barColors.get(3));
			}

			barColor = chartStyleCursor.getString(chartStyleCursor
					.getColumnIndex(OMSDatabaseConstants.CHART_BAR_COLOR5));
			if (barColor != null && !barColor.isEmpty()) {
				barColors.set(4, barColor);
			} else {
				barColors.set(4, barColors.get(4));
			}

			// table styles
			String tempColor = chartStyleCursor
					.getString(chartStyleCursor
							.getColumnIndex(OMSDatabaseConstants.REPORT_TABLE_HIGHLIGHTED_COLUMN_COLOR));

			if (tempColor != null
					&& !tempColor.trim().equals(OMSConstants.EMPTY_STRING)) {
				highlightedColumnColor = tempColor;
			}

			// fetch table styling
			Cursor tableStyleCursor = null;
			try {
				String tableStyleName = chartStyleCursor
						.getString(chartStyleCursor
								.getColumnIndex(OMSDatabaseConstants.REPORT_TABLE_STYLE));
				if (!TextUtils.isEmpty(tableStyleName)) {
					tableStyleCursor = styleGuideHelper
							.getTableStyleDetails(tableStyleName.trim());
				}
			} catch (Exception e) {
				Log.e(TAG, "Exception while getting table styling from DB.:"
						+ e.getMessage());
			}
			if (tableStyleCursor != null && tableStyleCursor.moveToFirst()) {
				try {
					tableHeaderStyle = styleGuideHelper
							.getFontStyle(tableStyleCursor.getString(tableStyleCursor
									.getColumnIndex(OMSDatabaseConstants.TABLE_STYLE_COLUMN_LABEL_FONT_STYLE)));
				} catch (Exception e) {
					Log.e(TAG,
							"Exception while getting table font details from DB.:"
									+ e.getMessage());
				}
				try {
					tableRowTextStyle = styleGuideHelper
							.getFontStyle(tableStyleCursor.getString(tableStyleCursor
									.getColumnIndex(OMSDatabaseConstants.TABLE_STYLE_ROW_LABEL_FONT_STYLE)));
				} catch (Exception e) {
					Log.e(TAG,
							"Exception while getting table font details from DB.:"
									+ e.getMessage());
				}

				tempColor = tableStyleCursor
						.getString(tableStyleCursor
								.getColumnIndex(OMSDatabaseConstants.TABLE_STYLE_COLUMN_BACKGROUND_COLOR));

				if (tempColor != null
						&& !tempColor.trim().equals(OMSConstants.EMPTY_STRING)) {
					tableHeaderBgColor = tempColor;
				}

				tempColor = tableStyleCursor
						.getString(tableStyleCursor
								.getColumnIndex(OMSDatabaseConstants.TABLE_STYLE_ALTERNATE_COLOR1));

				if (tempColor != null
						&& !tempColor.trim().equals(OMSConstants.EMPTY_STRING)) {
					tableRowAlternateColor1 = tempColor;
				}

				tempColor = tableStyleCursor
						.getString(tableStyleCursor
								.getColumnIndex(OMSDatabaseConstants.TABLE_STYLE_ALTERNATE_COLOR2));

				if (tempColor != null
						&& !tempColor.trim().equals(OMSConstants.EMPTY_STRING)) {
					tableRowAlternateColor2 = tempColor;
				}
			}
		}
	}

	private void selectView(int position) {
		viewTypeSelectedIndex = position;

		chartLayout.setVisibility(View.GONE);
		reportTableDataLL.setVisibility(View.GONE);
		legendLL.setVisibility(View.GONE);

		Log.d(TAG, "Report Type:" + position);
		switch (position) {
		case VIEW_TYPE_REPORT:
			displayChart();
			break;
		case VIEW_TYPE_TABLE:
			*/
/*if (tableDataColList.size() > 0) {
				displayTable();
			} else {
				Toast.makeText(mContext,
						getResources().getString(R.string.table_notconfigured),
						Toast.LENGTH_SHORT).show();
				displayChart();
			}*//*

			break;
		case VIEW_TYPE_REPORT_WITH_TABLE:
			*/
/*displayChart();

			if (tableDataColList.size() > 0) {
				displayTable();
			} else {
				Toast.makeText(mContext,
						getResources().getString(R.string.table_notconfigured),
						Toast.LENGTH_SHORT).show();
			}*//*

			break;
		default:
			break;
		}

		if (position == VIEW_TYPE_REPORT && dialog.isShowing()) {
			// table will take care of dismissing the dialog after loading

			dialog.dismiss();

		}
	}

	private void displayLegend() {
		Log.d(TAG, "Add Legend,SelectedFactPos:" + mSelectedFactPos);

		if (mSelectedFactPos == 0 && mYColumnNameList != null
				&& mYColumnNameList.size() > 1) {

			legendLL.setVisibility(View.VISIBLE);
			LinearLayout legendBlockLL = (LinearLayout) tabletView
					.findViewById(R.id.chart_legend);
			legendBlockLL.removeAllViews();

			int colorBlockSize = 15;
			int legendFontSize = 18;

			LayoutParams layputParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			LinearLayout layout = null;

			TextView colorView = null;
			TextView textView = null;

			for (int i = 0; i < mYColumnNameList.size(); i++) {
				layout = new LinearLayout(mContext);
				layout.setPadding(10, 10, 10, 10);

				colorView = new TextView(mContext);
				colorView.setText("  ");
				colorView.setHeight(colorBlockSize);
				colorView.setWidth(colorBlockSize);
				if (!TextUtils.isEmpty(barColors.get(i))) {
					colorView.setBackgroundColor(Color.parseColor(barColors
							.get(i)));
				}
				colorView.setGravity(Gravity.CENTER);

				textView = new TextView(mContext);
				try {
					textView.setText(mYColumnLabelList.get(i));
				} catch (Exception ex) {
					// have to fix this
					Log.e(TAG, "Exception occured " + ex.getLocalizedMessage());
				}
				textView.setTextColor(Color.BLACK);
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(legendFontSize);
				textView.setPadding(4, 0, 0, 0);

				layout.addView(colorView, layputParams);
				layout.addView(textView, layputParams);
				legendBlockLL.addView(layout, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
			}

		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		MedDataUtil.setFontScale(getActivity());
		//OMSCommonUtil.setFontScale(getActivity());
		toolBarLayout.invalidate();
		switch (viewTypeSelectedIndex) {
		case VIEW_TYPE_TABLE:
			if (tableDataColList.size() > 0) {
				displayTable();
			} else {
				Toast.makeText(mContext,
						"table_notconfigured",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case VIEW_TYPE_REPORT_WITH_TABLE:
			if (tableDataColList.size() > 0) {
				displayTable();
			} else {
				Toast.makeText(mContext,
						"table_notconfigured",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	*/
/*@Override
	public void onPrepareOptionsMenu(android.view.Menu menu) {
		menu.clear();
		if (mYColumnNameList != null && mYColumnNameList.size() > 0
				&& tableHeaderLabelList != null
				&& tableHeaderLabelList.size() > 0)
			getActivity().getMenuInflater().inflate(R.menu.chart_view_type,
					menu);
	};*//*


*/
/*	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
	*//*
*/
/*	if (OMSApplication.getInstance().getmDrawerToggle() != null
				&& OMSApplication.getInstance().getmDrawerToggle()
						.onOptionsItemSelected(item)) {
			return true;
		}*//*
*/
/*
		switch (item.getItemId()) {
		case R.id.menu_report:
			selectView(VIEW_TYPE_REPORT);
			return true;
		case R.id.menu_table:
			selectView(VIEW_TYPE_TABLE);
			return true;
		case R.id.menu_report_with_table:
			selectView(VIEW_TYPE_REPORT_WITH_TABLE);
			return true;
		case android.R.id.home:
			 if(Integer.parseInt(OMSApplication.getInstance().getAppId())>0){
				 
			if(!navigation) {
           Log.i(TAG, "You have forgotten to specify the parentActivityName in the AndroidManifest!");
          // onBackPressed();
           if (OMSApplication.getInstance().getmDrawerToggle() == null){
               
               getActivity().onBackPressed();
               }else{
           getActivity().onBackPressed();
               }
			 }
			 }
       return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}*//*


	*/
/**
	 * Applying initial setting
	 *//*

	private void init() {

		// set the title
		if (chartTitle != null && chartTitle.trim().length() > 0) {
			setActionBarTitle(chartTitle);
		}

		// tool bar styling
		if (!TextUtils.isEmpty(toolBarColor)) {
			toolBarLayout.setBackgroundColor(Color.parseColor(toolBarColor));
		}

		// background styling
		if (reportBGImage != null && !reportBGImage.trim().isEmpty()) {
			int drawableResid = styleGuideHelper
					.getDrawbleResourceID(reportBGImage);
			if (drawableResid == 0) {
				*/
/*Log.e(TAG,
						getResources().getString(
								R.string.error_bg_not_available));*//*

				Toast.makeText(
						mContext,
						"error_bg_not_available",
						Toast.LENGTH_SHORT).show();
			}
			mainLayout.setBackgroundResource(drawableResid);
		} else if (!TextUtils.isEmpty(reportBGColor)) {
			mainLayout.setBackgroundColor(Color.parseColor(reportBGColor));
		} else {
			mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
	}

	private void executeLauchBL() {
		new BLExecutorActionCenter(getActivity(), mContainerId,
				BarChartFragmentt.this, configDBAppId, trnsUsidList)
				.doBLAction(launchBL);
	}
	private  boolean navigation=false;
	@Override
	public void onResume() {
		super.onResume();
	*/
/*	OMSFactory.enableNavigationDrawer(getActivity(), "TableLayout", configDBAppId, uniqueId, customContainerId, customContainerId);
		navigation = new NavigationHelper()
		.isNavigationDrawerAvailable(configDBAppId,uniqueId);
		Log.d(TAG, "Sliding Menu visibility:::"+navigation);
		if(!navigation) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
			getActivity().getActionBar().setDisplayShowCustomEnabled(true);
			DrawerLayout drawerView = (DrawerLayout) getActivity()
					.findViewById(R.id.drawer_layout);
				getActivity().getActionBar().setIcon(R.drawable.ic_core_logo);
			drawerView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			OMSApplication.getInstance().getmDrawerToggle().setDrawerIndicatorEnabled(false);
		}else{
			getActivity().getActionBar().setDisplayShowCustomEnabled(true);
			DrawerLayout drawerView = (DrawerLayout) getActivity()
					.findViewById(R.id.drawer_layout);
//			drawerView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
			OMSApplication.getInstance().getmDrawerToggle().setDrawerIndicatorEnabled(true);
		}*//*

	}
}*/
