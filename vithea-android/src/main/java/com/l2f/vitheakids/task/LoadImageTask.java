package com.l2f.vitheakids.task;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class LoadImageTask extends AsyncTask<String, Void, Void> {
	private Bitmap imgLoad;
	private ImageView imgView;
	private Activity activity;

	public LoadImageTask(Activity act, ImageView imgView) {
		super();
		this.imgView = imgView;
		this.activity = act;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		Log.d("LoadImageTask", params[0]);
		String url = params[0].replace("\\","/");
		imgLoad = LoadImageFromWeb(url);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		imgView.setVisibility(View.VISIBLE);
		imgView.setImageBitmap(imgLoad);
	}
	
	public static Bitmap LoadImageFromWeb(String urlString) {
	    try {
            Log.d("imageURL", urlString);
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			return BitmapFactory.decodeStream(conn.getInputStream());
	    } catch (Exception e) {
			e.printStackTrace();
	    }
	    return null;
	}

}

