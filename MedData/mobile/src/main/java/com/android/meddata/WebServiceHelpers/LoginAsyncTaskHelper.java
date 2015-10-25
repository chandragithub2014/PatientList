/*
package com.android.meddata.WebServiceHelpers;

*/
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
 *//*

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.meddata.constants.OMSConstants;
import com.android.meddata.constants.OMSDefaultValues;
import com.android.meddata.constants.OMSMessages;
import com.android.meddata.interfaces.OMSReceiveListener;


*/
/**
 * 
 * LoginAsyncTaskHelper: Invokes Login Service and authenticates User's
 * credential.
 * 
 * @author 280779
 * 
 *//*

public class LoginAsyncTaskHelper extends AsyncTask<String, Void, String> {

	private final String TAG = this.getClass().getSimpleName();
	private OMSReceiveListener rListener = null;
	private String loginServiceUrl = null;
	private ProgressDialog pDialog = null;
	private final Random myRandom = new Random();
	//private NetworkUsageAnalyzer analyzer = null;
	private int connectionID = OMSDefaultValues.NONE_DEF_CNT.getValue();
	
	private String userNameKey = null;
	private String userName = null;
	
	private String passWordKey = null;
	private String passWord = null;
	public static ProgressDialog pd =null;
	private static final String CONNECTION_PREFIX = "CON";
	private Context context;
	final String DB_PROCESS_DURATION="dbprocessduration";
    final String SERVER_PROCESS_DURATION="serverprocessduration";
	public LoginAsyncTaskHelper(Context ctx, OMSReceiveListener receiveListener) {
		this.context = ctx;
		this.rListener = receiveListener;
*/
/*		this.pd = new TransparentProgressDialog(ctx,
				R.drawable.ic_core_image_for_rotation, "Loading...");
		pd.show();
		pd.setTitle(Html.fromHtml("<b><H1>"
				+ ctx.getResources().getString(R.string.log_message)
				+ "</H1></b>"));
*//*

		pd = new ProgressDialog(ctx);
		pd.setMessage("Authorizing...");
		pd.setCancelable(false);
		pd.show();
	//	this.connectionID = myRandom.nextInt(Integer.MAX_VALUE);
	//	this.analyzer = NetworkUsageAnalyzer.getInstance(ctx);

	}

	@Override
	protected String doInBackground(String... args) {
		String response = null;
		loginServiceUrl = args[0];
		
		loginServiceUrl = loginServiceUrl.replace("pljs.meddata.com", "dev-patientlists.meddata.com");
		
		userNameKey = args[1];
		userName = args[2];
		
		passWordKey = args[3];
		passWord = args[4];
		
		*/
/*Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put(OMSMessages.USERNAME_STRING.getValue(), userName);
		uriParams.put(OMSMessages.PASSWORD_STRING.getValue(), passWord);*//*

		Map<String, String> httpHeaders = new HashMap<String, String>();
		httpHeaders.put(OMSMessages.ACCEPT.getValue(),
				OMSMessages.APP_JSON.getValue());
		httpHeaders.put(OMSMessages.CONTENT_TYPE.getValue(),
				OMSMessages.APP_JSON.getValue());
		//response = postData(loginServiceUrl, uriParams, httpHeaders);
		response = getData(loginServiceUrl, null, httpHeaders);
		
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		if (rListener != null) {
			rListener.receiveResult(result);
		}

		*/
/*if (pd.isShowing() && !(OMSApplication.getInstance().getMetDataMap() != null && OMSApplication.getInstance().getMetDataMap().get(OMSConstants.MEDDATA_KEY_FLAG).toString().equals("Y"))) {
			pd.dismiss();
		}*//*

	}

	*/
