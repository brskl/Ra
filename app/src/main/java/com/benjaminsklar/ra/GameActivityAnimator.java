package com.benjaminsklar.ra;

import android.animation.Animator;
import android.animation.AnimatorSet;
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

    static ViewPropertyAnimator initializeDrawOne(GameActivity gameActivity) {
        Log.d(GameActivityAnimator.class.toString(), "initializeDrawOne()");

        ImageView ivDest;
        Rect rectStart = new Rect();
        Rect rectAuction = new Rect();
        Rect rectDest = new Rect();
        Game game = Game.getInstance();
        GameActivityAnimator gameActivityAnimator = new GameActivityAnimator();
        AnimatorSet animatorSet = new AnimatorSet();

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
        gameActivityAnimator.imageView.setX(rectStart.centerX() - imageLayout.width / 2);
        gameActivityAnimator.imageView.setY(rectStart.centerY() - imageLayout.height / 2);
        gameActivityAnimator.imageView.setAlpha(0.0f);
        gameActivityAnimator.imageView.setScaleX(1.0f);
        gameActivityAnimator.imageView.setScaleY(1.0f);

        ViewPropertyAnimator animator = gameActivityAnimator.imageView.animate();
        animator.setDuration(1000);
        animator.alpha(1.0f).xBy(rectAuction.centerX() - rectStart.centerX()).yBy(rectAuction.centerY() - rectStart.centerY());
        animator.scaleX(2.0f);
        animator.scaleY(2.0f);

        animator.setListener(gameActivityAnimator);

        return animator;
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
