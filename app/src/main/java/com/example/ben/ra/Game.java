package com.example.ben.ra;

import android.util.Log;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Ben on 12/7/2016.
 */

class Game {
    static final int nMinPlayers_c = 3;
    static final int nMaxPlayers_c = 5;
    static final int nMaxAuction_c = 8;
    static final int nEpochs_c = 3;
    static final int iScoreGoldValue_c = 3;
    static final int iScoreGodValue_c = 2;
    static final int iScorePharaohMinValue_c = -2;
    static final int iScorePharaohMaxValue_c = 5;
    static final int iScoreCivValue_0_c = -5;
    static final int iScoreCivValue_3_c = 5;
    static final int iScoreCivValue_4_c = 10;
    static final int iScoreCivValue_5_c = 15;
    static final int iScoreMonNumValue_5_c = 15;
    static final int iScoreMonNumValue_4_c = 10;
    static final int iScoreMonNumValue_3_c = 5;
    static final int iScoreMonTypeValue_8_c = 15;
    static final int iScoreMonTypeValue_7_c = 10;
    static final int iScoreSunMinValue_c = -5;
    static final int iScoreSunMaxValue_c = 5;
    static final int iTilesLostPerDisaster_c = 2;

    enum Status { TurnStart, DrewTile, UsedGod, CallsAuction, AuctionInProgress, AuctionWon, AuctionEveryonePassed, ResolveDisaster, EpochOver };
    enum Tile {
        tNone,	// none
        tRa,	// Ra
        tGod,	// God
        tGold,	// Gold
        tPharaoh,	// Pharaoh
        tNile, tFlood, // Nile, Flooding Nile
        tCiv1, tCiv2, tCiv3, tCiv4, tCiv5, // Civilization tiles
        tMon1, tMon2, tMon3, tMon4, tMon5, tMon6, tMon7, tMon8, // Monument tiles
        tDisasterP, tDisasterN, tDisasterC, tDisasterM // Disaster tiles (Pharaoh, Nile/Flood, Civ, Monument)
    };
    // Number of each kind of tile, must be in same order as enum
    private static final int anTileTypes_c[] = {
            0,	// none
            30,	// Ra
            8,	// God
            5,	// Gold
            25,	// Pharaoh
            25, 12,	// Nile, Flooding Nile
            5, 5, 5, 5, 5,	// Civilization tiles
            5, 5, 5, 5, 5, 5, 5, 5,	// Monument tiles
            2, 2, 4, 2 // Disaster tiles (Pharaoh, Nile/Flood, Civ, Monument)
    };

    private static Game instance = null;

    int nPlayers = 0;
    int iPlayerCurrent = -1; // n-1 index
    int iEpoch; // 1,2,3 - current epoch
    Status statusCurrent;
    int nRa; // number of Ra tiles played
    Player [] aPlayers = null;
    int iAtAuctionSun;
    ArrayList<Tile> altAuction = null;
    ArrayList<Tile> altTilebag = null;
    int [] anTilebag = null;
    Tile tLastDrawn;

    // auction state
    int iAuctionPlayerCaller;
    int iAuctionPlayerCurrent;
    int iAuctionPlayerHighest;
    int iAuctionHighBid;
    boolean fAuctionVoluntary;

    private MyRandom rndPlay = null;

    private Game() {
        // private constructor so as to defeat normal instantiation

        rndPlay = new MyRandom();
    }

    static Game getInstance() {
        if (instance == null)
        {
            instance = new Game();
        }
        return instance;
    }

    int getNPlayers()
    {
        return nPlayers;
    }

    int getEpoch() {
        return iEpoch;
    }

    Status getStatusCurrent() { return statusCurrent; }

    int getAtAuctionSun() { return iAtAuctionSun; }

    int getRas() { return nRa; }

    int getMaxRas() {return nPlayers + 5; }

    Player getPlayerCurrent() { return aPlayers[iPlayerCurrent]; }

    Player getAuctionPlayerCurrent() { return aPlayers[iAuctionPlayerCurrent]; }

    Player getAuctionPlayerHighest() { return aPlayers[iAuctionPlayerHighest]; }

    int getAuctionHighBid() { return iAuctionHighBid; }

    boolean FAuctionCurrentPlayerBidHighest() { return (iAuctionPlayerCurrent == iAuctionPlayerHighest); }

    boolean FAuctionTrackFull()
    {
        return (altAuction.size() == nMaxAuction_c);
    }

