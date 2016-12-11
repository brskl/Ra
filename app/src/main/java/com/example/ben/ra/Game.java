package com.example.ben.ra;

import android.util.Log;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ben on 12/7/2016.
 */

public class Game {
    static final int nMinPlayers_c = 3;
    static final int nMaxPlayers_c = 5;

    private static Game instance = null;

    protected int nPlayers = 0;
    protected int iPlayerCurrent = -1; // n-1 index
    protected int iEpoch = 1; // 1,2,3 - current epoch
    protected int nRa = 0; // number of Ra tiles played
    protected Player [] aPlayers = null;
    private Random rndPlay = null;

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

    public int getRas() { return nRa; }

    public int getMaxRas() {return nPlayers + 5; }

    public int getPlayerCurrent() { return iPlayerCurrent; }

    public void initialize(int nPlayersValue)
    {
        if (nPlayersValue < nMinPlayers_c || nPlayersValue > nMaxPlayers_c)
            throw new IllegalArgumentException("Illegal number of players");

        Log.v("Gameplay", "Initializing game for " + nPlayersValue + " players.");

        rndPlay = new Random(); // TODO: replace with own random class which can save sequence and replay
        nPlayers = nPlayersValue;

        aPlayers = new Player[nPlayersValue];
    }

    public void setPlayer(int index, String name, boolean fLocal, boolean fHuman, int aiLevel)
    {
        Log.v(Game.class.toString(), "Initializing player[" + Integer.toString(index) + "]");

        // TODO: conditional of fHuman, new PlayerHuman() vs. new PlayerAI()
        aPlayers[index] = new Player(name);
    }

    public void initializeSuns()
    {
        Log.v(Game.class.toString(), "Initializing Suns");
        // assert (aPlayers != null);

        int nSunsPerPlayer;
        int aSunsInitial[][];
        ArrayList<Integer> alSunList = new ArrayList<Integer>(nPlayers);
        int i, j;
        int iSunList, iSun;

        // give initial Sun sets to players, determine starting player. First set must be that for starting player
        switch (nPlayers)
        {
            case 3:
                aSunsInitial = new int[][] {{2, 5, 8, 13}, {3, 6, 9, 12}, {4, 7, 10, 11}};
                nSunsPerPlayer = 4;
                break;
            case 4:
                aSunsInitial = new int[][] {{2, 6, 13}, {3, 7, 12}, {4, 8, 11}, {5, 9, 10}};
                nSunsPerPlayer = 3;
                break;
            case 5:
                aSunsInitial = new int[][] {{2, 7, 16}, {3, 8, 15}, {4, 9, 14}, {5, 10, 13}, {6, 11, 12}};
                nSunsPerPlayer = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid number of players");
        }

        // make list of players
        for (i = 0; i < nPlayers; i++)
        {
            alSunList.add(i);
        }

        for (i = 0; i < nPlayers; i++)
        {
            // if only 1 player left
            if (i == nPlayers - 1)
            {
                // assert (1 == alSunList.size()); // only 1 remaining set
                iSunList = 0;
            }
            else
            {
                // randomly choose a player
                // Assert.assertTrue("", alSunList.size() > 1);
                iSunList = rndPlay.nextInt(alSunList.size());
            }

            iSun = alSunList.remove(iSunList);
            Log.v("Gameplay", "Initial sun distribution, player " + Integer.toString(i+1) + " getting set #" + Integer.toString(iSun+1) + " of suns.");
            aPlayers[i].setSuns(new ArrayList<Integer>(nSunsPerPlayer));
            aPlayers[i].setSunsNext(new ArrayList<Integer>(nSunsPerPlayer));
            for (j = 0; j < nSunsPerPlayer; j++) {
                aPlayers[i].getSuns().add(aSunsInitial[iSun][j]);
            }
            if (iSun == 0)
            {
                Log.v("Gameplay", "Starting player is player #" + Integer.toString(i+1));
                iPlayerCurrent = i; // first player
            }
        }
    }
}
