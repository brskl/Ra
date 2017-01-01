package com.example.ben.ra;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by Ben on 12/9/2016.
 */
abstract class Player {
    protected String sName = null;
    protected ArrayList<Integer> alSuns = null, alSunsNext = null;
    protected boolean fLocal = true;
    protected int [] nTiles = null; // count of tiles of type with index eaqual to tile ordinal

    Player(String name, boolean local)
    {
        sName = name;
        fLocal = local;

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
}
