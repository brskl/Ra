package com.benjaminsklar.ra;

/**
 * Created by Ben on 4/22/2017.
 */

public class GameActivityUpdate {
    GameActivity gameActivity;

    GameActivityUpdate(GameActivity gameActivityValue)
    {
        gameActivity = gameActivityValue;
    }

    void UpdateDisplay(){
        gameActivity.UpdateDisplayRound();
        gameActivity.UpdateDisplayRaTiles();
        gameActivity.UpdateDisplayPlayersSuns();
        gameActivity.UpdateDisplayStatus();
        gameActivity.UpdateDisplayAuction();
        gameActivity.UpdateDisplayButtons();
    }
}
