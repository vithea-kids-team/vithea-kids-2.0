package com.l2f.vitheakids;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ReinforcementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReinforcementFragment extends Fragment {
    private byte[] bitmap;


    public ReinforcementFragment() {
        // Required empty public constructor
    }


    public static ReinforcementFragment newInstance(byte[] bitmap) {
        ReinforcementFragment fragment = new ReinforcementFragment();

        fragment.bitmap = bitmap;
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.fragment_reinforcement, container, false);
        ImageView image = (ImageView) viewFragment.findViewById(R.id.reinforcement_image);
        Glide.with(getActivity()).load(bitmap).apply(RequestOptions.skipMemoryCacheOf(true)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(image);
        return viewFragment;
    }


   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/
}
