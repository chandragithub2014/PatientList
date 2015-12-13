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
package com.android.meddata.reportsDTO;

import java.util.List;

import android.graphics.RectF;

/**
 * BarChartData : contains properties of bar chart.
 * 
 * @author 280779
 */
public class BarChartDTO {
	private int paddingTop;
	private int paddingBottom;
	private int paddingLeft;
	private int paddingRight;
	private String graphName;
	private RectF graphRect;
	private String[] xLabel;
	private List<String[]> allXValues;
	private String[] xValue;
	private float yStartVal;
	private float yInterval;
	private float yEndVal;
	private int barStartColor;
	private int barEndColor;
	private int lineColor;
	private int trendLineColor;
	private boolean lineGraph;
	private boolean lineShading;
	private boolean barGraph;
	private boolean trendLineGraph;
	private int backgroundStyle;
	private int trendType;
	private int fontSize;

	public int getPaddingTop() {
		return paddingTop;
	}

	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	public int getPaddingBottom() {
		return paddingBottom;
	}

	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	public int getPaddingLeft() {
		return paddingLeft;
	}

	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	public int getPaddingRight() {
		return paddingRight;
	}

	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	public RectF getGraphRect() {
		return graphRect;
	}

	public void setGraphRect(RectF graphRect) {
		this.graphRect = graphRect;
	}

	public String[] getxLabel() {
		return xLabel.clone();
	}

	public void setxLabel(String[] xLabel) {
		this.xLabel = xLabel;
	}

	public List<String[]> getAllxValues() {
		return allXValues;
	}

	public void setAllxValues(List<String[]> yAxisValuesList) {
		this.allXValues = yAxisValuesList;
	}

	public String[] getxValue() {
		return xValue.clone();
	}

	public void setxValue(String[] xValue) {
		this.xValue = xValue;
	}

	public float getyStartVal() {
		return yStartVal;
	}

	public void setyStartVal(float yStartVal) {
		this.yStartVal = yStartVal;
	}

	public float getyInterval() {
		return yInterval;
	}

	public void setyInterval(float yInterval) {
		this.yInterval = yInterval;
	}

	public float getyEndVal() {
		return yEndVal;
	}

	public void setyEndVal(float yEndVal) {
		this.yEndVal = yEndVal;
	}

	public boolean isLineGraph() {
		return lineGraph;
	}

	public void setLineGraph(boolean lineGraph) {
		this.lineGraph = lineGraph;
	}

	public boolean isLineShading() {
		return lineShading;
	}

	public void setLineShading(boolean lineShading) {
		this.lineShading = lineShading;
	}

	public boolean isBarGraph() {
		return barGraph;
	}

	public void setBarGraph(boolean barGraph) {
		this.barGraph = barGraph;
	}

	public void setBarStartColor(int barStartColor) {
		this.barStartColor = barStartColor;
	}

	public int getBarStartColor() {
		return barStartColor;
	}

	public void setBarEndColor(int barEndColor) {
		this.barEndColor = barEndColor;
	}

	public int getBarEndColor() {
		return barEndColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setTrendLineColor(int trendLineColor) {
		this.trendLineColor = trendLineColor;
	}

	public int getTrendLineColor() {
		return trendLineColor;
	}

	public void setTrendLineGraph(boolean trendLineGraph) {
		this.trendLineGraph = trendLineGraph;
	}

	public boolean isTrendLineGraph() {
		return trendLineGraph;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setBackgroundStyle(int backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}

	public int getBackgroundStyle() {
		return backgroundStyle;
	}

	public void setTrendType(int trendType) {
		this.trendType = trendType;
	}

	public int getTrendType() {
		return trendType;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getFontSize() {
		return fontSize;
	}
}