    ArrayList<Tile> getAuction() { return altAuction; }

    Tile getTileLastDrawn() { return tLastDrawn; }

    Player [] getPlayers() { return aPlayers; }


    private void initalizeTiles()
    {
        Log.v(Game.class.toString(), "Initializing tiles.");

        int nTiles;
        int i, j;

        anTilebag = anTileTypes_c.clone();

        // put tiles in bag
        // total is constant, but easier to figure , TODO: change this???
        nTiles = 0;
        for (i = 0; i < anTileTypes_c.length; i++) {
            nTiles += anTileTypes_c[i];
        }

        altTilebag = new ArrayList<Tile>(nTiles);

        for (i = 0; i < anTileTypes_c.length; i++)
        {
            for (j = 0; j < anTileTypes_c[i]; j++)
            {
                altTilebag.add(Tile.values()[i]);
            }
        }
    }

    void initialize(int nPlayersValue)
    {
        if (nPlayersValue < nMinPlayers_c || nPlayersValue > nMaxPlayers_c)
            throw new IllegalArgumentException("Illegal number of players");

        Log.v(Game.class.toString(), "Initializing game for " + nPlayersValue + " players.");

        nPlayers = nPlayersValue;

        aPlayers = new Player[nPlayersValue];

        altAuction = new ArrayList<Tile>(nMaxAuction_c);

        tLastDrawn = Tile.tNone;
        statusCurrent = Status.TurnStart;
        nRa = 0;
        iAtAuctionSun = 1; // Sun tile 1 always in auction at start of game
        iEpoch = 1;

        initalizeTiles();
    }

    void setPlayer(int index, String name, boolean fLocal, boolean fHuman, int aiLevel)
    {
        Log.v(Game.class.toString(), "Initializing player[" + Integer.toString(index) + "]");

        if (fHuman)
        {
            aPlayers[index] = new PlayerHuman(name, fLocal);
        }
        else
        {
            aPlayers[index] = new PlayerAi(name, fLocal, aiLevel);
        }
    }

    void initializeSuns()
    {
        Log.v(Game.class.toString(), "Initializing Suns");
        Assert.assertNotNull(aPlayers);

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
                Assert.assertEquals(1, alSunList.size()); // only 1 remaining set
                iSunList = 0;
            }
            else
            {
                // randomly choose a player
                Assert.assertTrue(alSunList.size() > 1);
                iSunList = rndPlay.nextInt(alSunList.size());
            }

