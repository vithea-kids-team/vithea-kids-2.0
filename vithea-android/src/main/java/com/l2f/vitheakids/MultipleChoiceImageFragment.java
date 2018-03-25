package com.l2f.vitheakids;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.l2f.vitheakids.Storage.ImageStorage;
import com.l2f.vitheakids.model.Answer;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.Exercise;
import com.l2f.vitheakids.model.Resource;
import com.l2f.vitheakids.util.Prompting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MultipleChoiceImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MultipleChoiceImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleChoiceImageFragment extends Fragment {

    private Exercise exercise;
    private Child child;
    private ImageStorage imageStorage;
    private String seqName;
    private View fragmentView;
    private OnFragmentInteractionListener mListener;
    private VitheaKidsActivity  act;
    private List<ImageView> rightAnswers = new ArrayList<>();
    private List<ImageView> wrongAnswers = new ArrayList<>();

    private List<Answer> answers;
    public MultipleChoiceImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MultipleChoiceImageFragment.
     */
    public static MultipleChoiceImageFragment newInstance(Exercise exercise, Child child, ImageStorage imageStorage, String seqName) {
        MultipleChoiceImageFragment fragment = new MultipleChoiceImageFragment();
        fragment.child=child;
        fragment.exercise=exercise;
        fragment.imageStorage=imageStorage;
        fragment.seqName=seqName;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_multiple_choice_image, container, false);

        act = (VitheaKidsActivity) getActivity();

        //question
        TextView questionText = (TextView) fragmentView.findViewById(R.id.questionText);
        String  question  = exercise.getQuestion().getQuestionDescription();
        question  = (child.toUpperCase())? question.toUpperCase(): question;
        questionText.setText(question);

        // stimulus
        TextView stimulusText = (TextView) fragmentView.findViewById(R.id.stimulusText);
        String stimulusTextContent = exercise.getQuestion().getStimulusText();
        if(stimulusText!=null) {
            stimulusTextContent = (child.toUpperCase()) ? stimulusTextContent.toUpperCase() : stimulusTextContent;
            stimulusText.setText(stimulusTextContent);
        }
        //getting answers to shuffle
        answers = exercise.getAnswers();

        drawAnswers();

        return fragmentView ;
    }

    public void drawAnswers(){
        List<Integer> idImageViews = new ArrayList<>(Arrays.asList(R.id.first, R.id.second, R.id.third, R.id.four, R.id.five, R.id.six));
        List<ImageView> optionsList = new ArrayList<>();

        int numberAnswers = answers.size();

        for (int i = 0; i < numberAnswers; i++) {
            ImageView option = (ImageView) fragmentView.findViewById(idImageViews.get(i));
            Answer answer = answers.get(i);
            Resource answerImage = exercise.getAnswers().get(i).getStimulus();
            String path = answerImage != null ? answerImage.getResourcePath() : "";

            if (!path.isEmpty()) {
                String domain = getString(R.string.resources_addr_kids);
                path = domain + path;

                byte[] bitmap = imageStorage.getImage(this.seqName, answerImage.getResourceId());
                imageStorage.setImageOfView(getContext(),option,bitmap);
            }

            option.setVisibility(View.VISIBLE);
            Prompting.resetScratchImage(getContext(),option);

            if (answer.isRightAnswer()) {
                option.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        act.rightAnswerHandler(v, child); //fixme create interface
                    }
                });
                rightAnswers.add(option);



            } else {
                option.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        distractorHandler(v);
                    }
                });
                wrongAnswers.add(option);
            }
            optionsList.add(option);

        }

        if (act.promptingActive && child.getPrompting() != null) {
            if (child.getPrompting().getPromptingStrategy().equals("ALWAYS")) {
                if (child.getPrompting().getPromptingColor()) {
                    for (ImageView rightAnswer : rightAnswers) {
                        Prompting.setImageColor(getContext(), rightAnswer);
                    }
                }
                if (child.getPrompting().getPromptingSize()) {
                    for (ImageView rightAnswer : rightAnswers) {
                        Prompting.setImageSize(getContext(), wrongAnswers);
                    }
                }
                if (child.getPrompting().getPromptingScratch()) {
                    for (ImageView v : optionsList) {
                        if (!rightAnswers.contains(v)) {
                            Prompting.scratchImage(getContext(), v);
                        }
                    }
                }
            }
        }

    }


    public void distractorHandler(View v){
        act.attempts++;
        if (act.promptingActive && child.getPrompting() != null ) {
            if (child.getPrompting().getPromptingHide()) {
                FrameLayout fr = (FrameLayout) v.getParent();
                ImageView im  = (ImageView) fr.getChildAt(1);
                im.setVisibility(View.INVISIBLE);
            }

            if (child.getPrompting().getPromptingScratch()){
                Prompting.scratchImage(getContext(), (ImageView) v);
            }

            if(child.getPrompting().getPromptingColor()) {
                for (ImageView imageView : rightAnswers) {
                    Prompting.setImageColor(getContext(), imageView);
                }
            }

            if(child.getPrompting().getPromptingSize()) {
                for(ImageView imageView: rightAnswers) {
                    Log.d("prompting", "image");
                    Prompting.setImageSize(getContext(), wrongAnswers);
                }
            }
            else {
                act.readWithOrWithoutEmotion(child, "Tenta outra vez.");
            }


        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
           /// mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
