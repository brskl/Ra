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

public class GameActivityAnimationTile implements Animation.AnimationListener{
    GameActivity gameActivity;
    ImageView ivTile = null;

    GameActivityAnimationTile(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }

        gameActivity = gameActivityValue;
    }

    AnimationSet initializeTakeAll() {
        Log.d(GameActivityAnimationTile.class.toString(), "initializeTakeAll()");
        Game game = Game.getInstance();
        int nTiles = game.getAuction().size();
        int i;
        ImageView imageView;
        Rect rectStart = new Rect();
        Rect rectDest = new Rect();

        AnimationSet animationSet = new AnimationSet(true);
        gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()].getDrawingRect(rectDest);
        gameActivity.llGameActivity.offsetDescendantRectToMyCoords(gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()], rectDest);

        for (i = 0; i < nTiles; i++) {
            GameActivityAnimationTile gameActivityAnimationTile = new GameActivityAnimationTile(gameActivity);
            AnimationSet animationSetTile;
            TranslateAnimation translateAnimation;
            AlphaAnimation alphaAnimation;
            ImageView ivAuctionTile = gameActivity.aivAuctionItems[i];
            // TODO: Create animation for particular tile and add to instance animationSet

            animationSetTile = new AnimationSet(true);
            imageView = new ImageView(gameActivity);
            gameActivityAnimationTile.ivTile = imageView;
            imageView.setImageResource(gameActivity.TileImageRes(game.getAuction().get(i)));

            ivAuctionTile.getDrawingRect(rectStart);
            gameActivity.llGameActivity.offsetDescendantRectToMyCoords(ivAuctionTile, rectStart);

            ViewGroup.LayoutParams startLayout = ivAuctionTile.getLayoutParams();
            ViewGroup.LayoutParams imageLayout = new ViewGroup.LayoutParams(startLayout.width, startLayout.height);

            imageView.setLayoutParams(imageLayout);
            // TODO: Adjusting rects to llGameActivity but can't add to this viewGroup as it is a linearLayout. Is there a better way?
            imageView.setX(rectStart.left - gameActivity.rlBoard.getLeft());
            imageView.setY(rectStart.top - gameActivity.rlBoard.getTop());
            gameActivity.rlBoard.addView(imageView);

            imageView.setAnimation(animationSetTile);

            translateAnimation = new TranslateAnimation(0, rectDest.centerX() - rectStart.centerX(), 0, rectDest.centerY() - rectStart.centerY());
            translateAnimation.setDuration(1000);
            translateAnimation.setStartOffset(100 * i);
            alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(100*i);

            animationSetTile.addAnimation(translateAnimation);
            animationSetTile.addAnimation(alphaAnimation);
            animationSetTile.setFillAfter(true);
            animationSetTile.setAnimationListener(gameActivityAnimationTile);

            animationSet.addAnimation(animationSetTile);
        }
        // TODO: Add swapping of Sun tiles

        return animationSet;
    }

    AnimationSet initializeDrawOne() {
        Log.d(GameActivityAnimationTile.class.toString(), "initializeDrawOne()");

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
        Log.d(GameActivityAnimationTile.class.toString(), "close()");
        if (ivTile != null) {
            Log.d(GameActivityAnimationTile.class.toString(), "Trying removing single tile");
            if (animation.hasEnded()) {
                Log.d(GameActivityAnimationTile.class.toString(), "Actually removing single tile");
                gameActivity.rlBoard.removeView(ivTile);
                ivTile = null;
            }
        }
    }

    public void onAnimationEnd(Animation animation) {
        Log.d(GameActivityAnimationTile.class.toString(), "onAnimationEnd()");
        close(animation);
    }

    public void onAnimationRepeat(Animation animation) {
        Log.d(GameActivityAnimationTile.class.toString(), "onAnimationRepeat()");
    }

    public void onAnimationStart(Animation animation) {
        Log.d(GameActivityAnimationTile.class.toString(), "onAnimationStart()");
    }
}
