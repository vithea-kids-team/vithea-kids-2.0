package com.l2f.vitheakids;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.l2f.vitheakids.Storage.ImageStorage;
import com.l2f.vitheakids.model.Answer;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.Exercise;
import com.l2f.vitheakids.model.ExerciseLogInfo;
import com.l2f.vitheakids.model.PersonalMessage;
import com.l2f.vitheakids.model.Resource;
import com.l2f.vitheakids.model.SequenceExercises;
import com.l2f.vitheakids.model.SequenceLogInfo;
import com.l2f.vitheakids.rest.FetchChildInfo;
import com.l2f.vitheakids.rest.FetchChildLogin;
import com.l2f.vitheakids.rest.SendLogs;
import com.l2f.vitheakids.task.LoadImageTask;
import com.l2f.vitheakids.task.ReadTask;
import com.l2f.vitheakids.task.StopRead;
import com.l2f.vitheakids.util.ExerciseMenuListWithoutImageAdapter;
import com.l2f.vitheakids.util.CanvasUtil;
import com.l2f.vitheakids.util.Prompting;
import com.l2f.vitheakids.util.TaskListener;
import com.unity3d.player.*;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class VitheaKidsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, TaskListener {

    private String TAG = VitheaKidsActivity.class.getSimpleName();

// **** DECLARATIONS *******************************************************************************

    // Unity stuff
    private UnityPlayer mUnityPlayer;

    // Layout stuff
    private LayoutInflater linflater;
    private ListView lv;
    private BaseAdapter mAdapter;

    // Bundle info
    public String userName;
    public String password;
    private String greetingMsg;
    private String exListMsg;
    public Child child = null;
    private ExerciseLogInfo currentExerciseLogInfo;
    private SequenceLogInfo currentSequenceLogInfo;

    // Exercises
    private long currentSequenceId;
    private int currentExercisePosition;
    private int attempts = 0;
    private boolean skipped = false;
    private boolean correctAnswer = false;
    private boolean inExercise;
    private boolean inHomeScreen = true;
    private boolean inSequenceScreen;
    public boolean promptingActive;
    public boolean reinforcementActive;
    private List<String> currentAnswers;
    private Button rightAnswerButton;
    private String lastInstruction;
    private List<Exercise> exercises;

    private View characterContainer;
    private ActionBar actionBar;
    private ImageView rightAnswer;

    private ImageStorage imageStorage;

    private String seqName;

    private Exercise currentExercise;

    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 0;

    private static final String SEQUENCE_LOG_TAG = "sequence_log";

    Logger logger;
    // *************************************************************************************************

    public void initActivePrompting(Boolean state){
        promptingActive = state;
    }
    public void initActiveReinforcement(Boolean state){
        reinforcementActive = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkStoragePermission();
        logger = LogHelper.getLogger( "VitheaKidsActivity" );

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        imageStorage = (ImageStorage) getApplication();


        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy
        setContentView(R.layout.main);

        initUnityCharacter();   // conf unity player
        initViews();            // conf views and layouts

        new FetchChildInfo(VitheaKidsActivity.this).execute();

    }

    @Override
    protected void onStop(){

        super.onStop();
        finish();
    }

    private void initViews() {

        characterContainer = findViewById(R.id.characterContainer);

        // Orientations
        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_PORTRAIT) {
            int height = (int) (CanvasUtil.getHeight(getWindowManager().getDefaultDisplay()) * 0.45);
            characterContainer.getLayoutParams().height = height;
        } else {
            characterContainer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        // Unity layout
        FrameLayout layout = (FrameLayout) findViewById(R.id.view1);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View view =  mUnityPlayer.getView();
        layout.addView(view, 0, lp);



    }
    private void initUnityCharacter() {
        mUnityPlayer = new UnityPlayer(this);
        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
        boolean trueColor8888 = false;
        mUnityPlayer.init(glesMode, trueColor8888);
    }
    private void setupActionBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        actionBar.show();
    }
    private void hideActionBar() {
        actionBar.hide();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }
    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
    }
    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }
    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        checkStoragePermission();
        mUnityPlayer.resume();
    }
    // Low Memory Unity
   @Override
    public void onLowMemory() {
        super.onLowMemory();
       // mUnityPlayer.lowMemory();
    }
    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            //mUnityPlayer.lowMemory();
        }
    }
    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }
    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }
    // For some reason the multiple key event type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override
   public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }
    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { return mUnityPlayer.injectEvent(event); }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }
    /*API12*//*
    public boolean onGenericMotionEvent(MotionEvent event) { return mUnityPlayer.injectEvent(event); }
*/
// **** View setters - methods that change the view ************************************************

    public void setSequenceSelectionView() {
        Log.d("entreiEKLECTION", "setSequenceSelectionView called");

        setupActionBar(getString(R.string.sequences_of_exercise));

        Log.d(TAG, "setSequenceSelectionView called");

        inHomeScreen = false;
        inSequenceScreen = true;

        // Clear rightFrameLayout
        LinearLayout rightFrameLayout = (LinearLayout) findViewById(R.id.rightFrame);
        rightFrameLayout.removeAllViews();

        ViewGroup.LayoutParams layoutParams = rightFrameLayout.getLayoutParams();
        layoutParams.height = CanvasUtil.getHeight(getWindowManager().getDefaultDisplay());

        linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Inflate view
        LinearLayout  view = (LinearLayout ) linflater.inflate(R.layout.exercise_menu_layout, null);
        rightFrameLayout.addView(view);
        Log.d("DEBUG", "list view by inflater" + view);

        // Setup adapter
        lv = (ListView) findViewById(R.id.list_by_type);            // exercise_menu_layout
        mAdapter = new ExerciseMenuListWithoutImageAdapter(this);   // util folder

        // Load data
        if(this.child.getSequencesList().size() == 0){
            TextView textView = new TextView(this.getApplicationContext());
            textView.setText(R.string.noClasses);
            textView.setTextColor(Color.BLACK);
            LayoutParams par = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
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
        final VitheaKidsActivity  act = this;
        // Setup onClickListener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, position + " " + id);
                SequenceExercises seq = child.getSequencesList().get(position);
                currentSequenceId = seq.getSequenceId();

                if(child.getSequenceExercisesPreferences().getSequenceExercisesOrder().equals("RANDOM")){
                    Log.d("shuffleEnable", "true");
                    Collections.shuffle(seq.getSequenceExercises());
                }

                exercises = seq.getSequenceExercises();
                currentExercisePosition = 0;

                initNewSequenceLogInfo();

                inExercise = true;          // Start exercises
                inHomeScreen = false;       // Not in home screen
                inSequenceScreen = false;   // Not in sequence screen

                seqName = seq.getName();

                List<Resource> resources = child.getAllResourcesBySeqPosition(position);
                new LoadImageTask(resources,seq.getName(),act).execute();

                         // Draw view exercise
            }
        });

    }

    LinearLayout exerciseView;
    View exerciseLong;//view with option that should be redrawn
    View imageOptionsView;
    public void setExerciseView() {
        exerciseLong = null; //
        imageOptionsView =null;
        //stop character talk
        new StopRead().execute();

        setupActionBar(getString(R.string.exercise));

        // trick because of the next button
        inExercise = true;

        // Clear rightFrameLayout
        LinearLayout rightFrameLayout = (LinearLayout) findViewById(R.id.rightFrame);
        rightFrameLayout.removeAllViews();

        ViewGroup.LayoutParams layoutParams = rightFrameLayout.getLayoutParams();
        layoutParams.height = CanvasUtil.getHeight(getWindowManager().getDefaultDisplay());
        linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ScrollView scroll_view = (ScrollView) linflater.inflate(R.layout.scroll, null);
        rightFrameLayout.addView(scroll_view);
        LinearLayout container = (LinearLayout) findViewById(R.id.scroll_container);

        // Set exercise info
        if (child != null) {
            currentExercise = exercises.get(currentExercisePosition);

            // Layout
            exerciseView = (LinearLayout) linflater.inflate(R.layout.main_exercise, null);
            container.addView(exerciseView);

            // Question
            TextView questionText = (TextView) findViewById(R.id.questionText);
            if (!child.getSequenceExercisesPreferences().getSequenceExerciseCapitalization().equals("DEFAULT")) {
                questionText.setText(currentExercise.getQuestion().getQuestionDescription().toUpperCase());
            } else {
                questionText.setText(currentExercise.getQuestion().getQuestionDescription());
            }

            // TODO TYPE OF EXERCISES - in the future factory?
            if (currentExercise.getType().equals("TEXT")) {
                setSimpleMultipleChoiceExerciseView(currentExercise, exerciseView);   //text
            } else if (currentExercise.getType().equals("IMAGE")) {
                setImageMultipleChoiceExerciseView(currentExercise, exerciseView);    // image
            }

            // Exercises navigation
            View navigationView = linflater.inflate(R.layout.navigation_view, null);
            exerciseView.addView(navigationView);

            // Send to Animated Character
            readWithOrWithoutEmotion(child, currentExercise.getQuestion().getQuestionDescription());
            lastInstruction = currentExercise.getQuestion().getQuestionDescription();
/*
            ImageView repeat_view = (ImageView) findViewById(R.id.repeat_button_view);

            repeat_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                   // Toast.makeText(getApplicationContext(), lastInstruction, Toast.LENGTH_SHORT).show();
                    readWithOrWithoutEmotion(child, lastInstruction);
                }
            });*/
        }
    }
    /**
     * One image with four button options
     * @param exercise
     * @param container
     */
    public void setSimpleMultipleChoiceExerciseView(Exercise exercise, LinearLayout container)  {
        rightAnswerButton = null;

        if(exerciseLong!=null){
          ViewGroup parent = (ViewGroup) exerciseLong.getParent();
          parent.removeView(exerciseLong);
        }

        exerciseLong = (View) linflater.inflate(R.layout.exercise_long_options_layout, null);
        container.addView(exerciseLong);

        // Stimulus
        Resource stimulus = exercise.getQuestion().getStimulus();


        if (stimulus!=(null)) {

            byte[] image  = imageStorage.getImage(seqName,stimulus.getResourceId());
            ImageView img = (ImageView) findViewById(R.id.stimulusImage);
            setImageOfView(img,image);
        }
         else {
            ImageView img = (ImageView) findViewById(R.id.stimulusImage);
            img.setVisibility(View.GONE);
            LinearLayout layout = (LinearLayout) findViewById(R.id.layoutButtonText);
            //change top margin
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout.getLayoutParams();
            params.setMargins(0, 30, 0, 0);
            layout.setLayoutParams(params);        }

        // Exercise Options
        //  shuffle exerciseOptions
        if (!exercise.getAnswers().isEmpty()) {

            List<Answer> answers = exercise.getAnswers();
            if(exerciseLong==null) {
                Collections.shuffle(answers);
                exercise.setAnswers(answers);
            }
            List<Integer> idButtons = new ArrayList<>(Arrays.asList(R.id.button1, R.id.button2, R.id.button3, R.id.button4));
            List<Button> buttonList = new ArrayList<>();

            int numberAnswers = answers.size();
            if (numberAnswers > 4) numberAnswers = 4;

            for (int i = 0; i < numberAnswers; i++) {
                Button btn = (Button) findViewById(idButtons.get(i));
                Prompting.makeSelector(this, R.color.exercise_button_color);
                Answer answer = answers.get(i);
                if (!child.getSequenceExercisesPreferences().getSequenceExerciseCapitalization().equals("DEFAULT")) {
                    btn.setText(answer.getAnswerDescription().toUpperCase());
                } else {
                    btn.setText(answer.getAnswerDescription());
                }
                btn.setVisibility(View.VISIBLE);

                if (answer.isRightAnswer()) { //right answer
                    btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            rightAnswerHandler(v, child);
                        }
                    });
                    rightAnswerButton = btn;

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

            // Prompting
            // TODO REVIEW
            if (promptingActive && child.getPrompting() != null) {
                if (child.getPrompting().getPromptingStrategy().equals("ALWAYS")) {
                    if (child.getPrompting().getPromptingColor()) {
                        Prompting.setButtonColor(this.getApplicationContext(), rightAnswerButton);
                    }
                    if (child.getPrompting().getPromptingSize()) {
                        Prompting.setSizeText(this.getApplicationContext(),rightAnswerButton);
                    }
                    if (child.getPrompting().getPromptingScratch()) {
                        for (Button v : buttonList) {
                            if (v != rightAnswerButton) {
                                Prompting.scratchText(this.getApplicationContext(),v);
                            }
                        }
                    }
                    if (child.getPrompting().getPromptingRead()) {
                        Prompting.readAnswers(currentAnswers);
                    }
                }
            }
        }
    }



    /***}
     * Multiple images
     * @param exercise
     * @param container
     */
    private void setImageMultipleChoiceExerciseView(Exercise exercise, LinearLayout container)  {

        if(imageOptionsView!=null){
            Log.d("clean options", "clean");
            ViewGroup parent = (ViewGroup) imageOptionsView.getParent();
            parent.removeView(imageOptionsView);
        }
        imageOptionsView = (View) linflater.inflate(R.layout.exercise_multiple_images, null);
        container.addView(imageOptionsView);

        // stimulus
        TextView stimulusText = (TextView) findViewById(R.id.stimulusText);
        String stimulusTextContent = exercise.getQuestion().getStimulusText();

        if (!child.getSequenceExercisesPreferences().getSequenceExerciseCapitalization().equals("DEFAULT")) {
            stimulusText.setText(stimulusTextContent == null ? "" : stimulusTextContent.toUpperCase());
        } else {
            stimulusText.setText(stimulusTextContent == null ? "" : stimulusTextContent.toUpperCase());

        }

        // Stimulus


        // TODO REVIEW number answers
        List<Answer> answers = exercise.getAnswers();
        List<Integer> idImageViews = new ArrayList<>(Arrays.asList(R.id.first, R.id.second, R.id.third, R.id.four));
        List<ImageView> optionsList = new ArrayList<>();

        if (!answers.isEmpty()) {
            if(imageOptionsView==null) {
                Collections.shuffle(answers);
                exercise.setAnswers(answers);
            }
            int numberAnswers = answers.size();

            // FIXME Verify if will be possible to have more than 4 answers
            if(numberAnswers > 4) numberAnswers = 4;



            for (int i = 0; i < numberAnswers; i++) {
                ImageView option = (ImageView) findViewById(idImageViews.get(i));
                Answer answer = answers.get(i);
                Resource answerImage = exercise.getAnswers().get(i).getStimulus();
                String path = answerImage != null ? answerImage.getResourcePath() : "";

                if (!path.isEmpty()) {
                    String domain = getString(R.string.resources_addr_kids);
                    path = domain + path;

                    byte[] bitmap = imageStorage.getImage(seqName, answerImage.getResourceId());
                    setImageOfView(option,bitmap);
                }

                option.setVisibility(View.VISIBLE);

                if (answer.isRightAnswer()) {
                    option.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            rightAnswerHandler(v, child);


                        }
                    });
                    rightAnswer=option;



                } else {
                    option.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            distractorHandler(v);

                        }
                    });
                }
                optionsList.add(option);
            }
        }

        // TODO Prompting


        // TODO REVIEW
        if (promptingActive && child.getPrompting() != null) {
            if (child.getPrompting().getPromptingStrategy().equals("ALWAYS")) {
               if (child.getPrompting().getPromptingColor()) {
                   Prompting.setImageColor(this.getApplicationContext(), rightAnswer);
              }
               if (child.getPrompting().getPromptingSize()) {
                   Prompting.setImageSize(this.getApplicationContext(), rightAnswer);
               }

                if (child.getPrompting().getPromptingScratch()) {
                    for (ImageView v : optionsList) {
                        if (v != rightAnswer) {
                            Prompting.scratchImage(this.getApplicationContext(),v);
                        }
                    }
                }
            }
        }
    }

    public void setReinforcementView() {
        new StopRead().execute();//stop character talk and also  clean the text variable on unity

        Resource res;
        //// FIXME: 18/10/2017 
        try {
            res = child.getReinforcement().getReinforcementResource();
        } catch (NullPointerException e) {
            res = null;
        }

        if (reinforcementActive && res!=null) {
            hideActionBar(); // TODO maybe not hide?


            playMessage(child, "EXERCISE_REINFORCEMENT");

            // Clear rightFrameLayout
            LinearLayout rightFrameLayout = (LinearLayout) findViewById(R.id.rightFrame);
            rightFrameLayout.removeAllViews();

            ViewGroup.LayoutParams layoutParams = rightFrameLayout.getLayoutParams();
            layoutParams.height = CanvasUtil.getHeight(getWindowManager().getDefaultDisplay());
            linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ScrollView scroll_view = (ScrollView) linflater.inflate(R.layout.scroll, null);
            rightFrameLayout.addView(scroll_view);


            View ref_view = (View) linflater.inflate(R.layout.reinforcement_view, null);
            LinearLayout container = (LinearLayout) findViewById(R.id.scroll_container);
            container.addView(ref_view);


            ImageView image = (ImageView) findViewById(R.id.reinforcement_image);
            byte[] bitmap = imageStorage.getImage(seqName, res.getResourceId());
            Log.d("reinforcement", Integer.toString(bitmap.length));
            //setImageOfView(image,bitmap);
            Glide.with(VitheaKidsActivity.this).load(bitmap).apply(RequestOptions.skipMemoryCacheOf(true)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(image);

            image.setVisibility(View.VISIBLE);
            // equivalent to next button
            ImageButton closeReinforcement = (ImageButton) findViewById(R.id.closeReinforcement);
            closeReinforcement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextExerciseHandler();
                }
            });
        }
    }
    // last screen when show basic stats
    public void setFinalResultsView() {

        // Clear rightFrameLayout
        LinearLayout rightFrameLayout = (LinearLayout) findViewById(R.id.rightFrame);
        rightFrameLayout.removeAllViews();

        android.widget.RelativeLayout.LayoutParams para = (android.widget.RelativeLayout.LayoutParams) rightFrameLayout.getLayoutParams();
        para.height = CanvasUtil.getHeight(getWindowManager().getDefaultDisplay());

        linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RelativeLayout results_view = (RelativeLayout) linflater.inflate(R.layout.end_exercises, null);

        ScrollView scroll_view = (ScrollView) linflater.inflate(R.layout.scroll, null);
        rightFrameLayout.addView(scroll_view);

        LinearLayout container = (LinearLayout) findViewById(R.id.scroll_container);
        container.addView(results_view);

        TextView totalViewResult = (TextView) findViewById(R.id.totalViewResult);
        //totalViewResult.setText(Integer.valueOf(currentSequenceLogInfo.getNumberOfExercises()).toString());

        TextView skippedViewResult = (TextView) findViewById(R.id.skippedViewResult);
        //skippedViewResult.setText(Integer.valueOf(currentSequenceLogInfo.getSkippedCount()).toString());

        TextView correctViewResult = (TextView) findViewById(R.id.correctViewResult);
        //correctViewResult.setText(Integer.valueOf(currentSequenceLogInfo.getCorrectExercises()).toString());

        //Float distractorAvg = Float.valueOf(currentSequenceLogInfo.getDistractorHitsAvg());
        TextView distractorViewResult = (TextView) findViewById(R.id.distractorViewResult);
        //distractorViewResult.setText(String.format("%.2f", distractorAvg).toString());

    }
    public void setNavigationView() {

        ImageButton next = (ImageButton) findViewById(R.id.nextExerciseButton);
        TextView nextText = (TextView) findViewById(R.id.nextExerciseTextView);
        ImageButton previous = (ImageButton) findViewById(R.id.previousExerciseButton);
        TextView previousText = (TextView) findViewById(R.id.previousExerciseTextView);
        ImageButton end = (ImageButton) findViewById(R.id.endExerciseButton);
        TextView endText = (TextView) findViewById(R.id.endExerciseTextView);

        Boolean isLastExercise = currentExercisePosition == exercises.size() - 1;
        Log.d("POSICAO DO EXERCICIO",Integer.toString(currentExercisePosition) );

        if (isLastExercise && next != null) {
            next.setVisibility(View.INVISIBLE);
            nextText.setVisibility(View.INVISIBLE);
            end.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    skipped = true;
                    endHandler();
                }
            });
        } else {
            Log.d("PROXIMO",Integer.toString(currentExercisePosition) );
            next.setVisibility(View.VISIBLE);
            end.setVisibility(View.INVISIBLE);
            endText.setVisibility(View.INVISIBLE);
            next.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    skipped = true;
                    nextExerciseHandler();
                }
            });
        }

        Boolean isFirstExercise = currentExercisePosition == 0;
        if (isFirstExercise && previous != null) {
            previous.setVisibility(View.INVISIBLE);
            previousText.setVisibility(View.INVISIBLE);
        } else {
            previous.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    previousExerciseHandler();
                }
            });
        }
    }

