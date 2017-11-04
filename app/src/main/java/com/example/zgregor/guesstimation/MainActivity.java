package com.example.zgregor.guesstimation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CreateGameButtonClick(View v) {
        Intent intent = new Intent (getApplicationContext(), CreateGame.class);
        startActivity(intent);
    }

    public void CreateAccountButtonClick(View v) {
        Intent intent2 = new Intent (getApplicationContext(), CreateAccount.class);
        startActivity(intent2);
    }
}
