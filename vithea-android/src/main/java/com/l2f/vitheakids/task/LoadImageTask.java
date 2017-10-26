package com.l2f.vitheakids.task;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.l2f.vitheakids.R;
import com.l2f.vitheakids.Storage.ImageStorage;
import com.l2f.vitheakids.VitheaKidsActivity;
import com.l2f.vitheakids.model.Resource;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class LoadImageTask extends AsyncTask<String, Integer, Void> {
	private Bitmap imgLoad;
	private VitheaKidsActivity activity;
	private long id;
	private String url;
	private String className;
	private List<Resource> resources;
    private ProgressDialog progress;


    public LoadImageTask(List<Resource> resources, String className, VitheaKidsActivity act) {
		super();
		this.activity = act;
		this.id = id;
		this.url = act.getString(R.string.resources_addr_kids);
		this.className= className;
		this.resources = resources;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
        progress=new ProgressDialog(activity);
        progress.setMessage("A preparar aula...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setMax(100);
        progress.setCancelable(false);
        progress.show();

    }
	
	@Override
	protected Void doInBackground(String... params) {
		ImageStorage imageStorage = (ImageStorage) activity.getApplication();

        int length = resources.size();
        int i=0;
        int progressN =0;
		for(Resource res : resources){
            Log.d("LoadImageTask", this.url+res.getResourcePath());
            Log.d("id", Long.toString(res.getResourceId()));
            imgLoad = LoadImageFromWeb(this.url+res.getResourcePath());
            imageStorage.addImage(className,res.getResourceId(), imgLoad);
            i++;
            progressN = ((i*100)/length);
            Log.d("progress", Integer.toString(progressN) +"i: " + Integer.toString(i) );
            publishProgress((int)progressN);

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
        progress.dismiss();
        activity.setExerciseView();
        activity.setNavigationView();        // Button - skip, finish, etc

		/*imgView.setVisibility(View.VISIBLE);
		imgView.setImageBitmap(imgLoad);*/


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

    @Override
    protected void onProgressUpdate(Integer... progressN) {
        super.onProgressUpdate(progressN);
        // Update the progress dialog

        Log.d("progress11111", Integer.toString(progressN[0]));

        progress.setProgress(progressN[0]);
        // Dismiss the progress dialog
        //mProgressDialog.dismiss();
    }

}

