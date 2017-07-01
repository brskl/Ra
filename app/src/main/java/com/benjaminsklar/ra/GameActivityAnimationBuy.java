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
 * Created by Ben on 6/28/2017.
 */

public class GameActivityAnimationBuy implements Animation.AnimationListener {
    GameActivity gameActivity;
    ImageView aivAnimationAuctionTiles[];

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
        Rect rectStart = new Rect();
        Rect rectDest = new Rect();

        aivAnimationAuctionTiles = new ImageView[nTiles];
        AnimationSet animationSet = new AnimationSet(true);
        gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()].getDrawingRect(rectDest);
        gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(gameActivity.arlPlayers[game.getAuctionPlayerHighestIndex()], rectDest);
        rectDest.offset(-gameActivity.aivAuctionItems[0].getWidth() / 2, - gameActivity.aivAuctionItems[0].getHeight()/2);

        for (i = 0; i < nTiles; i++) {
            AnimationSet animationSetTile;
            TranslateAnimation translateAnimation;
            AlphaAnimation alphaAnimation;
            ImageView ivAuctionTile = gameActivity.aivAuctionItems[i];

            aivAnimationAuctionTiles[i] = gameActivity.aivAnimationTiles[i];

            animationSetTile = new AnimationSet(true);
            aivAnimationAuctionTiles[i].setImageResource(gameActivity.TileImageRes(game.getAuction().get(i)));

            ivAuctionTile.getDrawingRect(rectStart);
            gameActivity.rlGameActivity.offsetDescendantRectToMyCoords(ivAuctionTile, rectStart);

            aivAnimationAuctionTiles[i].setX(rectStart.left - gameActivity.rlGameActivity.getLeft());
            aivAnimationAuctionTiles[i].setY(rectStart.top - gameActivity.rlGameActivity.getTop());

            aivAnimationAuctionTiles[i].setAnimation(animationSetTile);

            translateAnimation = new TranslateAnimation(0, rectDest.centerX() - rectStart.centerX(), 0, rectDest.centerY() - rectStart.centerY());
            translateAnimation.setDuration(1000);
            translateAnimation.setStartOffset(100 * i);
            alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(100*i);

            animationSetTile.addAnimation(translateAnimation);
            animationSetTile.addAnimation(alphaAnimation);
            animationSetTile.setFillAfter(true);

            animationSet.addAnimation(animationSetTile);
        }
        // TODO: Add swapping of Sun tiles

        animationSet.setAnimationListener(this);
        return animationSet;
    }

    void close(Animation animation) {
        Log.d(GameActivityAnimationBuy.class.toString(), "close()");
        for (ImageView imageView : aivAnimationAuctionTiles) {
            imageView.setVisibility(View.INVISIBLE);
        }
        aivAnimationAuctionTiles = null;
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
