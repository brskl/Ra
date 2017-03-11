package com.example.ben.ra;

import com.example.ben.ra.Player;

/**
 * Created by Ben on 12/31/2016.
 */

class PlayerHuman extends Player {
    PlayerHuman(String name, boolean local)
    {
        super(name, local);
    }

    boolean getHuman() { return true; }
}
