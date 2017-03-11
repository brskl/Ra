package com.example.ben.ra;

import android.util.Log;

import java.util.Random;

/**
 * Created by Ben on 1/4/2017.
 */

public class MyRandom extends Random {


    public MyRandom()
    {
        super();
        Log.v(MyRandom.class.toString(), "constructor()");
    }

    @Override
    synchronized public void setSeed(long seed)
    {
        super.setSeed(seed);
        Log.v(MyRandom.class.toString(), "setSeed("+ seed + ")");
    }

    @Override
    public int nextInt(int n) {
        int value = super.nextInt(n);

        Log.v(MyRandom.class.toString(), "nextInt("+n+") returns " + value);
        return value;
    }
}