//**** Region Handlers - what happens when you do some actions ************************************

    protected void rightAnswerHandler(View v, Child child) {
        inExercise = false;
        correctAnswer = true;
        playMessage(child, "EXERCISE_REINFORCEMENT"); // TODO Only reinforcement?
        if(reinforcementActive) playReinforcement(child);
        else nextExerciseHandler();
        attempts = 0;
    }

    private void playReinforcement(Child child) { // TODO child.getReinforcement() - efficiency
        switch (child.getReinforcement().getReinforcementStrategy()) {
            case "ALWAYS":
                setReinforcementView();
                break;
            case "AT_FIRST":
                if(attempts == 0) {
                    setReinforcementView();
                }
                else nextExerciseHandler();
                break;
            case "OFF": // TODO OFF Reinforcement
            default:
                nextExerciseHandler();
                break;
        }
    }
    public void playMessage(Child child, String exercise_reinforcement) {
        //Exercise reinforcement
        List<PersonalMessage> personalMessages = child.getPersonalMessageList();
        String sentence = "";
        for (PersonalMessage message : personalMessages) {
            if (message.getMessageType().equals(exercise_reinforcement)) {
                sentence = message.getMessage();
                Log.d("greetings", sentence);
                break;
            }
        }

        // if generic, must check if it should say it worth joy or sadness
        if (!sentence.isEmpty()) {
            readWithOrWithoutEmotion(child,sentence);
            lastInstruction = sentence;
        }
    }
    protected void distractorHandler(View v) {
        attempts++;

        // TODO REVIEW AND REFACTOR PROMPTING
        // Prompting
        if (promptingActive && child.getPrompting() != null ) {
            Exercise exercise = exercises.get(currentExercisePosition);
            Boolean isTextExercise = exercise.getType().equals("TEXT");
            Boolean isImageExercise = exercise.getType().equals("IMAGE");

            if (child.getPrompting().getPromptingStrategy().equals("IF_NEEDED") || child.getPrompting().getPromptingStrategy().equals("ALWAYS")) {
                if (child.getPrompting().getPromptingHide()) {
                    v.setVisibility(View.INVISIBLE);
                    if(isImageExercise){
                        FrameLayout fr = (FrameLayout) v.getParent();
                        ImageView im  = (ImageView) fr.getChildAt(1);
                        im.setVisibility(View.INVISIBLE);
                    }
                    if(isTextExercise){
                        currentAnswers.remove(((Button) v).getText().toString().toUpperCase());}
                }
                else {
                    if (child.getPrompting().getPromptingScratch() && isTextExercise) {
                        Prompting.scratchText(this.getApplicationContext(), (Button) v);
                    }

                    if (child.getPrompting().getPromptingScratch() && isImageExercise){
                        Prompting.scratchImage(this.getApplicationContext(), (ImageView) v);
                    }
                }
                if (child.getPrompting().getPromptingColor() && isTextExercise) {
                    Prompting.setButtonColor(this.getApplicationContext(), (Button) rightAnswerButton);
                }

                if (child.getPrompting().getPromptingColor() && isImageExercise) {
                   Prompting.setImageColor(this.getApplicationContext(),rightAnswer);
                }
                if(child.getPrompting().getPromptingSize() && isTextExercise) {
                    Prompting.setSizeText(this.getApplicationContext(), rightAnswerButton);
                }

                if(child.getPrompting().getPromptingSize() && isImageExercise) {
                    Prompting.setImageSize(this.getApplicationContext(), rightAnswer);
                }


                if(child.getPrompting().getPromptingRead() && isTextExercise){
                        Prompting.readAnswers(currentAnswers);

                }
                else {
                    readWithOrWithoutEmotion(child, "Tenta outra vez.");
                }
            }
        }
    }
    private void previousExerciseHandler() {
        this.currentExercisePosition--;
        initNewExerciseLogInfo();
        inExercise = true;
        setExerciseView();
        setNavigationView();
    }
    protected void nextExerciseHandler() {
        logExercise();

        this.currentExercisePosition++;
        Boolean hasFinished = currentExercisePosition >= exercises.size();

        if (!hasFinished) {
            initNewExerciseLogInfo();

            inExercise = true;
            setExerciseView();
            setNavigationView();
        } else {
            endHandler();
        }
    }

    protected void endHandler() {
        // TODO Send logger and show results
        //new SendLogs(this, currentSequenceLog).execute();
        //setFinalResultsView();

        if (skipped) logExercise();

        logSequence();

        //Sequence reinforcement
        if(reinforcementActive) playMessage(child, "SEQUENCE_REINFORCEMENT");

        setSequenceSelectionView();
    }
    protected void homeHandler() {
        setSequenceSelectionView();


    }

    protected  void backMenuHandler(){
        CustomDialogClass cdd=new CustomDialogClass(this);
        cdd.show();

    }

    // logout
    // TODO Verify why sometimes new child inherit stuff from the last child
    private void exitHandler(){
        this.child = null;
        SharedPreferences settings = this.getSharedPreferences(getString(R.string.APP_PREFERENCES), MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.clear();
        prefEditor.commit();
        //Thread.sleep(10000);
        Intent i = new Intent(VitheaKidsActivity.this, LoginActivity.class);
            // Add new Flag to start new Activity
        startActivity(i);
    }

    private void reinforcementHandler(MenuItem item){
        reinforcementActive = !(reinforcementActive);
        if(reinforcementActive){
            item.setTitle("Desligar Reforço ");
        }
        else
            item.setTitle("Ligar Reforço");

        if(!inSequenceScreen){
           // setExerciseView();
           // setNavigationView();
        }
    }
    private void promptingHandler(MenuItem item){
        promptingActive = !(promptingActive);
        if(promptingActive){
            item.setTitle("Desligar Ajuda");
        }
        else item.setTitle("Ligar ajuda");

        Log.e("home", "" + inHomeScreen);
        Log.e("sequence", "" + inSequenceScreen);

        if(!inSequenceScreen){
           if(currentExercise.getType().equals("TEXT")){
               setSimpleMultipleChoiceExerciseView(currentExercise,exerciseView); //redraw buttons to clean all prompting types applied
           }

            if(currentExercise.getType().equals("IMAGE")){
               setImageMultipleChoiceExerciseView(currentExercise,exerciseView); //redraw buttons to clean all prompting types applied
            }
           // setNavigationView();
        }
    }

// **** Menu ***************************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem itemPrompting = menu.findItem(R.id.prompting_option);
        MenuItem itemReinforcement = menu.findItem(R.id.reinforcement_option);

        if(promptingActive) itemPrompting.setTitle("Desligar Ajuda");
        else itemPrompting.setTitle("Ligar Ajuda");
        if(reinforcementActive) itemReinforcement.setTitle("Desligar Reforço");
        else itemReinforcement.setTitle("Ligar Reforço");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (!inHomeScreen) {
            if (!inSequenceScreen && id == R.id.end_option) {
                endHandler();
                //return true;
            } else if (id == R.id.exit) {
                homeHandler();
            }
        }
        if (id == R.id.exit) {
            exitHandler();
        }
        if(id == R.id.back_menu_option){
            backMenuHandler();
        }
        else if(id == R.id.prompting_option){
            promptingHandler(item);
        }
        else if(id == R.id.reinforcement_option){
            reinforcementHandler(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.end_option);
        MenuItem itemBack = menu.findItem(R.id.back_menu_option);

        if (!inSequenceScreen) {
            item.setEnabled(true);
            itemBack.setEnabled(true);
        } else {
            // disabled
            item.setEnabled(false);
            itemBack.setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Generates a new drawable with a new color
     * @param color
     * @return StateListDrawable
     */

    public  StateListDrawable makeSelector(int color) {

        StateListDrawable stateListDrawable = new StateListDrawable();
        Resources res = this.getApplicationContext().getResources();
        Drawable buttonSelected = res.getDrawable(R.drawable.buttonselected);
        GradientDrawable  buttonNormal = (GradientDrawable) res.getDrawable(R.drawable.bottonnormal);
        buttonNormal.setColor(color);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, buttonSelected );
        stateListDrawable.addState(new int[]{}, buttonNormal);

        return stateListDrawable;
    }

    /**
     * readWithOrWithoutEmotion
     * @param sentence
     * Verify if the text has to be pronounced with emotion
     * Launch a task to read the text
     */
    public void readWithOrWithoutEmotion(Child child, String sentence){

        if (child.getEmotions()) {
            new ReadTask().execute(sentence, "joy");
        } else {
            new ReadTask().execute(sentence);
        }

    }

    public void setImageOfView(ImageView  imageView , byte[] bytes){
        Glide.get(this.getApplicationContext()).clearMemory();
        Glide.with(this.getApplicationContext()).load(bytes).apply(RequestOptions.skipMemoryCacheOf(true)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).into(imageView);
    }


    /**
     * Logs Methods
     */
    private void initNewExerciseLogInfo() {
        correctAnswer = false;
        skipped = false;
        long childID = child.getChildId();
        long exerciseID = exercises.get(currentExercisePosition).getId();
        String promptingStrategy = child.getPrompting().getPromptingStrategy();
        String reinforcementStrategy = child.getReinforcement().getReinforcementStrategy();

        currentExerciseLogInfo = new ExerciseLogInfo(childID, exerciseID, promptingStrategy, reinforcementStrategy);
    }

    private void initNewSequenceLogInfo() {
        long childID = child.getChildId();
        long sequenceID = currentSequenceId;
        int numberOfExercises = child.getSequenceBySequenceID(sequenceID).getNumberOfExercises();

        currentSequenceLogInfo = new SequenceLogInfo(childID, sequenceID, numberOfExercises);

        initNewExerciseLogInfo();
    }

    private void logExercise() {

        //Log object parameters
        int numberOfWrongAttempts = attempts;

        // log() also adds the exerciseLogInfo to the current sequenceLogInfo
        currentExerciseLogInfo.log(numberOfWrongAttempts, correctAnswer, skipped, currentSequenceLogInfo, currentExercisePosition);

    }

    private void logSequence() {
        //Log to file in the Android device
        String sequenceLogInfoJson = currentSequenceLogInfo.log();

        //Log.i("SEQUENCE_LOG", sequenceLogInfoJson);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(SEQUENCE_LOG_TAG, sequenceLogInfoJson);

        //TODO Endpoint
        final String url = getString(R.string.ws_uri) + getString(R.string.child_sequence_uri);

        //Send Logs to Server
        new SendLogs(body, this, url, this).execute();

    }

    /** Storage Permission Methods **/

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_EXTERNAL_STORAGE) {
            // Request for write external storage permission.
            if (grantResults.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Permission request was denied.
                requestStoragePermission();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void checkStoragePermission() {
        // Check if the Storage permission has been granted
        if (ActivityCompat.checkSelfPermission(VitheaKidsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // if was not granted
            requestStoragePermission();
        }
    }

    /**
     * Requests the {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * an AlertDialog that includes additional information.
     */
    private void requestStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a AlertDialog with a button to request the missing permission.
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Permissões");
            dialogBuilder.setMessage("É necessária a permissão de armazenamento para a realização de testes.");
            dialogBuilder.setCancelable(true);
            dialogBuilder.setNeutralButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            // Request the permission
                            ActivityCompat.requestPermissions(VitheaKidsActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_REQUEST_EXTERNAL_STORAGE);
                        }
                    });

            AlertDialog permissionAlertDialog = dialogBuilder.create();
            permissionAlertDialog.show();

        } else {
            //Permission is not available. Requesting storage permission.

            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(VitheaKidsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**  TaskListener Methods (for SendLogs task) **/
    @Override
    public void onTaskStarted() {
        //TODO
    }

    @Override
    public void onTaskFinished(ResponseEntity<String> response) {
        //TODO
    }
}

