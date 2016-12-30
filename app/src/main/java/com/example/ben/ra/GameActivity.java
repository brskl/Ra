package com.example.ben.ra;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

    protected void PlayerLiveBidDialog()
    {
        Log.v(GameActivity.class.toString(), "Doing Live Player bid dialog");

        String [] asHumanBidChoices;
        ArrayList<String> alBidChoices = new ArrayList<String>(5); // TODO replace 5 with constant
        Game game = Game.getInstance();
        ArrayList<Integer> alSuns;

        Assert.assertTrue(game.FCanBid());
        Assert.assertTrue(game.getAuctionPlayerCurrent().getHuman());
        Assert.assertTrue(game.getAuctionPlayerCurrent().getLocal());

        alSuns = game.getAuctionPlayerCurrent().getSuns();

        for (Integer iValue: alSuns)
        {
            if (iValue > game.getAuctionHighBid())
                alBidChoices.add(Integer.toString(iValue));
        }
        if (!game.FMustBid())
        {
            alBidChoices.add(getString(R.string.BidPass));
        }

        asHumanBidChoices = new String [alBidChoices.size()];
        alBidChoices.toArray(asHumanBidChoices);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.TitleBidDialog);
        builder.setCancelable(false);
        builder.setSingleChoiceItems(asHumanBidChoices, -1, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                String sBid;
                int iBidValue;
                Game game = Game.getInstance();
                ArrayList<Integer> alSuns = game.getAuctionPlayerCurrent().getSuns();

                Log.v(GameActivity.class.toString(), "Player bid dialog clicked item " + item);
                sBid = (String) ((AlertDialog) dialog).getListView().getItemAtPosition(item);
                if (sBid.equals(getString(R.string.BidPass)))
                    iBidValue = 0; // Pass
                else
                {
                    iBidValue = Integer.parseInt(sBid);

                }

                game.MakeBid(iBidValue);
                UpdateDisplay();
                dialog.dismiss();
            }

        });
        builder.show();

    }

    protected void DoAuctionBid()
    {
        Log.v(GameActivity.class.toString(), "Doing auction bid");

        Game game = Game.getInstance();

        // Check if current player can bid
        if (!game.FCanBid())
            return;

        if (game.getAuctionPlayerCurrent().getHuman()) {
            PlayerLiveBidDialog();
        } else {
            Player player = game.getAuctionPlayerCurrent();
            Assert.assertFalse(player.getHuman());

            // Get AI decision
            game.MakeBid(player.AiBid());
        }
      }

    protected void StartAuction(boolean fVoluntary)
    {
        Log.v(GameActivity.class.toString(), "Auction started, voluntary " + fVoluntary);

        Game game = Game.getInstance();

        Assert.assertTrue(( game.getStatusCurrent() == Game.Status.TurnStart) ||
                          ((game.getStatusCurrent() == Game.Status.DrewTile) &&
                           (game.getTileLastDrawn() == Game.Tile.tRa)));

        game.InitAuction(fVoluntary);
        game.SetNextPlayerAuction();

        DoAuctionBid();
    }

    public void onClickGame(View v){
        Log.d(GameActivity.class.toString(), "onClickGame");

        Game game = Game.getInstance();

        switch(v.getId()) {
            case R.id.buttonOK:
                Log.v(GameActivity.class.toString(), "buttonOK pressed");
                onClickGameOk(v);
                break;
            case R.id.buttonAuction:
                Log.v(GameActivity.class.toString(), "buttonAuction pressed");
                // if auction track full, auction is not voluntary
                StartAuction(!game.FAuctionTrackFull());
                break;
            case R.id.buttonDraw:
                Log.v(GameActivity.class.toString(), "buttonDraw pressed");
                Assert.assertFalse(game.FAuctionTrackFull());
                game.DrawTile();
                break;
            case R.id.buttonGod:
                Log.v(GameActivity.class.toString(), "buttonGod pressed");
        //        PlayerGodDialog(); // ???
                break;
            case R.id.buttonTiles:
            {
                Log.v(GameActivity.class.toString(), "Starting RaTilesActivity");
                Intent intent = new Intent(v.getContext(), TilesActivity.class);
                startActivity(intent);
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
            case DrewTile:
                if (game.getTileLastDrawn() == Game.Tile.tRa)
                {
                    if (game.TestEpochOver()) {
                        //                   DoEpochOver();
                        ;
                    } else {
                        StartAuction(false);
                    }
                } else {
                    game.SetNextPlayerTurn();
                }
                break;
            case AuctionEveryonePassed:
                game.SetNextPlayerTurn();
                break;
            case CallsAuction:
                StartAuction(true);
                break;
            case AuctionInProgress:
                if (game.FAuctionFinished())
                {
                    game.ResolveAuction();
                }
                else
                {
                    game.SetNextPlayerAuction();
                    DoAuctionBid();
                }
                break;
            case AuctionWon:
                if (game.TestEpochOver()) {
                    // TODO: DoEpochOver()
                    ;
                } else {
                    game.SetNextPlayerTurn();
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
        String sStatus = null;

        switch(game.getStatusCurrent())
        {
            case TurnStart:
                sStatus = getString(R.string.StatusTurnStart, game.getPlayerCurrent().getName());
                break;
            case DrewTile:
                sStatus = getString(R.string.StatusDrewTile, game.getPlayerCurrent().getName(), TileString(game.getTileLastDrawn()));
                break;
            case EpochOver:
                sStatus = getString(R.string.StatusEpochOver, game.getEpoch());
                break;
            case CallsAuction:
                sStatus = getString(R.string.StatusCallsAuction, game.getPlayerCurrent().getName());
                break;
            case AuctionInProgress:
                if (game.FAuctionCurrentPlayerBidHighest()) {
                    sStatus = getString(R.string.StatusAuctionPlayerBid, game.getAuctionPlayerCurrent().getName(), game.getAuctionHighBid());
                } else {
                    sStatus = getString(R.string.StatusAuctionPlayerPassed, game.getAuctionPlayerCurrent().getName());
                }
                break;
            case AuctionWon:
                sStatus = getString(R.string.StatusAuctionWon, game.getAuctionPlayerHighest().getName());
                break;
            case AuctionEveryonePassed:
                sStatus = getString(R.string.StatusAuctionEveryonePassed);
                break;
            default:
                // TODO replace with assert
                sStatus = "Not Yet Implemented";
                break;
        }

        Assert.assertNotNull("Illegal Status", sStatus);
        tvStatus.setText(sStatus);
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
