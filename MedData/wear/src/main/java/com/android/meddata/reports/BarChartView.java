/*
 * Copyright (C) 2013 - Cognizant Technology Solutions.
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
 */
package com.android.meddata.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.android.meddata.reportsDTO.BarChartDTO;
import com.android.meddata.reportsDTO.FontStyleDTO;
import com.android.meddata.reportsDTO.ReportDTO;


/**
 * Helper class to draw Bar Chart View for all possible Y-Axis views in One
 * report.
 * 
 * @author 280779
 * 
 */
@SuppressLint("DefaultLocale")
public class BarChartView extends View implements OnGestureListener,
		OnDoubleTapListener {
	private static final int DEFAULT_HEIGHT = 200;
	private static final int DEFAULT_WIDTH = 200;

	private float yLabelWidth = 45;
	private float xLabelHeight = 45;

	/** Contains bar mBarXmlData properties */
	private BarChartDTO mBarChartData = new BarChartDTO();

	/** To store current progress of seek bar */
	private int seekBarProgress = 0;

	/** To know the currently highlighted bar */
	private static int highlightedBar = 0;

	/** Contains xml bar mBarXmlData data */
	private ReportDTO[] mBarXmlData = null;

	private float noOfXIntervals;
	private float noOfYIntervals;
	private float xIntervalWidthPixels;
	private float yIntervalWidthPixels;
	private float yStartValue = 0;
	private float yEndtValue = 0;
	private float yInterval = 0;
	private int skipHorizontalLines = 0;
	private int skipXLables = 0;
	private int mLayoutHeight;
	private int mLayoutWidth;
	private String xAxisLable = null;
	private String yAxisLable = null;
	private static final int AXIS_LABLE_SPACE = 5;
	private int barType = 0;
	private boolean isSingleTap = false;
	private boolean isLongPressed = false;

	private List<PointF> lineCoords = new ArrayList<PointF>();
	private GestureDetector gestureDetector;
	private boolean isDraged;

	private List<String[]> yAxisValuesList = null;
	private List<List<PointF>> listlineCoords = new ArrayList<List<PointF>>();

	private int yAxisSize = 1;
	/* Styles Declarations */
	public List<String> barColors = null;
	public int highlightedBarColor = 0x44FFFFFF;
	public FontStyleDTO axisLabelsStyle = null;
	public Typeface axisLabelsFont = Typeface.create("SANS_SERIF", 1);
	public int axisLabelsColor = Color.GRAY;
	public float axisLabelsSize = 24;
	public FontStyleDTO colNameStyle = null;
	public Typeface axisColNameFont = Typeface.create("SANS_SERIF", 2);
	public int axisColNameColor = Color.GRAY;
	public float axisColNameSize = 26;
	public FontStyleDTO highlightedLabelStyle = null;
	public Typeface highlightedLabelFont = Typeface.create("SANS_SERIF", 2);
	public int highlightedLabelColor = Color.WHITE;
	public float highlightedLabelSize = 26;
	private static final String TAG = BarChartView.class.getSimpleName();
	private int rectMargin = 30;
	public interface BAR_TYPE {
		int VERTICAL = 0;
		int HORIZONTAL = 1;
	}

	public void setBarType(int type) {
		this.barType = type;
		isSingleTap = false;
		init();
		this.invalidate();
	}

	public int getBarType() {
		return this.barType;
	}

	public void setYStartValue(int ystartValue) {
		this.yStartValue = ystartValue;
	}

	public void setYEndtValue(int yendValue) {
		this.yEndtValue = yendValue;
	}

	public void setYInterval(int yinterval) {
		this.yInterval = yinterval;
	}

	public void setSkipHorizontalLines(int skiphorizontalLines) {
		this.skipHorizontalLines = skiphorizontalLines;
	}

	public void setSkipXLables(int skipxLables) {
		this.skipXLables = skipxLables;
	}

	public void setChart(ReportDTO[] mBarXmlData) {
		this.mBarXmlData = Arrays.copyOf(mBarXmlData, mBarXmlData.length);
	}

	public BarChartView(Context context) {
		super(context);
		initialize(context);
	}

	public BarChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	public BarChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mLayoutWidth = measureWidth(widthMeasureSpec);
		mLayoutHeight = measureHeight(heightMeasureSpec);
		init();
		setMeasuredDimension(mLayoutWidth, mLayoutHeight);
	}

	/**
	 * Measures Layout Width.
	 * 
	 * @param measureSpec
	 * @return int
	 */
	private int measureWidth(int measureSpec) {
		int preferred = DEFAULT_WIDTH;
		return getMeasurement(measureSpec, preferred);
	}

	/**
	 * Measures Layout Height.
	 * 
	 * @param measureSpec
	 * @return int
	 */
	private int measureHeight(int measureSpec) {
		int preferred = DEFAULT_HEIGHT;
		return getMeasurement(measureSpec, preferred);
	}

	/**
	 * Calculates Layout measures for preferred specifications.
	 * 
	 * @param measureSpec
	 * @param preferred
	 * @return int
	 */
	private int getMeasurement(int measureSpec, int preferred) {
		int specSize = MeasureSpec.getSize(measureSpec);
		int measurement = 0;

		switch (MeasureSpec.getMode(measureSpec)) {
		case MeasureSpec.EXACTLY:
			// This means the mLayoutWidth of this view has been given.
			measurement = specSize;
			break;
		case MeasureSpec.AT_MOST:
			// Take the minimum of the preferred size and what
			// we were told to be.
			measurement = Math.min(preferred, specSize);
			break;
		default:
			measurement = preferred;
			break;
		}

		return measurement;
	}

	/**
	 * Enables bar graph double tap
	 */
	@SuppressWarnings("deprecation")
	private void initialize(Context context) {
	//	new OMSStyleGuideHelper(context);
		gestureDetector = new GestureDetector(this);
		gestureDetector.setOnDoubleTapListener(this);
	}

	/**
	 * Graph size is set as per screen size. It is called once for orientation
	 * change
	 */
	private void init() {
		float xAxisLength;
		float yAxisLength;
		this.setBackgroundColor(Color.TRANSPARENT);
		/*if (highlightedLabelStyle != null) {
			highlightedLabelSize = (highlightedLabelSize > highlightedLabelStyle.getFontSize()) ? highlightedLabelSize : highlightedLabelStyle.getFontSize();
//			highlightedLabelSize = highlightedLabelStyle.getFontSize();
			highlightedLabelFont = highlightedLabelStyle.getFontTypeFace();
			highlightedLabelColor = highlightedLabelStyle.getFontColor();
		}

		if (axisLabelsStyle != null) {
			axisLabelsSize = (axisLabelsSize > axisLabelsStyle.getFontSize()) ? axisLabelsSize : axisLabelsStyle.getFontSize();
//			axisLabelsSize = axisLabelsStyle.getFontSize();
			axisLabelsFont = axisLabelsStyle.getFontTypeFace();
			axisLabelsColor = axisLabelsStyle.getFontColor();
		}

		if (colNameStyle != null) {
			axisColNameSize = (axisLabelsSize > colNameStyle.getFontSize()) ? axisColNameSize : colNameStyle.getFontSize();
//			axisColNameSize = colNameStyle.getFontSize();
			axisColNameFont = colNameStyle.getFontTypeFace();
			axisColNameColor = colNameStyle.getFontColor();
		}*/

		mBarChartData = new BarChartDTO();
		List<String> allYvalues = new ArrayList<String>();
		yAxisValuesList = new ArrayList<String[]>();
		String xValue[] = null;
		String xLabel[] = null;

		mBarChartData.setGraphName("Encounters");//mBarXmlData[0].getChartName());
		mBarChartData.setFontSize(12);

		for (int i = 0; i < mBarXmlData.length; i++) {

			xLabel = new String[mBarXmlData[0].getSegLabel().size()];
			xValue = new String[mBarXmlData[0].getSegValue().size()];

			int index = 0;
			for (String s : mBarXmlData[i].getSegLabel()) {
				xLabel[index++] = s;
			}
			index = 0;
			for (String s : mBarXmlData[i].getSegValue()) {
				xValue[index++] = s;
				allYvalues.add(s);
			}

			yAxisValuesList.add(xValue);
		}
		mBarChartData.setxLabel(xLabel);
		mBarChartData.setAllxValues(yAxisValuesList);

		yEndtValue = maxValue(allYvalues);

		mBarChartData.setyStartVal(yStartValue);
		mBarChartData.setyEndVal(yEndtValue);
		mBarChartData.setyInterval(yInterval);

		switch (getBarType()) {
		case BAR_TYPE.VERTICAL:
			yLabelWidth = measureTextMaxWidthForVerticalBarYValues()
					+ AXIS_LABLE_SPACE;
			xLabelHeight = measureTextMaxHeightForVerticalBarXValues()
					+ AXIS_LABLE_SPACE;
			break;
		case BAR_TYPE.HORIZONTAL:
			yLabelWidth = measureTextMaxWidthForHorizontalBarYValues()
					+ AXIS_LABLE_SPACE;
			xLabelHeight = measureTextMaxHeightForHorizontalBarXValues()
					+ AXIS_LABLE_SPACE;
			break;
		default:
			break;
		}
		
		RectF rect = new RectF(0 + yLabelWidth + rectMargin, rectMargin,
				this.mLayoutWidth - rectMargin, this.mLayoutHeight - 50 - rectMargin-10);
		mBarChartData.setGraphRect(rect);

		xAxisLength = mBarChartData.getGraphRect().right
				- mBarChartData.getGraphRect().left;
		yAxisLength = mBarChartData.getGraphRect().bottom
				- mBarChartData.getGraphRect().top;

		switch (getBarType()) {
		case BAR_TYPE.VERTICAL:
			noOfXIntervals = mBarChartData.getxLabel().length;
			noOfYIntervals = ((mBarChartData.getyEndVal() - mBarChartData
					.getyStartVal()) / mBarChartData.getyInterval());
			xIntervalWidthPixels = xAxisLength / noOfXIntervals;
			yAxisSize = mBarChartData.getAllxValues().size();
			xIntervalWidthPixels = xIntervalWidthPixels / yAxisSize;
			yIntervalWidthPixels = yAxisLength / noOfYIntervals;
			break;

		case BAR_TYPE.HORIZONTAL:
			noOfXIntervals = ((mBarChartData.getyEndVal() - mBarChartData
					.getyStartVal()) / mBarChartData.getyInterval());
			noOfYIntervals = mBarChartData.getxLabel().length;
			xIntervalWidthPixels = xAxisLength / noOfXIntervals;
			yIntervalWidthPixels = yAxisLength / noOfYIntervals;
			break;
		default:
			break;
		}

	}

	/**
	 * Measure text max width for horizontal bar Y values
	 * 
	 * @return float
	 */
	float measureTextMaxWidthForHorizontalBarYValues() {
		Paint paint = new Paint();
		paint.setColor(axisLabelsColor);
		paint.setAntiAlias(true);
		paint.setTextSize(axisLabelsSize);
		float maxTextWidth = 0;
		float textWidth = 0;

		if (skipXLables <= 0) {
			skipXLables = 1;
		}

		for (int i = skipXLables - 1; i < mBarChartData.getxLabel().length; i = i
				+ skipXLables) {
			String textToBeDisplayed = "";
			textToBeDisplayed = " " + mBarChartData.getxLabel()[i];
			textWidth = paint.measureText(textToBeDisplayed);
			if (maxTextWidth < textWidth) {
				maxTextWidth = textWidth;
			}
		}
		return maxTextWidth;
	}

	/**
	 * measure text max width for vertical bar Y values
	 * 
	 * @return float
	 */
	float measureTextMaxWidthForVerticalBarYValues() {
		Paint paint = new Paint();
		paint.setColor(axisLabelsColor);
		paint.setAntiAlias(true);
		paint.setTextSize(axisLabelsSize);
		float maxTextWidth = 0;
		float textWidth = 0;
		float startY = 0;

		while (startY <= mBarChartData.getyEndVal()) {
			String textToBeDisplayed = " " + startY;
			textWidth = paint.measureText(textToBeDisplayed);
			if (maxTextWidth < textWidth) {
				maxTextWidth = textWidth;
			}
			startY += mBarChartData.getyInterval();
		}
		return maxTextWidth;
	}

	/**
	 * measure text max height for vertical bar X values
	 * 
	 * @return float
	 */
	float measureTextMaxHeightForVerticalBarXValues() {
		Paint paint = new Paint();
		paint.setColor(axisLabelsColor);
		paint.setAntiAlias(true);
		paint.setTextSize(axisLabelsSize);
		float maxTextWidth = 0;
		float textWidth = 0;

		if (skipXLables <= 0) {
			skipXLables = 1;
		}
		for (int i = skipXLables - 1; i < mBarChartData.getxLabel().length; i = i
				+ skipXLables) {
			String textToBeDisplayed = " " + mBarChartData.getxLabel()[i];
			textWidth = paint.measureText(textToBeDisplayed);
			if (maxTextWidth < textWidth) {
				maxTextWidth = textWidth;
			}
		}
		return maxTextWidth;
	}

	/**
	 * measure text max height for horizontal bar X values
	 * 
	 * @return float
	 */
	float measureTextMaxHeightForHorizontalBarXValues() {

		Paint paint = new Paint();
		paint.setColor(axisLabelsColor);
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(axisLabelsSize);
		float maxTextWidth = 0;
		float startX = 0;
		float textWidth = 0;
		while (startX <= mBarChartData.getyEndVal()) {
			String textToBeDisplayed = " " + startX;
			textWidth = paint.measureText(textToBeDisplayed);
			if (maxTextWidth < textWidth) {
				maxTextWidth = textWidth;
			}
			startX += mBarChartData.getyInterval();
		}
		return maxTextWidth;
	}

	/**
	 * Returns Maximum value from Y Axis values.
	 * 
	 * @param chars
	 * @return int
	 */
	private int maxValue(List<String> chars) {
		float maxOrginal = Float.parseFloat(chars.get(0));
		int max;
		int noOfIntervals = 10;

		for (int ktr = 0; ktr < chars.size(); ktr++) {
			if (Float.parseFloat(chars.get(ktr)) > maxOrginal) {
				maxOrginal = Float.parseFloat(chars.get(ktr));
			}
		}

		max = (int) maxOrginal;
		max = ((max / 10) + 1) * 10;

		setYInterval((int) (max / noOfIntervals));
		return (int) max;
	}

	/**
	 * Bar graph view's on draw
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		drawVerticalBarsYcoords(canvas);
		drawVerticalBarsXcoords(canvas);
		drawGrid(canvas);
		createVerticalChart(canvas);
		try {
			highlightBar(canvas);
		} catch (Exception e) {
			Log.e(TAG, "Exception Occured in OnDraw " + e.getLocalizedMessage());
		}
	}

	/**
	 * Based on seekbar progress the bar in the barchart is highlighted
	 */
	private void highlightBar(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(mBarChartData.getLineColor());
		paint.setAntiAlias(true);
		paint.setStrokeWidth(7);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);

		highlightedBar = (int) (seekBarProgress / (xIntervalWidthPixels));

		int index = (highlightedBar == (int) noOfXIntervals) ? highlightedBar
				: (highlightedBar + 1);
		PointF point = lineCoords.get(index);
		canvas.drawPoint(point.x, point.y, paint);
	}

	/**
	 * Returns Y-Axis Values
	 * 
	 * @param listYValues
	 * @return
	 */
	private List<String> getYAxisVlues(List<String[]> listYValues) {
		List<String> yValues = new ArrayList<String>();
		int yAxisSize = mBarChartData.getAllxValues().size();
		String[] xValues = (String[]) mBarChartData.getAllxValues().get(0);
		String[] xTempValues = null;

		for (int j = 0; j < xValues.length; j++) {
			for (int k = 0; k < yAxisSize; k++) {
				xTempValues = (String[]) mBarChartData.getAllxValues().get(k);
				yValues.add(xTempValues[j]);
			}
		}

		return yValues;
	}

	/**
	 * Draws bar mBarXmlData here. Each bar is drawn using LinearGradient from
	 * start to end color. Each bar's top middle point is stored for line graph.
	 */
	private void createVerticalChart(Canvas canvas) {
		listlineCoords.clear();

		float yAxisLength = mBarChartData.getGraphRect().bottom
				- mBarChartData.getGraphRect().top;
		float yIntervalWidthPixels = yAxisLength / noOfYIntervals;
		float yUnitInterval = yIntervalWidthPixels
				/ mBarChartData.getyInterval();

		float barLeft = mBarChartData.getGraphRect().left;
		float barTop = mBarChartData.getGraphRect().top;
		float barRight = mBarChartData.getGraphRect().left;
		float barBottom = mBarChartData.getGraphRect().bottom;

		Paint paint = new Paint();
		RectF bar = null;
		List<RectF> listBars = new ArrayList<RectF>();
		RectF hightLightedRect = null;
		Paint fullBarPaint = null;
		List<String> finalYList = getYAxisVlues(mBarChartData.getAllxValues());

		int j = 0;
		int temp = 0;
		int leftGap = 0;
		int rightGap = 0;
		LinearGradient gradient = null;
		Paint paint1 = null;
		Paint textPaint = null;

		int dataSize = finalYList.size();
		for (int i = 0; i < dataSize; i++, j++) {
			bar = new RectF();

			if (temp == yAxisSize) {
				temp = 0;
			}
			barTop = yAxisLength
					- (yUnitInterval * (Float.parseFloat(finalYList.get(i)) - mBarChartData
							.getyStartVal()));

			barRight += xIntervalWidthPixels;

			if ((i != 0) && (i % yAxisSize == 0)) {
				leftGap = 2;
			} else {
				leftGap = 0;
				rightGap = 0;
			}

			bar.set((barLeft) + leftGap, mBarChartData.getGraphRect().top
					+ barTop, (barRight) - rightGap, barBottom);

			if (j == 0) {
				lineCoords.add(0,
						new PointF(barLeft, mBarChartData.getGraphRect().top
								+ barTop));
			}
			lineCoords.add(j + 1,
					new PointF(barLeft + xIntervalWidthPixels / 2,
							mBarChartData.getGraphRect().top + barTop));

			highlightedBar = (int) (seekBarProgress / xIntervalWidthPixels);
			if (highlightedBar == j) {
				gradient = new LinearGradient(bar.left, bar.top, bar.right,
						bar.bottom, Color.parseColor(barColors.get(temp)),
						Color.parseColor(barColors.get(temp)), TileMode.CLAMP);
				paint.setShader(gradient);
				canvas.drawRect(bar, paint);

				if (isLongPressed) {
					int barWidth = (int) ((bar.right-bar.left)/2);
					hightLightedRect = new RectF(bar.left +barWidth,
							mBarChartData.getGraphRect().top, (bar.right-barWidth)+2,
							bar.bottom);

					fullBarPaint = new Paint();
					fullBarPaint.setColor(highlightedBarColor);
					//fullBarPaint.setAlpha(140);
					canvas.drawRect(hightLightedRect, fullBarPaint);

					textPaint = new Paint();
					textPaint.setTextSize(highlightedLabelSize);
					textPaint.setTypeface(highlightedLabelFont);
					textPaint.setColor(highlightedLabelColor);
					/*if (i == 0) {
						canvas.drawText("" + finalYList.get(i), bar.right,
								mBarChartData.getGraphRect().top, textPaint);
					} else if (i == dataSize - 1) {
						canvas.drawText("" + finalYList.get(i), bar.left - 25
								- finalYList.get(i).length(),
								mBarChartData.getGraphRect().top, textPaint);
					} else*/ {
						float textWidth = textPaint.measureText(finalYList.get(i));
						canvas.drawText("" + finalYList.get(i), bar.left + barWidth -(textWidth/2),
								mBarChartData.getGraphRect().top, textPaint);
					}
				}
			} else {
				paint1 = new Paint();
				gradient = new LinearGradient(bar.left, bar.top, bar.right,
						bar.bottom, Color.parseColor(barColors.get(temp)),
						Color.parseColor(barColors.get(temp)), TileMode.CLAMP);
				paint1.setShader(gradient);
				canvas.drawRect(bar, paint1);
			}

			barLeft += xIntervalWidthPixels;
			temp++;
			listBars.add(bar);
		}

		lineCoords.add(j + 1, new PointF(barRight,
				mBarChartData.getGraphRect().top + barTop));

		listlineCoords.add(lineCoords);

	}

	/**
	 * Draws Y- coordinates of Bar Chart
	 * 
	 * @param canvas
	 */
	private void drawVerticalBarsYcoords(Canvas canvas) {
		int startY = (int) mBarChartData.getyStartVal();
		int textStartY = (int) mBarChartData.getGraphRect().bottom;

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(axisLabelsSize);
		paint.setTypeface(axisLabelsFont);
		paint.setColor(axisLabelsColor);

		float textStartX = 0;
		while (startY <= mBarChartData.getyEndVal()) {
			String textToBeDisplayed = startY + " ";
			textStartX = mBarChartData.getGraphRect().left
					- paint.measureText(textToBeDisplayed);
			canvas.drawText(textToBeDisplayed, textStartX, textStartY
					+ mBarChartData.getFontSize() / 2, paint);
			startY += mBarChartData.getyInterval();
			textStartY -= yIntervalWidthPixels;
		}

		if (yAxisLable != null && !yAxisLable.trim().equals("")) {
			Path path = new Path();
			paint.setTextSize(axisColNameSize);
			paint.setTypeface(axisColNameFont);
			paint.setColor(axisColNameColor);
			path.moveTo(
					20,
					((mBarChartData.getGraphRect().bottom - paint
							.measureText(yAxisLable)) / 2)
							+ paint.measureText(yAxisLable));
			path.lineTo(20, (mBarChartData.getGraphRect().bottom - paint
					.measureText(yAxisLable)) / 2);
			canvas.drawTextOnPath(yAxisLable, path, 0, 0, paint);
		}
	}

	/**
	 * Draws X- coordinates for Vertical Bars
	 * 
	 * @param canvas
	 */
	private void drawVerticalBarsXcoords(Canvas canvas) {
		int tempXIntervalWidthPixels = (int) (xIntervalWidthPixels * yAxisSize);
		int textStartY = (int) mBarChartData.getGraphRect().bottom;
		int textStartX = (int) (mBarChartData.getGraphRect().left
				+ tempXIntervalWidthPixels / 2.0f);
		Path path = null;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(axisLabelsSize);
		paint.setTypeface(axisLabelsFont);
		paint.setColor(axisLabelsColor);
		float textWidth = 0;
		if (skipXLables <= 0) {
			skipXLables = 1;
		}
		for (int i = skipXLables - 1; i < mBarChartData.getxLabel().length; i = i
				+ skipXLables) {
			textStartX = (int) ((mBarChartData.getGraphRect().left + tempXIntervalWidthPixels / 2)
					+ tempXIntervalWidthPixels * i);
			String textToBeDisplayed = " " + mBarChartData.getxLabel()[i] + " ";
			textWidth = paint.measureText(textToBeDisplayed);
			path = new Path();
			path.moveTo(textStartX, textStartY + textWidth);
			path.lineTo(textStartX+ textWidth/2, textStartY);
			canvas.drawTextOnPath(textToBeDisplayed, path, 0, 0, paint);
		}

		if (xAxisLable != null) {
			paint.setTextSize(axisColNameSize);
			paint.setTypeface(axisColNameFont);
			paint.setColor(axisColNameColor);
			canvas.drawText(xAxisLable, mBarChartData.getGraphRect().right / 2,
					mLayoutHeight - 5, paint);
		}
	}

	/**
	 * Draws background grids for Bar chart Report
	 */
	private void drawGrid(Canvas canvas) {
		Paint paint = new Paint();
		Paint axisPaint = new Paint();
		axisPaint.setStrokeWidth(2);
		paint.setColor(Color.BLACK);
		canvas.drawLine(mBarChartData.getGraphRect().left - 4,
				mBarChartData.getGraphRect().bottom,
				mBarChartData.getGraphRect().right + 4,
				mBarChartData.getGraphRect().bottom, axisPaint);
		canvas.drawLine(mBarChartData.getGraphRect().left,
				mBarChartData.getGraphRect().top - 4,
				mBarChartData.getGraphRect().left,
				mBarChartData.getGraphRect().bottom + 4, axisPaint);
		//paint.setStrokeWidth(1);
		float startX;
		float startY;
		float stopX;
		float stopY;

		switch (getBarType()) {
		case BAR_TYPE.VERTICAL:
			startX = mBarChartData.getGraphRect().left;
			startY = mBarChartData.getGraphRect().bottom
					- (skipHorizontalLines + 1) * yIntervalWidthPixels;
			stopX = mBarChartData.getGraphRect().right;
			stopY = mBarChartData.getGraphRect().bottom
					- (skipHorizontalLines + 1) * yIntervalWidthPixels;
			for (; startY > mBarChartData.getGraphRect().top;) {
				paint.setColor(Color.DKGRAY);
				canvas.drawLine(startX, startY, stopX, stopY, paint);
				if (skipHorizontalLines <= 0)
					skipHorizontalLines = 0;
				startY -= (skipHorizontalLines + 1) * yIntervalWidthPixels;
				stopY -= (skipHorizontalLines + 1) * yIntervalWidthPixels;

			}
			break;

		case BAR_TYPE.HORIZONTAL:
			startX = mBarChartData.getGraphRect().left
					+ (skipHorizontalLines + 1) * xIntervalWidthPixels;
			startY = mBarChartData.getGraphRect().top;
			stopX = mBarChartData.getGraphRect().left
					+ (skipHorizontalLines + 1) * xIntervalWidthPixels;
			stopY = mBarChartData.getGraphRect().bottom;
			for (; startX < mBarChartData.getGraphRect().right;) {
				paint.setColor(Color.DKGRAY);
				canvas.drawLine(startX, startY, stopX, stopY, paint);
				if (skipHorizontalLines <= 0)
					skipHorizontalLines = 0;
				startX += (skipHorizontalLines + 1) * xIntervalWidthPixels;
				stopX += (skipHorizontalLines + 1) * xIntervalWidthPixels;

			}
			break;
		default:
			break;
		}
	}

	public boolean onDown(MotionEvent motionEvent) {
		return false;
	}

	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1,
			float vX, float vY) {
		return false;
	}

	public void onLongPress(MotionEvent motionEvent) {
		isLongPressed = true;
		seekBarProgress = (int) ((int) motionEvent.getX() - mBarChartData
				.getGraphRect().left);
		invalidate();
	}

	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1,
			float vX, float vY) {
		return false;
	}

	public void onShowPress(MotionEvent motionEvent) {
	}

	public boolean onSingleTapUp(MotionEvent motionEvent) {
		isSingleTap = !isSingleTap;
		return false;
	}

	public boolean onDoubleTap(MotionEvent motionEvent) {
		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent motionEvent) {
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
		return false;
	}

	/**
	 * Double tap is checked. Then multi touch with two touches alone is
	 * recognized.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.gestureDetector.onTouchEvent(event)) {
			return true;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE && !isLongPressed) {
			setDraged(true);
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE && isLongPressed) {
			if (event.getX() > mBarChartData.getGraphRect().left
					&& event.getX() < mBarChartData.getGraphRect().right) {
				int newValue = (int) ((int) event.getX() - mBarChartData
						.getGraphRect().left);
				if (newValue != seekBarProgress) {
					seekBarProgress = newValue;
					invalidate();
					return true;
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			isLongPressed = false;
			invalidate();
		}
		return true;
	}

	public void setChartData(ReportDTO[] chartData) {
		mBarXmlData = Arrays.copyOf(chartData, chartData.length);
		isSingleTap = false;
		init();
		this.invalidate();
	}

	public boolean isDraged() {
		return isDraged;
	}

	public void setDraged(boolean isDraged) {
		this.isDraged = isDraged;
	}

	public String getXAxisLable() {
		return xAxisLable;
	}

	public void setXAxisLable(String xAxisLable) {
		this.xAxisLable = xAxisLable;
	}

	public String getYAxisLable() {
		return this.yAxisLable;
	}

	public void setYAxisLable(String yAxisLable) {
		this.yAxisLable = yAxisLable;
	}
	private int getPixelValue(int size) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				size, getResources().getDisplayMetrics());
	}
}
