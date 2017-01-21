package com.example.ben.ra;

import junit.framework.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ben on 12/9/2016.
 */
abstract class Player implements Serializable {
    static final int iScoreStart_c = 10;
    String sName = null;
    ArrayList<Integer> alSuns = null, alSunsNext = null;
    boolean fLocal = true;
    int [] nTiles = null; // count of tiles of type with index eaqual to tile ordinal
    int iScore;
    int [] aiScoreEpoch = null; // God, Gold, Pharoah, Nile/Flood, Civ, Monument, (Suns, player Sun total)
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

    String getName()
    {
        return sName;
    }

    boolean getLocal() { return fLocal; }

    abstract boolean getHuman();

    ArrayList<Integer> getSuns()
    {
        return alSuns;
    }

    void setSuns(ArrayList<Integer> alSunsValue)
    {
        alSuns = alSunsValue;
    }

    ArrayList<Integer> getSunsNext()
    {
        return alSunsNext;
    }

    void setSunsNext(ArrayList<Integer> alSunsNextValue)
    {
        alSunsNext = alSunsNextValue;
    }

    int[] getNTiles() { return nTiles; }

    int getScore() { return iScore; }

    void MoveSuns()
    {
        Assert.assertNotNull(alSuns);
        Assert.assertNotNull(alSunsNext);

        // Move any remaining Suns to next list
        while (!alSuns.isEmpty())
        {
            alSunsNext.add(alSuns.remove(0));
        }

        // Make next list into the current list
        alSuns = alSunsNext;
        alSunsNext = new ArrayList<Integer>(alSuns.size());
        Collections.sort(alSuns);
    }

    void SetForNextEpoch()
    {
        MoveSuns();

        // remove appropriate tiles
        nTiles[Game.Tile.tCiv1.ordinal()] = 0;
        nTiles[Game.Tile.tCiv2.ordinal()] = 0;
        nTiles[Game.Tile.tCiv3.ordinal()] = 0;
        nTiles[Game.Tile.tCiv4.ordinal()] = 0;
        nTiles[Game.Tile.tCiv5.ordinal()] = 0;
        nTiles[Game.Tile.tGod.ordinal()] = 0;
        nTiles[Game.Tile.tGold.ordinal()] = 0;
        nTiles[Game.Tile.tFlood.ordinal()] = 0;

        iScore += aiScoreEpoch[iScoreGod_c] + aiScoreEpoch[iScoreGold_c] + aiScoreEpoch[iScorePharoah_c] + aiScoreEpoch[iScoreNile_c] + aiScoreEpoch[iScoreCiv_c];

        aiScoreEpoch = null;
    }


}