/**
	 * 
	 * Invokes Login Service for given URL and User's credential and return
	 * response as String.
	 * 
	 * @param url
	 * @param uriParams
	 * @param httpHeaders
	 * @return response in String
	 *//*

	private String postData(String url, Map<String, String> uriParams,
			Map<String, String> httpHeaders) {
		String response = null;
		HttpClient httpclient=null;

		try {



			Log.d(TAG, "Login Service Url :" + url);
			HttpPost httpPost = new HttpPost(url);
			if (httpHeaders != null) {
				Set<String> headers = httpHeaders.keySet();
				for (String header : headers) {
					httpPost.setHeader(header, httpHeaders.get(header));
				}
			}
			if (uriParams != null) {
				JSONObject jsonObj = new JSONObject();
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				Set<String> keys = uriParams.keySet();

				for (String key : keys) {
					if (uriParams.get(key) != null)
						pairs.add(new BasicNameValuePair(key, uriParams
								.get(key)));
					jsonObj.put(key, uriParams.get(key));
				}
				JSONObject manJson = new JSONObject();
				manJson.put(OMSMessages.LOGIN.getValue(), jsonObj);
				StringEntity entity = new StringEntity(manJson.toString());
				httpPost.setEntity(entity);

			}
			HttpResponse httpResponse = httpclient.execute(httpPost);
		//	analyzer.updateConnectionStatus(connectionID, true);
			StatusLine statusLine = httpResponse.getStatusLine();
			Log.d(TAG, "Service response code:" + statusLine.getStatusCode());
			if (statusLine.getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					InputStream is = httpEntity.getContent();
					response = convertStreamtoString(is);

					JSONObject responseJSON = new JSONObject(response);
					JSONObject responseJSONObject = responseJSON
							.getJSONObject(OMSMessages.RESPONSE_STRING
									.getValue());
					response = responseJSONObject
							.getString(OMSMessages.AUTHENTICATION_STATUS
									.getValue());
					Log.d(TAG, "Service response:" + response);
					if (response != null) {
						return response;
					}
				}
			} else {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					InputStream is = httpEntity.getContent();
					response = convertStreamtoString(is);

					if (response == null || !response.trim().equals("")) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put(OMSMessages.ERROR.getValue(),
								statusLine.getStatusCode());
						jsonObject.put(
								OMSMessages.ERROR_DESCRIPTION.getValue(),
								statusLine.toString());
						response = jsonObject.toString();
					}
				}
			}
		} catch (SocketTimeoutException se) {
			Log.e(TAG, "Timed out" + se.getMessage());
			if (rListener != null) {
				rListener.receiveResult("sockettimeout");
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			se.printStackTrace();

		} catch (IOException e) {
			Log.e(TAG,
					"IOException occurred while executing Login Service"
							+ e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG,
					"Exception occurred while executing Login Service"
							+ e.getMessage());
			e.printStackTrace();

		}
		return response;
	}
	
	
	@SuppressWarnings("unchecked")
	private String getData(String serviceUrl, Map<String, String> uriParams,
			Map<String, String> httpHeaders){

		String configResponse = null;
		HttpClient httpclient = null;
		HttpResponse response = null;
		HttpEntity httpEntity = null;
		HttpParams httpParameters = new BasicHttpParams();
		// AppMonitor

		int timeoutSocket = 5000;
		
		if (uriParams != null) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Set<String> keys = uriParams.keySet();
			
			if(!serviceUrl.endsWith("?"))
				serviceUrl += "?";
			
			for (String key : keys) {
				if (uriParams.get(key) != null)
					params.add(new BasicNameValuePair(key, uriParams
							.get(key)));				
			}		
		    String paramString = URLEncodedUtils.format(params, "utf-8");
		    serviceUrl += paramString;
		}

		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);



		HttpGet httpget = new HttpGet(serviceUrl);
		HttpPost httpPost = new HttpPost(serviceUrl);
		// AppMonitor
	//	analyzer.sentConnectionRequest(connectionID, serviceUrl.length());
		 if (OMSConstants.IS_MEDDATA_APP){

				try {
					JSONObject loginJsonObject = new JSONObject();
					loginJsonObject.put("Login_Id", userName);
					loginJsonObject.put("Password", passWord);
					JSONObject requestJsonObject = new JSONObject();
					requestJsonObject.put("request", loginJsonObject);
					Log.i(TAG, "Log in Request JSON : "+requestJsonObject.toString());
					StringEntity entity = new StringEntity(requestJsonObject.toString());
					httpPost.setEntity(entity);
					httpPost.addHeader("content-type","application/json");
					response = httpclient.execute(httpPost);

					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == OMSConstants.STATUSCODE_OK) {
						httpEntity = response.getEntity();
						if (httpEntity != null) {
								String jsonContent = EntityUtils
										.toString(httpEntity,"UTF-8");
								Log.i(TAG, "Login Service Response: " + jsonContent);
								jsonContent.replace("\\n", "");
								jsonContent.replace("\\t", "");
								JSONObject responseJsonObject = new JSONObject(jsonContent);
								String flag = responseJsonObject.getString("Flag");
								//if(flag.equalsIgnoreCase(OMSConstants.MEDDATA_LOGIN_SUCCESSFULL) || flag.equalsIgnoreCase(OMSConstants.MEDDATA_LOGIN_SUCCESSFULL)){
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_ACTIVIE, responseJsonObject.getString("Active"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_ATTEMPTS, responseJsonObject.getString("Attempts"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_DESIGNATION, responseJsonObject.getString("Designation"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_EMAILID, responseJsonObject.getString("EmailID"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_ENCOUNTERTABLE, responseJsonObject.getString("EncounterTable"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_ENTITYID, responseJsonObject.getString("EntityID"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_FLAG, responseJsonObject.getString("Flag"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_IMAGEBASE, responseJsonObject.getString("ImageBase64"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_IMAGEID, responseJsonObject.getString("ImageID"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_KEY, responseJsonObject.getString("Key"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_LOCATION, responseJsonObject.getString("Location"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_LOCATION_NAME, responseJsonObject.getString("LocationName"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_LOCATIONS, responseJsonObject.getString("Locations"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_LOCKED, responseJsonObject.getString("Locked"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_LOGIN_IDL, userName);
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_NAME, responseJsonObject.getString("Name"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_PHONENO, responseJsonObject.getString("PhoneNumber"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_REQ_TYPE, responseJsonObject.getString("ReqType"));
						
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_SCAN_SETTINGSL, responseJsonObject.getString("ScanSetting"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_SPECILAZATION, responseJsonObject.getString("Specialization"));
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_USERNAME, responseJsonObject.getString("UserName"));
									
									int roleId ;
									if(Integer.parseInt(responseJsonObject.getString("Role")) == 1){
										roleId = 2;
										
									}else{
										roleId = 1;
									}
									OMSApplication.getInstance().getMetDataMap().put(OMSConstants.MEDDATA_KEY_ROLE, "" + roleId);
									OMSApplication.getInstance().setRoleID(roleId);
									
									configResponse = OMSMessages.LOGIN_SUCCESS
											.getValue();
									
									//TransDatabaseUtil.deleteAllRecord("userdetails");
									ContentValues c=  new ContentValues();
									c.put("Login_Id",responseJsonObject.getString("LoginId"));
									c.put("Password","");
									c.put("usid","1");
									c.put("isdelete", 0);
								//	TransDatabaseUtil.update("userdetails", c, null, null);
									SharedPreferences sharedPreferences = PreferenceManager
											.getDefaultSharedPreferences(this.context);
									 Editor editor = sharedPreferences.edit();  
							
									editor.putString("USER_ID",userName);
									editor.commit();;
								*/
