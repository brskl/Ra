package com.example.ben.ra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import junit.framework.Assert;

public class MainActivity extends AppCompatActivity {

    String fileGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(MainActivity.class.toString(), "onCreate");

        Button btnResume = (Button) findViewById(R.id.buttonResume);

        // Test if saved game file exists, enable 'Resume' button.
        String [] fileList = fileList();
        fileGame = null;
        for (int i = 0; i < fileList.length; i++)
        {
            if (fileList[i].contentEquals("Ra.game"))
            {
                fileGame = fileList[i];
                break;
            }
        }
        btnResume.setEnabled(fileGame != null);
    }

    public void onClick(View v)
    {
        Log.d(MainActivity.class.toString(), "onClickMain");

        switch(v.getId())
        {
            case R.id.buttonNew:
                {
                Log.v(MainActivity.class.toString(), "Starting NewGameActivity");

                Intent intent = new Intent(v.getContext(), NewGameActivity.class);
                startActivity(intent);
                }
                break;
            case R.id.buttonResume:
                {
                    Assert.assertNotNull(fileGame);

                    Log.v(MainActivity.class.toString(), "Loading " + fileGame);
                    Game.readFromFile(this, fileGame);

                    Log.v(MainActivity.class.toString(), "Starting GameActivity");
                    Intent intent = new Intent(v.getContext(), GameActivity.class);
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
