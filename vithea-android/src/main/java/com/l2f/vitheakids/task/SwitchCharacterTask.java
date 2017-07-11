package com.l2f.vitheakids.task;

import android.os.AsyncTask;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

/**
 * Created by Claudia on 17/05/2017.
 */

public class SwitchCharacterTask extends AsyncTask<String, Void, Void> {

    // Possible changes (cin, cout):
    //cat, john2
    //cat, edgar1
    //filipe, cat5
    //filipe, edgar1
    //edgar, john2
    //edgar, cat5

    @Override
    protected Void doInBackground(String... c) {
            if(!c[0].isEmpty() && !c[1].isEmpty()) {
                UnityPlayer.UnitySendMessage("GameManager", "switchCharProxy", c[0] + ";" + c[1]);
            }

        return null;
    }
}
