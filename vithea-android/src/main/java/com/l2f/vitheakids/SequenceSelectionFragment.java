package com.l2f.vitheakids;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.Exercise;
import com.l2f.vitheakids.model.Resource;
import com.l2f.vitheakids.model.SequenceExercises;
import com.l2f.vitheakids.task.LoadImageTask;
import com.l2f.vitheakids.util.ExerciseMenuListWithoutImageAdapter;

import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SequenceSelectionFragment.OnFragmentSelectionListener} interface
 * to handle interaction events.
 * Use the {@link SequenceSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SequenceSelectionFragment extends Fragment {

    private Child child;


    private OnFragmentSelectionListener mListener;

    public SequenceSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SequenceSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SequenceSelectionFragment newInstance(Child child) {
        SequenceSelectionFragment fragment = new SequenceSelectionFragment();
        fragment.child = child;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_sequence_selection, container, false);
        ListView lv = (ListView) view.findViewById(R.id.list_by_type);
        BaseAdapter mAdapter = new ExerciseMenuListWithoutImageAdapter(getContext());


        // Load data
        if(this.child.getSequencesList().size() == 0){
            TextView textView = new TextView(this.getContext());
            textView.setText(R.string.noClasses);
            textView.setTextColor(Color.BLACK);
            LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            par.setMargins(50,0,0,0);
            textView.setLayoutParams(par);
            view.addView(textView);
        }
        int i = 0;
        for (SequenceExercises sInfo : this.child.getSequencesList()) {
            // Shows only sequences with exercises
            if (sInfo.getSequenceExercises().size() > 0) {
                ((ExerciseMenuListWithoutImageAdapter) mAdapter).add(sInfo.getName());
            }
            i++;
        }
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SequenceExercises seq = child.getSequencesList().get(position);

                if(child.getSequenceExercisesPreferences().getSequenceExercisesOrder().equals("RANDOM")){
                    Collections.shuffle(seq.getSequenceExercises());
                }

                mListener.onFragmentSelection(seq.getSequenceId(), seq.getName(), seq.getSequenceExercises(), position);




                // Draw view exercise at LoadImageTask
            }
        });

        return view;
    }


    /**
     * Vithea kids activity is the listener
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSelectionListener) {
            mListener = (OnFragmentSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentSelectionListener {
        void onFragmentSelection(Long idSeq, String seqName, List<Exercise> exercises, int position);
    }
}
