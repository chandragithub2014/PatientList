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

import android.graphics.Typeface;

/**
 * This class is to represent the font style.
 * 
 * @author 354701
 * 
 */
public class FontStyleDTO {

	private float fontSize;
	private Typeface fontTypeFace;
	private int fontColor;

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public Typeface getFontTypeFace() {
		return fontTypeFace;
	}

	public void setFontTypeFace(Typeface fontTypeFace) {
		this.fontTypeFace = fontTypeFace;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FontStyleDTO [fontSize=");
		builder.append(fontSize);
		builder.append(", ");
		if (fontTypeFace != null) {
			builder.append("fontTypeFace=");
			builder.append(fontTypeFace);
			builder.append(", ");
		}
		builder.append("fontColor=");
		builder.append(fontColor);
		builder.append("]");
		return builder.toString();
	}
	
	
}