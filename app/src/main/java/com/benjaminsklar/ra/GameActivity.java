package com.benjaminsklar.ra;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
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


    private TextView tvEpoch;
    private TextView tvStatus;
    private TextView tvCurrentPlayer;
    private LinearLayout allPlayerSuns[] = new LinearLayout[Game.nMaxPlayers_c];
    private Button btnOk;
    private Button btnAuction;
    private ImageButton btnDraw;
    private Button btnGod;
    private ImageView aivAuctionItems[] = new ImageView[Game.nMaxAuction_c];
    private ImageView aivRaTiles[] = new ImageView[Game.nMaxRas_c];
    private com.benjaminsklar.ra.SunImageView ivAuctionSun;

    Animation anim = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(GameActivity.class.toString(), "onCreate");

        sTiles = getResources().getStringArray(R.array.Tiles);

        tvEpoch = (TextView) findViewById(R.id.textViewEpoch);
        tvStatus = (TextView) findViewById(R.id.textViewStatus);
        tvCurrentPlayer = (TextView) findViewById(R.id.textViewCurrentPlayer);
        allPlayerSuns[0] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer1);
        allPlayerSuns[1] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer2);
        allPlayerSuns[2] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer3);
        allPlayerSuns[3] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer4);
        allPlayerSuns[4] = (LinearLayout) findViewById(R.id.linearLayoutSunsPlayer5);
        btnOk = (Button) findViewById(R.id.buttonOK);
        btnAuction = (Button) findViewById(R.id.buttonAuction);
        btnDraw = (ImageButton) findViewById(R.id.buttonDraw);
        btnGod = (Button) findViewById(R.id.buttonGod);
        ivAuctionSun = (com.benjaminsklar.ra.SunImageView) findViewById(R.id.ivAuctionSun);
        aivAuctionItems[0] = (ImageView) findViewById(R.id.ivAuction0);
        aivAuctionItems[1] = (ImageView) findViewById(R.id.ivAuction1);
        aivAuctionItems[2] = (ImageView) findViewById(R.id.ivAuction2);
        aivAuctionItems[3] = (ImageView) findViewById(R.id.ivAuction3);
        aivAuctionItems[4] = (ImageView) findViewById(R.id.ivAuction4);
        aivAuctionItems[5] = (ImageView) findViewById(R.id.ivAuction5);
        aivAuctionItems[6] = (ImageView) findViewById(R.id.ivAuction6);
        aivAuctionItems[7] = (ImageView) findViewById(R.id.ivAuction7);
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

        SetNumplayerUI();
        UpdateDisplayPlayerNames();

        UpdateDisplay();
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
                    UpdateDisplay();
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
                StartAnimationDrawTile();
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
                    StartAnimationDrawTile();
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

    void StartAnimationDrawTile() {
        Animation animTrans1, animTrans2;
        AnimationSet animationSet;
        ImageView ivDest;

        Game game = Game.getInstance();
        Game.Tile tile = game.getTileLastDrawn();
        RelativeLayout rlBoard = (RelativeLayout) findViewById(R.id.relativeLayoutBoard);
        RelativeLayout rlAuction = (RelativeLayout) findViewById(R.id.relativeLayoutAuction);
        ImageView ivTileDrawn = new ImageView(this);

        if (tile == Game.Tile.tRa) {
            ivDest = aivRaTiles[game.getRas()-1];
        } else {
            ivDest = aivAuctionItems[game.getAuction().size()-1];
        }
        Rect rectStart = new Rect();
        Rect rectAuction = new Rect();
        Rect rectDest = new Rect();
        btnDraw.getDrawingRect(rectStart);
        rlBoard.offsetDescendantRectToMyCoords(btnDraw, rectStart);
        rlAuction.getDrawingRect(rectAuction);
        rlBoard.offsetDescendantRectToMyCoords(rlAuction, rectAuction);
        ivDest.getDrawingRect(rectDest);
        rlBoard.offsetDescendantRectToMyCoords(ivDest, rectDest);

        ViewGroup.LayoutParams destLayout = ivDest.getLayoutParams();
        ViewGroup.LayoutParams imageLayout = new ViewGroup.LayoutParams(destLayout.width, destLayout.height);

        ivTileDrawn.setImageResource(TileImageRes(tile));
        ivTileDrawn.setLayoutParams(imageLayout);
        ivTileDrawn.setX(rectStart.centerX() - imageLayout.width / 2);
        ivTileDrawn.setY(rectStart.centerY() - imageLayout.height / 2);

        rlBoard.addView(ivTileDrawn);

        animationSet = new AnimationSet(true);

        animTrans1 = new TranslateAnimation(0, rectAuction.centerX() - rectStart.centerX(), 0, rectAuction.centerY() - rectStart.centerY());
        animTrans2 = new TranslateAnimation(0, rectDest.centerX() - rectAuction.centerX(), 0, rectDest.centerY() - rectAuction.centerY());

        ivTileDrawn.setAnimation(animationSet);
        // TODO: replace durations with setting value
        animTrans1.setDuration(1000);
        animTrans2.setDuration(1000);
        animTrans2.setStartOffset(1500);

        animationSet.addAnimation(animTrans1);
        animationSet.addAnimation(animTrans2);
        animationSet.setFillAfter(false);

        animationSet.startNow();

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
        tvEpoch.setText(getResources().getString(R.string.TitleEpoch, game.getEpoch()));

        // current player
        tvCurrentPlayer.setText(getResources().getString(R.string.CurrentPlayer, game.getPlayerCurrent().getName()));
    }

    void UpdateDisplayRaTiles() {
        Game game = Game.getInstance();

        // current number of Ra tiles
        int i;
        for (i = 0; i < game.getRas(); i++) {
            aivRaTiles[i].setImageResource(R.drawable.tile_ra);
        }
        for (; i < game.getMaxRas(); i++)
        {
            aivRaTiles[i].setImageResource(0);
        }
    }

    void UpdateDisplayPlayerSuns(int iPlayer)
    {
        Game game = Game.getInstance();
        int iChild = 0;
        int i;
        com.benjaminsklar.ra.SunImageView sivCurrent;
        LinearLayout llPlayerSuns = allPlayerSuns[iPlayer];
        int nChild = llPlayerSuns.getChildCount();


        for (i = 0; i < game.aPlayers[iPlayer].getSuns().size(); i++)
        {
            sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSuns.getChildAt(iChild++);
            sivCurrent.setiValue(game.aPlayers[iPlayer].alSuns.get(i));
            sivCurrent.setVisibility(View.VISIBLE);
        }

        sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSuns.getChildAt(iChild++);
        sivCurrent.setVisibility(View.INVISIBLE);

        for (i = 0; i < game.aPlayers[iPlayer].getSunsNext().size(); i++)
        {
            sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSuns.getChildAt(iChild++);
            sivCurrent.setiValue(game.aPlayers[iPlayer].alSunsNext.get(i));
            sivCurrent.setVisibility(View.VISIBLE);
        }

        while (iChild < nChild) {
            sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSuns.getChildAt(iChild++);
            sivCurrent.setVisibility(View.INVISIBLE);
        }
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
                    // if local human, check if bid-dialog is open
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
                sStatus = getString(R.string.StatusResolveDisaster, game.getAuctionPlayerHighest().getName());
                break;
            case ResolveDisasterCompleted:
                sStatus = getString(R.string.StatusResolveDisasterCompleted, game.getAuctionPlayerHighest().getName());
                break;
            default:
                // TODO replace with assert
                sStatus = "Not Yet Implemented";
                break;
        }

        Assert.assertNotNull("Illegal Status", sStatus);
        tvStatus.setText(sStatus);
    }

    private int TileImageRes(Game.Tile etValue) {
        return aiTileImageRes_c[etValue.ordinal()];
    }

    private String TileString(int iValue)
    {
        return sTiles[iValue];
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

        ivAuctionSun.setiValue(game.getAtAuctionSun());

        int i;
        for (i = 0; i < game.getAuction().size(); i++)
        {
            int resId;

            resId = TileImageRes(game.getAuction().get(i));

            if (resId != 0) {
                aivAuctionItems[i].setVisibility(View.VISIBLE);
                aivAuctionItems[i].setImageResource(resId);
            } else {
                // TODO: Replace if != 0 with assert
                aivAuctionItems[i].setImageResource(0);
            }
        }
        // clear remaining ImageViews
        for (;i < Game.nMaxAuction_c; i++)
        {
            // TODO: Is there a better way to clear image
            aivAuctionItems[i].setImageResource(0);;
        }
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
        UpdateDisplayRaTiles();
        UpdateDisplayPlayersSuns();
        UpdateDisplayStatus();
        UpdateDisplayAuction();
        UpdateDisplayButtons();
    }
}
