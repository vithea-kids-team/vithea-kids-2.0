package com.l2f.vitheakids;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.l2f.vitheakids.R;

/**
 * Created by silvi on 04/09/2017.
 */

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public VitheaKidsActivity act;
    public Button yes, no;
    public Dialog d;

    public CustomDialogClass(VitheaKidsActivity a){
        super(a, R.style.My_Dialog);
        this.act = (VitheaKidsActivity) a;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes= (Button) findViewById(R.id.yes);
        no= (Button) findViewById(R.id.no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.yes:
                act.setSequenceSelectionView();
                break;
            case R.id.no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
