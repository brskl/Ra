package com.example.ben.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class NewGameActivity extends AppCompatActivity {
    private int nPlayers;
    private boolean [] afHuman = new boolean [Game.nMaxPlayers_c];
    private String [] asNames = new String[Game.nMaxPlayers_c];
    private int [] aiAILevel = new int[Game.nMaxPlayers_c];

    private final int [] aicbPlayers = { R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5 } ;
    private final int [] aietPlayers = {R.id.editText1, R.id.editText2, R.id.editText3, R.id.editText4, R.id.editText5 };
    private final String [] aisPrefNames = { "Player1Name", "Player2Name", "Player3Name", "Player4Name", "Player5Name" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Log.d(NewGameActivity.class.toString(), "onCreate");
    }

    public void onClickNewGameRadio(View v)
    {
        Log.d(NewGameActivity.class.toString(), "onClickNewGameRadio");

        switch(v.getId()) {
        }
    }

    public void onClickNewGameCheckbox(View v)
    {
        Log.d(NewGameActivity.class.toString(), "onClickNewGameCheckbox");

        switch(v.getId()) {
        }
    }

    public void onClickNewGameButton(View v)
    {
        Log.d(NewGameActivity.class.toString(), "onClickNewGameButton");

        switch(v.getId()) {
        }
    }
}
