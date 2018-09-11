package com.heartyy.heartyyfresh.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.heartyy.heartyyfresh.global.Global;

import java.io.IOException;

public class RegisterBackground extends AsyncTask<String, String, String> {

	GoogleCloudMessaging gcm;
	Context context;
	String regid;
	//String SENDER_ID = "1054933944853";
	String SENDER_ID =   "931761255353";
	SharedPreferences pref;

	public RegisterBackground(GoogleCloudMessaging gcm, Context context) {
		this.gcm = gcm;
		this.context = context;
	}

	public RegisterBackground() {

	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub

		String msg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			regid = gcm.register(SENDER_ID);
			msg = "Device registered, registration ID=" + regid;
			Log.d("111", msg);

			pref = context.getApplicationContext().getSharedPreferences(
					"MyPref", context.MODE_PRIVATE);
			Editor editor = pref.edit();
			editor.putString(Constants.DEVICE_TOKEN, regid);
			editor.commit();
			Global.devicetoken = regid;

		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
		}
		return msg;
	}

	@Override
	protected void onPostExecute(String msg) {
		// mDisplay.append(msg + "\n");
		/*
		 * pref = context.getApplicationContext().getSharedPreferences("MyPref",
		 * context.MODE_PRIVATE); Editor editor = pref.edit();
		 * editor.putString(Constants.DEVICE_TOKEN, regid); editor.commit();
		 */

		Global.devicetoken = regid;

	}

}
