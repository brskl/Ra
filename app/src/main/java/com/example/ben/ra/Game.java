package com.example.ben.ra;

/**
 * Created by Ben on 12/7/2016.
 */

public class Game {
    static final int nMinPlayers_c = 3;
    static final int nMaxPlayers_c = 5;

    private static Game instance = null;

    protected int nPlayers = 0;
    protected int iEpoch = 1; // 1,2,3 - current epoch

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
        if (nPlayersValue < 3 || nPlayersValue > nMaxPlayers_c)
            throw new IllegalArgumentException("Illegal number of players");

        nPlayers = nPlayersValue;
    }

    public void setPlayer(int index, String name, boolean fLocal, boolean fHuman, int aiLevel)
    {

    }
}
