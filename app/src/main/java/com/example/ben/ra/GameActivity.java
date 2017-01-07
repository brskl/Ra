package com.example.ben.ra;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;

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

    void PlayerHumanGodDialog()
    {
        Log.v(GameActivity.class.toString(), "Bringing up Player God dialog");

        Game game = Game.getInstance();
        Player player = game.getPlayerCurrent();
        Assert.assertTrue(game.FCanUseGod());
        Assert.assertTrue(player.getHuman());
        Assert.assertTrue(player.getLocal());


        // TODO: Maybe use MatrixCursor and provide to setMultiChoice
        String [] asTileChoices;
        AlertDialog.Builder builder;


        ArrayList<String> alGodChoices = new ArrayList<String>(Game.nMaxAuction_c);

        for (Game.Tile t: game.getAuction())
        {
            if ((t != Game.Tile.tGod) &&
                    (t != Game.Tile.tDisasterC) &&
                    (t != Game.Tile.tDisasterM) &&
                    (t != Game.Tile.tDisasterN) &&
                    (t != Game.Tile.tDisasterP))
            {
                alGodChoices.add(TileString(t));
            }
        }

        asTileChoices = new String [alGodChoices.size()];
        alGodChoices.toArray(asTileChoices);
        builder = new AlertDialog.Builder(this);
        // TODO: see if there is way for 'OK' button to be initially disabled
        builder.setTitle(R.string.TitleGodDialog);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int i;
                ListView lv;
                Game.Tile t;
                boolean fExchanged = false;

                Log.v(GameActivity.class.toString(), "God choice, Ok pressed");

                lv = ((AlertDialog) dialog).getListView();

                for (i = 0; i < lv.getCount(); i++)
                {
                    if (lv.isItemChecked(i))
                    {
                        t = StringTile((String) lv.getItemAtPosition(i));
                        if (Game.getInstance().DoExchangeForGod(t))
                            fExchanged = true;
                    }
                }
                if (fExchanged)
                    UpdateDisplay();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.v(GameActivity.class.toString(), "God choice, Cancel pressed");
                dialog.cancel();
            }
        });
        builder.setMultiChoiceItems(asTileChoices, null, new DialogInterface.OnMultiChoiceClickListener() {

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                Log.v(GameActivity.class.toString(), "God choice, item " + which + " checked " + isChecked);
                // enable/disable ok as appropriate

                Game game = Game.getInstance();
                int nChecked = 0;
                ListView lv;
                AlertDialog ad;
                Button btn;

                ad = (AlertDialog) dialog;
                lv = ad.getListView();
                nChecked = lv.getCheckedItemCount();
                btn = (Button) ad.getButton(AlertDialog.BUTTON_POSITIVE);
                if (0 < nChecked &&
                        nChecked <= game.getPlayerCurrent().getNTiles()[Game.Tile.tGod.ordinal()])
                    btn.setEnabled(true);
                else
                    btn.setEnabled(false);
            }
        });

        builder.show();
    }

    void PlayerHumanBidDialog()
    {
        Log.v(GameActivity.class.toString(), "Doing Live Player bid dialog");

        String [] asHumanBidChoices;
        ArrayList<String> alBidChoices = new ArrayList<String>(5); // TODO replace 5 with constant
        Game game = Game.getInstance();
        Player player = game.getAuctionPlayerCurrent();
        ArrayList<Integer> alSuns;

        Assert.assertTrue(game.FCanBid());
        Assert.assertTrue(player.getHuman());
        Assert.assertTrue(player.getLocal());

        alSuns = player.getSuns();

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

    void DoAuctionBid()
    {
        Log.v(GameActivity.class.toString(), "Doing auction bid");

        Game game = Game.getInstance();

        // Check if current player can bid
        if (!game.FCanBid())
            return;

        if (game.getAuctionPlayerCurrent().getHuman()) {
            PlayerHumanBidDialog();
        } else {
            PlayerAi player = (PlayerAi) game.getAuctionPlayerCurrent();
            Assert.assertFalse(player.getHuman());

            // Get AI decision on what to bid
            game.MakeBid(player.AiBid());
        }
    }

    void StartAuction(boolean fVoluntary)
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

    void DoEpochOver()
    {
        Game game = Game.getInstance();

        Assert.assertEquals(Game.Status.EpochOver, game.getStatusCurrent());
        Log.v(GameActivity.class.toString(), "DoEpochOver " + game.getEpoch());

        game.UpdateScore();

        Intent intent = new Intent(this, ScoreActivity.class);
        Log.v(GameActivity.class.toString(), "Starting ScoreActivity");
        startActivity(intent);
    }

    void onClickGame(View v){
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
                PlayerHumanGodDialog();
                break;
            case R.id.buttonTiles:
            {
                Log.v(GameActivity.class.toString(), "buttonTiles pressed");
                Log.v(GameActivity.class.toString(), "Starting TilesActivity");
                Intent intent = new Intent(v.getContext(), TilesActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.buttonScore:
            {
                Log.v(GameActivity.class.toString(), "buttonScore pressed");
                game.UpdateScore();
                Log.v(GameActivity.class.toString(), "Starting ScoreActivity");
                Intent intent = new Intent(v.getContext(), ScoreActivity.class);
                startActivity(intent);
            }
            break;

            default:
                Assert.fail("Should never Reach in onClickGame");
        }

        UpdateDisplay();
    }

    void onClickGameOk(View v)
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
                    StartAuction(false);
                }
                else
                {
                    Assert.assertFalse(game.getPlayerCurrent().getHuman());
                    Log.v(GameActivity.class.toString(), "Get AI decision on what to do");

                    // Get AI decision
                    // TODO: for now, just draw tile
                    game.DrawTile();
                }
                break;
            case DrewTile:
                if (game.getTileLastDrawn() == Game.Tile.tRa)
                {
                    if (game.TestEpochOver()) {
                        DoEpochOver();
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
            case UsedGod:
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
                if (!game.TestDisasters()) {
                    if (game.TestEpochOver()) {
                        DoEpochOver();
                    } else {
                        game.SetNextPlayerTurn();
                    }
                }
                break;
            case ResolveDisaster:
                if (game.ResolveDisastersAuto()) {
                    Player playerWinner = game.getAuctionPlayerHighest();
                    if (playerWinner.getHuman()) {
                        // bring up dialog

                        // TODO Remove
                        playerWinner.ResolveDisastersAi();
                    } else {
                        // call PlayerAI function
                        playerWinner.ResolveDisastersAi();
                    }
                }

                if (game.TestEpochOver()) {
                    DoEpochOver();
                } else {
                    game.SetNextPlayerTurn();
                }
                break;
            case EpochOver:
                if (game.SetupNextEpoch())
                {
                    // game over
                    Log.v(GameActivity.class.toString(), "Game over, finishing activity");
                    // TODO: exit activity, back to main menu, not player setup
                    finish();
                }
                break;
            default:
                Assert.fail();
        }
    }

    void UpdateDisplayPlayerNames(){
        TextView tv;
        Game game = Game.getInstance();
        final int atvIDs[] = {R.id.textViewNamePlayer1, R.id.textViewNamePlayer2, R.id.textViewNamePlayer3, R.id.textViewNamePlayer4, R.id.textViewNamePlayer5};

        for (int i = 0; i < game.getNPlayers(); i++)
        {
            tv = (TextView) findViewById(atvIDs[i]);
            tv.setText(getString(R.string.PlayerNamePlaceholder, game.aPlayers[i].getName()));
        }
    }

    void UpdateDisplayRound() {
        Game game = Game.getInstance();

        // current epoch
        tvEpoch.setText(String.format("%d", game.getEpoch()));

        // current number of Ra tiles
        tvRaTrackValue.setText(String.format("%d / %d", game.getRas(), game.getMaxRas()));

        // current player
        tvCurrentPlayer.setText(getResources().getString(R.string.CurrentPlayer, game.getPlayerCurrent().getName()));
    }

    void UpdateDisplayPlayerSuns(int iPlayer)
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

    void UpdateDisplayPlayersSuns()
    {
        Game game = Game.getInstance();

        for (int i = 0; i < game.getNPlayers(); i++)
        {
            UpdateDisplayPlayerSuns(i);
        }
    }

    void UpdateDisplayStatus(){
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
            case UsedGod:
                sStatus = getString(R.string.StatusUsedGod, game.getPlayerCurrent().getName());
                break;
            case ResolveDisaster:
                sStatus = getString(R.string.StatusResolveDisaster, game.getPlayerCurrent().getName());
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

    // TODO: better way to do this?
    private Game.Tile StringTile(String s)
    {
        int i;
        for (i = 0; i < sTiles.length; i++)
        {
            if (s.equals(sTiles[i]))
                return Game.Tile.values()[i];
        }
        return Game.Tile.tNone;
    }

    void UpdateDisplayAuction(){
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

    void UpdateDisplayButtons()
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
        btnDraw.setEnabled(!fOKonly && !game.FAuctionTrackFull());
        btnGod.setEnabled(!fOKonly && game.FCanUseGod());
    }

    void UpdateDisplay(){
        UpdateDisplayRound();
        UpdateDisplayPlayersSuns();
        UpdateDisplayStatus();
        UpdateDisplayAuction();
        UpdateDisplayButtons();
    }
}
