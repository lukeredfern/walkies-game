package com.mygdx.game;

/**
 * Created by BNAGY4 on 25/01/2016.
 */
public class Settings {
    private static Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    public static final int playerWalkSpeed = 200;

    private Settings() {
    }
}
