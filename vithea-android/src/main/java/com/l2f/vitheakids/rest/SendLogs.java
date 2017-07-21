package com.l2f.vitheakids.rest;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import com.l2f.vitheakids.R;
import com.l2f.vitheakids.VitheaKidsActivity;
import com.l2f.vitheakids.model.SequenceLogInfo;
import com.l2f.vitheakids.security.Protection;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

// TODO Activate Logs

public class SendLogs extends AsyncTask<Void, Void, Void> {
	
	private String TAG = VitheaKidsActivity.class.getSimpleName() + ": " + SendLogs.class.getName();
	
	private VitheaKidsActivity activity;
	
	private SequenceLogInfo sequenceToSave;
	
	public SendLogs(VitheaKidsActivity act, SequenceLogInfo seq) {
		this.activity = act;
		this.sequenceToSave = seq;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		//TODO replace by correct end
		//final String url = activity.getString(R.string.ws_uri) + activity.getString(R.string.send_log_uri);
		
		//HttpAuthentication authHeader = new HttpBasicAuthentication(
		//		activity.userName, Protection.cypher(activity.password));
		
		// Prepare acceptable media type
		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_XML);
		acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
		acceptableMediaTypes.add(MediaType.TEXT_PLAIN);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		//requestHeaders.setAuthorization(authHeader);
		
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
//		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//		requestHeaders.setContentType(MediaType.APPLICATION_XML);

		requestHeaders.setAccept(acceptableMediaTypes);
		
	/*
		String logs = this.sequenceToSave.toJSON();
		Log.d(TAG, "JSON: " + logs);*/
		
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
//		MultiValueMap<String, SequenceLogInfo> body = new LinkedMultiValueMap<String, SequenceLogInfo>();
		body.add("test", "hello");
	/*	body.add("sequenceLog", logs);*/

	/*	Log.d(TAG, this.sequenceToSave.toString());*/
		
		/*HttpEntity<?> requestEntity = new HttpEntity<Object>(body,
				requestHeaders);*/
		
//		HttpEntity<?> requestEntity = new HttpEntity<Object>(this.sequenceToSave,
//				requestHeaders);
		
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		
		// I know this sounds stupid but it prevents a HttpMessageNotWritableException 
		FormHttpMessageConverter mainConverter = new FormHttpMessageConverter();
		mainConverter.addPartConverter(new MappingJackson2HttpMessageConverter());		
		messageConverters.add(mainConverter);
		//
		
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		messageConverters.add(new ByteArrayHttpMessageConverter());

		restTemplate.setMessageConverters(messageConverters);

	/*	try {
			// Make the network request
			Log.d(TAG, url);
			ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Boolean.class);
			Log.d(TAG, response.getBody().toString());
			//restTemplate.put(url, requestEntity);

		} catch (HttpClientErrorException e) {
			Log.e(TAG, e.getLocalizedMessage(), e);
			e.printStackTrace();
		}	*/
		return null;
	}	

}
