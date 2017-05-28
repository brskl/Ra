package com.benjaminsklar.ra;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private GameActivityUpdate gameActivityUpdate;

    // Must be in same order as RaGame.Tile enum
    // All 2 characters for formatting
    private String [] sTiles; // loaded from R.array.Tiles

    // TODO: make individualized images for Civ 1-5 and Mon 1-8
    // Image resource id for each kind of tile, must be in same order as enum
    private static final int aiTileImageRes_c [] = {
            0, // none
            R.drawable.tile_ra, // Ra
            R.drawable.tile_god, // God
            R.drawable.tile_gold,	// Gold
            R.drawable.tile_pharoah,	// Pharaoh
            R.drawable.tile_nile, R.drawable.tile_nile_flood,	// Nile, Flooding Nile
            R.drawable.tile_civ, R.drawable.tile_civ, R.drawable.tile_civ, R.drawable.tile_civ, R.drawable.tile_civ,	// Civilization tiles
            R.drawable.tile_monument, R.drawable.tile_monument, R.drawable.tile_monument, R.drawable.tile_monument, R.drawable.tile_monument, R.drawable.tile_monument, R.drawable.tile_monument, R.drawable.tile_monument,	// Monument tiles
            R.drawable.tile_pharoah_disaster, R.drawable.tile_nile_flood_disaster, R.drawable.tile_civ_disaster, R.drawable.tile_monument_disaster // Disaster tiles (Pharaoh, Nile/Flood, Civ, Monument)
    };

    boolean fAnimationEnabled = true;
    boolean fBiddingInProgress = false;
    RelativeLayout arlPlayers[] =new RelativeLayout[Game.nMaxPlayers_c];
    LinearLayout allPlayerSuns[] = new LinearLayout[Game.nMaxPlayers_c];
    LinearLayout allPlayerSunsNext[] = new LinearLayout[Game.nMaxPlayers_c];
    ImageView aivRaTiles[] = new ImageView[Game.nMaxRas_c];
    ImageView aivAuctionItems[] = new ImageView[Game.nMaxAuction_c];
    RelativeLayout rlBoard, rlAuction;
    LinearLayout llGameActivity;
    ImageButton btnDraw;

    GameActivityAnimationTile animationTile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(GameActivity.class.toString(), "onCreate");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        fAnimationEnabled = settings.getBoolean(SettingsActivity.sSettingsAnimationEnabled, true);
        gameActivityUpdate = new GameActivityUpdate(this);
        gameActivityUpdate.onCreate();

        sTiles = getResources().getStringArray(R.array.Tiles);

        arlPlayers[0] = (RelativeLayout) findViewById(R.id.relativeLayoutPlayer1);
        arlPlayers[1] = (RelativeLayout) findViewById(R.id.relativeLayoutPlayer2);
        arlPlayers[2] = (RelativeLayout) findViewById(R.id.relativeLayoutPlayer3);
        arlPlayers[3] = (RelativeLayout) findViewById(R.id.relativeLayoutPlayer4);
        arlPlayers[4] = (RelativeLayout) findViewById(R.id.relativeLayoutPlayer5);
        allPlayerSuns[0] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer1);
        allPlayerSuns[1] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer2);
        allPlayerSuns[2] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer3);
        allPlayerSuns[3] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer4);
        allPlayerSuns[4] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer5);
        allPlayerSunsNext[0] = (LinearLayout) findViewById(R.id.linearLayoutPlayerSunsNextPlayer1);
        allPlayerSunsNext[1] = (LinearLayout) findViewById(R.id.linearLayoutPlayerSunsNextPlayer2);
        allPlayerSunsNext[2] = (LinearLayout) findViewById(R.id.linearLayoutPlayerSunsNextPlayer3);
        allPlayerSunsNext[3] = (LinearLayout) findViewById(R.id.linearLayoutPlayerSunsNextPlayer4);
        allPlayerSunsNext[4] = (LinearLayout) findViewById(R.id.linearLayoutPlayerSunsNextPlayer5);
        aivRaTiles[0] = (ImageView) findViewById(R.id.ivRa0);
        aivRaTiles[1] = (ImageView) findViewById(R.id.ivRa1);
        aivRaTiles[2] = (ImageView) findViewById(R.id.ivRa2);
        aivRaTiles[3] = (ImageView) findViewById(R.id.ivRa3);
        aivRaTiles[4] = (ImageView) findViewById(R.id.ivRa4);
        aivRaTiles[5] = (ImageView) findViewById(R.id.ivRa5);
        aivRaTiles[6] = (ImageView) findViewById(R.id.ivRa6);
        aivRaTiles[7] = (ImageView) findViewById(R.id.ivRa7);
        aivRaTiles[8] = (ImageView) findViewById(R.id.ivRa8);
        aivRaTiles[9] = (ImageView) findViewById(R.id.ivRa9);

        rlBoard = (RelativeLayout) findViewById(R.id.relativeLayoutBoard);
        rlAuction = (RelativeLayout) findViewById(R.id.relativeLayoutAuction);
        llGameActivity = (LinearLayout) findViewById(R.id.activity_game);

        btnDraw = (ImageButton) findViewById(R.id.buttonDraw);



        SetNumplayerUI();
        gameActivityUpdate.UpdateDisplayPlayerNames();

        gameActivityUpdate.UpdateDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(GameActivity.class.toString(), "onResume");

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(GameActivity.class.toString(), "onStop");
        Game game = Game.getInstance();

        if (!game.FGameOver()) {
            game.getInstance().saveToFile(this, "Ra.game");
        }
    }

    private void SetNumplayerUI() {
        View v;
        Game game = Game.getInstance();
        int iChild;

        switch (game.getNPlayers()) {
            case 3:
                v = findViewById(R.id.relativeLayoutPlayer4);
                v.setVisibility(View.GONE);
                aivRaTiles[8].setVisibility(View.GONE);
                // fall through
            case 4:
                v = findViewById(R.id.relativeLayoutPlayer5);
                v.setVisibility(View.GONE);
                aivRaTiles[9].setVisibility(View.GONE);
                break;
            default:
                // do nothing
                break;
        }

        // Remove from layout sun tile spots not used
        for (iChild = game.getSunsPerPlayer(); iChild < Game.nMaxSunsPerPlayer; iChild++) {
            int iPlayer;
            for (iPlayer = 0; iPlayer < Game.nMaxPlayers_c; iPlayer++) {
                v = allPlayerSuns[iPlayer].getChildAt(iChild);
                v.setVisibility(View.GONE);
                v = allPlayerSunsNext[iPlayer].getChildAt(iChild);
                v.setVisibility(View.GONE);
            }
        }
    }

    String [] MakeTileList(int [] anTiles, Game.Tile tStart, Game.Tile tEnd )
    {
        ArrayList<String> alTileChoices = new ArrayList<String>();
        String [] asResult;

        for (int tc = tStart.ordinal(); tc <= tEnd.ordinal(); tc++)
        {
            for (int j = 0; j < anTiles[tc]; j++)
            {
                alTileChoices.add(TileString(tc));
            }
        }
        Assert.assertTrue(alTileChoices.size() > Game.iTilesLostPerDisaster_c);
        asResult = new String [alTileChoices.size()];
        alTileChoices.toArray(asResult);

        return asResult;
    }

    void PlayerHumanDisasterDialog(String [] asTileChoices)
    {
        Log.v(GameActivity.class.toString(), "Starting PlayerHumanDisasterDialog");

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // TODO: consider: modify title and MultiChoiceItems to do 2*#disaster tiles instead of constant 2
        builder.setTitle(R.string.TitleDisasterDialog);
        builder.setCancelable(false);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int i;
                ListView lv;
                Game.Tile t1 = Game.Tile.tNone, t2 = Game.Tile.tNone;
                Game game = Game.getInstance();

                Log.v(GameActivity.class.toString(), "Tile disaster choice, Ok pressed");
                lv = ((AlertDialog) dialog).getListView();
                Assert.assertEquals(Game.iTilesLostPerDisaster_c, lv.getCheckedItemCount());
                for (i = 0; i < lv.getCount(); i++)
                {
                    if (lv.isItemChecked(i)) {
                        if (t1 == Game.Tile.tNone) {
                            t1 = StringTile((String) lv.getItemAtPosition(i));
                        } else {
                            t2 = StringTile((String) lv.getItemAtPosition(i));
                            break;
                        }
                    }
                }

                Log.v (GameActivity.class.toString(), "Resolving disaster with " + TileString(t1) + ", " + TileString(t2));

                if (Game.FCivTile(t1)) {
                    game.ResolveDisasterCiv(t1, t2);
                } else {
                    Assert.assertTrue(Game.FMonumentTile(t1));
                    game.ResolveDisasterMonument(t1, t2);
                }

                if (!game.TestDisasters()) {
                    Assert.assertEquals(Game.Status.ResolveDisasterCompleted, game.getStatusCurrent());
                    gameActivityUpdate.UpdateDisplay();
                }
                dialog.dismiss();
            }
        });

        builder.setMultiChoiceItems(asTileChoices, null, new DialogInterface.OnMultiChoiceClickListener() {

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                Log.v(GameActivity.class.toString(), "Tile disaster choice, item " + which + " checked " + isChecked);
                // enable/disable ok as appropriate

                int nChecked = 0;
                ListView lv;
                AlertDialog ad;
                Button btn;

                ad = (AlertDialog) dialog;
                lv = ad.getListView();
                nChecked = lv.getCheckedItemCount();
                btn = (Button) ad.getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setEnabled(nChecked == 2);
            }
        });

        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                AlertDialog ad;
                Button btn;

                ad = (AlertDialog) dialog;
                btn = (Button) ad.getButton(AlertDialog.BUTTON_POSITIVE);

                btn.setEnabled(false);
            }
        });

        dialog.show();
    }

    void PlayerHumanDisasterHandling()
    {
        Game game = Game.getInstance();
        Player playerWinner = game.getAuctionPlayerHighest();
        int [] anTiles = playerWinner.getNTiles();
        String [] asTileChoices = null;

        Assert.assertTrue(playerWinner.getHuman());
        Assert.assertTrue((anTiles[Game.Tile.tDisasterC.ordinal()] > 0) ||
                          (anTiles[Game.Tile.tDisasterM.ordinal()] > 0));

        Log.v(GameActivity.class.toString(), "Handling disasters manually for human player " + playerWinner.getName());

        if (anTiles[Game.Tile.tDisasterC.ordinal()] > 0)
        {
            asTileChoices = MakeTileList(anTiles, Game.Tile.tCiv1, Game.Tile.tCiv5);
        } else {
            Assert.assertTrue(anTiles[Game.Tile.tDisasterM.ordinal()] > 0);
            asTileChoices = MakeTileList(anTiles, Game.Tile.tMon1, Game.Tile.tMon8);
        }

        Assert.assertNotNull(asTileChoices);
        PlayerHumanDisasterDialog(asTileChoices);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);;
        AlertDialog dialog;
        ArrayList<String> alGodChoices = new ArrayList<String>(Game.nMaxAuction_c);
        int nGodTiles = game.getPlayerCurrent().getNTiles()[Game.Tile.tGod.ordinal()];

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
        builder.setTitle(getResources().getQuantityString(R.plurals.TitleGodDialog, nGodTiles, nGodTiles));
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
                    gameActivityUpdate.UpdateDisplay();
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

        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                AlertDialog ad;
                Button btn;

                ad = (AlertDialog) dialog;
                btn = (Button) ad.getButton(AlertDialog.BUTTON_POSITIVE);

                btn.setEnabled(false);
            }
        });

        dialog.show();
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
        fBiddingInProgress = true;

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
                fBiddingInProgress = false;
                gameActivityUpdate.UpdateDisplay();
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
                if (fAnimationEnabled) {
                    animationTile = new GameActivityAnimationTile(this);
                    animationTile.initializeDrawOne();
                    animationTile.startNow();
                }
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

        gameActivityUpdate.UpdateDisplay();
    }

    void onClickGameOk(View v)
    {
        Log.d(GameActivity.class.toString(), "onClickGameOk");

        if (animationTile != null) {
            animationTile.cancel();
        }

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
                    if (fAnimationEnabled) {
                        animationTile = new GameActivityAnimationTile(this);
                        animationTile.initializeDrawOne();
                        animationTile.startNow();
                    }
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
                    if (fAnimationEnabled) {
                        animationTile = new GameActivityAnimationTile(this);
                        animationTile.initializeTakeAll();
                        animationTile.startNow();
                    }
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
                        PlayerHumanDisasterHandling();
                    } else {
                        // call PlayerAI function
                        ((PlayerAi) playerWinner).ResolveDisastersAi();
                    }
                } else {
                    if (game.TestEpochOver()) {
                        DoEpochOver();
                    } else {
                        game.SetNextPlayerTurn();
                    }
                }
                break;
            case ResolveDisasterCompleted:
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
                    Log.v(GameActivity.class.toString(), "Game over, deleting default save file, finishing activity");

                    deleteFile("Ra.game");
                    finish();
                }
                break;
            default:
                Assert.fail();
        }
    }

    int TileImageRes(Game.Tile etValue) {
        return aiTileImageRes_c[etValue.ordinal()];
    }

    String TileString(int iValue)
    {
        return sTiles[iValue];
    }

    String TileString(Game.Tile etValue)
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
}
