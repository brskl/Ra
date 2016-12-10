package com.example.ben.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static com.example.ben.ra.Game.getInstance;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(GameActivity.class.toString(), "onCreate");

        SetNumplayerUI();
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
}
