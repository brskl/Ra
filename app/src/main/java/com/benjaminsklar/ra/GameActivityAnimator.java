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
        Rect rectStart = new Rect();
        Rect rectAuction = new Rect();
        Rect rectDest = new Rect();
        Game game = Game.getInstance();
        GameActivityAnimator gameActivityAnimator = new GameActivityAnimator();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animScale1x, animScale1y, animScale2, animAlpha1, animTrans1x, animTrans1y, animTrans2;

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

        ViewGroup.LayoutParams imageLayout = gameActivityAnimator.imageView.getLayoutParams();

        gameActivityAnimator.imageView.setImageResource(gameActivity.TileImageRes(tile));
        // TODO: try to replace with ofFloat(obj, xprop, yprop, path) for trans and scale
        animTrans1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "x", rectStart.centerX() - imageLayout.width / 2, rectAuction.centerX() - imageLayout.width);
        animTrans1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "y", rectStart.centerY() - imageLayout.height / 2, rectAuction.centerY() - imageLayout.height);
        animAlpha1 = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "alpha", 0.1f, 1.0f);
        animScale1x = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleX", 0.1f, 1.5f);
        animScale1y = ObjectAnimator.ofFloat(gameActivityAnimator.imageView, "scaleY", 0.1f, 1.5f);
        animTrans1x.setDuration(1000);
        animTrans1y.setDuration(1000);
        animAlpha1.setDuration(1000);
        animScale1x.setDuration(1000);
        animScale1y.setDuration(1000);
        animatorSet.play(animTrans1x).with(animTrans1y).with(animScale1x).with(animScale1y);
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
