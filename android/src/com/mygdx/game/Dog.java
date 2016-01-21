package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rebecca on 20/01/2016.
 */
public class Dog{
    float x;
    float y;
    float a;
    float b;
    float vx;
    float vy;
    boolean enabledState;
    Array<Array<Texture>> sprites;
    int stepCounter;

    public Dog(float x, float y, float a, float b){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = 0;
        this.vy = 0;
        this.enabledState = true;
        this.sprites = new Array<>();
        this.stepCounter = 0;
    }

    public Dog(float x, float y, float a, float b, float vx, float vy){
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.vx = vx;
        this.vy = vy;
        this.enabledState = true;
        this.sprites = new Array<>();
        this.stepCounter = 0;

    }

    public Rectangle getRectangle(){
        Rectangle rectangle = new Rectangle();
        rectangle.x = x - a/2;
        rectangle.y = y - b/2;
        rectangle.width = a;
        rectangle.height = b;

        return rectangle;
    }

    public void addSpriteSet(String[] list){
        Array<Texture> spriteList = new Array<>();
        for(String str: list){
            spriteList.add(new Texture(Gdx.files.internal(str)));
        }
        sprites.add(spriteList);
    }

    public Texture getSprite(boolean nextStep, float dir){
        int d = 0;

        if (dir>7*Math.PI/4||dir<Math.PI/4){
            d = 0;
        } else if (dir>Math.PI/4&&dir<3*Math.PI/4){
            d = 1;
        } else if (dir>3*Math.PI/4&&dir<5*Math.PI/4){
            d = 2;
        } else if (dir>5*Math.PI/4&&dir<7*Math.PI/4){
            d = 3;
        }

        if (nextStep){
            stepCounter++;
            if (stepCounter>sprites.get(d).size-1) {
                stepCounter = 0;
            }
        }
        return sprites.get(d).get(stepCounter);
    }
}
