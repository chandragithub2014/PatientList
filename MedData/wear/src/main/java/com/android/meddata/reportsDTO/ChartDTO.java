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
import java.util.Map;

/**
 * ChartDataVO : holds the filters of reports in Map.
 * 
 * @author 280779
 * 
 */
public class ChartDTO {
	public Map<String, List<String>> filters;

	private ChartDTO() {
	}

	private static ChartDTO myObject = new ChartDTO();

	public static ChartDTO getChartDataInstance() {
		return myObject;
	}
}