            iSun = alSunList.remove(iSunList);
            Log.v(Game.class.toString(), "Initial sun distribution, player " + Integer.toString(i+1) + " getting set #" + Integer.toString(iSun+1) + " of suns.");
            aPlayers[i].setSuns(new ArrayList<Integer>(nSunsPerPlayer));
            aPlayers[i].setSunsNext(new ArrayList<Integer>(nSunsPerPlayer));
            for (j = 0; j < nSunsPerPlayer; j++) {
                aPlayers[i].getSuns().add(aSunsInitial[iSun][j]);
            }
            if (iSun == 0)
            {
                Log.v(Game.class.toString(), "Starting player is player #" + Integer.toString(i+1));
                iPlayerCurrent = i; // first player
            }
        }
    }

    void setRandomSeed(long seed)
    {
        Log.v(Game.class.toString(), "Setting random seed to " + seed);
        Assert.assertNotNull(rndPlay);

        rndPlay.setSeed(seed);
    }

    void DrawTile()
    {
        int iTile;

        Assert.assertTrue("Auction block should have space left", altAuction.size() < nMaxAuction_c);
        Assert.assertFalse("Tile bag should not be empty", altTilebag.isEmpty());

        iTile = rndPlay.nextInt(altTilebag.size());
        // iTile = 0; // test sequence

        tLastDrawn = altTilebag.remove(iTile);
        anTilebag[tLastDrawn.ordinal()]--;
        Assert.assertTrue(anTilebag[tLastDrawn.ordinal()] >= 0);
        Log.v(Game.class.toString(), "drawn from tile bag: " + tLastDrawn.name());
        if (tLastDrawn == Tile.tRa)
        {
            nRa++;
            Assert.assertTrue("Too many Ras", nRa <= getMaxRas());
        }
        else
        {
            altAuction.add(tLastDrawn);
        }
        statusCurrent = Status.DrewTile;
    }

    boolean DoExchangeForGod(Tile t)
    {
        int [] anPlayerTiles = getPlayerCurrent().getNTiles();

        if (anPlayerTiles[Tile.tGod.ordinal()] <= 0)
            return false;
        if (!altAuction.remove(t))
            return false;
        Log.v(Game.class.toString(), "Player " + getPlayerCurrent().getName() + " exchanged a God tile for a " + t);
        anPlayerTiles[Tile.tGod.ordinal()]--;
        anPlayerTiles[t.ordinal()]++;
        statusCurrent = Status.UsedGod;
        return true;
    }

    boolean FCanUseGod()
    {
        Player player = getPlayerCurrent();

        // Must have at least 1 god tile and their must be a tile other than a God tile in the auction
        // can take disaster tiles, but why would you?
        if (player.getNTiles()[Tile.tGod.ordinal()] == 0)
            return false;
        for (Tile t: altAuction)
        {
            if ((t != Tile.tGod) &&
                    (t != Tile.tDisasterC) &&
                    (t != Tile.tDisasterM) &&
                    (t != Tile.tDisasterN) &&
                    (t != Tile.tDisasterP))
                return true;
        }

        return false;
    }

    private boolean FEpochOver()
    {
        switch (statusCurrent)
        {
            case DrewTile:
                return (nRa == getMaxRas());
            case AuctionWon:
                for (Player player : aPlayers)
                {
                    if (!player.alSuns.isEmpty())
                        return false;
                }
                return true;
            default:
                return false;
        }
    }

    boolean TestEpochOver()
    {
        if (FEpochOver())
        {
            Assert.assertTrue(statusCurrent == Status.DrewTile || statusCurrent == Status.AuctionWon);
            statusCurrent = Status.EpochOver;

            return true;
        } else {
            return false;
        }
    }

    boolean FLastEpoch()
    {
        Assert.assertTrue("Invalid number of epochs", getEpoch() <= nEpochs_c && getEpoch() >= 1);
        return (getEpoch() == nEpochs_c);
    }

    boolean SetupNextEpoch()
    {
        int iSunValue, iMaxSun = Integer.MIN_VALUE;

        Log.v(Game.class.toString(), "SetupNextEpoch(), Epoch " + iEpoch + " over");
        Assert.assertEquals(Status.EpochOver, getStatusCurrent());

        if (!FLastEpoch())
        {
            for (int iPlayer = 0; iPlayer < nPlayers; iPlayer++)
            {
                aPlayers[iPlayer].SetForNextEpoch();
                iSunValue = aPlayers[iPlayer].alSuns.get(aPlayers[iPlayer].alSuns.size()-1);
                if (iSunValue > iMaxSun)
                {
                    iMaxSun = iSunValue;
                    iPlayerCurrent = iPlayer;
                }
            }
            Log.v(Game.class.toString(), "Starting player is " + iPlayerCurrent);

            nRa = 0;
            altAuction.clear();
            iEpoch++;

            statusCurrent = Status.TurnStart;
            return false;
        } else {
            // game over
            Log.v(Game.class.toString(), "Game over");
            return true;
        }
    }



    void UpdateScore()
    {
        Log.v(Game.class.toString(), "UpdateScore");

        int iPharaohMax = Integer.MIN_VALUE, iPharaohMin = Integer.MAX_VALUE;
        int iSunMax = Integer.MIN_VALUE, iSunMin = Integer.MAX_VALUE;

        for (Player player: aPlayers)
        {
            player.aiScoreEpoch = new int[9];

            if (player.getNTiles()[Tile.tPharaoh.ordinal()] < iPharaohMin)
                iPharaohMin = player.getNTiles()[Tile.tPharaoh.ordinal()];
            if (player.getNTiles()[Tile.tPharaoh.ordinal()] > iPharaohMax)
                iPharaohMax = player.getNTiles()[Tile.tPharaoh.ordinal()];

            player.aiScoreEpoch[Player.iScoreSunsTotal_c] = 0;
            if (FLastEpoch()) {
                for (int i: player.getSuns()) {
                    player.aiScoreEpoch[Player.iScoreSunsTotal_c] += i;
                }
                for (int i: player.getSunsNext()) {
                    player.aiScoreEpoch[Player.iScoreSunsTotal_c] += i;
                }
                if (player.aiScoreEpoch[Player.iScoreSunsTotal_c] < iSunMin)
                    iSunMin = player.aiScoreEpoch[Player.iScoreSunsTotal_c];
                if (player.aiScoreEpoch[Player.iScoreSunsTotal_c] > iSunMax)
                    iSunMax = player.aiScoreEpoch[Player.iScoreSunsTotal_c];
            }
        }

        Log.v(Game.class.toString(), String.format("Pharaoh min/max %d/%d", iPharaohMin, iPharaohMax));
        Log.v(Game.class.toString(), String.format("Sun min/max %d/%d", iSunMin, iSunMax));

        for (Player player: aPlayers) {
            // God 2*#
            player.aiScoreEpoch[Player.iScoreGod_c] = iScoreGodValue_c * player.getNTiles()[Tile.tGod.ordinal()];

            // Gold 3*#
            player.aiScoreEpoch[Player.iScoreGold_c] = iScoreGoldValue_c * player.getNTiles()[Tile.tGold.ordinal()];

            // Pharaohs min# -2, max# 5, all same 0
            if (iPharaohMin != iPharaohMax) {
                if (player.getNTiles()[Tile.tPharaoh.ordinal()] == iPharaohMin) {
                    player.aiScoreEpoch[Player.iScorePharoah_c] = iScorePharaohMinValue_c;
                } else {
                    if (player.getNTiles()[Tile.tPharaoh.ordinal()] == iPharaohMax) {
                        player.aiScoreEpoch[Player.iScorePharoah_c] = iScorePharaohMaxValue_c;
                    }
                }
            }

            // Nile,Flood sum if at least 1 flood
            if (player.getNTiles()[Tile.tFlood.ordinal()] > 0) {
                player.aiScoreEpoch[Player.iScoreNile_c] = player.getNTiles()[Tile.tFlood.ordinal()] + player.getNTiles()[Tile.tNile.ordinal()];
            }

            // Civ: #types =0->-5, 3->5, 4->10, 5->15
            int nCategories = 0;
            for (int i = Tile.tCiv1.ordinal(); i <= Tile.tCiv5.ordinal(); i++)
            {
                if (player.getNTiles()[i] > 0)
                    nCategories++;
            }
            switch (nCategories)
            {
                case 5:
                    player.aiScoreEpoch[Player.iScoreCiv_c] = iScoreCivValue_5_c;
                    break;
                case 4:
                    player.aiScoreEpoch[Player.iScoreCiv_c] = iScoreCivValue_4_c;
                    break;
                case 3:
                    player.aiScoreEpoch[Player.iScoreCiv_c] = iScoreCivValue_3_c;
                    break;
                case 0:
                    player.aiScoreEpoch[Player.iScoreCiv_c] = iScoreCivValue_0_c;
                    break;
                default:
                    // no change in score
                    break;
            }

            if (FLastEpoch())
            {
                // Monuments nDiff 0-6 -> #, 7 -> 10, 8 -> 15 and
                // For each type 3->5, 4->10, 5->15
                int nMonumentTypes = 0;
                for (int i = Tile.tMon1.ordinal(); i <= Tile.tMon8.ordinal(); i++)
                {
                    if (player.getNTiles()[i] > 0)
                    {
                        nMonumentTypes++;
                        switch(player.getNTiles()[i])
                        {
                            case 5:
                                player.aiScoreEpoch[Player.iScoreMonument_c] += iScoreMonNumValue_5_c;
                                break;
                            case 4:
                                player.aiScoreEpoch[Player.iScoreMonument_c] += iScoreMonNumValue_4_c;
                                break;
                            case 3:
                                player.aiScoreEpoch[Player.iScoreMonument_c] += iScoreMonNumValue_3_c;
                                break;
                            default:
                                break;
                        }
                    }
                }
                switch(nMonumentTypes)
                {
                    case 8:
                        player.aiScoreEpoch[Player.iScoreMonument_c] += iScoreMonTypeValue_8_c;
                        break;
                    case 7:
                        player.aiScoreEpoch[Player.iScoreMonument_c] += iScoreMonTypeValue_7_c;
                        break;
                    default:
                        Assert.assertTrue(nMonumentTypes >= 0 && nMonumentTypes <= 6);
                        player.aiScoreEpoch[Player.iScoreMonument_c] += nMonumentTypes;
                        break;
                }

                // Sun total, min -5, max 5
                if (player.aiScoreEpoch[Player.iScoreSunsTotal_c] == iSunMin)
                {
                    player.aiScoreEpoch[Player.iScoreSuns_c] = iScoreSunMinValue_c;
                } else {
                    if (player.aiScoreEpoch[Player.iScoreSunsTotal_c] == iSunMax) {
                        player.aiScoreEpoch[Player.iScoreSuns_c] = iScoreSunMaxValue_c;
                    }
                }

            }

            // new total
            player.aiScoreEpoch[Player.iScoreTotal_c] = player.getScore() +
                player.aiScoreEpoch[Player.iScoreGod_c] +
                player.aiScoreEpoch[Player.iScoreGold_c] +
                player.aiScoreEpoch[Player.iScorePharoah_c] +
                player.aiScoreEpoch[Player.iScoreNile_c] +
                player.aiScoreEpoch[Player.iScoreCiv_c] +
                player.aiScoreEpoch[Player.iScoreMonument_c] +
                player.aiScoreEpoch[Player.iScoreSuns_c];
        }
    }

    void SetNextPlayerTurn()
    {
        int iNext = iPlayerCurrent;
        int i;

        // Can be same player if current player has suns and all other players do not.

        for (i = 0; i < nPlayers; i++)
        {
            iNext = (iNext + 1) % nPlayers;
            if (!aPlayers[iNext].alSuns.isEmpty())
                break;
        }
        iPlayerCurrent = iNext;
        Assert.assertTrue("Can't determine next player", !aPlayers[iPlayerCurrent].alSuns.isEmpty() || FEpochOver());
        statusCurrent = Status.TurnStart;
    }

    void InitAuction(boolean fVoluntary)
    {
        Log.v(Game.class.toString(), "Auction called voluntary: " + fVoluntary + " by player " + getPlayerCurrent().getName());

        fAuctionVoluntary = fVoluntary;
        iAuctionPlayerCaller = iPlayerCurrent;
        iAuctionPlayerCurrent = iPlayerCurrent;
        iAuctionPlayerHighest = Integer.MIN_VALUE;
        iAuctionHighBid = Integer.MIN_VALUE;
        statusCurrent = Status.AuctionInProgress;
    }

    void SetNextPlayerAuction()
    {
        Log.v(Game.class.toString(), "SetNextPlayerAuction");

        int iNext = iAuctionPlayerCurrent;
        int i;

        for (i = 0; i < nPlayers; i++)
        {
            iNext = (iNext + 1) % nPlayers;
            if (!aPlayers[iNext].alSuns.isEmpty())
                break;
        }
        iAuctionPlayerCurrent = iNext;
        Assert.assertTrue("Can't determine next player", !aPlayers[iAuctionPlayerCurrent].alSuns.isEmpty() || FEpochOver());
    }

    boolean FCanBid()
    {
        int iHighestSun;

        Assert.assertTrue(iAuctionPlayerHighest < nPlayers);
        Assert.assertTrue(0 <= iAuctionPlayerCurrent && iAuctionPlayerCurrent < nPlayers);

        // if no bid
        if (iAuctionHighBid == 0)
            return true;

        Assert.assertTrue(aPlayers[iAuctionPlayerCurrent].alSuns.size() > 0);
        iHighestSun = aPlayers[iAuctionPlayerCurrent].alSuns.get(aPlayers[iAuctionPlayerCurrent].alSuns.size() - 1);
        if (iHighestSun > iAuctionHighBid)
            return true;
        Log.v(Game.class.toString(), "Player " + aPlayers[iAuctionPlayerCurrent].getName() + " can not bid");
        return false;
    }

    boolean FMustBid()
    {
        // player must bid if voluntary auction and everyone else passed
        if (iAuctionHighBid > 0)
            return false;
        if (!fAuctionVoluntary)
            return false;
        if (iAuctionPlayerCurrent != iAuctionPlayerCaller)
            return false;

        return true;
    }

    boolean FAuctionFinished()
    {
        return (iAuctionPlayerCaller == iAuctionPlayerCurrent);
    }

    void MakeBid(int valueBid)
    {
        if (valueBid == 0)
        {
            Log.v(Game.class.toString(), "Player " + getAuctionPlayerCurrent().getName() + " passes");
            return;
        }

        if (!getAuctionPlayerCurrent().getSuns().contains(valueBid)) {
            throw new IllegalArgumentException("Invalid sun value");
        }
        if (valueBid <= iAuctionHighBid) {
            throw new IllegalArgumentException("Illegal bid, too low");
        }
        Log.v(Game.class.toString(), "Player " + getAuctionPlayerCurrent().getName() + " bids tile with value of " + valueBid);
        iAuctionPlayerHighest = iAuctionPlayerCurrent;
        iAuctionHighBid = valueBid;

    }

    // return value: true - player needs to resolve non-auto disaster tiles
    private boolean ResolveDisastersAuto()
    {
        Player playerWinner = aPlayers[iAuctionPlayerHighest];
        int [] aiPlayerTiles = playerWinner.getNTiles();
        int nDisasterTiles;
        int nLose;
        int nHave;
        boolean fResult = false;

        // if any Funerals (Pharaoh) disaster tiles
        if (aiPlayerTiles[Tile.tDisasterP.ordinal()] > 0)
        {
            nDisasterTiles = aiPlayerTiles[Tile.tDisasterP.ordinal()];
            nLose = iTilesLostPerDisaster_c * nDisasterTiles;
            aiPlayerTiles[Tile.tDisasterP.ordinal()] = 0;
            if (aiPlayerTiles[Tile.tPharaoh.ordinal()] < nLose)
            {
                nLose = aiPlayerTiles[Tile.tPharaoh.ordinal()];
            }
            Log.v(Game.class.toString(), "Pharaoh disaster tile(s) " + nDisasterTiles + ", lose " +
                    nLose + " of " +
                    aiPlayerTiles[Tile.tPharaoh.ordinal()] + " Pharaoh tiles " );
            aiPlayerTiles[Tile.tPharaoh.ordinal()] -= nLose;
        }

        // if any Drought (Flood, Nile) disaster tiles
        if (aiPlayerTiles[Tile.tDisasterN.ordinal()] > 0)
        {
            int nLoseFlood = 0, nLoseNile = 0;
            nDisasterTiles = aiPlayerTiles[Tile.tDisasterN.ordinal()];
            nLose = iTilesLostPerDisaster_c * nDisasterTiles;
            aiPlayerTiles[Tile.tDisasterN.ordinal()] = 0;
            if (aiPlayerTiles[Tile.tFlood.ordinal()] >= nLose)
            {
                nLoseFlood = nLose;
            }
            else
            {
                nLoseFlood = aiPlayerTiles[Tile.tFlood.ordinal()];
                nLose -= nLoseFlood;
                if (aiPlayerTiles[Tile.tNile.ordinal()] >= nLose)
                {
                    nLoseNile = nLose;
                }
                else
                {
                    nLoseNile = aiPlayerTiles[Tile.tNile.ordinal()];
                }
            }
            Log.v(Game.class.toString(), "Nile/Flood disaster tile(s) " + nDisasterTiles + ", lose " +
                    nLoseFlood + " flood tiles and " + nLoseNile + " Nile tiles");
            aiPlayerTiles[Tile.tFlood.ordinal()] -= nLoseFlood;
            aiPlayerTiles[Tile.tNile.ordinal()] -= nLoseNile;
        }

        // if any Unrest (Civilization) disaster tiles
        if (aiPlayerTiles[Tile.tDisasterC.ordinal()] > 0)
        {
            int nCivTiles;
            nDisasterTiles = aiPlayerTiles[Tile.tDisasterC.ordinal()];
            nLose = iTilesLostPerDisaster_c * nDisasterTiles;

            nCivTiles = aiPlayerTiles[Tile.tCiv1.ordinal()] +
                    aiPlayerTiles[Tile.tCiv2.ordinal()] +
                    aiPlayerTiles[Tile.tCiv3.ordinal()] +
                    aiPlayerTiles[Tile.tCiv4.ordinal()] +
                    aiPlayerTiles[Tile.tCiv5.ordinal()];
            if (nCivTiles <= nLose)
            {
                Log.v(Game.class.toString(), "Civ disaster tile(s) " + nDisasterTiles + ", with only " + nCivTiles + " Civ tiles, lose all");
                aiPlayerTiles[Tile.tDisasterC.ordinal()] = 0;

                aiPlayerTiles[Tile.tCiv1.ordinal()] = 0;
                aiPlayerTiles[Tile.tCiv2.ordinal()] = 0;
                aiPlayerTiles[Tile.tCiv3.ordinal()] = 0;
                aiPlayerTiles[Tile.tCiv4.ordinal()] = 0;
                aiPlayerTiles[Tile.tCiv5.ordinal()] = 0;
            }
            else
            {
                // TODO: check if all civ tiles are the same type, then just deduct from that single type

                Log.v(Game.class.toString(), "Civ disaster tile(s) " + nDisasterTiles + ", with " + nCivTiles + " Civ tiles, user input needed");
                statusCurrent = Status.ResolveDisaster;
                fResult = true;
            }
        }

        // if any Earthquake (Monument) disaster tiles
        if (aiPlayerTiles[Tile.tDisasterM.ordinal()] > 0)
        {
            int nMonumentTiles;
            nDisasterTiles = aiPlayerTiles[Tile.tDisasterM.ordinal()];
            nLose = iTilesLostPerDisaster_c * nDisasterTiles;

            nMonumentTiles = aiPlayerTiles[Tile.tMon1.ordinal()] +
                    aiPlayerTiles[Tile.tMon2.ordinal()] +
                    aiPlayerTiles[Tile.tMon3.ordinal()] +
                    aiPlayerTiles[Tile.tMon4.ordinal()] +
                    aiPlayerTiles[Tile.tMon5.ordinal()] +
                    aiPlayerTiles[Tile.tMon6.ordinal()] +
                    aiPlayerTiles[Tile.tMon7.ordinal()] +
                    aiPlayerTiles[Tile.tMon8.ordinal()];

            if (nMonumentTiles <= nLose)
            {
                aiPlayerTiles[Tile.tDisasterM.ordinal()] = 0;

                aiPlayerTiles[Tile.tMon1.ordinal()] = 0;
                aiPlayerTiles[Tile.tMon2.ordinal()] = 0;
                aiPlayerTiles[Tile.tMon3.ordinal()] = 0;
                aiPlayerTiles[Tile.tMon4.ordinal()] = 0;
                aiPlayerTiles[Tile.tMon5.ordinal()] = 0;
                aiPlayerTiles[Tile.tMon6.ordinal()] = 0;
                aiPlayerTiles[Tile.tMon7.ordinal()] = 0;
                aiPlayerTiles[Tile.tMon8.ordinal()] = 0;

                Log.v(Game.class.toString(), "Monument disaster tile(s) " + nDisasterTiles + " with only " + nMonumentTiles + " Mon tiles, lose all");
            }
            else
            {
                // TODO: check if all monument tiles are of a single type, then can just deduct from that type

                Log.v(Game.class.toString(), "Monument disaster tile(s) " + nDisasterTiles + " with " + nMonumentTiles + " Mon tiles, user input needed");
                statusCurrent = Status.ResolveDisaster;
                fResult = true;
            }
        }

        return fResult;
    }

    // return value: true - player needs to resolve non-auto disaster tiles
    boolean ResolveAuction()
    {
        if (iAuctionHighBid == Integer.MIN_VALUE) {
            Log.v(Game.class.toString(), "Everyone passed in auction");
            statusCurrent = Status.AuctionEveryonePassed;
            if (FAuctionTrackFull()) {
                // throw out tiles
                Log.v(Game.class.toString(), "Cleared tiles because auction track is full");
                altAuction.clear();
            }

            return false;
        } else {
            Log.v(Game.class.toString(), "Player " + aPlayers[iAuctionPlayerHighest].getName() + " won auction with sun tile value of " + iAuctionHighBid);
            Assert.assertTrue(0 <= iAuctionPlayerHighest && iAuctionPlayerHighest < nPlayers);

            Player playerWinner = aPlayers[iAuctionPlayerHighest];
            int iSunWinner;

            statusCurrent = Status.AuctionWon;
            playerWinner.getSunsNext().add(iAtAuctionSun); // move sun in auction to Player's suns for next Epoch
            // Remove winning bid sun from Player's current suns
            for (iSunWinner = 0; iSunWinner < playerWinner.getSuns().size(); iSunWinner++)
            {
                if (iAuctionHighBid == playerWinner.getSuns().get(iSunWinner)) {
                    break;
                }
            }
            Assert.assertTrue(iSunWinner < playerWinner.getSuns().size());
            playerWinner.getSuns().remove(iSunWinner);

            iAtAuctionSun = iAuctionHighBid; // Move winning bid to auction


            // move tiles into player's tiles
            while (altAuction.size() > 0)
            {
                Tile tCurrent = altAuction.remove(0);
                playerWinner.getNTiles()[tCurrent.ordinal()]++;
            }

            return ResolveDisastersAuto();
        }

    }
}


