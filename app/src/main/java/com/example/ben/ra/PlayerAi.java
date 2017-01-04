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
}
