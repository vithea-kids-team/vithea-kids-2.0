package com.l2f.vitheakids.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.l2f.vitheakids.LoginActivity;
import com.l2f.vitheakids.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Claudia on 01/02/2017.
 */

public final class Api {

    private static final String AUTHORIZATION = "Authorization";

    public static ResponseEntity<String> get(String url, Activity act) {

        // Prepare acceptable media type
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_XML);
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        acceptableMediaTypes.add(MediaType.TEXT_PLAIN);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        requestHeaders.setAccept(acceptableMediaTypes);

        //add token to header
        String token = getToken(act);
        requestHeaders.add(AUTHORIZATION,token);
        Log.d(AUTHORIZATION, token );
        HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

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

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        Log.d(TAG, String.format("response:%s", response.getBody()));

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            SharedPreferences settings = act.getSharedPreferences("AppPreferences", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.clear();
            prefEditor.commit();
        }

        return response;
    }

    public static ResponseEntity<String> post(String url, MultiValueMap<String, Object> body, Activity act, String requestType) {
        // Prepare acceptable media type
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_XML);
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
        acceptableMediaTypes.add(MediaType.TEXT_PLAIN);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        requestHeaders.setAccept(acceptableMediaTypes);

        //add token to header
        String token = getToken(act);
        if(!requestType.equals("login")) {
            requestHeaders.add(AUTHORIZATION, token);
        }
        Log.d(AUTHORIZATION, token );

        HttpEntity<?> requestEntity = new HttpEntity<Object>(body,
                requestHeaders);

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

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //Log.d(TAG, "response:" + response.getBody().toString());

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            SharedPreferences settings = act.getSharedPreferences("AppPreferences", MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.clear();
            prefEditor.commit();
        }

        return response;
    }

    public static String getToken(Activity act){
        SharedPreferences pref = act.getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String token = pref.getString("authorization","");
        return token;
    }
}
