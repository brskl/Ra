package com.benjaminsklar.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TilesActivity extends AppCompatActivity {

    // must be same order as columns labeled in table
    static final int [] aiO = {
            Game.Tile.tGod.ordinal(),
            Game.Tile.tGold.ordinal(),
            Game.Tile.tPharaoh.ordinal(),
            Game.Tile.tNile.ordinal(),
            Game.Tile.tFlood.ordinal(),
            Game.Tile.tCiv1.ordinal(),
            Game.Tile.tCiv2.ordinal(),
            Game.Tile.tCiv3.ordinal(),
            Game.Tile.tCiv4.ordinal(),
            Game.Tile.tCiv5.ordinal(),
            Game.Tile.tMon1.ordinal(),
            Game.Tile.tMon2.ordinal(),
            Game.Tile.tMon3.ordinal(),
            Game.Tile.tMon4.ordinal(),
            Game.Tile.tMon5.ordinal(),
            Game.Tile.tMon6.ordinal(),
            Game.Tile.tMon7.ordinal(),
            Game.Tile.tMon8.ordinal()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiles);

        Log.v(TilesActivity.class.toString(), "onCreate");

        Game game = Game.getInstance();
        LayoutInflater inflater = getLayoutInflater();
        TableLayout tl = (TableLayout) findViewById(R.id.TableLayoutTiles);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for (Player player: game.getPlayers()) {
            TextView tv;
            TableRow tr;
            String sTile;

            int [] aiT = player.getNTiles();

            tr = (TableRow) inflater.inflate(R.layout.tablerow_tiles, tl, false);

            tv = (TextView) tr.getChildAt(0);
            tv.setText(player.getName());
            tv = (TextView) tr.getChildAt(1);
            tv.setText(Integer.toString(aiT[aiO[0]]));
            tv = (TextView) tr.getChildAt(2);
            tv.setText(Integer.toString(aiT[aiO[1]]));
            tv = (TextView) tr.getChildAt(3);
            tv.setText(Integer.toString(aiT[aiO[2]]));
            tv = (TextView) tr.getChildAt(4);
            sTile = String.format("%d(%d)", aiT[aiO[3]], aiT[aiO[4]]);
            tv.setText(sTile);
            tv = (TextView) tr.getChildAt(5);
            sTile = String.format("%2d/%2d/%2d/%2d/%2d", aiT[aiO[5]], aiT[aiO[6]], aiT[aiO[7]], aiT[aiO[8]], aiT[aiO[9]]);
            tv.setText(sTile);
            tv = (TextView) tr.getChildAt(6);
            sTile = String.format("%2d/%2d/%2d/%2d/%2d/%2d/%2d/%2d", aiT[aiO[10]], aiT[aiO[11]], aiT[aiO[12]], aiT[aiO[13]], aiT[aiO[14]], aiT[aiO[15]], aiT[aiO[16]], aiT[aiO[17]]);
            tv.setText(sTile);
            tl.addView(tr);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TilesActivity.class.toString(), "onResume");

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
