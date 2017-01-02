package com.example.ben.ra;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by Ben on 12/9/2016.
 */
abstract class Player {
    static final int iScoreStart_c = 10;
    protected String sName = null;
    protected ArrayList<Integer> alSuns = null, alSunsNext = null;
    protected boolean fLocal = true;
    protected int [] nTiles = null; // count of tiles of type with index eaqual to tile ordinal
    protected int iScore;
    public int [] aiScoreEpoch = null; // God, Gold, Pharoah, Nile/Flood, Civ, Monument, (Suns, player Sun total)
    static final int iScoreGod_c = 0;
    static final int iScoreGold_c = 1;
    static final int iScorePharoah_c = 2;
    static final int iScoreNile_c = 3;
    static final int iScoreCiv_c = 4;
    static final int iScoreMonument_c = 5; // epoch 3 only
    static final int iScoreSuns_c = 6; // epoch 3 only
    static final int iScoreSunsTotal_c = 7; // epoch 3 only
    static final int iScoreTotal_c = 8;

    Player(String name, boolean local)
    {
        sName = name;
        fLocal = local;
        iScore = iScoreStart_c;
        
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

    abstract public boolean getHuman();

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

    public int getScore() { return iScore; }
}
