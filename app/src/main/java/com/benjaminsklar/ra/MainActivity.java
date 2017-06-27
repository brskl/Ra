package com.benjaminsklar.ra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

/**
 * Main or starting activity for app
 */
public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    String fileGame;
    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(MainActivity.class.toString(), "onCreate");

        ivSplash = (ImageView) findViewById(R.id.splash);

        // Test if saved game file exists, enable 'Resume' button.
        String [] fileList = fileList();
        fileGame = null;
        for (String sname: fileList)
        {
            if (sname.contentEquals("Ra.game"))
            {
                fileGame = sname;
                break;
            }
        }

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashanim);
        animation.setAnimationListener(this);
        ivSplash.startAnimation(animation);

    }

    public void onAnimationEnd(Animation animation) {
        Button btnNew = (Button) findViewById(R.id.buttonNew);
        Button btnQuit = (Button) findViewById(R.id.buttonQuit);
        Button btnResume = (Button) findViewById(R.id.buttonResume);
        Button btnSettings = (Button) findViewById(R.id.buttonSettings);
        Button btnRules = (Button) findViewById(R.id.buttonRules);
        TextView tvLoading = (TextView) findViewById(R.id.textViewLoading);

        btnNew.setVisibility(View.VISIBLE);
        btnQuit.setVisibility(View.VISIBLE);
        btnResume.setVisibility(View.VISIBLE);
        btnResume.setEnabled(fileGame != null);
        btnSettings.setVisibility(View.VISIBLE);
        btnRules.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.INVISIBLE);
    }

    public void onAnimationRepeat(Animation animation)
    {
        ;
    }

    public void onAnimationStart(Animation animation) {

        Button btnNew = (Button) findViewById(R.id.buttonNew);
        Button btnQuit = (Button) findViewById(R.id.buttonQuit);
        Button btnResume = (Button) findViewById(R.id.buttonResume);
        Button btnSettings = (Button) findViewById(R.id.buttonSettings);
        Button btnRules = (Button) findViewById(R.id.buttonRules);
        TextView tvLoading = (TextView) findViewById(R.id.textViewLoading);

        btnNew.setVisibility(View.INVISIBLE);
        btnQuit.setVisibility(View.INVISIBLE);
        btnResume.setVisibility(View.INVISIBLE);
        btnSettings.setVisibility(View.INVISIBLE);
        btnRules.setVisibility(View.INVISIBLE);
        tvLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(MainActivity.class.toString(), "onResume");

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
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
            case R.id.buttonSettings:
                {
                Log.v(MainActivity.class.toString(), "Starting SettingsActivity");

                Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(intent);
                }
                break;
            case R.id.buttonRules:
                {
                    Log.v(MainActivity.class.toString(), "Starting RulesActivity");

                    Intent intent = new Intent(v.getContext(), RulesActivity.class);
                    startActivity(intent);
                }
        }
    }
}
