package com.example.ben.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.example.ben.ra.Game.getInstance;

public class GameActivity extends AppCompatActivity {

    private TextView tvEpoch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(GameActivity.class.toString(), "onCreate");

        tvEpoch = (TextView) findViewById(R.id.textViewEpochValue);

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
        final int tvIDs[] = {R.id.textViewNamePlayer1, R.id.textViewNamePlayer2, R.id.textViewNamePlayer3, R.id.textViewNamePlayer4, R.id.textViewNamePlayer5};

        for (int i = 0; i < game.getNPlayers(); i++)
        {
            tv = (TextView) findViewById(tvIDs[i]);
            tv.setText(getString(R.string.PlayerNamePlaceholder, game.aPlayers[i].getName()));
        }
    }

    protected void UpdateDisplayRound() {
        Game game = Game.getInstance();

        // current epoch
        tvEpoch.setText(String.format("%d", game.getEpoch()));

    }

    protected void UpdateDisplay(){
        UpdateDisplayRound();
    }
}
