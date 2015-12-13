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

import java.util.ArrayList;
import java.util.List;

/**
 * ReportData : Contains the generic properties of Charts(Reports).
 * 
 * @author 280779
 * 
 */
public class ReportDTO {
	/** Chart name of pie chart. */
	private String chartName;

	/** Array list of labels of each pie-item of pie-chart. */
	private List<String> segLabel = new ArrayList<String>();

	/** Array list of values of each pie-item of pie-chart. */
	private List<String> segValue = new ArrayList<String>();

	/** Array list of colors of each pie-item of pie-chart. */
	private List<String> segColor = new ArrayList<String>();
	private float yStartValue;
	private float yInterval;
	private float yEndValue;


	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public String getChartName() {
		return chartName;
	}

	public void setSegLabel(List<String> segLabel) {
		this.segLabel = segLabel;
	}

	public List<String> getSegLabel() {
		return segLabel;
	}

	public void setSegValue(List<String> segValue) {
		this.segValue = segValue;
	}

	public List<String> getSegValue() {
		return segValue;
	}

	public void setSegColor(List<String> segColor) {
		this.segColor = segColor;
	}

	public List<String> getSegColor() {
		return segColor;
	}

	
	public float getyStartValue() {
		return yStartValue;
	}

	public void setyStartValue(float yStartValue) {
		this.yStartValue = yStartValue;
	}

	public float getyInterval() {
		return yInterval;
	}

	public void setyInterval(float yInterval) {
		this.yInterval = yInterval;
	}

	public float getyEndValue() {
		return yEndValue;
	}

	public void setyEndValue(float yEndValue) {
		this.yEndValue = yEndValue;
	}
}
