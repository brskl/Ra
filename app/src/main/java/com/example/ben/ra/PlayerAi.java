package com.example.ben.ra;

import junit.framework.Assert;

/**
 * Created by Ben on 12/31/2016.
 */

class PlayerAi extends Player {
    private int iAiLevel = -1;

    PlayerAi(String name, boolean local, int AiLevel) {
        super(name, local);
        iAiLevel = AiLevel;
    }

    boolean getHuman() { return false; }

    int AiBid()
    {
        Game game = Game.getInstance();

        Assert.assertTrue(game.FCanBid());

        // pass unless more than 4 tiles or must bid. If bid, always use lowest
        if (!game.FMustBid() && game.getAuction().size() <= 4) {
            return 0; // pass
        }

        for (Integer i: alSuns)
        {
            if (i > game.getAuctionHighBid())
            {
                return i;
            }
        }

        Assert.fail("Unable to determine AI bid");
        return 0;
    }

    private void ResolveDisastersAiCiv()
    {
        Game game = Game.getInstance();
        Game.Tile t1 = Game.Tile.tNone, t2 = Game.Tile.tNone;

        for (int i = Game.Tile.tCiv1.ordinal(); i <= Game.Tile.tCiv5.ordinal(); i++)
        {
            if (nTiles[i] > 0)
            {
                if (t1 == Game.Tile.tNone)
                {
                    t1 = Game.Tile.values()[i];
                    if (nTiles[i] > 1)
                    {
                        t2 = Game.Tile.values()[i];
                        break;
                    }
                } else {
                    t2 = Game.Tile.values()[i];
                    break;
                }
            }
        }
        Assert.assertTrue(Game.FCivTile(t1));
        Assert.assertTrue(Game.FCivTile(t2));

        game.ResolveDisasterCiv(t1, t2);
    }

    private void ResolveDisastersAiMon()
    {
        Game game = Game.getInstance();
        Game.Tile t1 = Game.Tile.tNone, t2 = Game.Tile.tNone;

        for (int i = Game.Tile.tMon1.ordinal(); i <= Game.Tile.tMon8.ordinal(); i++)
        {
            if (nTiles[i] > 0)
            {
                if (t1 == Game.Tile.tNone)
                {
                    t1 = Game.Tile.values()[i];
                    if (nTiles[i] > 1)
                    {
                        t2 = Game.Tile.values()[i];
                        break;
                    }
                } else {
                    t2 = Game.Tile.values()[i];
                    break;
                }
            }
        }
        Assert.assertTrue(Game.FMonumentTile(t1));
        Assert.assertTrue(Game.FMonumentTile(t2));

        game.ResolveDisasterMonument(t1, t2);
    }

    void ResolveDisastersAi()
    {
        Game game = Game.getInstance();

        while (nTiles[Game.Tile.tDisasterC.ordinal()] > 0)
        {
            ResolveDisastersAiCiv();
        }
        while (nTiles[Game.Tile.tDisasterM.ordinal()] > 0)
        {
            ResolveDisastersAiMon();
        }

        boolean fDisasters = game.TestDisasters();
        Assert.assertFalse(fDisasters);
        Assert.assertEquals(Game.Status.ResolveDisasterCompleted, game.getStatusCurrent());
    }
}
