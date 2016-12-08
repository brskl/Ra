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

        Log.d(MainActivity.class.toString(), "onCreate");
    }

    public void onClickMain(View v)
    {
        Log.d(MainActivity.class.toString(), "onClickMain");

        switch(v.getId())
        {
            case R.id.buttonPlay:
            {
                Log.v(MainActivity.class.toString(), "Starting NewGameActivity");

                Intent intent = new Intent(v.getContext(), NewGameActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.buttonQuit:
                Log.v(MainActivity.class.toString(), "Exitting app");
                finish();
                break;
        }
    }
}
