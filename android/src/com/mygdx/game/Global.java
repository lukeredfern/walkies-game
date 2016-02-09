package com.mygdx.game;

/**
 * Created by BNAGY4 on 25/01/2016.
 */
public class Global {
    private static Global ourInstance = new Global();

    public static int obstacleSpawnRate = 50;		// percentage chance of spawning an obstacle per iteration
	public static int maxObstacleSpawnAtOnce = 2;	// maximum number of obstacles to spawn per iteration

	public static final float backgroundScrollSpeed = 100;

    public static final float leadLength = 100;
    public static final float leadStiffness = 10000.0f;
    public static final float leadDamping = 100.0f;



    public static final int screenH = 800;
    public static final int screenW = 480;
    public static long dogTargetChangeTime = 3; // 3 seconds


    public static Global getInstance() {
        return ourInstance;
    }
    private Global() {
    }
}
