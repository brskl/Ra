package com.benjaminsklar.ra;

import android.graphics.Rect;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by Ben on 5/27/2017.
 */

public class GameActivityAnimationDraw implements Animation.AnimationListener{
    GameActivity gameActivity;
    ImageView ivTile = null;

    GameActivityAnimationDraw(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }

        gameActivity = gameActivityValue;
    }

    AnimationSet initializeDrawOne() {
        Log.d(GameActivityAnimationDraw.class.toString(), "initializeDrawOne()");

        Animation animTrans1, animTrans2;
        ImageView ivDest;
        Game game = Game.getInstance();
        Rect rectStart = new Rect();
        Rect rectAuction = new Rect();
        Rect rectDest = new Rect();

        Game.Tile tile = game.getTileLastDrawn();
        ivTile = gameActivity.aivAnimationTiles[0];

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

        ViewGroup.LayoutParams imageLayout = ivTile.getLayoutParams();

        ivTile.setImageResource(gameActivity.TileImageRes(tile));
        ivTile.setX(rectStart.centerX() - imageLayout.width / 2);
        ivTile.setY(rectStart.centerY() - imageLayout.height / 2);


        AnimationSet animationSet = new AnimationSet(true);
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

        return animationSet;
    }

    void close(Animation animation) {
        Log.d(GameActivityAnimationDraw.class.toString(), "close()");
        if (ivTile != null) {
            Log.d(GameActivityAnimationDraw.class.toString(), "Trying removing single tile");
            ivTile.setImageResource(0);
        }
    }

    public void onAnimationEnd(Animation animation) {
        Log.d(GameActivityAnimationDraw.class.toString(), "onAnimationEnd()");
        close(animation);
    }

    public void onAnimationRepeat(Animation animation) {
        Log.d(GameActivityAnimationDraw.class.toString(), "onAnimationRepeat()");
    }

    public void onAnimationStart(Animation animation) {
        Log.d(GameActivityAnimationDraw.class.toString(), "onAnimationStart()");
    }
}
