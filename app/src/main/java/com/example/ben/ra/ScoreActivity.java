package com.example.ben.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

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
        int [] aiSE;
        Game game = Game.getInstance();

        tv = (TextView) findViewById(R.id.textViewTitleScore);
        // tv.setText(getString(R.string.TitleScoreEpoch, RaGame.rgCurrent.iEpoch));

        tl = (TableLayout) findViewById(R.id.tableLayoutScore);
        for (Player player: game.getPlayers())
        {
            // aiSE = game.getPlayers()[iPlayer].iScoreEpoch;
            tr = (TableRow) inflater.inflate(R.layout.tablerow_score, tl, false);

            // name
            tv = (TextView) tr.getChildAt(0);
            tv.setText(player.getName());

            /*
            // previous score
            tv = (TextView) tr.getChildAt(1);
            tv.setText(Integer.toString(RaGame.rgCurrent.aPlayers[iPlayer].iScore));

            // God
            tv = (TextView) tr.getChildAt(2);
            tv.setText(Integer.toString(aiSE[0]));
            // Gold
            tv = (TextView) tr.getChildAt(3);
            tv.setText(Integer.toString(aiSE[1]));

            // Pharaoh
            tv = (TextView) tr.getChildAt(4);
            sScore = String.format("%d(%d)", aiSE[2], RaGame.rgCurrent.aPlayers[iPlayer].nTiles[RaGame.Tile.tPharaoh.ordinal()]);
            tv.setText(sScore);

            // Nile
            tv = (TextView) tr.getChildAt(5);
            tv.setText(Integer.toString(aiSE[3]));

            // Civ
            tv = (TextView) tr.getChildAt(6);
            tv.setText(Integer.toString(aiSE[4]));

            if (RaGame.rgCurrent.iEpoch == 3)
            {
                // Monuments
                tv = (TextView) tr.getChildAt(7);
                tv.setText(Integer.toString(aiSE[5]));

                // Suns
                tv = (TextView) tr.getChildAt(8);
                sScore = String.format("%d(%d)", aiSE[6], aiSE[7]);
                tv.setText(sScore);
            }

            // new score
            tv = (TextView) tr.getChildAt(9);
            sScore = Integer.toString(RaGame.rgCurrent.aPlayers[iPlayer].iScore + aiSE[0] + aiSE[1] + aiSE[2] +aiSE[3] + aiSE[4] + aiSE[5] + aiSE[6]);
            tv.setText(sScore);
*/
            tl.addView(tr);
        }
    }
}
