package com.benjaminsklar.ra;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

/**
 * Created by Ben on 7/1/2017.
 */

public class GameActivityAnimator implements Animator.AnimatorListener {
    ImageView imageView; // TODO: Is this necessary? Is it possible to use getTarget in onAnim callbacks

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
            gameActivityAnimator.imageView = gameActivity.aivAnimationTileRa;
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
        animTrans1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "x", rectStart.centerX() - imageHalfX , rectAuction.centerX() - imageHalfX);
        animTrans1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "y", rectStart.centerY() - imageHalfY, rectAuction.centerY() - imageHalfY);
        animTrans2x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "x", rectDest.centerX() - imageHalfX);
        animTrans2y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "y", rectDest.centerY() - imageHalfY);
        animAlpha1 = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "alpha", 0.1f, 1.0f);
        animScale1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleX", 0.1f, 1.5f);
        animScale1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleY", 0.1f, 1.5f);
        animScale2x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleX", 1.0f);
        animScale2y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleY", 1.0f);
        animTrans1x.setDuration(1000);
        animTrans1y.setDuration(1000);
        animTrans2x.setDuration(1000);
        animTrans2y.setDuration(1000);
        animTrans2x.setStartDelay(1500);
        animTrans2y.setStartDelay(1500);
        animAlpha1.setDuration(1000);
        animScale1x.setDuration(1000);
        animScale1y.setDuration(1000);
        animScale2x.setDuration(1000);
        animScale2y.setDuration(1000);
        animScale2x.setStartDelay(1500);
        animScale2y.setStartDelay(1500);
        animatorSet.play(animTrans1x).with(animTrans1y).with(animTrans2x).with(animTrans2y);
        animatorSet.play(animTrans1x).with(animScale1x).with(animScale1y).with(animScale2x).with(animScale2y);
        animatorSet.play(animTrans1x).with(animAlpha1);

        animatorSet.addListener(gameActivityAnimator);

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
