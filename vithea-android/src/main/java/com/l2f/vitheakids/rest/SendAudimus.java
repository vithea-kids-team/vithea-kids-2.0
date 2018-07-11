package com.l2f.vitheakids.rest;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.l2f.vitheakids.VitheaKidsActivity;
import com.l2f.vitheakids.util.Api;
import com.l2f.vitheakids.util.TaskListener;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class SendAudimus extends AsyncTask<Void, Void, ResponseEntity<String>> {

	private String TAG = VitheaKidsActivity.class.getSimpleName() + ": " + SendAudimus.class.getName();

	private final TaskListener listener;
	private final MultiValueMap<String, Object> body;
	private final String url;
	private final Activity activity;

	public SendAudimus(MultiValueMap<String, Object> body, TaskListener listener, String url, Activity activity) {
		this.listener = listener;
		this.body = body;
		this.url = url;
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		listener.onTaskStarted();
	}

	@Override
	protected ResponseEntity<String> doInBackground(Void... arg0) {

		ResponseEntity<String> response = null;

		try {
			response = Api.post(url, body, this.activity, "sendAudimus");
		}
		catch (HttpClientErrorException e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(ResponseEntity<String> response) {
		listener.onTaskFinished(response);
	}

}
