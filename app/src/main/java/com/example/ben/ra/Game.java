package com.example.ben.ra;

/**
 * Created by Ben on 12/7/2016.
 */

public class Game {
    static final int nMinPlayers_c = 3;
    static final int nMaxPlayers_c = 5;

    private static Game instance = null;

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

    public void initialize(int nPlayers)
    {

    }

    public void setPlayer(int index, String name, boolean fLocal, boolean fHuman, int aiLevel)
    {

    }
}
