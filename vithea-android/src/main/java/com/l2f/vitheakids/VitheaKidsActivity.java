package com.l2f.vitheakids;

import com.l2f.vitheakids.model.Answer;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.Exercise;
import com.l2f.vitheakids.model.ExerciseLogInfo;
import com.l2f.vitheakids.model.PersonalMessage;
import com.l2f.vitheakids.model.Resource;
import com.l2f.vitheakids.model.SequenceExercises;
import com.l2f.vitheakids.model.SequenceLogInfo;
import com.l2f.vitheakids.rest.FetchChildInfo;
import com.l2f.vitheakids.task.LoadImageTask;
import com.l2f.vitheakids.task.ReadTask;
import com.l2f.vitheakids.util.ExerciseMenuListWithoutImageAdapter;
import com.l2f.vitheakids.util.CanvasUtil;
import com.unity3d.player.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
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

public class VitheaKidsActivity extends AppCompatActivity {
    private String TAG = VitheaKidsActivity.class.getSimpleName();

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
    private ExerciseLogInfo currentExerciseLog;
    private SequenceLogInfo currentSequenceLog;

    // Exercises
    private long currentSequenceId;
    private int currentExercisePosition;
    private int attempts = 0;
    private boolean inExercise;
    private boolean inHomeScreen;
    private boolean inSequenceScreen;
    private boolean promptingActive;
    private boolean reinforcementActive;
    private List<String> currentAnswers;
    private Button rightAnswerButton;
    private String lastInstruction;
    private List<Exercise> exercises;

    private View characterContainer;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

        setContentView(R.layout.main);

        init();
        initViews();

