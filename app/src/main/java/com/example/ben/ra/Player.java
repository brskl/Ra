package com.example.ben.ra;

import java.util.ArrayList;

/**
 * Created by Ben on 12/9/2016.
 */
public class Player {
    protected String sName = null;
    protected ArrayList<Integer> alSuns = null, alSunsNext = null;

    Player(String name)
    {
        sName = name;
    }

    public String getName()
    {
        return sName;
    }

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
}
