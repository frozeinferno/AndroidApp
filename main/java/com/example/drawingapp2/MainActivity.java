package com.example.drawingapp2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //variables needed to create the canvas the user draw on
    private DrawingView drawingView;
    private ImageButton paint;
    private Button eraseButton;
    private Button paintButton;
    private Button backButton;
    private Resources.Theme theme; //this variable was simply created to fill out the setImageDrawable() method
                                   //I'm not quite sure what it does. it is used because the method I was actually
                                   //going to use was deprecated


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //here, I set up the requirements for the MainActivity UI, including the canvas and the paint colours
        drawingView = findViewById(R.id.drawing);
        LinearLayout paintLayout = findViewById(R.id.paint_colors);
        paint = (ImageButton)paintLayout.getChildAt(0);

        //here I simply instantiate each of the buttons on the MainActivity UI, so I can add the onClickListener()
        //to each of the buttons so I can dictate what each of the buttons do when pressed within the onClick() method
        eraseButton = findViewById(R.id.erase_btn);
        eraseButton.setOnClickListener(this);

        paintButton = findViewById(R.id.paint_btn);
        paintButton.setOnClickListener(this);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(this);


        //this is where I place the service within my program, the accelerometer sensor
        Intent myIntent = new Intent(MainActivity.this, ShakeFeature.class);
        startService(myIntent);
    }

    //this method dictates what happens when one of the paint colour buttons is pressed
    public void paintClicked(View view) {
        if(view != paint) {
            if(drawingView.getErase() == true) {
                drawingView.setErase(false);
                //a toast is a notification which temporarily appears onscreen to notify the user of an update within
                //the program
                Toast eraseOff = Toast.makeText(getApplicationContext(), "Eraser off", Toast.LENGTH_SHORT);
                eraseOff.show();
            }
            drawingView.setErase(false);
            String colour = view.getTag().toString();
            //this is what sets the colour that the user draws in; what allows the user to change from
            //colour to colour
            drawingView.setColour(colour);
            paint.setImageDrawable(getResources().getDrawable(R.drawable.paint, theme));
        }
    }

    @Override
    public void onClick(View view) {
        //here, I simply dictate what happens when each of the buttons on the UI is pressed
        if(view.getId() == R.id.paint_btn) {
            drawingView.setErase(false);
            Toast eraserToast = Toast.makeText(getApplicationContext(), "Eraser off", Toast.LENGTH_SHORT);
            eraserToast.show();
        }

        if(view.getId() == R.id.erase_btn) {
            drawingView.setErase(true);
            Toast eraserToast = Toast.makeText(getApplicationContext(), "Eraser on", Toast.LENGTH_SHORT);
            eraserToast.show();
        }

        if(view.getId() == R.id.back_btn) {
            //here, I create an AlertDialog, asking the user whether they want to leave back to the main menu
            AlertDialog.Builder backDialog = new AlertDialog.Builder(this);
            backDialog.setTitle("Back to main screen");
            backDialog.setMessage("Go back to main menu?");
            backDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    backToHome();
                }
            });
            backDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            backDialog.show();
        }
    }

    //this method is created because it wouldn't let me create an intent within the AlertDialog
    //setPositiveMessage() method
    public void backToHome() {
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
    }
}

