package com.benjaminsklar.ra;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by Ben on 4/22/2017.
 */

public class GameActivityUpdate {
    GameActivity gameActivity;

    private Button btnOk;
    private Button btnAuction;
    private ImageButton btnDraw;
    private Button btnGod;
    private com.benjaminsklar.ra.SunImageView ivAuctionSun;
    private ImageView aivAuctionItems[] = new ImageView[Game.nMaxAuction_c];

    GameActivityUpdate(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }

        gameActivity = gameActivityValue;
    }

    void onCreate() {
        btnOk = (Button) gameActivity.findViewById(R.id.buttonOK);
        btnAuction = (Button) gameActivity.findViewById(R.id.buttonAuction);
        btnDraw = (ImageButton) gameActivity.findViewById(R.id.buttonDraw);
        btnGod = (Button) gameActivity.findViewById(R.id.buttonGod);
        ivAuctionSun = (com.benjaminsklar.ra.SunImageView) gameActivity.findViewById(R.id.ivAuctionSun);
        aivAuctionItems[0] = (ImageView) gameActivity.findViewById(R.id.ivAuction0);
        aivAuctionItems[1] = (ImageView) gameActivity.findViewById(R.id.ivAuction1);
        aivAuctionItems[2] = (ImageView) gameActivity.findViewById(R.id.ivAuction2);
        aivAuctionItems[3] = (ImageView) gameActivity.findViewById(R.id.ivAuction3);
        aivAuctionItems[4] = (ImageView) gameActivity.findViewById(R.id.ivAuction4);
        aivAuctionItems[5] = (ImageView) gameActivity.findViewById(R.id.ivAuction5);
        aivAuctionItems[6] = (ImageView) gameActivity.findViewById(R.id.ivAuction6);
        aivAuctionItems[7] = (ImageView) gameActivity.findViewById(R.id.ivAuction7);
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

    void UpdateDisplayAuction(){
        Game game = Game.getInstance();

        ivAuctionSun.setiValue(game.getAtAuctionSun());

        int i;
        for (i = 0; i < game.getAuction().size(); i++)
        {
            int resId;

            resId = gameActivity.TileImageRes(game.getAuction().get(i));

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

    void UpdateDisplay(){
        gameActivity.UpdateDisplayRound();
        gameActivity.UpdateDisplayRaTiles();
        gameActivity.UpdateDisplayPlayersSuns();
        gameActivity.UpdateDisplayStatus();
        UpdateDisplayAuction();
        UpdateDisplayButtons();
    }
}
