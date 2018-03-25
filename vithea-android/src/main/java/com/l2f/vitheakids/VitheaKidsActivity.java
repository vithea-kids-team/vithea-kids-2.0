package com.l2f.vitheakids;

import com.l2f.vitheakids.Storage.ImageStorage;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.Exercise;
import com.l2f.vitheakids.model.ExerciseLogInfo;
import com.l2f.vitheakids.model.PersonalMessage;
import com.l2f.vitheakids.model.Resource;
import com.l2f.vitheakids.model.SequenceLogInfo;
import com.l2f.vitheakids.rest.FetchChildInfo;
import com.l2f.vitheakids.rest.SendLogs;
import com.l2f.vitheakids.task.LoadImageTask;
import com.l2f.vitheakids.task.ReadTask;
import com.l2f.vitheakids.task.StopRead;
import com.l2f.vitheakids.util.TaskListener;
import com.unity3d.player.*;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class VitheaKidsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, TaskListener, SequenceSelectionFragment.OnFragmentSelectionListener {

    private String TAG = VitheaKidsActivity.class.getSimpleName();

// **** DECLARATIONS *******************************************************************************

    // Unity stuff
    private UnityPlayer mUnityPlayer;

    // Bundle info
    public Child child = null;
    private ExerciseLogInfo currentExerciseLogInfo;
    private SequenceLogInfo currentSequenceLogInfo;

    // Exercises
    private long currentSequenceId;
    private int currentExercisePosition;
    public int attempts = 0;
    private boolean skipped = false;
    private boolean correctAnswer = false;
    private boolean inExercise;
    private boolean inHomeScreen = true;
    private boolean inSequenceScreen;
    public boolean promptingActive;
    public boolean reinforcementActive;
    private String seqName;
    private List<Exercise> exercises;

    private ActionBar actionBar;

    private ImageStorage imageStorage;

    private Exercise currentExercise;
    private FragmentTransaction fragmentTransaction;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        checkStoragePermission();
        logger = LogHelper.getLogger( "VitheaKidsActivity" );

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        imageStorage = (ImageStorage) getApplication();
        initUnityCharacter();

        //getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy
        setContentView(R.layout.main);

        // conf unity player
        //initViews();            // conf views and layouts

        new FetchChildInfo(VitheaKidsActivity.this).execute();

    }

    @Override
    protected void onStop(){

        super.onStop();
        finish();
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

        fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.child_fragment_container, SequenceSelectionFragment.newInstance(child));
        fragmentTransaction.commit();

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


        // Set exercise info
        if (child != null) {
            currentExercise = exercises.get(currentExercisePosition);
            fragmentTransaction = this.getSupportFragmentManager().beginTransaction();

            if (currentExercise.getType().equals("TEXT")) {
                fragmentTransaction.replace(R.id.child_fragment_container,  MultipleChoiceTextFragment.newInstance(currentExercise, child, imageStorage,seqName));
            } else if (currentExercise.getType().equals("IMAGE")) {
                fragmentTransaction.replace(R.id.child_fragment_container,  MultipleChoiceImageFragment.newInstance(currentExercise, child, imageStorage,seqName));
            }
            fragmentTransaction.commit();

            // Send to Animated Character
            readWithOrWithoutEmotion(child, currentExercise.getQuestion().getQuestionDescription());

        }
    }

    public void setReinforcementView() {
        new StopRead().execute();//stop character talk and also  clean the text variable on unity
        findViewById(R.id.previous).setVisibility(View.INVISIBLE);//remove  end button of navigation view
        findViewById(R.id.end).setVisibility(View.INVISIBLE);//remove  previous of navigation view

        Resource res;
        res = child.getReinforcement().getReinforcementResource();

        if (reinforcementActive && res!=null) {
            hideActionBar(); // TODO maybe not hide
            playReinforcementMessage(child, "EXERCISE_REINFORCEMENT");

            byte[] bitmap = imageStorage.getImage(seqName, res.getResourceId());//getting reinforcement image

            fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.child_fragment_container, ReinforcementFragment.newInstance(bitmap));
            fragmentTransaction.commit();

            findViewById(R.id.next).setVisibility(View.VISIBLE);

            findViewById(R.id.previousExerciseButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextExerciseHandler();
                }
            });
        }
    }

    public void setNavigationView() {

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);
        RelativeLayout end = (RelativeLayout) findViewById(R.id.end);

        Boolean isLastExercise = currentExercisePosition == exercises.size() - 1;
        Boolean isfirstExercise = currentExercisePosition == 0;
        Log.d("POSICAO DO EXERCICIO",Integer.toString(currentExercisePosition) );
        ImageButton  buttonPrevious = (ImageButton ) findViewById(R.id.previousExerciseButton);
        ImageButton  buttonNext= (ImageButton ) findViewById(R.id.nextExerciseButton);

        if (isLastExercise) {
            Log.d("lastExercise", "it's the last exercise");
            next.setVisibility(View.INVISIBLE);
            end.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);

            ImageButton  buttonEnd = (ImageButton) findViewById(R.id.endExerciseButton);



            buttonEnd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    skipped = true;
                    endHandler();
                }
            });

            buttonPrevious.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    previousExerciseHandler();
                }
            });

        } else if(!isLastExercise && !isfirstExercise) {
            Log.d("not last, not first", "exercise");
            next.setVisibility(View.VISIBLE);
            end.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.VISIBLE);

            buttonNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    skipped = true;
                    nextExerciseHandler();
                }
            });
            buttonPrevious.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    previousExerciseHandler();
                }
            });
        }
        else if(isfirstExercise){
            Log.d("first", "exercise");

            next.setVisibility(View.VISIBLE);
            end.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.INVISIBLE);
            buttonNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    skipped = true;
                    nextExerciseHandler();
                }
            });
        }
    }

