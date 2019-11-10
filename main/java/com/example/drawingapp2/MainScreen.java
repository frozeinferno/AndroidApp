package com.example.drawingapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {

    //variables for the buttons on the UI
    private Button startButton;
    private Button helpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //here I simply instantiate the buttons, allocating them to their ID in the xml file and applying the onClickListener(),
        //so that they can be assigned an action in the onClick() method
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(this);
    }

    public void onClick(View view) {

        //this simply dictates the action that takes place should one of the buttons be pressed, in this case
        //changing from activity to activity
        if(view.getId() == R.id.start_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.helpButton) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
    }
}
