package com.l2f.vitheakids;


import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.l2f.vitheakids.Storage.ImageStorage;
import com.l2f.vitheakids.model.Answer;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.Exercise;
import com.l2f.vitheakids.model.MultipleChoice;
import com.l2f.vitheakids.model.Resource;
import com.l2f.vitheakids.util.Prompting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.graphics.Color.parseColor;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MultipleChoiceTextFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MultipleChoiceTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleChoiceTextFragment extends Fragment {


    private MultipleChoice exercise;
    private Child child;
    private OnFragmentInteractionListener mListener;
    private ImageStorage imageStorage;
    private String seqName;
    private VitheaKidsActivity activity;
    private List<String> currentAnswers;
    private List<Button> rightAnswerButtons;
    private List<Answer> answers;
    private View fragmentView;
    /**
     *
     * @param exercise
     * @param child
     * @param seqName
     * @param imageStorage //storage with the images of the class
     * @return
     */
    public static MultipleChoiceTextFragment newInstance(MultipleChoice exercise, Child child, ImageStorage imageStorage, String seqName) {
        MultipleChoiceTextFragment fragment = new MultipleChoiceTextFragment();
        fragment.exercise = exercise;
        fragment.child = child;
        fragment.imageStorage = imageStorage;
        fragment.seqName = seqName;
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (VitheaKidsActivity) this.getActivity();

        fragmentView = inflater.inflate(R.layout.fragment_mulltiple_choice_text,container,false);

        TextView questionText = (TextView) fragmentView.findViewById(R.id.questionText);
        String  question  = exercise.getQuestion().getQuestionDescription();
        rightAnswerButtons = new ArrayList<>();

        //question
        question  = (child.toUpperCase())? question.toUpperCase(): question;
        questionText.setText(question);

        // Stimulus
        Resource stimulus = exercise.getQuestion().getStimulus();

        if (stimulus!=(null)) {

            byte[] image  = imageStorage.getImage(seqName,stimulus.getResourceId());
            ImageView img = (ImageView) fragmentView.findViewById(R.id.stimulusImage);
            imageStorage.setImageOfView(getContext(),img,image);
        }
        else {
            ImageView img = (ImageView) fragmentView.findViewById(R.id.stimulusImage);
            img.setVisibility(View.GONE);
            LinearLayout layout = (LinearLayout) fragmentView.findViewById(R.id.layoutButtonText);
            //change top margin
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout.getLayoutParams();
            params.setMargins(0, 30, 0, 0);
            layout.setLayoutParams(params);
        }

        // Exercise Options
        //  shuffle exerciseOptions
        if (!exercise.getAnswersList().isEmpty()) {

            answers = exercise.getAnswersList();
            // if(exerciseLong==null) { //todo  keep  the answers order  when redrawn
            Collections.shuffle(answers);
            exercise.setAnswersList(answers);
            // }
            drawAnswers();
        }
        return fragmentView;
    }

    protected  void distractorHandler(View v){
        activity.attempts++;
        if(activity.promptingActive && child.getPrompting()!=null){
            if(child.getPrompting().getPromptingHide()) {
                currentAnswers.remove(((Button) v).getText().toString().toUpperCase());
                v.setVisibility(View.INVISIBLE);
            }
            if (child.getPrompting().getPromptingScratch()) {
                Prompting.scratchText(getContext(), (Button) v);
            }
            if(child.getPrompting().getPromptingColor()){
                for(Button b : rightAnswerButtons){
                    Prompting.setButtonColor(activity, b);
                };
            }
            if(child.getPrompting().getPromptingSize()) {
                for(Button b : rightAnswerButtons) {
                    Prompting.setSizeText(getContext(), b);
                }
            }
            if(child.getPrompting().getPromptingRead()){
                Prompting.readAnswers(currentAnswers);

            }
        }

    }

    public void drawAnswers(){
        List<Integer> idButtons = new ArrayList<>(Arrays.asList(R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6));
        List<Button> buttonList = new ArrayList<>();
        int numberAnswers = answers.size();

        for (int i = 0; i < numberAnswers; i++) {
            Log.d("answer", i + " " + answers.get(i).getAnswerDescription());
            Button btn = (Button) fragmentView.findViewById(idButtons.get(i));
            Answer answer = answers.get(i);
            btn.setBackgroundDrawable(Prompting.makeSelector(getContext(), getResources().getColor(R.color.exercise_button_color)));

            //fixme testar prompting com cores!
            if (!child.getSequenceExercisesPreferences().getSequenceExerciseCapitalization().equals("DEFAULT")) {
                btn.setText(answer.getAnswerDescription().toUpperCase());
            } else {
                btn.setText(answer.getAnswerDescription());
            }
            btn.setVisibility(View.VISIBLE);

            if (answer.isRightAnswer()) { //right answer
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        activity.rightAnswerHandler();
                    }
                });
                rightAnswerButtons.add(btn);

            } else {
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        distractorHandler(v);
                    }
                });
            }
            buttonList.add(btn);
        }
        currentAnswers = new ArrayList<>();
        for (int i = 0; i < numberAnswers; i++) {
            currentAnswers.add(buttonList.get(i).getText().toString().toUpperCase());
        }

        if (activity.promptingActive && child.getPrompting() != null) {
            if (child.getPrompting().getPromptingStrategy().equals("ALWAYS")) {
                if (child.getPrompting().getPromptingColor()) {
                    for (Button b : rightAnswerButtons) {
                        Prompting.setButtonColor(activity, b);
                    }
                }
                if (child.getPrompting().getPromptingSize()) {
                    for (Button b : rightAnswerButtons) {
                        Prompting.setSizeText(activity, b);
                    }
                }
                if (child.getPrompting().getPromptingScratch()) {
                    for (Button v : buttonList) {
                        if (!rightAnswerButtons.contains(v)) {
                            Prompting.scratchText(activity, v);
                        }
                    }
                }
                if (child.getPrompting().getPromptingRead()) {
                    Prompting.readAnswers(currentAnswers);
                }
                else{
                    activity.readWithOrWithoutEmotion(child, "Tenta outra vez.");
                }
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
