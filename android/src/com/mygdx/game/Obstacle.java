package com.mygdx.game;

/**
 * Created by BNAGY4 on 29/01/2016.
 */
public class Obstacle extends MovingBody{


    public Obstacle(float x, float y, float width, float height) {
        this(x, y, width, height, 0f, 0f);
    }

    public Obstacle(float x, float y, float width, float height, float vx, float vy) {
        super(x, y, width, height, vx, vy);
    }
}
