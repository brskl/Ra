package com.example.ben.ra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;

import static com.example.ben.ra.Game.getInstance;

public class GameActivity extends AppCompatActivity {
    // Must be in same order as RaGame.Tile enum
    // All 2 characters for formatting
    private final String sTiles [] = {
            "",		// none
            "Ra",	// Ra
            "G ",	// God
            "Au",	// Gold
            "P ",	// Pharaoh
            "N ",	// Nile
            "NF",	// Nile Flood
            "C1", "C2", "C3", "C4", "C5",	// Civs
            "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", // Monuments
            "DP", "DN", "DC", "DM"	// Disasters (Pharaoh, Nile, Civs, Monuments)
    };


    private TextView tvEpoch;
    private TextView tvStatus;
    private TextView tvCurrentPlayer;
    private TextView tvRaTrackValue;
    private TextView tvAuctionItems;
    private TextView atvPlayerSuns[] = new TextView[Game.nMaxPlayers_c];
    private Button btnOk;
    private Button btnAuction;
    private Button btnDraw;
    private Button btnGod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(GameActivity.class.toString(), "onCreate");

        tvEpoch = (TextView) findViewById(R.id.textViewEpochValue);
        tvStatus = (TextView) findViewById(R.id.textViewStatus);
        tvCurrentPlayer = (TextView) findViewById(R.id.textViewCurrentPlayer);
        tvRaTrackValue = (TextView) findViewById(R.id.textViewRaTrackValue);
        tvAuctionItems = (TextView) findViewById(R.id.textViewAuctionItems);
        atvPlayerSuns[0] = (TextView) findViewById(R.id.textViewSunsPlayer1);
        atvPlayerSuns[1] = (TextView) findViewById(R.id.textViewSunsPlayer2);
        atvPlayerSuns[2] = (TextView) findViewById(R.id.textViewSunsPlayer3);
        atvPlayerSuns[3] = (TextView) findViewById(R.id.textViewSunsPlayer4);
        atvPlayerSuns[4] = (TextView) findViewById(R.id.textViewSunsPlayer5);
        btnOk = (Button) findViewById(R.id.buttonOK);
        btnAuction = (Button) findViewById(R.id.buttonAuction);
        btnDraw = (Button) findViewById(R.id.buttonDraw);
        btnGod = (Button) findViewById(R.id.buttonGod);

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

        Game game = Game.getInstance();

        switch(v.getId()) {
            case R.id.buttonOK:
                Log.v("UI", "buttonOK pressed");
                onClickGameOk(v);
                break;
            case R.id.buttonAuction:
                Log.v("UI", "buttonAuction pressed");
                // if auction track full, auction is not voluntary
        //         StartAuction(!game.FAuctionTrackFull());
                break;
            case R.id.buttonDraw:
                Log.v("UI", "buttonDraw pressed");
                Assert.assertFalse(game.FAuctionTrackFull());
                game.DrawTile();
                break;
            case R.id.buttonGod:
                Log.v("UI", "buttonGod pressed");
         //       PlayerGodDialog(); // ???
                break;
            case R.id.buttonTiles:
            {
                Log.v("UI", "Starting RaTilesActivity");
        //        Intent intent = new Intent(v.getContext(), RaTilesActivity.class);
        //        startActivity(intent);
            }
            break;
        }

        UpdateDisplay();
    }

    public void onClickGameOk(View v)
    {
        Log.d(GameActivity.class.toString(), "onClickGameOk");

        Game game = Game.getInstance();
        switch (game.getStatusCurrent())
        {
            case TurnStart:
                Assert.assertFalse(game.getPlayerCurrent().getHuman());
                if (game.FAuctionTrackFull())
                {
                    Log.v(GameActivity.class.toString(), "start in-voluntaary auction");
                    // TODO: Add code to call StartAuction()
                }
                else
                {
                    // Get AI decision
                    // TODO: for now, just draw tile
                    Log.v(GameActivity.class.toString(), "Get AI decision on what to do");
                    // TODO: add code to draw tile
                    game.DrawTile();
                }
                break;
            default:
                Assert.fail();
        }
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

        // current number of Ra tiles
        tvRaTrackValue.setText(String.format("%d / %d", game.getRas(), game.getMaxRas()));

        // current player
        tvCurrentPlayer.setText(getResources().getString(R.string.CurrentPlayer, game.getPlayerCurrent().getName()));
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

    protected void UpdateDisplayStatus(){
        Game game = Game.getInstance();

        switch(game.getStatusCurrent())
        {
            case TurnStart:
                tvStatus.setText(getString(R.string.StatusTurnStart, game.getPlayerCurrent().getName()));
                break;
            default:
                // TODO replace with assert
                tvStatus.setText("Not Yet Implemented");
                break;
        }
    }

    private String TileString(Game.Tile etValue)
    {
        return sTiles[etValue.ordinal()];
    }

    protected void UpdateDisplayAuction(){
        Game game = Game.getInstance();

        StringBuilder sbAuction = new StringBuilder();

        // add current sun
        sbAuction.append(String.format("%2d; ", game.getAtAuctionSun()));

        // add current tiles
        for (Game.Tile tile: game.getAuction())
        {
            sbAuction.append(TileString(tile) + " ");
        }

        tvAuctionItems.setText(sbAuction.toString());
    }

    protected void UpdateDisplayButtons()
    {
        boolean fCurrentPlayerLocalHuman;
        boolean fOKonly = true;
        Game game = Game.getInstance();

        fCurrentPlayerLocalHuman = (game.getPlayerCurrent().getHuman() && game.getPlayerCurrent().getLocal());
        if (fCurrentPlayerLocalHuman)
        {
            if (game.getStatusCurrent() == Game.Status.TurnStart)
                fOKonly = false;
        }

        btnOk.setEnabled(fOKonly);
        btnAuction.setEnabled(!fOKonly);
        // TODO add Draw button
        // TODO add God button
    }

    protected void UpdateDisplay(){
        UpdateDisplayRound();
        UpdateDisplayPlayersSuns();
        UpdateDisplayStatus();
        UpdateDisplayAuction();
        UpdateDisplayButtons();
    }
}
