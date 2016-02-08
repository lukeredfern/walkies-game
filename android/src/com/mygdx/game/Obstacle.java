package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rebecca on 19/01/2016.
 */

public class Obstacle {
    float x;
    float y;
    float a;
    float b;
    float vx;
    float vy;
    boolean enabledState;
    boolean persist;
    float penalty;
    float maxSpeed;
    Texture sprite = null;

    public Obstacle(float x, float y, float a, float b){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = 0;
        this.vy = 0;
        this.enabledState = true;
        this.penalty = 0;
    }

    public Obstacle(float x, float y, float a, float b, float penalty){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = 0;
        this.vy = 0;
        this.enabledState = true;
        this.penalty = penalty;
    }

    public Obstacle(float x, float y, float a, float b, float vx, float vy){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = vx;
        this.vy = vy;
        this.enabledState = true;
        this.penalty = 0;
    }

    public Obstacle(float x, float y, float a, float b, float vx, float vy, float penalty){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = vx;
        this.vy = vy;
        this.enabledState = true;
        this.penalty = penalty;
    }

    public Obstacle(float x, float y,int type){
        this.x = x;
        this.y = y;
        this.enabledState = true;
        switch(type){
            case 0://dog
                this.a = 48;
                this.b = 48;
                this.sprite = new Texture(Gdx.files.internal("dog_sprite_4_R.png"));
                this.penalty = -0.01f;
                this.persist = true;
                break;
            case 1:
                this.a = 34;
                this.b = 18;
                this.sprite = new Texture(Gdx.files.internal("bone_sprite.png"));
                this.penalty = 0.01f;
                this.persist = false;
                break;
        }
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
