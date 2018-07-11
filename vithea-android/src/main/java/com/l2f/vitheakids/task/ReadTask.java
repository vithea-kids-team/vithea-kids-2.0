package com.l2f.vitheakids.task;

import android.os.AsyncTask;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

public class ReadTask extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... params) {

		if(params[0].equals("cat")) UnityPlayer.UnitySendMessage("/cat5/tgrl1_body/tgrl1_head", "setVoice", "Violeta_slow");					// cat
		else if(params[0].equals("edgar")) UnityPlayer.UnitySendMessage("/edgar1/tguy1_head", "setVoice", "Viriato_slow");	    			// edgar
		else if(params[0].equals("filipe")) UnityPlayer.UnitySendMessage("/john2/tguy5_body/tguy5_head", "setVoice", "Vicente_slow"); 		// filipe

		if (params.length > 2 && !params[1].isEmpty() && !params[2].isEmpty()) {
            Log.d("saythis", params[1]+ ";" + params[2] +";1");
			UnityPlayer.UnitySendMessage("GameManager", "SayThisWithEmotion", params[1] + ";" + params[2] +";1");
		} else if (!params[1].isEmpty()) {
			Log.d("saythis", params[0]);
            Log.d("saythis", params[1]);

            UnityPlayer.UnitySendMessage("GameManager", "SayThis", params[1]);

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
