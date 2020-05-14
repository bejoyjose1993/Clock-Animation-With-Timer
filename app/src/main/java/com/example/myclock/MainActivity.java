package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener, AdapterView.OnItemSelectedListener {

    ClockSurfaceView clockSurfaceView = null;
    FrameLayout frm = null;
    Button btInColor = null, btOutColor = null, btnAlarm = null;
    ImageView changeImg = null;
    LinearLayout tobtab = null;
    int selectedColor = 0;
    int selectedColorIn = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        clockSurfaceView = new ClockSurfaceView(this, 330);
        setContentView(R.layout.activity_main);

        try{
            this.getSupportActionBar().hide();
        }catch(NullPointerException e){

        }


        frm=(FrameLayout)findViewById(R.id.frameLayout);
        frm.addView(clockSurfaceView);

        btInColor=(Button)findViewById(R.id.changeInnerBt);
        btOutColor=(Button)findViewById(R.id.changeOuterBt);
        btnAlarm = (Button) findViewById(R.id.alarmBt);
        changeImg = (ImageView) findViewById(R.id.imageView);
        tobtab = findViewById(R.id.tobtab);

        Spinner spinner = (Spinner) findViewById(R.id.clubSpinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Club_Name, R.layout.spinner_custom_layout);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE);
        String selectedLogo = prefs.getString("club_logo", "");
        System.out.println("selectedColor"+selectedLogo);
        if(selectedLogo != null && selectedLogo != ""){
            String [] club_names =  getResources().getStringArray(R.array.Club_Name);
            int logoIndex = Arrays.asList(club_names).indexOf(selectedLogo);
            spinner.setSelection(logoIndex);
        }else{
            spinner.setSelection(1);
        }



        btInColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog =  ColorPickerDialog.newBuilder().setColor(Color.BLUE).create();
                colorPickerDialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
                    @Override
                    public void onColorSelected(int dialogId, int color) {
                        clockSurfaceView.changeInColor(color);
                        //tobtab.setBackgroundColor(color);
                        selectedColorIn = color;
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                        editor.putInt("selectedColorIn", selectedColorIn);
                        editor.apply();
                    }

                    @Override
                    public void onDialogDismissed(int dialogId) {

                    }
                });
                colorPickerDialog.show(getFragmentManager(),"ColorPicker");
            }

        });

        btOutColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog =  ColorPickerDialog.newBuilder().setColor(Color.BLUE).create();
                colorPickerDialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
                    @Override
                    public void onColorSelected(int dialogId, int color) {
                        clockSurfaceView.changeColor(color);
                        tobtab.setBackgroundColor(color);
                        selectedColor = color;
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
                        editor.putInt("selectedColor", selectedColor);
                        editor.apply();
                    }

                    @Override
                    public void onDialogDismissed(int dialogId) {

                    }
                });
                colorPickerDialog.show(getFragmentManager(),"ColorPicker");
            }

        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CountdownTimmer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectedColor",selectedColor);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        clockSurfaceView.changeLogo(item,tobtab,changeImg);
        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putString("club_logo", item);
        editor.apply();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    protected  void  onResume(){
        super.onResume();
        clockSurfaceView.onResumeMySurfaceView();
    }

    protected  void  onPause(){
        super.onPause();
        clockSurfaceView.onPauseMySurfaceView();
    }

    @Override
    public void onColorSelected(int dialogId, int color) {

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
