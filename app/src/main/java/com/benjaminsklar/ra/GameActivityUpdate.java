package com.benjaminsklar.ra;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

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
    private TextView tvStatus;

    GameActivityUpdate(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }

        gameActivity = gameActivityValue;
    }

    void onCreate() {
        tvStatus = (TextView) gameActivity.findViewById(R.id.textViewStatus);
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

    void UpdateDisplayStatus(){
        Game game = Game.getInstance();
        String sStatus = null;

        switch(game.getStatusCurrent())
        {
            case TurnStart:
                sStatus = gameActivity.getString(R.string.StatusTurnStart, game.getPlayerCurrent().getName());
                break;
            case DrewTile:
                sStatus = gameActivity.getString(R.string.StatusDrewTile, game.getPlayerCurrent().getName(), gameActivity.TileString(game.getTileLastDrawn()));
                break;
            case EpochOver:
                sStatus = gameActivity.getString(R.string.StatusEpochOver, game.getEpoch());
                break;
            case CallsAuction:
                sStatus = gameActivity.getString(R.string.StatusCallsAuction, game.getPlayerCurrent().getName());
                break;
            case AuctionInProgress:
                if (game.FAuctionCurrentPlayerBidHighest()) {
                    sStatus = gameActivity.getString(R.string.StatusAuctionPlayerBid, game.getAuctionPlayerCurrent().getName(), game.getAuctionHighBid());
                } else {
                    sStatus = gameActivity.getString(R.string.StatusAuctionPlayerPassed, game.getAuctionPlayerCurrent().getName());
                }
                break;
            case AuctionWon:
                sStatus = gameActivity.getString(R.string.StatusAuctionWon, game.getAuctionPlayerHighest().getName());
                break;
            case AuctionEveryonePassed:
                sStatus = gameActivity.getString(R.string.StatusAuctionEveryonePassed);
                break;
            case UsedGod:
                sStatus = gameActivity.getString(R.string.StatusUsedGod, game.getPlayerCurrent().getName());
                break;
            case ResolveDisaster:
                sStatus = gameActivity.getString(R.string.StatusResolveDisaster, game.getAuctionPlayerHighest().getName());
                break;
            case ResolveDisasterCompleted:
                sStatus = gameActivity.getString(R.string.StatusResolveDisasterCompleted, game.getAuctionPlayerHighest().getName());
                break;
            default:
                // TODO replace with assert
                sStatus = "Not Yet Implemented";
                break;
        }

        Assert.assertNotNull("Illegal Status", sStatus);
        tvStatus.setText(sStatus);
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
        UpdateDisplayStatus();
        UpdateDisplayAuction();
        UpdateDisplayButtons();
    }
}
