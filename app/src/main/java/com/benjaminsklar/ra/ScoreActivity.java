package com.benjaminsklar.ra;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Log.d(ScoreActivity.class.toString(), "onCreate");

        TextView tv;
        TableRow tr;
        TableLayout tl;
        String sScore;
        LayoutInflater inflater = getLayoutInflater();
        Game game = Game.getInstance();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (game.getStatusCurrent() == Game.Status.EpochOver)
        {
            if (game.FLastEpoch())
            {
                sScore = getString(R.string.TitleScoreFinal);
            } else {
                sScore = getString(R.string.TitleScoreEpochEnd, game.getEpoch());
            }
        } else {
            sScore = getString(R.string.TitleScoreEpochDuring, game.getEpoch());
        }
        tv = (TextView) findViewById(R.id.textViewTitleScorePeriod);
        tv.setText(sScore);

        tl = (TableLayout) findViewById(R.id.tableLayoutScore);
        for (Player player: game.getPlayers())
        {
            tr = (TableRow) inflater.inflate(R.layout.tablerow_score, tl, false);

            // name
            tv = (TextView) tr.getChildAt(0);
            tv.setText(player.getName());

            // previous score
            tv = (TextView) tr.getChildAt(1);
            tv.setText(Integer.toString(player.getScore()));

            // God
            tv = (TextView) tr.getChildAt(2);
            tv.setText(Integer.toString(player.aiScoreEpoch[Player.iScoreGod_c]));
            // Gold
            tv = (TextView) tr.getChildAt(3);
            tv.setText(Integer.toString(player.aiScoreEpoch[Player.iScoreGold_c]));

            // Pharaoh
            tv = (TextView) tr.getChildAt(4);
            sScore = String.format("%d(%d)", player.aiScoreEpoch[Player.iScorePharoah_c], player.getNTiles()[Game.Tile.tPharaoh.ordinal()]);
            tv.setText(sScore);

            // Nile
            tv = (TextView) tr.getChildAt(5);
            tv.setText(Integer.toString(player.aiScoreEpoch[Player.iScoreNile_c]));

            // Civ
            tv = (TextView) tr.getChildAt(6);
            tv.setText(Integer.toString(player.aiScoreEpoch[Player.iScoreCiv_c]));

            if (game.FLastEpoch())
            {
                // Monuments
                tv = (TextView) tr.getChildAt(7);
                tv.setText(Integer.toString(player.aiScoreEpoch[Player.iScoreMonument_c]));

                // Suns
                tv = (TextView) tr.getChildAt(8);
                sScore = String.format("%d(%d)", player.aiScoreEpoch[Player.iScoreSuns_c], player.aiScoreEpoch[Player.iScoreSunsTotal_c]);
                tv.setText(sScore);
            }

            // new total score
            tv = (TextView) tr.getChildAt(9);
            tv.setText(Integer.toString(player.aiScoreEpoch[Player.iScoreTotal_c]));

            tl.addView(tr);
        }

        // TODO replace conditional with if (Game Over)
        if (true) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean fFirst = true;

            stringBuilder.append("Ra Score: ");
            for (Player player: game.getPlayers()) {
                if (!fFirst) {
                    stringBuilder.append(", ");
                } else {
                    fFirst = false;
                }
                stringBuilder.append(player.getName());
                stringBuilder.append(" ");
                if (!player.getHuman()) {
                    stringBuilder.append(getResources().getString(R.string.tbtnAI));
                    stringBuilder.append(" ");
                }
                stringBuilder.append(": ");
                stringBuilder.append(player.aiScoreEpoch[Player.iScoreTotal_c]);
            }

            mShareIntent = new Intent();
            mShareIntent.setAction(Intent.ACTION_SEND);
            mShareIntent.setType("text/plain");
            mShareIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(ScoreActivity.class.toString(), "onResume");

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(ScoreActivity.class.toString(), "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.menu_score, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mShareActionProvider != null && mShareIntent != null) {
            mShareActionProvider.setShareIntent(mShareIntent);
        }

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(ScoreActivity.class.toString(), "onPrepareOptionsMenu");
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // TODO: hide/show share menu item
        return true;
    }
}
