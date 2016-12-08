package com.example.ben.ra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickMain(View v)
    {
        switch(v.getId())
        {
            case R.id.buttonPlay:
            {
                Log.v("UI", "Starting NewGameActivity");

                Intent intent = new Intent(v.getContext(), NewGameActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.buttonQuit:
                Log.v("UI", "Exitting app");
                finish();
                break;
        }
    }
}
