package com.example.ben.ra;

import java.util.ArrayList;

/**
 * Created by Ben on 12/9/2016.
 */
public class Player {
    protected String sName = null;
    protected ArrayList<Integer> alSuns = null, alSunsNext = null;
    // TODO: seperate into PlayerHuman and PlayerAi, make Player an abstract class
    protected boolean fHuman;
    protected boolean fLocal = true;
    protected int iAiLevel = -1;
    protected int [] nTiles = null; // count of tiles of type with index eaqual to tile ordinal

    Player(String name, boolean local, boolean human, int AiLevel)
    {
        sName = name;
        fLocal = local;
        fHuman = human;
        iAiLevel = AiLevel;

        nTiles = new int [Game.Tile.values().length];
        // TODO: Check if this init is necessary
        for (int i = 0; i < nTiles.length; i++)
            nTiles[i] = 0;
    }

    public String getName()
    {
        return sName;
    }

    public boolean getLocal() { return fLocal; }

    public boolean getHuman() { return fHuman; }

    public int getAiLevel() { return iAiLevel; }

    public ArrayList<Integer> getSuns()
    {
        return alSuns;
    }

    public void setSuns(ArrayList<Integer> alSunsValue)
    {
        alSuns = alSunsValue;
    }

    public ArrayList<Integer> getSunsNext()
    {
        return alSunsNext;
    }

    public void setSunsNext(ArrayList<Integer> alSunsNextValue)
    {
        alSunsNext = alSunsNextValue;
    }

    public int[] getNTiles() { return nTiles; }
}
