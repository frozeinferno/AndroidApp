package com.example.drawingapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mainMenu = findViewById(R.id.backButton);
        mainMenu.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.backButton) {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
        }
    }
}