/*}else{
									configResponse = OMSMessages.LOGIN_FAILURE_INVALID_USER
											.getValue();
								}*//*

								
						}
					}
					else if (statusLine.getStatusCode() == OMSConstants.STATUSCODE_METHOD_NOT_ALLOWED) {
						Log.e(TAG, "status code["+statusLine.getStatusCode()+"] Reason["+statusLine.getReasonPhrase()+"]");
						configResponse = OMSMessages.LOGIN_FAILURE_SERVICE_INCORRECT.getValue();
					} else {
						Log.e(TAG, "status code["+statusLine.getStatusCode()+"] Reason["+statusLine.getReasonPhrase()+"]");
						configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
					}
				}
			*/
/*	catch(SocketTimeoutException e){
					Log.e(TAG,
							"SocketTimeoutException occurred while excecuting the Config Service."
									+ e.getMessage());
					if (rListener != null) {
						rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
					}
					e.printStackTrace();
				}
				catch (SocketException e) {
					Log.e(TAG,
							"SocketException occurred while excecuting the Config Service."
									+ e.getMessage());
					if (rListener != null) {
						rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
					}
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(TAG,
							"IOException occurred while while excecuting the Config Service."
									+ e.getMessage());
					e.printStackTrace();
				}*//*
 catch (Exception e) {
					if(pd != null)
					  pd.dismiss();
					
					Log.e(TAG,
							"Exception occurred while excecuting the Login Service."
									+ e.getMessage());
					e.printStackTrace();
					if (rListener != null) {
						configResponse = "SERVER_ERROR";
						return configResponse;
					}
				}
				
			}
		//MedData Changes end	
			else{
				try {
					response = httpclient.execute(httpget);
					// AppMonitor
				//	analyzer.updateConnectionStatus(connectionID, true);
		
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == OMSConstants.STATUSCODE_OK) {
						httpEntity = response.getEntity();
						if (httpEntity != null) {
							try {
								String jsonContent = EntityUtils
										.toString(httpEntity,"UTF-8");
								Log.i(TAG, "Login Service Response: " + jsonContent);
								jsonContent.replace("\\n", "");
								jsonContent.replace("\\t", "");
							
								// AppMonitor
							*/
