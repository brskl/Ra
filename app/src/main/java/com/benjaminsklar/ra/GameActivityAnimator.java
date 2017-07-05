package com.benjaminsklar.ra;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;


/**
 * Created by Ben on 7/1/2017.
 */

public class GameActivityAnimator implements Animator.AnimatorListener {
    static long lDuration=1000; // TODO: get from setting
    static long lDelay=100; // TODO: get from setting
    static long lDelayTrans2 = 1500; // TODO: get from setting

    ImageView imageView; // TODO: Is this necessary? Is it possible to use getTarget in onAnim callback


    static AnimatorSet initializeDrawOne(GameActivity gameActivity) {
        Log.d(GameActivityAnimator.class.toString(), "initializeDrawOne()");

        ImageView ivDest;
        int imageHalfX, imageHalfY;
        Rect rectStart = new Rect();
        Rect rectAuction = new Rect();
        Rect rectDest = new Rect();
        Game game = Game.getInstance();
        GameActivityAnimator gameActivityAnimator = new GameActivityAnimator();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animScale1x, animScale1y, animScale2x, animScale2y, animAlpha1, animTrans1x, animTrans1y, animTrans2x, animTrans2y;

        Game.Tile tile = game.getTileLastDrawn();

        if (tile == Game.Tile.tRa) {
            gameActivityAnimator.imageView = gameActivity.ivAnimationTileRa;
            ivDest = gameActivity.aivRaTiles[game.getRas()-1];
        } else {
            gameActivityAnimator.imageView = gameActivity.aivAnimationTiles[0];
            ivDest = gameActivity.aivAuctionItems[game.getAuction().size()-1];
        }

        gameActivityAnimator.imageView.setVisibility(View.VISIBLE);
        gameActivity.btnDraw.getDrawingRect(rectStart);
        gameActivity.rlAuction.getDrawingRect(rectAuction);
        ivDest.getDrawingRect(rectDest);

        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.btnDraw, rectStart);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.rlAuction, rectAuction);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(ivDest, rectDest);

        gameActivityAnimator.imageView.setImageResource(gameActivity.TileImageRes(tile));
        imageHalfX = gameActivityAnimator.imageView.getWidth() / 2;
        imageHalfY = gameActivityAnimator.imageView.getHeight() / 2;


        // TODO: try to replace with ofFloat(obj, xprop, yprop, path) for trans and scale
        animTrans1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "x", rectStart.centerX() - imageHalfX, rectAuction.centerX() - imageHalfX);
        animTrans1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "y", rectStart.centerY() - imageHalfY, rectAuction.centerY() - imageHalfY);
        animTrans2x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "x", rectDest.centerX() - imageHalfX);
        animTrans2y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "y", rectDest.centerY() - imageHalfY);
        animAlpha1 = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "alpha", 0.1f, 1.0f);
        animScale1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleX", 0.1f, 1.5f);
        animScale1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleY", 0.1f, 1.5f);
        animScale2x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleX", 1.0f);
        animScale2y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleY", 1.0f);
        animTrans1x.setDuration(lDuration);
        animTrans1y.setDuration(lDuration);
        animTrans2x.setDuration(lDuration);
        animTrans2y.setDuration(lDuration);
        animTrans2x.setStartDelay(lDelayTrans2);
        animTrans2y.setStartDelay(lDelayTrans2);
        animAlpha1.setDuration(lDuration);
        animScale1x.setDuration(lDuration);
        animScale1y.setDuration(lDuration);
        animScale2x.setDuration(lDuration);
        animScale2y.setDuration(lDuration);
        animScale2x.setStartDelay(lDelayTrans2);
        animScale2y.setStartDelay(lDelayTrans2);
        animatorSet.play(animTrans1x).with(animTrans1y).with(animTrans2x).with(animTrans2y);
        animatorSet.play(animTrans1x).with(animScale1x).with(animScale1y).with(animScale2x).with(animScale2y);
        animatorSet.play(animTrans1x).with(animAlpha1);

        animatorSet.addListener(gameActivityAnimator);

        return animatorSet;
    }

    static AnimatorSet initializeTakeAll(GameActivity gameActivity) {
        Log.d(GameActivityAnimator.class.toString(), "initializeTakeAll()");
        Game game = Game.getInstance();
        int nTiles = game.getAuction().size();
        int i;
        long lDelayCurrent;
        Rect rectStart = new Rect();
        Rect rectDest = new Rect();
        int imageHalfX, imageHalfY;
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet animatorSetTile;
        GameActivityAnimator gameActivityAnimator;
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator animTrans1x, animTrans1y, animScale1x, animScale1y;

        gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()].getDrawingRect(rectDest);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()], rectDest);
        imageHalfX = gameActivity.aivAuctionItems[0].getWidth() / 2;
        imageHalfY = gameActivity.aivAuctionItems[0].getHeight() / 2;

        for (i = 0, lDelayCurrent = 0; i < nTiles; i++, lDelayCurrent += lDelay) {
            gameActivityAnimator = new GameActivityAnimator();
            animatorSetTile = new AnimatorSet();
            ImageView ivAuctionTile = gameActivity.aivAuctionItems[i];
            ivAuctionTile.getDrawingRect(rectStart);
            gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(ivAuctionTile, rectStart);
            gameActivityAnimator.imageView = gameActivity.aivAnimationTiles[i];
            gameActivityAnimator.imageView.setImageResource(gameActivity.TileImageRes(game.getAuction().get(i)));
            // due to delayed start (for most) setting initial X,Y before making visible;
            gameActivityAnimator.imageView.setX(rectStart.centerX() - imageHalfX);
            gameActivityAnimator.imageView.setY(rectStart.centerY() - imageHalfY);
            gameActivityAnimator.imageView.setVisibility(View.VISIBLE);

            ObjectAnimator animAlpha;

            animTrans1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "x", rectDest.centerX() - imageHalfX);
            animTrans1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "y", rectDest.centerY() - imageHalfY);
            animAlpha = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "alpha", 1.0f, 0.0f);
            // TODO: consider changing interpolator for animAlpha so lasts longer until end
            animatorSetTile.play(animTrans1x).with(animTrans1y).with(animAlpha);
            animatorSetTile.setDuration(lDuration);
            animatorSetTile.setStartDelay(lDelayCurrent);
            animatorSetTile.addListener(gameActivityAnimator);

            animatorList.add(animatorSetTile);
        }

        // TODO: Add swapping of Sun tiles
        int iPlayerSunsNext;
        float fScaleX, fScaleY;
        float imageWidth, imageHeight;
        com.benjaminsklar.ra.SunImageView sivPlayerSunDest;
        animatorSetTile = new AnimatorSet();
        gameActivity.gameActivityUpdate.copyAuctionSunLayout(gameActivity.ivAnimationAuctionSun); // TODO: Can this be done in GameActivity.onCreate
        gameActivity.gameActivityUpdate.copyAuctionSunPosval(gameActivity.ivAnimationAuctionSun);
        gameActivityAnimator = new GameActivityAnimator();
        gameActivityAnimator.imageView = gameActivity.ivAnimationAuctionSun;
        gameActivityAnimator.imageView.setVisibility(View.VISIBLE);

        iPlayerSunsNext = 0; // TODO: Calculate this better
        sivPlayerSunDest = (com.benjaminsklar.ra.SunImageView) gameActivity.allPlayerSunsNext[game.getAuctionPlayerHighestIndex()].getChildAt(iPlayerSunsNext);
        sivPlayerSunDest.getDrawingRect(rectDest);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(sivPlayerSunDest, rectDest);
        imageWidth = gameActivityAnimator.imageView.getLayoutParams().width;
        imageHeight = gameActivityAnimator.imageView.getLayoutParams().height;
        // TODO: Can fScaleX/Y be calculated once in gameActivity.onCreate?
        fScaleX = rectDest.width() / imageWidth;
        fScaleY = rectDest.height() / imageHeight;

        animTrans1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "x", rectDest.centerX() - (imageWidth / 2.0f));
        animTrans1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "y", rectDest.centerY() - (imageHeight / 2.0f));
        animScale1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleX", 1.0f, fScaleX);
        animScale1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleY", 1.0f, fScaleY);
        animatorSetTile.playTogether(animTrans1x, animTrans1y, animScale1x, animScale1y);
        animatorSetTile.setDuration(lDuration*4);
        animatorSetTile.setStartDelay(lDelayCurrent);
        animatorSetTile.addListener(gameActivityAnimator);
        animatorList.add(animatorSetTile);

        animatorSet.playTogether(animatorList);
        return animatorSet;
    }

    public void onAnimationRepeat (Animator animation) {
        Log.d(GameActivityAnimator.class.toString(), "onAnimationRepeat");
    }

    public void onAnimationCancel (Animator animation) {
        Log.d(GameActivityAnimator.class.toString(), "onAnimationCancel");
    }

    public void onAnimationEnd (Animator animation) {
        Log.d(GameActivityAnimator.class.toString(), "onAnimationEnd");
        imageView.setVisibility(View.INVISIBLE);
    }

    public void onAnimationStart (Animator animation) {
        Log.d(GameActivityAnimator.class.toString(), "onAnimationStart");
    }
}
