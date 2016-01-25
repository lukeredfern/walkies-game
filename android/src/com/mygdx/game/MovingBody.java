package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rebecca on 19/01/2016.
 */

public class MovingBody {
    float x;
    float y;
    float a;
    float b;
    float vx;
    float vy;
    boolean enabledState;

    float maxSpeed;

    public MovingBody(float x, float y, float a, float b){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = 0;
        this.vy = 0;
        this.enabledState = true;
    }

    public MovingBody(float x, float y, float a, float b, float vx, float vy){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = vx;
        this.vy = vy;
        this.enabledState = true;
    }

    public Rectangle getRectangle(){
        Rectangle rectangle = new Rectangle();
        rectangle.x = x - a/2;
        rectangle.y = y - b/2;
        rectangle.width = a;
        rectangle.height = b;

        return rectangle;
    }

}
