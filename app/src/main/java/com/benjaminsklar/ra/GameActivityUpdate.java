package com.benjaminsklar.ra;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import junit.framework.Assert;

/**
 * Created by Ben on 4/22/2017.
 */

public class GameActivityUpdate {
    GameActivity gameActivity;

    private Button btnOk;
    private Button btnAuction;
    private Button btnGod;
    private com.benjaminsklar.ra.SunImageView ivAuctionSun;

    private TextView tvEpoch;
    private TextView tvCurrentPlayer;
    private TextView tvStatus;

    GameActivityUpdate(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }

        gameActivity = gameActivityValue;
    }

    void onCreate() {
        tvEpoch = (TextView) gameActivity.findViewById(R.id.textViewEpoch);
        tvCurrentPlayer = (TextView) gameActivity.findViewById(R.id.textViewCurrentPlayer);
        tvStatus = (TextView) gameActivity.findViewById(R.id.textViewStatus);
        btnOk = (Button) gameActivity.findViewById(R.id.buttonOK);
        btnAuction = (Button) gameActivity.findViewById(R.id.buttonAuction);
        btnGod = (Button) gameActivity.findViewById(R.id.buttonGod);
        ivAuctionSun = (com.benjaminsklar.ra.SunImageView) gameActivity.findViewById(R.id.ivAuctionSun);

    }

    void copyAuctionSunLayout(com.benjaminsklar.ra.SunImageView ivAnimationAuctionSun) {
        RelativeLayout.LayoutParams imageLayout = new RelativeLayout.LayoutParams(ivAuctionSun.getWidth(), ivAuctionSun.getHeight());
        ivAnimationAuctionSun.setLayoutParams(imageLayout);
    }

    void copyAuctionSunPosval(com.benjaminsklar.ra.SunImageView ivAnimationAuctionSun) {
        Rect rect = new Rect();

        ivAnimationAuctionSun.setiValue(ivAuctionSun.getiValue());
        ivAuctionSun.getDrawingRect(rect);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(ivAuctionSun, rect);
        ivAnimationAuctionSun.setX(rect.left);
        ivAnimationAuctionSun.setY(rect.top);
    }

    void UpdateDisplayPlayerNames(){
        TextView tv;
        Game game = Game.getInstance();
        final int atvIDs[] = {R.id.textViewNamePlayer1, R.id.textViewNamePlayer2, R.id.textViewNamePlayer3, R.id.textViewNamePlayer4, R.id.textViewNamePlayer5};

        for (int i = 0; i < game.getNPlayers(); i++)
        {
            tv = (TextView) gameActivity.findViewById(atvIDs[i]);
            tv.setText(gameActivity.getString(R.string.PlayerNamePlaceholder, game.aPlayers[i].getName()));
        }
    }

    void UpdateDisplayRound() {
        Game game = Game.getInstance();

        // current epoch
        tvEpoch.setText(gameActivity.getResources().getString(R.string.TitleEpoch, game.getEpoch()));

        // current player
        tvCurrentPlayer.setText(gameActivity.getResources().getString(R.string.CurrentPlayer, game.getPlayerCurrent().getName()));
    }

    void UpdateDisplayRaTiles() {
        Game game = Game.getInstance();

        // current number of Ra tiles
        int i;
        for (i = 0; i < game.getRas(); i++) {
            gameActivity.aivRaTiles[i].setImageResource(R.drawable.tile_ra);
        }
        for (; i < game.getMaxRas(); i++)
        {
            gameActivity.aivRaTiles[i].setImageResource(0);
        }
    }

    void UpdateDisplayPlayerSuns(int iPlayer)
    {
        Game game = Game.getInstance();
        int iChild = 0;
        int i;
        com.benjaminsklar.ra.SunImageView sivCurrent;
        LinearLayout llPlayerSuns = gameActivity.allPlayerSuns[iPlayer];
        LinearLayout llPlayerSunsNext = gameActivity.allPlayerSunsNext[iPlayer];
        int nChild = llPlayerSuns.getChildCount();

        iChild = 0;
        for (i = 0; i < game.aPlayers[iPlayer].getSuns().size(); i++)
        {
            sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSuns.getChildAt(iChild++);
            sivCurrent.setiValue(game.aPlayers[iPlayer].alSuns.get(i));
            sivCurrent.setVisibility(View.VISIBLE);
        }

        while (iChild < game.getSunsPerPlayer()) {
            sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSuns.getChildAt(iChild++);
            sivCurrent.setVisibility(View.INVISIBLE);
        }

        iChild = 0;
        while (iChild < game.getSunsPerPlayer() - game.aPlayers[iPlayer].getSunsNext().size()) {
            sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSunsNext.getChildAt(iChild++);
            sivCurrent.setVisibility(View.INVISIBLE);
        }
        for (i = 0; i < game.aPlayers[iPlayer].getSunsNext().size(); i++)
        {
            sivCurrent = (com.benjaminsklar.ra.SunImageView) llPlayerSunsNext.getChildAt(iChild++);
            sivCurrent.setiValue(game.aPlayers[iPlayer].alSunsNext.get(i));
            sivCurrent.setVisibility(View.VISIBLE);
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
                    if (gameActivity.fBiddingInProgress) {
                        sStatus = gameActivity.getString(R.string.StatusAuctionPlayerInProgress, game.getAuctionPlayerCurrent().getName());
                    } else {
                        sStatus = gameActivity.getString(R.string.StatusAuctionPlayerPassed, game.getAuctionPlayerCurrent().getName());
                    }
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
        gameActivity.btnDraw.setEnabled(!fOKonly && !game.FAuctionTrackFull());
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
                gameActivity.aivAuctionItems[i].setVisibility(View.VISIBLE);
                gameActivity.aivAuctionItems[i].setImageResource(resId);
            } else {
                // TODO: Replace if != 0 with assert
                gameActivity.aivAuctionItems[i].setImageResource(0);
            }
        }
        // clear remaining ImageViews
        for (;i < Game.nMaxAuction_c; i++)
        {
            // TODO: Is there a better way to clear image
            gameActivity.aivAuctionItems[i].setImageResource(0);;
        }
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
