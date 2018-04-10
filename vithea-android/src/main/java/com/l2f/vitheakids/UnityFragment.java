package com.l2f.vitheakids;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.*;
import android.widget.LinearLayout;
import android.view.ViewGroup;

import com.l2f.vitheakids.R;
import com.unity3d.player.UnityPlayer;

/**
 * Created by silvi on 07/03/2018.
 */

public class UnityFragment extends Fragment {
    UnityPlayer mUnityPlayer;
    VitheaKidsActivity activity ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("fragmento", "entrei");
        View viewFragment = inflater.inflate(R.layout.unity_fragment,container,false);
        activity = (VitheaKidsActivity) getActivity();
        mUnityPlayer = activity.getmUnityPlayer();
        FrameLayout layout  = (FrameLayout) viewFragment.findViewById(R.id.view1);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view =  mUnityPlayer.getView();
        layout.addView(view, 0, lp);

        return viewFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUnityPlayer.resume();

    }


    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
    }
}
