package com.benjaminsklar.ra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import junit.framework.Assert;

public class NewGameActivity extends AppCompatActivity {
    private int nPlayers;
    private boolean [] afHuman = new boolean [Game.nMaxPlayers_c];
    private String [] asNames = new String[Game.nMaxPlayers_c];
    private int [] aiAILevel = new int[Game.nMaxPlayers_c];
    private long lSeed;
    private boolean fSeed = false;

    private final int [] aitbPlayers = { R.id.toggleButtonPlayer1, R.id.toggleButtonPlayer2, R.id.toggleButtonPlayer3, R.id.toggleButtonPlayer4, R.id.toggleButtonPlayer5 } ;
    private final int [] aietPlayers = {R.id.editText1, R.id.editText2, R.id.editText3, R.id.editText4, R.id.editText5 };
    private final String [] aisPrefNames = { "Player1Name", "Player2Name", "Player3Name", "Player4Name", "Player5Name" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Log.d(NewGameActivity.class.toString(), "onCreate");

        ToggleButton tb;
        EditText et;
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupNumPlayers);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String asNamesAiEasy[] = getResources().getStringArray(R.array.NamesAiEasy);
        String asNamesAiMedium[] = getResources().getStringArray(R.array.NamesAiMedium);
        String asNamesAiHard[] = getResources().getStringArray(R.array.NamesAiHard);

        nPlayers = settings.getInt("NumPlayers", 3);
        afHuman[0] = true;
        afHuman[1] = settings.getBoolean("Player2Human", false);
        afHuman[2] = settings.getBoolean("Player3Human", false);
        afHuman[3] = settings.getBoolean("Player4Human", false);
        afHuman[4] = settings.getBoolean("Player5Human", false);

        for (int i = 0; i < Game.nMaxPlayers_c; i++)
        {
            if (afHuman[i])
                asNames[i] = settings.getString(aisPrefNames[i], this.getResources().getString(R.string.defaultName));
        }

        aiAILevel[0] = 0;
        aiAILevel[1] = 0;
        aiAILevel[2] = 0;
        aiAILevel[3] = 0;
        aiAILevel[4] = 0;


        switch(nPlayers)
        {
            default:
                Log.w(NewGameActivity.class.toString(), "Preference for number of players was invalid");
                nPlayers = 3;
                // fall through
            case 3:
                rg.check(R.id.radioPlayers3);
                break;
            case 4:
                rg.check(R.id.radioPlayers4);
                break;
            case 5:
                rg.check(R.id.radioPlayers5);
                break;
        }

        for (int i = 0; i < Game.nMaxPlayers_c; i++)
        {
            tb = (ToggleButton) findViewById(aitbPlayers[i]);
            tb.setChecked(afHuman[i]);
            if (!afHuman[i])
            {
                asNames[i] = AIname(i);
            }
            et = (EditText) findViewById(aietPlayers[i]);
            et.setText(asNames[i]);
        }

        fSeed = settings.getBoolean("SeedEnabled", false);

        if (fSeed) {
            lSeed = settings.getLong("Seed", 0);
            et = (EditText) findViewById(R.id.editTextSeed);
            et.setText(String.format("%d", lSeed));
        }

