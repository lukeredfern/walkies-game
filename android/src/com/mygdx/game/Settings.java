package com.mygdx.game;

/**
 * Created by BNAGY4 on 25/01/2016.
 */
public class Settings {
    private static Settings ourInstance = new Settings();

    public static int obstacleSpawnRate = 50;		// percentage chance of spawning an obstacle per iteration
	public static int maxObstacleSpawnAtOnce = 2;	// maximum number of obstacles to spawn per iteration

	public static final int playerWalkSpeed = 100;



	public static Settings getInstance() {
        return ourInstance;
    }
    private Settings() {
    }
}
