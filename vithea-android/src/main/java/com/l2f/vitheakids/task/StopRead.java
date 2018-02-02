package com.l2f.vitheakids.task;

import android.os.AsyncTask;

import com.unity3d.player.UnityPlayer;

/**
 * Created by silvi on 15/11/2017.
 */

public class StopRead extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        UnityPlayer.UnitySendMessage("GameManager", "Stop","");
        return null;
    }
}