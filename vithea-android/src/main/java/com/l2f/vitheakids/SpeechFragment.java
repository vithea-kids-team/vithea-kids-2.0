package com.l2f.vitheakids;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Process;

import com.l2f.vitheakids.RawBuffer.RawBuffer;
import com.l2f.vitheakids.Storage.ImageStorage;
import com.l2f.vitheakids.model.Answer;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.Exercise;
import com.l2f.vitheakids.model.Resource;
import com.l2f.vitheakids.model.SpeechExercise;
import com.l2f.vitheakids.rest.SendAudimus;
import com.l2f.vitheakids.util.TaskListener;

import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.view.View.OnClickListener;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpeechFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpeechFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpeechFragment extends Fragment implements TaskListener {

    private SpeechExercise exercise;
    private ImageStorage imageStorage;
    private String seqName;
    private View fragmentView;
    private Child child;
    private List<Answer> answers;

    private byte[] buffer;
    private int buffersizebytes;
    private AudioRecord recorder;
    private boolean recdone = true;
    private ArrayList<RawBuffer> buffers;
    private ExecutorService threadedExecuter = null;
    private Button start, stop;


    public SpeechFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SpeechFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpeechFragment newInstance(SpeechExercise exercise, ImageStorage imageStorage, String seqName, Child child) {
        SpeechFragment fragment = new SpeechFragment();
        fragment.exercise = exercise;
        fragment.imageStorage = imageStorage;
        fragment.seqName = seqName;
        fragment.child = child;
        fragment.threadedExecuter = Executors.newCachedThreadPool();
        fragment.buffersizebytes = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        fragment.buffer = new byte[fragment.buffersizebytes];
        fragment.recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, fragment.buffersizebytes * 2);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_speech, container, false);

        // question
        TextView questionText = (TextView) fragmentView.findViewById(R.id.questionText);
        String  question  = exercise.getQuestionSpeech();
        question  = (child.toUpperCase())? question.toUpperCase(): question;
        questionText.setText(question);

        // Stimulus
        Resource stimulus = exercise.getStimulusSpeech();
        byte[] image  = imageStorage.getImage(seqName,stimulus.getResourceId());
        ImageView img = (ImageView) fragmentView.findViewById(R.id.stimulusImage1);
        imageStorage.setImageOfView(getContext(),img,image);

        // Buttons
        start = (Button) fragmentView.findViewById(R.id.ButtonStart);
        stop = (Button) fragmentView.findViewById(R.id.ButtonStop);
        //stop.setVisibility(View.INVISIBLE);

        if (!exercise.getAnswers().isEmpty()) {
            answers = exercise.getAnswers();
            for (int i=0; i < answers.size(); i++) {
                Log.d("answer", answers.get(i).getAnswerDescription());
            }
        }
        else {
            Log.d("answer", "nao há");
        }

        start.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("entreistart", "entrei start");

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                        if (recorder.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
                            Log.d("entreistart", "RECORDSTATE_STOPPED");
                            recorder.startRecording();
                            //changeButtons();
                        }

                        buffers = new ArrayList<RawBuffer>();
                        recdone = false;
                        Log.d("entreistart", "" + recdone);

                        while (recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                            int res = recorder.read(buffer, 0, buffersizebytes);
                            if (res == AudioRecord.ERROR_INVALID_OPERATION || res == AudioRecord.ERROR_BAD_VALUE || res == 0 || res == -1) {
                                continue;
                            }

                            RawBuffer rawBuffer = new RawBuffer(buffer, res);
                            buffers.add(rawBuffer);
                            try {
                                Log.d("entreistart", "" + rawBuffer.call());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        recdone = true;
                        Log.d("entreistart", "" + recdone);
                    }
                }).start();
            }
        });

        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("entreistop", "entrei stop");

                recorder.stop();

                Log.d("entreistop", "" + buffers.size());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                        while (!recdone) ;

                        StringBuilder sb = new StringBuilder();
                        try {
                            for (int i = 0; i < buffers.size(); i++) {
                                String call = buffers.get(i).call();
                                Log.d("entreistop", call);
                                sb.append(call);
                            }
                            Log.d("entreistop", sb.toString());

                            /*List<Future<String>> lft = threadedExecuter.invokeAll(buffers);

                            for (int i = 0; i < lft.size(); i++) {
                                Future<String> ft = lft.get(i);
                                try {
                                    sb.append(ft.get());
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                            */
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // answers
                        if (!exercise.getAnswers().isEmpty()) {
                            answers = exercise.getAnswers();
                        }

                        String sentaud = "[" + sb.substring(0, sb.length() - 1) + "]";
                        String xmlgramar = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
                                "<grammar root=\"main\" version=\"1.0\" xmlns=\"http://www.w3.org/2001/06/grammar\" xml:lang=\"pt-PT\">" +
                                "<rule id=\"main\">" +  "<item repeat=\"0-1\">" + "<one-of>";

                        for (int i=0; i < answers.size(); i++) {
                            xmlgramar += "<item><ruleref special=\"GARBAGE\"/>" +
                                    answers.get(i).getAnswerDescription() +
                                    "<ruleref special=\"GARBAGE\"/></item>";
                        }
                        xmlgramar += "<item><ruleref special=\"GARBAGE\"/></item>" +
                                        "</one-of>" + "</item>" + "</rule>" + "</grammar>";

                        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                        body.add("task", "echo-kws-asr");
                        body.add("sbytes", sentaud);
                        body.add("lang","pt");
                        body.add("grxml", xmlgramar);
                        Log.d("entreistop", xmlgramar);

                        //Send Audimus Request to Server
                        final String url = "http://www.l2f.inesc-id.pt/~pfialho/tts/frameworkProxyASR.php";
                            new SendAudimus(body, (VitheaKidsActivity) getActivity(), url, (VitheaKidsActivity) getActivity()).execute();
                            /*try {
                                Log.d("RESPONSE", response.get().toString());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }*/

                            buffers = null;
                        }
                }).start();
                //Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
            }
        });

        // Inflate the layout for this fragment
        return fragmentView;
    }


    public void changeButtons(){
        start.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onTaskFinished(ResponseEntity<String> response) {

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
