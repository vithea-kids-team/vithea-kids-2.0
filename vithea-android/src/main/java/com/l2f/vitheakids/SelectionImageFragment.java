package com.l2f.vitheakids;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.l2f.vitheakids.Storage.ImageStorage;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.SelectionArea;
import com.l2f.vitheakids.model.SelectionImageExercise;
import com.l2f.vitheakids.util.ImageViewTouch;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the* to handle interaction events.
 * Use the {@link SelectionImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectionImageFragment extends Fragment {
   private SelectionImageExercise exercise;
   private ImageStorage imageStorage;
   private String seqName;
   private View fragmentView;
   private Child child;






    public SelectionImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters
     * @return A new instance of fragment SelectionImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectionImageFragment newInstance(SelectionImageExercise exercise, ImageStorage imageStorage, String seqName, Child child) {
        SelectionImageFragment fragment = new SelectionImageFragment();
        fragment.exercise = exercise;
        fragment.imageStorage = imageStorage;
        fragment.seqName = seqName;
        fragment.child = child;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fixme
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_selection_image, container, false);

        //question
        TextView questionText = (TextView) fragmentView.findViewById(R.id.questionText);
        String  question  = exercise.getQuestion1();
        question  = (child.toUpperCase())? question.toUpperCase(): question;
        questionText.setText(question);

        ImageViewTouch imageViewTouch = new ImageViewTouch(getContext(),exercise.getSelectionAreas().get(0));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) exercise.getWidthOriginal(), (int) exercise.getHeightOriginal());
        layoutParams.gravity = Gravity.CENTER;
        imageViewTouch.setLayoutParams(layoutParams);
        byte[] image  = imageStorage.getImage(seqName,exercise.getStimulus().getResourceId());
        imageStorage.setImageOfView(getContext(),imageViewTouch,image);

        LinearLayout linearLayout = (LinearLayout) fragmentView.findViewById(R.id.containerImageTouch);
        linearLayout.addView(imageViewTouch);

        imageViewTouch.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                float X = event.getX();
                float Y = event.getY();
                SelectionArea selectionArea = exercise.getSelectionAreas().get(0);
                if(X >= selectionArea.getStartX() && X <= (selectionArea.getEndX()+selectionArea.getStartX()) && Y >= selectionArea.getStartY() && Y <= (selectionArea.getStartY() + selectionArea.getEndY())){
                    ((VitheaKidsActivity) getActivity()).rightAnswerHandler();
                }

                return true;
            }
        });
        
        return  fragmentView;
    }




}
