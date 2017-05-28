package com.benjaminsklar.ra;

import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by Ben on 5/27/2017.
 */

public class GameActivityAnimationTile implements Animation.AnimationListener{
    GameActivity gameActivity;
    ImageView ivTile;
    ImageView [] ivTiles;
    AnimationSet animationSet;

    GameActivityAnimationTile(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }

        gameActivity = gameActivityValue;
    }

    void initializeTakeAll() {
        Game game = Game.getInstance();
        int nTiles = game.getAuction().size();
        int i;
        ivTiles = new ImageView[nTiles];

        animationSet = new AnimationSet(true);
        animationSet.setAnimationListener(this);
        animationSet.setFillAfter(true);

        for (i = 0; i < nTiles; i++) {
            AnimationSet animationSetTile;
            TranslateAnimation translateAnimation;
            AlphaAnimation alphaAnimation;
            // TODO: Create animation for particular tile and add to instance animationSet
        }
    }

    void initializeDrawOne() {
        Animation animTrans1, animTrans2;
        ImageView ivDest;
        Game game = Game.getInstance();
        Rect rectStart = new Rect();
        Rect rectAuction = new Rect();
        Rect rectDest = new Rect();

        Game.Tile tile = game.getTileLastDrawn();
        ivTile = new ImageView(gameActivity);

        if (tile == Game.Tile.tRa) {
            ivDest = gameActivity.aivRaTiles[game.getRas()-1];
        } else {
            ivDest = gameActivity.aivAuctionItems[game.getAuction().size()-1];
        }

        gameActivity.btnDraw.getDrawingRect(rectStart);
        gameActivity.rlAuction.getDrawingRect(rectAuction);
        ivDest.getDrawingRect(rectDest);

        gameActivity.rlBoard.offsetDescendantRectToMyCoords(gameActivity.btnDraw, rectStart);
        gameActivity.rlBoard.offsetDescendantRectToMyCoords(gameActivity.rlAuction, rectAuction);
        gameActivity.rlBoard.offsetDescendantRectToMyCoords(ivDest, rectDest);

        ViewGroup.LayoutParams destLayout = ivDest.getLayoutParams();
        ViewGroup.LayoutParams imageLayout = new ViewGroup.LayoutParams(destLayout.width, destLayout.height);

        ivTile.setImageResource(gameActivity.TileImageRes(tile));
        ivTile.setLayoutParams(imageLayout);
        ivTile.setX(rectStart.centerX() - imageLayout.width / 2);
        ivTile.setY(rectStart.centerY() - imageLayout.height / 2);

        gameActivity.rlBoard.addView(ivTile);

        animationSet = new AnimationSet(true);
        animTrans1 = new TranslateAnimation(0, rectAuction.centerX() - rectStart.centerX(), 0, rectAuction.centerY() - rectStart.centerY());
        animTrans2 = new TranslateAnimation(0, rectDest.centerX() - rectAuction.centerX(), 0, rectDest.centerY() - rectAuction.centerY());
        ivTile.setAnimation(animationSet);

        // TODO: replace durations with setting value
        animTrans1.setDuration(1000);
        animTrans2.setDuration(1000);
        animTrans2.setStartOffset(1500);
        animationSet.addAnimation(animTrans1);
        animationSet.addAnimation(animTrans2);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(this);
    }

    void startNow() {
        animationSet.startNow();
    }

    void cancel() {
        animationSet.cancel();
    }

    void close() {
        if (ivTile != null) {
            gameActivity.rlBoard.removeView(ivTile);
            ivTile = null;
        } else {
            // TODO: Add assert ivTiles[] is not null
            for (ImageView iv : ivTiles) {
                gameActivity.llGameActivity.removeView(iv);
            }
            ivTiles = null;
        }
    }

    public void onAnimationEnd(Animation animation) {
        close();
        gameActivity.animationTile = null;
    }

    public void onAnimationRepeat(Animation animation) {
        ;
    }

    public void onAnimationStart(Animation animation) {
        ;
    }
}
