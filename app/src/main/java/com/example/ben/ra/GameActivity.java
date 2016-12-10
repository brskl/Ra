package com.example.ben.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.example.ben.ra.Game.getInstance;

public class GameActivity extends AppCompatActivity {

    private TextView tvEpoch;
    private TextView tvStatus;
    private TextView tvCurrentPlayer;
    private TextView atvPlayerSuns[] = new TextView[Game.nMaxPlayers_c];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(GameActivity.class.toString(), "onCreate");

        tvEpoch = (TextView) findViewById(R.id.textViewEpochValue);
        tvStatus = (TextView) findViewById(R.id.textViewStatus);
        tvCurrentPlayer = (TextView) findViewById(R.id.textViewCurrentPlayer);
        atvPlayerSuns[0] = (TextView) findViewById(R.id.textViewSunsPlayer1);
        atvPlayerSuns[1] = (TextView) findViewById(R.id.textViewSunsPlayer2);
        atvPlayerSuns[2] = (TextView) findViewById(R.id.textViewSunsPlayer3);
        atvPlayerSuns[3] = (TextView) findViewById(R.id.textViewSunsPlayer4);
        atvPlayerSuns[4] = (TextView) findViewById(R.id.textViewSunsPlayer5);

        SetNumplayerUI();
        UpdateDisplayPlayerNames();

        UpdateDisplay();
    }

    private void SetNumplayerUI() {
        View v;
        Game game = Game.getInstance();

        switch (game.getNPlayers()) {
            case 3:
                v = findViewById(R.id.textViewNamePlayer4);
                v.setVisibility(View.GONE);
                v = findViewById(R.id.textViewSunsPlayer4);
                v.setVisibility(View.GONE);
                // fall through
            case 4:
                v = findViewById(R.id.textViewNamePlayer5);
                v.setVisibility(View.GONE);
                v = findViewById(R.id.textViewSunsPlayer5);
                v.setVisibility(View.GONE);
                break;
            default:
                // do nothing
                break;
        }
    }

    public void onClickGame(View v){
        Log.d(GameActivity.class.toString(), "onClickGame");

    }

    protected void UpdateDisplayPlayerNames(){
        TextView tv;
        Game game = Game.getInstance();
        final int atvIDs[] = {R.id.textViewNamePlayer1, R.id.textViewNamePlayer2, R.id.textViewNamePlayer3, R.id.textViewNamePlayer4, R.id.textViewNamePlayer5};

        for (int i = 0; i < game.getNPlayers(); i++)
        {
            tv = (TextView) findViewById(atvIDs[i]);
            tv.setText(getString(R.string.PlayerNamePlaceholder, game.aPlayers[i].getName()));
        }
    }

    protected void UpdateDisplayRound() {
        Game game = Game.getInstance();

        // current epoch
        tvEpoch.setText(String.format("%d", game.getEpoch()));

        // current player
        tvCurrentPlayer.setText(getResources().getString(R.string.CurrentPlayer, game.getPlayerCurrent()+1));
    }

    protected void UpdateDisplayPlayerSuns(int iPlayer)
    {
        Game game = Game.getInstance();
        StringBuilder sb = new StringBuilder();
        boolean fFirst = true;
        int i;

        for (i = 0; i < game.aPlayers[iPlayer].getSuns().size(); i++)
        {
            if (fFirst)
                fFirst = false;
            else
                sb.append(", ");
            sb.append(game.aPlayers[iPlayer].getSuns().get(i).toString());
        }

        sb.append(" : ");
        fFirst = true;
        for (i = 0; i < game.aPlayers[iPlayer].getSunsNext().size(); i++)
        {
            if (fFirst)
                fFirst = false;
            else
                sb.append(", ");
            sb.append(game.aPlayers[iPlayer].alSunsNext.get(i).toString());
        }
        atvPlayerSuns[iPlayer].setText(sb.toString());
    }

    protected void UpdateDisplayPlayersSuns()
    {
        Game game = Game.getInstance();

        for (int i = 0; i < game.getNPlayers(); i++)
        {
            UpdateDisplayPlayerSuns(i);
        }
    }

    protected void UpdateDisplay(){
        UpdateDisplayRound();
        UpdateDisplayPlayersSuns();
    }
}