        EnableUx();
    }

    private String AIname(int iPlayer)
    {
        final String asNamesAiEasy[] = getResources().getStringArray(R.array.NamesAiEasy);
        final String asNamesAiMedium[] = getResources().getStringArray(R.array.NamesAiMedium);
        final String asNamesAiHard[] = getResources().getStringArray(R.array.NamesAiHard);

        switch(aiAILevel[iPlayer])
        {
            default:
                Log.w(NewGameActivity.class.toString(), "AI level was invalid for player " + (iPlayer+1));
                aiAILevel[iPlayer] = 0;
                // fall through
            case 0:
                return asNamesAiEasy[iPlayer];
            case 1:
                return asNamesAiMedium[iPlayer];
            case 2:
                return asNamesAiHard[iPlayer];
        }
    }

    private void EnableUx()
    {
        View et;
        ToggleButton tb;
        LinearLayout linearLayout;

        for (int i = 0; i < Game.nMaxPlayers_c; i++)
        {
            tb = (ToggleButton) findViewById(aitbPlayers[i]);
            et = findViewById(aietPlayers[i]);
            et.setEnabled(tb.isChecked());
            if (i >= 3)
            {
                if (i < nPlayers)
                {
                    tb.setVisibility(View.VISIBLE);
                    et.setVisibility(View.VISIBLE);
                }
                else
                {
                    tb.setVisibility(View.INVISIBLE);
                    et.setVisibility(View.INVISIBLE);
                }
            }
        }

        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeed);
        if (fSeed) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    private void SetupClickToggleButton(View v, int iPlayer)
    {
        EditText et;

        afHuman[iPlayer] = ((ToggleButton) v).isChecked();
        et = (EditText) findViewById(aietPlayers[iPlayer]);
        if (!afHuman[iPlayer])
        {
            asNames[iPlayer] = AIname(iPlayer);
            et.setText(asNames[iPlayer]);
        }
        // TODO: else if new human, make the text field have the focus
    }

    private void SavePreferences()
    {
        EditText et;

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("NumPlayers", nPlayers);
        editor.putBoolean("Player2Human", afHuman[1]);
        editor.putBoolean("Player3Human", afHuman[2]);
        editor.putBoolean("Player4Human", afHuman[3]);
        editor.putBoolean("Player5Human", afHuman[4]);
        for (int i = 0; i < Game.nMaxPlayers_c; i++)
        {
            if (afHuman[i])
            {
                et = (EditText) findViewById(aietPlayers[i]);
                asNames[i] = et.getText().toString();
                editor.putString(aisPrefNames[i], asNames[i]);
            }
        }

        if (fSeed) {
            et = (EditText) findViewById(R.id.editTextSeed);
            lSeed = Long.parseLong(et.getText().toString());
            editor.putLong("Seed", lSeed);
        }

        editor.apply();
    }

    private void StartGame(View v)
    {
        // Setup Game instance
        Game game = Game.getInstance();

        if (fSeed) {
            game.setRandomSeed(lSeed);
        }

        game.initialize(nPlayers);
        for (int i = 0; i < nPlayers; i++) {
            game.setPlayer(i, asNames[i], true, afHuman[i], aiAILevel[i]);
        }
        game.initializeSuns();

        // Start GameActivity
        Intent intent = new Intent(v.getContext(), GameActivity.class);
        startActivity(intent);

        finish();
    }

    void onClickNewGameRadio(View v)
    {
        Log.d(NewGameActivity.class.toString(), "onClickNewGameRadio");

        switch(v.getId()) {
            case R.id.radioPlayers3:
                Log.v(NewGameActivity.class.toString(), "radioPlayers3 pressed");
                nPlayers = 3;
                break;
            case R.id.radioPlayers4:
                Log.v(NewGameActivity.class.toString(), "radioPlayers4 pressed");
                nPlayers = 4;
                break;
            case R.id.radioPlayers5:
                Log.v(NewGameActivity.class.toString(), "radioPlayers5 pressed");
                nPlayers = 5;
                break;
        }

        EnableUx();
    }

    void onClickNewGameToggleButton(View v)
    {
        Log.d(NewGameActivity.class.toString(), "onClickNewGameToggleButton");

        switch(v.getId()) {
            case R.id.toggleButtonPlayer1:
                Assert.fail("CheckBox1 clicked, should never be enabled");
                break;
            case R.id.toggleButtonPlayer2:
                Log.v(NewGameActivity.class.toString(), "checkBox2 pressed");
                SetupClickToggleButton(v, 1);
                break;
            case R.id.toggleButtonPlayer3:
                Log.v(NewGameActivity.class.toString(), "checkBox3 pressed");
                SetupClickToggleButton(v, 2);
                break;
            case R.id.toggleButtonPlayer4:
                Log.v(NewGameActivity.class.toString(), "checkBox4 pressed");
                SetupClickToggleButton(v, 3);
                break;
            case R.id.toggleButtonPlayer5:
                Log.v(NewGameActivity.class.toString(), "checkBox5 pressed");
                SetupClickToggleButton(v, 4);
                break;
        }
        EnableUx();
    }

    void onClickNewGameButton(View v)
    {
        Log.d(NewGameActivity.class.toString(), "onClickNewGameButton");

        switch(v.getId()) {
            case R.id.buttonCancel:
                Log.v(NewGameActivity.class.toString(), "buttonCancel pressed");
                finish(); // TODO: return cancel request code
                break;
            case R.id.buttonOK:
                Log.v(NewGameActivity.class.toString(), "buttonOK pressed");
                SavePreferences();

                // TODO: Consider deleting default game file.

                StartGame(v);

        }
    }
}