        new FetchChildInfo(VitheaKidsActivity.this).execute();
    }

    private void initViews() {

        characterContainer = findViewById(R.id.characterContainer);

        if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_PORTRAIT) {
            int height = (int) (CanvasUtil.getHeight(getWindowManager().getDefaultDisplay()) * 0.45);
            characterContainer.getLayoutParams().height = height;
        } else {
            characterContainer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        // Unity layout
        FrameLayout layout = (FrameLayout) findViewById(R.id.view1);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.addView(mUnityPlayer.getView(), 0, lp);
    }

    private void init() {
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
        mUnityPlayer.resume();
    }

    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
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

    // For some reason the multiple keyevent type is not supported by the ndk.
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    /*API12*/
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mUnityPlayer.injectEvent(event);
    }

    //endregion

    //
    //region View setters - methods that change the view
    //

    public void setSequenceSelectionView() {

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
        View view = (View) linflater.inflate(R.layout.exercise_menu_layout, null);
        rightFrameLayout.addView(view);
        Log.d("DEBUG", "list view by inflater" + view);


        // Setup adapter
        lv = (ListView) findViewById(R.id.list_by_type); //exercise_menu_layout
        mAdapter = new ExerciseMenuListWithoutImageAdapter(this); //util folder

        //load data
        int i = 0;
        for (SequenceExercises sInfo : this.child.getSequencesList()) {
            Log.d("sequenceName", sInfo.getName());
            //shows only sequences with exercises
            if (sInfo.getSequenceExercises().size() > 0) {
                ((ExerciseMenuListWithoutImageAdapter) mAdapter).add(sInfo.getName());
            }
            i++;
        }

        lv.setAdapter(mAdapter);


        // Setup onClickListener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, position + " " + id);
                SequenceExercises seq = child.getSequencesList().get(position);
                currentSequenceId = seq.getSequenceId();
                exercises = seq.getSequenceExercises();
                currentExercisePosition = 0;
                inExercise = true;
                inHomeScreen = false;
                inSequenceScreen = false;

                setExerciseView();
                setNavigationView();

            }
        });

    }

    public void setExerciseView() {

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
            Exercise exercise = exercises.get(currentExercisePosition);

            if (exercise.getType().equals("TEXT")) {
                setSimpleMultipleChoiceExerciseView(exercise, container);
            } else if (exercise.getType().equals("IMAGE")) {
                setImageMultipleChoiceExerciseView(exercise, container);
            }

            //exercises navigation
            View navigationView = linflater.inflate(R.layout.navigation_view, null);
            container.addView(navigationView);

            // Send to Catarina
            new ReadTask().execute(exercise.getQuestion().getQuestionDescription());
            lastInstruction = exercise.getQuestion().getQuestionDescription();
        }
    }

    /***
     * Multiple images
     * @param exercise
     * @param container
     */
    private void setImageMultipleChoiceExerciseView(Exercise exercise, LinearLayout container) {
        View ex_view = (View) linflater.inflate(R.layout.exercise_multiple_images, null);
        container.addView(ex_view);

        // Question
        TextView questionText = (TextView) findViewById(R.id.questionText);
        questionText.setText(exercise.getQuestion().getQuestionDescription());

        // Stimulus
        TextView stimulusText = (TextView) findViewById(R.id.stimulusText);
        String stimulusTextContent = exercise.getQuestion().getStimulusText();
        stimulusText.setText(stimulusTextContent == null ? "" : stimulusTextContent);

        ImageView first = (ImageView) findViewById(R.id.first);
        ImageView second = (ImageView) findViewById(R.id.second);
        ImageView thirst = (ImageView) findViewById(R.id.thirst);
        ImageView four = (ImageView) findViewById(R.id.four);

        List<ImageView> optionsList = new ArrayList<ImageView>(Arrays.asList(first, second, thirst, four));

        Collections.shuffle(optionsList);

        // Options
        List<Answer> answers = exercise.getAnswers();
        if (!answers.isEmpty()) {
            for (int i = 0; i < answers.size() && i < 4; i++) {
                ImageView option = optionsList.get(i);
                Answer answer = answers.get(i);

                Resource answerImage = exercise.getAnswers().get(i).getStimulus();
                String path = answerImage != null ? answerImage.getResourcePath() : "";

                if (!path.isEmpty()) {

                    String domain = getString(R.string.resources_addr_kids);
                    path = domain + path;

                    new LoadImageTask(this, option).execute(path);

                }

                option.setVisibility(View.VISIBLE);

                if (answer.getAnswerId() == exercise.getRightAnswer().getAnswerId()) {
                    //right answer
                    option.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            rightAnswerHandler(v, child);
                        }
                    });
                } else {
                    option.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            distractorHandler(v);
                        }
                    });
                }
            }
        }
    }

    /***
     * One image with four button options
     * @param exercise
     * @param container
     */

    public void setSimpleMultipleChoiceExerciseView(Exercise exercise, LinearLayout container) {
        View ex_view = (View) linflater.inflate(R.layout.exercise_words_layout, null);
        container.addView(ex_view);

        // Question
        TextView questionText = (TextView) findViewById(R.id.questionText);
        questionText.setText(exercise.getQuestion().getQuestionDescription());

        // Stimulus
        Resource stimulus = exercise.getQuestion().getStimulus();
        String path = stimulus != null ? stimulus.getResourcePath() : "";

        if (!path.isEmpty()) {

            String domain = getString(R.string.resources_addr_kids);
            path = domain + path;

            ImageView img = (ImageView) findViewById(R.id.stimulusImage);
            new LoadImageTask(this, img).execute(path);
        }


        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        Button b4 = (Button) findViewById(R.id.button4);

        List<Button> buttonList = new ArrayList<Button>(Arrays.asList(b1, b2, b3, b4));

        Collections.shuffle(buttonList);

        // Options
        List<Answer> answers = exercise.getAnswers();
        if (!answers.isEmpty()) {
            for (int i = 0; i < 4 || i < answers.size(); i++) {
                Button btn = buttonList.get(i);
                Answer answer = answers.get(i);
                btn.setText(answer.getAnswerDescription().toUpperCase());
                btn.setVisibility(View.VISIBLE);

                if (answer.getAnswerId() == exercise.getRightAnswer().getAnswerId()) {
                    //right answer
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
            }
        }

        currentAnswers = new ArrayList<String>(Arrays.asList(
                b1.getText().toString().toUpperCase(),
                b2.getText().toString().toUpperCase(),
                b3.getText().toString().toUpperCase(),
                b4.getText().toString().toUpperCase()));


        // Prompting
        if (child.getPrompting() != null ) {
            if (child.getPrompting().getPromptingStrategy().equals("ALWAYS")) {
                if (child.getPrompting().getPromptingColor()) {
                    rightAnswerButton.setBackgroundColor(Color.BLUE);
                }

                if(child.getPrompting().getPromptingSize()) {
                    rightAnswerButton.setTextSize(20);
                }

                if (child.getPrompting().getPromptingScratch()) {
                    for (View v: buttonList) {
                        if (v != rightAnswerButton){
                            ((TextView)v).setPaintFlags(((TextView)v).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ((Button)v).setTextColor(getColor(R.color.debug_red));
                            } else {
                                ((Button)v).setTextColor(getResources().getColor(R.color.debug_red));
                            }
                        }
                    }
                }
            }
        }

    }

    public void setReinforcementView() {
        String imgURL;

        try {
            imgURL = child.getReinforcement().getReinforcementResource().getResourcePath();
        } catch (NullPointerException e) {
            imgURL = "";
        }

        if (!imgURL.isEmpty()) {
            hideActionBar();
            // Clear rightFrameLayout
            LinearLayout rightFrameLayout = (LinearLayout) findViewById(R.id.rightFrame);
            rightFrameLayout.removeAllViews();

            ViewGroup.LayoutParams layoutParams = rightFrameLayout.getLayoutParams();
            layoutParams.height = CanvasUtil.getHeight(getWindowManager().getDefaultDisplay());

            linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View ref_view = (View) linflater.inflate(R.layout.reinforcement_view, null);

            ScrollView scroll_view = (ScrollView) linflater.inflate(R.layout.scroll, null);
            rightFrameLayout.addView(scroll_view);

            LinearLayout container = (LinearLayout) findViewById(R.id.scroll_container);
            container.addView(ref_view);

            String path = getString(R.string.resources_addr_kids) + imgURL;

            ImageView image = (ImageView) findViewById(R.id.reinforcement_image);
            new LoadImageTask(this, image).execute(path);

            ImageButton closeReinforcement = (ImageButton) findViewById(R.id.closeReinforcement);
            closeReinforcement.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    nextExerciseHandler();
                }
            });
        }
    }

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
        totalViewResult.setText(Integer.valueOf(currentSequenceLog.getTotalCount()).toString());

        TextView skippedViewResult = (TextView) findViewById(R.id.skippedViewResult);
        skippedViewResult.setText(Integer.valueOf(currentSequenceLog.getSkippedCount()).toString());

        TextView correctViewResult = (TextView) findViewById(R.id.correctViewResult);
        correctViewResult.setText(Integer.valueOf(currentSequenceLog.getCorrectCount()).toString());

        Float distractorAvg = Float.valueOf(currentSequenceLog.getDistractorHitsAvg());
        TextView distractorViewResult = (TextView) findViewById(R.id.distractorViewResult);
        distractorViewResult.setText(String.format("%.2f", distractorAvg).toString());

    }

	/*public void setNavigationView() {

		View view = (View) linflater.inflate(R.layout.navigation_view, null);
		setNavigationLayout(view);

		// onClick handlers

		ImageButton next = (ImageButton) findViewById(R.id.nextButton);

		Boolean isLastExercise = currentExercisePosition >= exercises.size() - 1;

		if(isLastExercise) {
			next.setVisibility(View.INVISIBLE);
		}
		else {
			next.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					nextExerciseHandler();
				}
			});
		}


		ImageButton end = (ImageButton) findViewById(R.id.endButton);

		Boolean hasFinished = currentExercisePosition >= exercises.size();

		if(hasFinished) {
			end.setVisibility(View.INVISIBLE);
		}
		else {
			end.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					endHandler();
				}
			});
		}

		ImageButton home = (ImageButton) findViewById(R.id.homeButton);
		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				homeHandler();
			}
		});

	}*/

    public void setNavigationView() {
        ImageButton next = (ImageButton) findViewById(R.id.nextExerciseButton);
        ImageButton previous = (ImageButton) findViewById(R.id.previousExerciseButton);
        ImageButton end = (ImageButton) findViewById(R.id.endExerciseButton);

        Boolean isLastExercise = currentExercisePosition == exercises.size() - 1;
        if (isLastExercise && next != null) {
            next.setVisibility(View.INVISIBLE);
            end.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    endHandler();
                }
            });
        } else {
            end.setVisibility(View.INVISIBLE);
            next.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    nextExerciseHandler();
                }
            });
        }

        Boolean isFirstExercise = currentExercisePosition == 0;
        if (isFirstExercise && previous != null) {
            previous.setVisibility(View.INVISIBLE);
        } else {
            previous.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    previousHandler();
                }
            });
        }

    }

    private void previousHandler() {
        this.currentExercisePosition--;
        inExercise = true;
        setExerciseView();
        setNavigationView();
    }

    //endregion

    //
    //region Handlers - what happens when you do some actions
    //

    protected void rightAnswerHandler(View v, Child child) {

        inExercise = false;
        playMessage(child, "EXERCISE_REINFORCEMENT");
        playReinforcement(child);
        attempts = 0;
    }

    private void playReinforcement(Child child) {
        switch (child.getReinforcement().getReinforcementStrategy()) {
            case "ALWAYS":
                setReinforcementView();
                break;
            case "AT_FIRST":
                if(attempts == 0) {
                    setReinforcementView();
                }
                break;
            case "OFF":
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
                break;
            }
        }

        if (!sentence.isEmpty()) {
            if (child.getEmotions()) {
                new ReadTask().execute(sentence, "joy");
            } else {
                new ReadTask().execute(sentence);
            }

            lastInstruction = sentence;
        }
    }

    protected void distractorHandler(View v) {
        attempts++;

        // Prompting
        if (child.getPrompting() != null ) {
            Exercise exercise = exercises.get(currentExercisePosition);
            Boolean isTextExercise = exercise.getType().equals("TEXT");
            if (child.getPrompting().getPromptingStrategy().equals("IF_NEEDED")) {
                if (child.getPrompting().getPromptingHide()) {
                    v.setVisibility(View.INVISIBLE);
                } else {
                    if (child.getPrompting().getPromptingColor() && isTextExercise) {
                        rightAnswerButton.setBackgroundColor(Color.BLUE);
                    }

                    if (child.getPrompting().getPromptingScratch() && isTextExercise) {
                        ((TextView)v).setPaintFlags(((TextView)v).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ((Button)v).setTextColor(getColor(R.color.debug_red));
                        } else {
                            ((Button)v).setTextColor(getResources().getColor(R.color.debug_red));
                        }
                    }

                    if(child.getPrompting().getPromptingSize() && isTextExercise) {
                        rightAnswerButton.setTextSize(20);
                    }
                }
            } else if (child.getPrompting().getPromptingStrategy().equals("ALWAYS")) {
                if (child.getPrompting().getPromptingHide()) {
                    v.setVisibility(View.INVISIBLE);
                }
            }

                    // Read answers
//                    for (String cd : currentAnswers) {
//                        new ReadTask().execute(cd);
//                        lastInstruction = cd;
//                    }
        }

        new ReadTask().execute("Tenta outra vez.");
    }

    protected void nextExerciseHandler() {

        this.currentExercisePosition++;

        Boolean hasFinished = currentExercisePosition >= exercises.size();
        if (!hasFinished) {
            inExercise = true;
            setExerciseView();
            setNavigationView();
        } else {
            endHandler();
        }
    }

    protected void endHandler() {
        //new SendLogs(this, currentSequenceLog).execute();
        //setFinalResultsView();

        //Sequence reinforcement
        playMessage(child, "SEQUENCE_REINFORCEMENT");

        setSequenceSelectionView();
    }

    protected void homeHandler() {
        setSequenceSelectionView();
    }

    private void exitHandler() {
        this.child = null;
        SharedPreferences settings = this.getSharedPreferences(getString(R.string.APP_PREFERENCES), MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.clear();
        prefEditor.commit();

        Intent i = new Intent(this, LoginActivity.class);
        VitheaKidsActivity.this.startActivity(i);
    }

    //endregion

    //
    //region Menu
    //


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Does not work !!!
//		MenuItem promptingToggle = (MenuItem) menu.findItem(R.id.prompting_check);
//		promptingToggle.setCheckable(true);
//
//		promptingToggle.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//
//				if(item.isChecked()) {
//					promptingActive = true;
//				}
//				else {
//					promptingActive = false;
//				}
//				return true;
//			}
//		});


        // I honestly hope there's a better way to do this

//		int currentAPIversion = android.os.Build.VERSION.SDK_INT;
//
//		if (currentAPIversion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
//
//			Log.d(TAG, "Such modern, much new");
//
//			Switch promptingToggle = (Switch) menu.findItem(R.id.prompting_option).getActionView().findViewById(R.id.switch_prompting_option);
//
//			promptingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//					if (isChecked) {
//						promptingActive = true;
//						Log.d(TAG, "P on");
//
//			        } else {
//			        	promptingActive = false;
//			        	Log.d(TAG, "P off");
//			        }
//
//				}
//			});
//
//			Switch reinforcementToggle = (Switch) menu.findItem(R.id.reinforcement_option).getActionView().findViewById(R.id.switch_reinforcement_option);
//
//			reinforcementToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//					if (isChecked) {
//						reinforcementActive = true;
//						Log.d(TAG, "R on");
//
//			        } else {
//			        	reinforcementActive = false;
//			        	Log.d(TAG, "R off");
//			        }
//
//				}
//			});
//
//		} else{
//
//			Log.d(TAG, "Old but gold");
//
//			ToggleButton promptingToggle = (ToggleButton) menu.findItem(R.id.prompting_option).getActionView().findViewById(R.id.switch_prompting_option);
//
//			promptingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//					if (isChecked) {
//						promptingActive = true;
//						Log.d(TAG, "P on");
//			        } else {
//			        	promptingActive = false;
//			        	Log.d(TAG, "P off");
//			        }
//
//				}
//			});
//
//			ToggleButton reinforcementToggle = (ToggleButton) menu.findItem(R.id.reinforcement_option).getActionView().findViewById(R.id.switch_reinforcement_option);
//
//			reinforcementToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//					if (isChecked) {
//						reinforcementActive = true;
//						Log.d(TAG, "R on");
//			        } else {
//			        	reinforcementActive = false;
//			        	Log.d(TAG, "R off");
//			        }
//
//				}
//			});
//		}
//


        return true;
    }

    //
    // Preferences Menu
    //

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

        return super.onOptionsItemSelected(item);
    }

    //
    // Back button
    //

//	@Override
//	public void onBackPressed() {
//
//		// TODO finish Are you sure? alert dialog
//
//		new AlertDialog.Builder(getApplicationContext())
//
//			.setMessage(getString(R.string.are_you_sure_msg))
//
//			.setCancelable(true)
//
//			.setNegativeButton(getString(R.string.cancel_msg),
//					new DialogInterface.OnClickListener() {
//
//				public void onClick(DialogInterface dialog,
//						int id) {
//
//					// TODO
//				}
//			})
//
//			.setPositiveButton("Yes",
//					new DialogInterface.OnClickListener() {
//
//				public void onClick(final DialogInterface dialog, int id) {
//
//					// TODO
//				}
//			}).create();
//
//
//		//d.show();
//	}

    //endregion
}