/*	analyzer.receivedConnectionResponse(connectionID,
										httpEntity.getContentLength(),
										OMSDatabaseConstants.GET_TYPE_REQUEST);
								*//*

							*/
/*	if(validateUser(jsonContent)){
									//authentication passed. Insert record to the trans table
									DataParser dataParser = new DataParser(context);
									//dataParser.parseAndUpdate(jsonContent);
									
									configResponse = OMSMessages.LOGIN_SUCCESS
											.getValue();
								}else{
									configResponse = OMSMessages.LOGIN_FAILURE_INVALID_USER
											.getValue();
								}*//*

							} catch (IOException e) {
								Log.e(TAG,
										"Exception occurred while parsing the Service response."
												+ e.getMessage());
								e.printStackTrace();
							}
						}
					} else if (statusLine.getStatusCode() == OMSConstants.STATUSCODE_METHOD_NOT_ALLOWED) {
						Log.e(TAG, "status code["+statusLine.getStatusCode()+"] Reason["+statusLine.getReasonPhrase()+"]");
						configResponse = OMSMessages.LOGIN_FAILURE_SERVICE_INCORRECT.getValue();
					} else {
						Log.e(TAG, "status code["+statusLine.getStatusCode()+"] Reason["+statusLine.getReasonPhrase()+"]");
						configResponse = OMSMessages.NETWORK_RESPONSE_ERROR.getValue();
						
						// Failed
						*/
/*httpEntity = response.getEntity();
						if (httpEntity != null) {
							inStream = httpEntity.getContent();
							// AppMonitor
							analyzer.receivedConnectionResponse(connectionID,
									httpEntity.getContentLength(),
									OMSDatabaseConstants.GET_TYPE_REQUEST);
							result = convertStreamToString(inStream);
		
							if (result != null) {
								Log.d(TAG, "response :" + result);
								return configResponse;
								
							} else {
								try {
									JSONObject jsonObject = new JSONObject();
									jsonObject.put(OMSMessages.ERROR.getValue(),
											statusLine.getStatusCode());
									jsonObject.put(
											OMSMessages.ERROR_DESCRIPTION.getValue(),
											statusLine.toString());
									result = jsonObject.toString();
								} catch (JSONException e) {
									Log.e(TAG,
											"JSONException occurred when is ConfigDBParse Failed."
													+ e.getMessage());
									e.printStackTrace();
								}
							}
						}*//*

					}
				}
				catch(SocketTimeoutException e){
					Log.e(TAG,
							"SocketTimeoutException occurred while excecuting the Config Service."
									+ e.getMessage());
					if (rListener != null) {
						rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
					}
					e.printStackTrace();
				}
				catch (SocketException e) {
					Log.e(TAG,
							"SocketException occurred while excecuting the Config Service."
									+ e.getMessage());
					if (rListener != null) {
						rListener.receiveResult(OMSMessages.CONFIG_DB_ERROR.getValue());
					}
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(TAG,
							"IOException occurred while while excecuting the Config Service."
									+ e.getMessage());
					e.printStackTrace();
				} catch (Exception e) {
					Log.e(TAG,
							"Exception occurred while excecuting the Config Service."
									+ e.getMessage());
					e.printStackTrace();
				}
				}
		return configResponse;
	
	}

	*/
