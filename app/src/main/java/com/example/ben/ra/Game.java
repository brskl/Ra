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
    static final int nMaxAuction_c = 8;
    static final int nEpochs_c = 3;

    public enum Status { TurnStart, DrewTile, UsedGod, CallsAuction, AuctionInProgress, AuctionWon, AuctionEveryonePassed, AuctionUserMakingBid, ResolveDisaster, EpochOver };
    public enum Tile {
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

    protected int nPlayers = 0;
    protected int iPlayerCurrent = -1; // n-1 index
    protected int iEpoch; // 1,2,3 - current epoch
    protected Status statusCurrent;
    protected int nRa; // number of Ra tiles played
    protected Player [] aPlayers = null;
    protected int iAtAuctionSun;
    protected ArrayList<Tile> altAuction = null;
    protected ArrayList<Tile> altTilebag = null;
    protected int [] anTilebag = null;
    protected Tile tLastDrawn;

    // auction state
    protected int iAuctionPlayerCaller;
    protected int iAuctionPlayerCurrent;
    protected int iAuctionPlayerHighest;
    protected int iAuctionHighBid;
    protected boolean fAuctionVoluntary;

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

    public Status getStatusCurrent() { return statusCurrent; }

    public int getAtAuctionSun() { return iAtAuctionSun; }

    public int getRas() { return nRa; }

    public int getMaxRas() {return nPlayers + 5; }

    public Player getPlayerCurrent() { return aPlayers[iPlayerCurrent]; }

    public Player getAuctionPlayerCurrent() { return aPlayers[iAuctionPlayerCurrent]; }

    public int getAuctionHighBid() { return iAuctionHighBid; }

    public boolean FAuctionCurrentPlayerBidHighest() { return (iAuctionPlayerCurrent == iAuctionPlayerHighest); }

    public boolean FAuctionTrackFull()
    {
        return (altAuction.size() == nMaxAuction_c);
    }

    public ArrayList<Tile> getAuction() { return altAuction; }

    public Tile getTileLastDrawn() { return tLastDrawn; }


    protected void initalizeTiles()
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

    public void initialize(int nPlayersValue)
    {
        if (nPlayersValue < nMinPlayers_c || nPlayersValue > nMaxPlayers_c)
            throw new IllegalArgumentException("Illegal number of players");

        Log.v(Game.class.toString(), "Initializing game for " + nPlayersValue + " players.");

        rndPlay = new Random(); // TODO: replace with own random class which can save sequence and replay
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

    public void setPlayer(int index, String name, boolean fLocal, boolean fHuman, int aiLevel)
    {
        Log.v(Game.class.toString(), "Initializing player[" + Integer.toString(index) + "]");

        // TODO: conditional of fHuman, new PlayerHuman() vs. new PlayerAI()
        aPlayers[index] = new Player(name, fLocal, fHuman, aiLevel);
    }

    public void initializeSuns()
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

    public void DrawTile()
    {
        int iTile;

        Assert.assertTrue("Auction block should have space left", altAuction.size() < nMaxAuction_c);
        Assert.assertFalse("Tile bag should not be empty", altTilebag.isEmpty());
        //iTile = rndPlay.nextInt(altTilebag.size());
        iTile = 0; // test sequence
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

    protected boolean FEpochOver()
    {
        switch (statusCurrent)
        {
            case DrewTile:
                if (nRa == getMaxRas()) {
                    return true;
                }
                else {
                    return false;
                }
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

    public boolean TestEpochOver()
    {
        if (FEpochOver())
        {
            Assert.assertTrue(statusCurrent == Status.DrewTile || statusCurrent == Status.AuctionWon);
            statusCurrent = Status.EpochOver;

            return true;
        }
        else {
            return false;
        }
    }

    public boolean FLastEpoch()
    {
        Assert.assertTrue("Invalid number of epochs", getEpoch() <= nEpochs_c && getEpoch() >= 1);
        return (getEpoch() == nEpochs_c);
    }

    public void SetNextPlayerTurn()
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

    protected void InitAuction(boolean fVoluntary)
    {
        Log.v(Game.class.toString(), "Auction called voluntary: " + fVoluntary + " by player " + getPlayerCurrent().getName());

        fAuctionVoluntary = fVoluntary;
        iAuctionPlayerCaller = iPlayerCurrent;
        iAuctionPlayerCurrent = iPlayerCurrent;
        iAuctionPlayerHighest = Integer.MIN_VALUE;
        iAuctionHighBid = Integer.MIN_VALUE;
        statusCurrent = Status.AuctionInProgress;
    }

    public void SetNextPlayerAuction()
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

    public boolean FCanBid()
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

    public boolean FAuctionFinished()
    {
        return (iAuctionPlayerCaller == iAuctionPlayerCurrent);
    }

    public void ResolveAuction()
    {
        // TEST CODE
        Log.v(Game.class.toString(), "Everyone passed in auction");
        statusCurrent = Status.AuctionEveryonePassed;
    }
}


