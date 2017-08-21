package com.l2f.vitheakids.rest;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import com.l2f.vitheakids.R;
import com.l2f.vitheakids.VitheaKidsActivity;
import com.l2f.vitheakids.model.AnimatedCharacter;
import com.l2f.vitheakids.model.Child;
import com.l2f.vitheakids.model.PersonalMessage;
import com.l2f.vitheakids.task.ReadTask;
import com.l2f.vitheakids.task.SwitchCharacterTask;
import com.l2f.vitheakids.util.Api;

/**
 * Created by Claudia on 06/02/2017.
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class FetchChildInfo extends AsyncTask<Void, Void, Child> {

    private Activity activity;

    public FetchChildInfo(Activity act){
        this.activity = act;
    }

    @Override
    protected Child doInBackground(Void... voids) {
        String url = activity.getString(R.string.ws_uri) + activity.getString(R.string.greeting_message);
        ResponseEntity<String> response = Api.get(url,activity);
        String exerciseInfo = response.getBody();

        ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
        // mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // String JSON -> class
        Child allExercises = null;
        try {
            allExercises = mapper.readValue(exerciseInfo,Child.class);
            Log.d("nameChild:  ", allExercises.getFirstName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allExercises; // retorno daqui entra no onPostExecute
    }

    @Override
    protected void onPostExecute(Child child) {

        // why child could be null?
        if (child != null){
            AnimatedCharacter animatedCharacter = child.getAnimatedCharacter();
            if (animatedCharacter != null) {
                new SwitchCharacterTask().execute(animatedCharacter.getName(), "john2"); // ??????
            }

            //Greeting Message
            ((VitheaKidsActivity) activity).playMessage(child, "GREETING_MESSAGE");
            ((VitheaKidsActivity) activity).child = child;              // store info of the child
            String promptingStrategy = child.getPrompting().getPromptingStrategy();
            String reinforcementStrategy = child.getReinforcement().getReinforcementStrategy();

            if(promptingStrategy.equals("OFF"))
                ((VitheaKidsActivity) activity).initActivePrompting(false);
            else ((VitheaKidsActivity) activity).initActivePrompting(true);

            if(reinforcementStrategy.equals("OFF"))
                ((VitheaKidsActivity) activity).initActiveReinforcement(false);
            else ((VitheaKidsActivity) activity).initActiveReinforcement(true);
            Log.d("here","iniciarSeq" );
            ((VitheaKidsActivity) activity).setSequenceSelectionView(); // change screen
        }
    }
}
