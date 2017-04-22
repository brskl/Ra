package com.benjaminsklar.ra;

import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Ben on 4/22/2017.
 */

public class GameActivityUpdate {
    GameActivity gameActivity;

    private Button btnOk;
    private Button btnAuction;
    private ImageButton btnDraw;
    private Button btnGod;

    GameActivityUpdate(GameActivity gameActivityValue)
    {
        if (gameActivityValue == null) {
            throw new IllegalArgumentException("null not allowed");
        }
        gameActivity = gameActivityValue;

        btnOk = (Button) gameActivity.findViewById(R.id.buttonOK);
        btnAuction = (Button) gameActivity.findViewById(R.id.buttonAuction);
        btnDraw = (ImageButton) gameActivity.findViewById(R.id.buttonDraw);
        btnGod = (Button) gameActivity.findViewById(R.id.buttonGod);
    }

    void UpdateDisplayButtons()
    {
        boolean fCurrentPlayerLocalHuman;
        boolean fOKonly = true;
        Game game = Game.getInstance();

        fCurrentPlayerLocalHuman = (game.getPlayerCurrent().getHuman() && game.getPlayerCurrent().getLocal());
        if (fCurrentPlayerLocalHuman)
        {
            if (game.getStatusCurrent() == Game.Status.TurnStart)
                fOKonly = false;
        }

        btnOk.setEnabled(fOKonly);
        btnAuction.setEnabled(!fOKonly);
        btnDraw.setEnabled(!fOKonly && !game.FAuctionTrackFull());
        btnGod.setEnabled(!fOKonly && game.FCanUseGod());
    }

    void UpdateDisplay(){
        gameActivity.UpdateDisplayRound();
        gameActivity.UpdateDisplayRaTiles();
        gameActivity.UpdateDisplayPlayersSuns();
        gameActivity.UpdateDisplayStatus();
        gameActivity.UpdateDisplayAuction();
        UpdateDisplayButtons();
    }
}
