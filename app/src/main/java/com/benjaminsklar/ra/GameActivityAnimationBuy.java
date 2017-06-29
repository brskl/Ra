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
 * Created by Ben on 6/28/2017.
 */

public class GameActivityAnimationBuy implements Animation.AnimationListener {
    GameActivity gameActivity;

    GameActivityAnimationBuy(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }

        gameActivity = gameActivityValue;
    }

    AnimationSet initializeTakeAll() {
        Log.d(GameActivityAnimationDraw.class.toString(), "initializeTakeAll()");
        Game game = Game.getInstance();
        int nTiles = game.getAuction().size();
        int i;
        ImageView imageView;
        Rect rectStart = new Rect();
        Rect rectDest = new Rect();

        AnimationSet animationSet = new AnimationSet(true);
        gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()].getDrawingRect(rectDest);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()], rectDest);
        imageView = gameActivity.aivAuctionItems[0];
        rectDest.offset(-imageView.getWidth() / 2, - imageView.getHeight()/2);

        for (i = 0; i < nTiles; i++) {
            GameActivityAnimationDraw gameActivityAnimationTile = new GameActivityAnimationDraw(gameActivity);
            AnimationSet animationSetTile;
            TranslateAnimation translateAnimation;
            AlphaAnimation alphaAnimation;
            ImageView ivAuctionTile = gameActivity.aivAuctionItems[i];
            // TODO: Create animation for particular tile and add to instance animationSet

            animationSetTile = new AnimationSet(true);
            imageView = new ImageView(gameActivity);
            imageView.setImageResource(gameActivity.TileImageRes(game.getAuction().get(i)));

            ivAuctionTile.getDrawingRect(rectStart);
            gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(ivAuctionTile, rectStart);

            ViewGroup.LayoutParams startLayout = ivAuctionTile.getLayoutParams();
            ViewGroup.LayoutParams imageLayout = new ViewGroup.LayoutParams(startLayout.width, startLayout.height);

            imageView.setLayoutParams(imageLayout);
            imageView.setX(rectStart.left - gameActivity.rlGameActivity.getLeft());
            imageView.setY(rectStart.top - gameActivity.rlGameActivity.getTop());
            gameActivity.rlGameActivity.addView(imageView);

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

    void close(Animation animation) {
        Log.d(GameActivityAnimationBuy.class.toString(), "close()");
    }

    public void onAnimationEnd(Animation animation) {
        Log.d(GameActivityAnimationBuy.class.toString(), "onAnimationEnd()");
        close(animation);
    }

    public void onAnimationRepeat(Animation animation) {
        Log.d(GameActivityAnimationBuy.class.toString(), "onAnimationRepeat()");
    }

    public void onAnimationStart(Animation animation) {
        Log.d(GameActivityAnimationBuy.class.toString(), "onAnimationStart()");
    }
}
