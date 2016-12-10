package com.example.ben.ra;

import android.util.Log;

/**
 * Created by Ben on 12/7/2016.
 */

public class Game {
    static final int nMinPlayers_c = 3;
    static final int nMaxPlayers_c = 5;

    private static Game instance = null;

    protected int nPlayers = 0;
    protected int iEpoch = 1; // 1,2,3 - current epoch
    protected Player [] aPlayers = null;

    private Game() {
        // defeat normal instantiation
    }

    public static Game getInstance() {
        if (instance == null)
        {
            instance = new Game();
        }
        return instance;
    }

    public int getNPlayers()
    {
        return nPlayers;
    }

    public int getEpoch() {
        return iEpoch;
    }

    public void initialize(int nPlayersValue)
    {
        if (nPlayersValue < nMinPlayers_c || nPlayersValue > nMaxPlayers_c)
            throw new IllegalArgumentException("Illegal number of players");

        Log.v("Gameplay", "Initializing game for " + nPlayersValue + " players.");

        nPlayers = nPlayersValue;

        aPlayers = new Player[nPlayersValue];
    }

    public void setPlayer(int index, String name, boolean fLocal, boolean fHuman, int aiLevel)
    {
        Log.v(Game.class.toString(), "Initializing player[" + Integer.toString(index) + "]");

        // TODO: conditional of fHuman, new PlayerHuman() vs. new PlayerAI()
        aPlayers[index] = new Player(name);
    }
}
