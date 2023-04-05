package com.truelight.cacihymn.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Switch;

import com.truelight.cacihymn.Models.MemoryData;
import com.truelight.cacihymn.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.setTitle("Settings");


        Log.d("Is night mode",MemoryData.getIsNightMode(SettingActivity.this)+"");

        Switch switchMode = (Switch)findViewById(R.id.switchMode);
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                Log.d("Check value ",b+"");

                if (compoundButton.isChecked()){

                    //MemoryData.setIsNighMode(true);
                    MemoryData.saveIsNightMode(SettingActivity.this,b);

                }else {

                   // MemoryData.setIsNighMode(false);
                    MemoryData.saveIsNightMode(SettingActivity.this,b);

                }
            }
        });


        if (MemoryData.getIsNightMode(SettingActivity.this)){

            switchMode.setChecked(true);
        }else{
            switchMode.setChecked(false);
        }




        Button about = (Button)findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                alertDialog.setTitle("CACI-DWOM (Version 1.6) ");
                alertDialog.setMessage(
                        "This app was developed for Christ Apostolic Church International." + "\nBy TrueLite Technology\n"+
                        "\nSPECIAL THANKS TO \n\n" +
                                "Godwill Ohene Siaw (C.A.C.I, Taifa - Branch).\n\n" +
                                "Cyril Atta Adjei Panin (C.A.C.I,Taifa - Branch).\n\n" +
                                "Mr Benjamin Anum Atteh (National Music Officer ).\n" +
                                "\nCopyright (2023) ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        Button invite =(Button)findViewById(R.id.invite);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


        Log.d("Check value ",MemoryData.getIsNightMode(SettingActivity.this)+"");

        Button changefont = (Button)findViewById(R.id.changefont);
        changefont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final NumberPicker picker = new NumberPicker(SettingActivity.this);
                picker.setMinValue(15);
                picker.setMaxValue(25);


                FrameLayout layout = new FrameLayout(SettingActivity.this);
                layout.addView(picker, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER));

                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("Select Text Size")
                        .setView(layout)
                        .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                MemoryData.saveFontSize(SettingActivity.this,picker.getValue());
                            }
                        })

                        .setNegativeButton(android.R.string.cancel, null)
                        .show();

            }
        });
    }
}
