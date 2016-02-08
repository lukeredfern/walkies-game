package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Rebecca on 08/02/2016.
 */
public class Button {

    float x;
    float y;
    float a;
    float b;
    String text;
    int id;

    public Button(float x, float y, float a, float b, String text, int id) {
        this.x = x;
        this.y = y;
        this.a = a;
        this.b = b;
        this.text = text;
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
