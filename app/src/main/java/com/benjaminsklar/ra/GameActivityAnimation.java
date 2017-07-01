package com.benjaminsklar.ra;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by Ben on 5/27/2017.
 */

public class GameActivityAnimation implements Animation.AnimationListener{
    ImageView ivTile = null;

    static AnimationSet initializeDrawOne(GameActivity gameActivity) {
        Log.d(GameActivityAnimation.class.toString(), "initializeDrawOne()");

        GameActivityAnimation gameActivityAnimation = new GameActivityAnimation();
        Animation animTrans1, animTrans2;
        ImageView ivDest;
        Game game = Game.getInstance();
        Rect rectStart = new Rect();
        Rect rectAuction = new Rect();
        Rect rectDest = new Rect();

        Game.Tile tile = game.getTileLastDrawn();

        if (tile == Game.Tile.tRa) {
            gameActivityAnimation.ivTile = gameActivity.aivAnimationTileRa;
            ivDest = gameActivity.aivRaTiles[game.getRas()-1];
        } else {
            gameActivityAnimation.ivTile = gameActivity.aivAnimationTiles[0];
            ivDest = gameActivity.aivAuctionItems[game.getAuction().size()-1];
        }

        gameActivityAnimation.ivTile.setVisibility(View.VISIBLE);
        gameActivity.btnDraw.getDrawingRect(rectStart);
        gameActivity.rlAuction.getDrawingRect(rectAuction);
        ivDest.getDrawingRect(rectDest);

        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.btnDraw, rectStart);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.rlAuction, rectAuction);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(ivDest, rectDest);

        ViewGroup.LayoutParams imageLayout = gameActivityAnimation.ivTile.getLayoutParams();

        gameActivityAnimation.ivTile.setImageResource(gameActivity.TileImageRes(tile));
        gameActivityAnimation.ivTile.setX(rectStart.centerX() - imageLayout.width / 2);
        gameActivityAnimation.ivTile.setY(rectStart.centerY() - imageLayout.height / 2);


        AnimationSet animationSet = new AnimationSet(true);
        animTrans1 = new TranslateAnimation(0, rectAuction.centerX() - rectStart.centerX(), 0, rectAuction.centerY() - rectStart.centerY());
        animTrans2 = new TranslateAnimation(0, rectDest.centerX() - rectAuction.centerX(), 0, rectDest.centerY() - rectAuction.centerY());
        gameActivityAnimation.ivTile.setAnimation(animationSet);

        // TODO: replace durations with setting value
        animTrans1.setDuration(1000);
        animTrans2.setDuration(1000);
        animTrans2.setStartOffset(1500);
        animationSet.addAnimation(animTrans1);
        animationSet.addAnimation(animTrans2);
        animationSet.setFillAfter(false);
        animationSet.setAnimationListener(gameActivityAnimation);

        return animationSet;
    }

    static AnimationSet initializeTakeAll(GameActivity gameActivity) {
        Log.d(GameActivityAnimation.class.toString(), "initializeTakeAll()");
        Game game = Game.getInstance();
        int nTiles = game.getAuction().size();
        int i;
        Rect rectStart = new Rect();
        Rect rectDest = new Rect();

        AnimationSet animationSet = new AnimationSet(true);
        gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()].getDrawingRect(rectDest);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()], rectDest);

        for (i = 0; i < nTiles; i++) {
            GameActivityAnimation gameActivityAnimation = new GameActivityAnimation();
            AnimationSet animationSetTile;
            TranslateAnimation translateAnimation;
            AlphaAnimation alphaAnimation;
            ImageView ivAuctionTile = gameActivity.aivAuctionItems[i];

            gameActivityAnimation.ivTile = gameActivity.aivAnimationTiles[i];
            gameActivityAnimation.ivTile.setVisibility(View.VISIBLE);

            animationSetTile = new AnimationSet(true);
            gameActivityAnimation.ivTile.setImageResource(gameActivity.TileImageRes(game.getAuction().get(i)));

            ivAuctionTile.getDrawingRect(rectStart);
            gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(ivAuctionTile, rectStart);

            gameActivityAnimation.ivTile.setX(rectStart.left - gameActivity.rlGameActivity.getLeft());
            gameActivityAnimation.ivTile.setY(rectStart.top - gameActivity.rlGameActivity.getTop());

            gameActivityAnimation.ivTile.setAnimation(animationSetTile);

            translateAnimation = new TranslateAnimation(0, rectDest.centerX() - rectStart.centerX(), 0, rectDest.centerY() - rectStart.centerY());
            translateAnimation.setDuration(1000);
            translateAnimation.setStartOffset(100 * i);
            alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(100*i);

            animationSetTile.addAnimation(translateAnimation);
            animationSetTile.addAnimation(alphaAnimation);
            animationSetTile.setFillAfter(false);
            animationSetTile.setAnimationListener(gameActivityAnimation);

            animationSet.addAnimation(animationSetTile);
        }
        // TODO: Add swapping of Sun tiles

        return animationSet;
    }

    void close(Animation animation) {
        Log.d(GameActivityAnimation.class.toString(), "close()");
        if (ivTile != null) {
            Log.d(GameActivityAnimation.class.toString(), "Trying removing single tile");
            ivTile.setVisibility(View.INVISIBLE);
            // ivTile.setImageResource(0);
            ivTile = null;
        }
    }

    public void onAnimationEnd(Animation animation) {
        Log.d(GameActivityAnimation.class.toString(), "onAnimationEnd()");
        close(animation);
    }

    public void onAnimationRepeat(Animation animation) {
        Log.d(GameActivityAnimation.class.toString(), "onAnimationRepeat()");
    }

    public void onAnimationStart(Animation animation) {
        Log.d(GameActivityAnimation.class.toString(), "onAnimationStart()");
    }
}
