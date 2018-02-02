package com.l2f.vitheakids.task;

import android.os.AsyncTask;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

public class ReadTask extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... params) {

		if (params.length > 1 && !params[0].isEmpty() && !params[1].isEmpty()) {

            Log.d("saythis", params[0]+ ";" + params[1] +";1");
			UnityPlayer.UnitySendMessage("GameManager", "SayThisWithEmotion", params[0] + ";" + params[1] +";1");
		} else if (!params[0].isEmpty()) {
            Log.d("saythis", params[0]);

            UnityPlayer.UnitySendMessage("GameManager", "SayThis", params[0]);
		}

		// 1 second + 600 ms for each 5 characters
		//slowDown(1000 + (700*(params[0].length()/5)));
		
		return null;
	}

	// trick for Catarina slowing down the speech
	private void slowDown(long ms) {
		try {
			Log.d(this.getClass().getName(), "ZZZZZZZZZ...");
			Thread.sleep(ms);
		} catch (InterruptedException e) {				
			e.printStackTrace();
		}
	}

}
