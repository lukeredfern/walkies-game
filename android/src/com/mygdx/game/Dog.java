package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rebecca on 20/01/2016.
 */
public class Dog{
    float x;    // x-pos relative to screen (but also world)
    float y;    // y-pos relative to screen // After calculating dog.y take off screenSpeed*dt
    float width;    // width
    float height;    // height
    float vx;   // x-velocity relative to world
    float vy;   // y-velocity relative to world
    boolean enabledState; // whether it interacts or not
    Array<Array<Texture>> sprites;
    int stepCounter;

	float mass;
	float damping;
	float force;
	float direction;
	float directionSpeed;

    public Dog(float x, float y, float width, float b){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = b;
        this.vx = 0;
        this.vy = 0;
        this.enabledState = true;
        this.sprites = new Array<>();
        this.stepCounter = 0; //
        this.mass = 0;
    }

    public Dog(float x, float y, float a, float b, float vx, float vy){
        this.x = x;
        this.y = y;
        this.width = a;
        this.height = b;
        this.vx = vx;
        this.vy = vy;
        this.enabledState = true;
        this.sprites = new Array<>();
        this.stepCounter = 0;

        this.mass = 0;
    }

    public Rectangle getRectangle(){
        Rectangle rectangle = new Rectangle();
        rectangle.x = x - width /2;
        rectangle.y = y - height /2;
        rectangle.width = width;
        rectangle.height = height;

        return rectangle;
    }

    public void addSpriteSet(String[] list){
        Array<Texture> spriteList = new Array<>();
        for(String str: list){
            spriteList.add(new Texture(Gdx.files.internal(str)));
        }
        sprites.add(spriteList);
    }

    public Texture getSprite(boolean nextStep){
        int d = 0;

        if (direction > 7 * Math.PI / 4 || direction < Math.PI / 4){
			// NORTH
            d = 0;
        } else if (direction > Math.PI / 4 && direction < 3 * Math.PI / 4) {
			// EAST
            d = 1;
        } else if (direction > 3 * Math.PI / 4 && direction < 5 * Math.PI / 4) {
			// SOUTH
            d = 2;
        } else if (direction > 5 * Math.PI / 4 && direction < 7 * Math.PI / 4) {
			// WEST
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