//**** Region Handlers - what happens when you do some actions ************************************

    protected void rightAnswerHandler(View v, Child child) {
        inExercise = false;
        correctAnswer = true;
        playReinforcementMessage(child, "EXERCISE_REINFORCEMENT");
        if(reinforcementActive) playReinforcement(child);
        else nextExerciseHandler();
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
                else nextExerciseHandler();
                break;
            case "OFF":
            default:
                nextExerciseHandler();
                break;
        }
    }

    /**
     * readWithOrWithoutEmotion
     * @param sentence
     * Verify if the text has to be uttered with emotion
     * Launch a task to read the text
     */
    public void readWithOrWithoutEmotion(Child child, String sentence){

        if (child.getEmotions()) {
            new ReadTask().execute(sentence, "joy");
        } else {
            new ReadTask().execute(sentence);
        }

    }
    public void playReinforcementMessage(Child child, String exercise_reinforcement) {
        //Exercise reinforcement
        List<PersonalMessage> personalMessages = child.getPersonalMessageList();
        String sentence = "";
        for (PersonalMessage message : personalMessages) {
            if (message.getMessageType().equals(exercise_reinforcement)) {
                sentence = message.getMessage();
                break;
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
         findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);//remove navigation view of exercises
        // TODO Send logger and show results
        //new SendLogs(this, currentSequenceLog).execute();

        if(skipped) logExercise();

        logSequence();

        //Say reinforcement message only if the child answer rightly in all exercises
        if(reinforcementActive && currentSequenceLogInfo.getCorrectExercises()==currentSequenceLogInfo.getNumberOfExercises() ) playReinforcementMessage(child, "SEQUENCE_REINFORCEMENT");

        setSequenceSelectionView();
    }
    protected void homeHandler() {
        setSequenceSelectionView();


    }

    protected  void backMenuHandler(){
        CustomDialogClass cdd=new CustomDialogClass(this);
        cdd.show();

    }

    private void exitHandler(){
        this.child = null;
        SharedPreferences settings = this.getSharedPreferences(getString(R.string.APP_PREFERENCES), MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.clear();
        prefEditor.commit();
        Intent i = new Intent(VitheaKidsActivity.this, LoginActivity.class);
        startActivity(i);
    }

    private void reinforcementHandler(MenuItem item){
        reinforcementActive = !(reinforcementActive);
        if(reinforcementActive){
            item.setTitle("Desligar Reforço ");
        }
        else
            item.setTitle("Ligar Reforço");
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
               MultipleChoiceTextFragment multipleChoiceExercise = (MultipleChoiceTextFragment) getSupportFragmentManager().findFragmentById(R.id.child_fragment_container);
               multipleChoiceExercise.drawAnswers();
            }
            if(currentExercise.getType().equals("IMAGE")){
                Log.d("clean prompting","Clean ajudas");
                MultipleChoiceImageFragment multipleChoiceExercise = (MultipleChoiceImageFragment) getSupportFragmentManager().findFragmentById(R.id.child_fragment_container);
                multipleChoiceExercise.drawAnswers(); //redraw buttons to clean all prompting types applied
            }
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


    public UnityPlayer getmUnityPlayer(){
        return mUnityPlayer;
    }

    public Exercise getCurrentExercise(){
        return currentExercise;
    }


    /**
     * Starts the sequence  of exercises chosen
     * @param idSeq
     * @param seqName
     * @param exercises
     */
    @Override
    public void onFragmentSelection(Long idSeq, String seqName, List<Exercise> exercises, int position) {

        inExercise = true;
        inHomeScreen = false;
        inSequenceScreen = false;

        //current sequence info
        this.seqName = seqName;
        this.exercises= exercises;
        this.currentSequenceId = idSeq;
        this.currentExercisePosition =0;

        initNewSequenceLogInfo();
        List<Resource> resources = child.getAllResourcesBySeqPosition(position);
        new LoadImageTask(resources,seqName,this).execute();
        View navView =  findViewById(R.id.nav_view);
        navView.setVisibility(View.VISIBLE);
        setNavigationView();        // Button - skip, finish, etc
    }
}