/**
	 * Converts InputStream into String
	 * 
	 * @param
	 * @return string
	 * @throws IOException
	 *//*

	private String convertStreamtoString(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + OMSMessages.NEWLINE_CHAR.getValue());
		}
		is.close();
		return sb.toString();
	}

	*/
/**
	 * Converts Input Stream to String.
	 * 
	 * @param
	 * @return String
	 *//*

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + OMSMessages.NEWLINE_CHAR.getValue());
			}
		} catch (IOException e) {
			Log.e("Config Parser",
					"IOException occurred while Converting Stream to String."
							+ e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.e("Config Parser",
						"IOException occurred while Converting Stream to String."
								+ e.getMessage());
				e.printStackTrace();
			}
		}
		String rr =  sb.toString();
		rr.replace("\\n", "");
		rr.replace("\\t", "");
		return rr;
	}

	*/
/**
	 * validate logged-in user password
	 * 
	 * @param jsonResponse
	 *//*

	private  boolean validateUser(String jsonResponse) {
		JSONObject inputJsonObject = null;
		JSONArray arrayOfRecords = null;
		JSONObject record = null;
		// This changes are for Oasis Project. # Start 
	//boolean requestStatus = false;
		try {
			inputJsonObject = new JSONObject(jsonResponse);
			
			Iterator<String> tableKeysIterator = inputJsonObject.keys();
			// This changes are for Oasis Project. # Start 
			//boolean validateStatus = false;
			while(tableKeysIterator.hasNext()){
				String tableNameKey  = tableKeysIterator.next();
		    	Log.d(TAG, "tableNameKey:"+tableNameKey);
		    	
		    	if("visiteddate".equalsIgnoreCase(tableNameKey)) continue;
		    	//Fetch dbprocess duration serverprocess duration
				else if(DB_PROCESS_DURATION.equalsIgnoreCase(tableNameKey)){
					continue;
				}else if(SERVER_PROCESS_DURATION.equalsIgnoreCase(tableNameKey)){
					continue;
				}
		    	//comment the below line for oasis
		    	arrayOfRecords = (JSONArray) inputJsonObject.get(tableNameKey);
		    	// This changes are for Oasis Project. # Start 
		    	
		    	*/
/*if(tableNameKey.equalsIgnoreCase("ResponseMessage") || tableNameKey.equalsIgnoreCase("UserKey")){
		    		String key 	= tableNameKey;
					String value = inputJsonObject.get(key).toString();
					if ("ResponseMessage".equals(key)) {
						if ("Success".equals(value)) 
							requestStatus = true;
						//break;
					}
					// This changes are for Oasis Project. # Start 
//					else if("UserKey".equals(key)) {
//						OMSApplication.getInstance().setUserKey(value);
//					}	
					// This changes are for Oasis Project. # End 
				} else {
					arrayOfRecords = (JSONArray) inputJsonObject
							.get(tableNameKey); *//*

					// This changes are for Oasis Project. # END
					for (int i = 0; i < arrayOfRecords.length(); i++) {
						record = (JSONObject) arrayOfRecords.get(i);

						if (record != null && record.length() > 0) {
							Iterator<String> tableIterator = record.keys();
							while (tableIterator.hasNext()) {
								String key = tableIterator.next();
								String value = record.get(key).toString();

								Log.d(TAG, "[" + key + ":" + value + "]");
								if (passWordKey.equals(key)) {
									if (passWord.equals(value))
										return true;
									break;
								}
							}
						}
					}
				}
			// This changes are for Oasis Project. # Start 
			//}
			// This changes are for Oasis Project. # End
		} catch (JSONException e) {
			Log.e(TAG, "JSONException occurred. Exception is:"+e.getMessage());
		}
		// This changes are for Oasis Project. # Start 
		//return requestStatus;
		// This changes are for Oasis Project. # End
		return false;
	}
}*/
