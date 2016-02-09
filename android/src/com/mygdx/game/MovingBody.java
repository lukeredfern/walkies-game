package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rebecca on 19/01/2016.
 */

public abstract class MovingBody {
    protected boolean enabledState;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 acceleration;
    protected float width;
    protected float height;
    protected float maxSpeed;
    protected float mass;
    protected float damping;
    protected float force;

    public MovingBody(float x, float y, float width, float height) {
        this(x, y, width, height, 0f, 0f);
    }

    public MovingBody(float x, float y, float width, float height, float vx, float vy) {
        position = new Vector2(x, y);
        velocity = new Vector2(vx, vy);
        acceleration = new Vector2();
        this.width = width;
        this.height = height;
        this.enabledState = true;
        this.enabledState = true;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isEnabled() {
        return enabledState;
    }

    public void enable() {
        this.enabledState = true;
    }

    public void disable() {
        this.enabledState = false;
    }

    public void update(float dt) {
        this.position.add(velocity.cpy().scl(dt));
    }

    public Rectangle getRectangle() {
        return new Rectangle(
                this.position.x - width / 2,
                this.position.y - width / 2,
                width,
                height);

    }

    protected void clamp() {
        if (this.position.x < 0)
            this.position.x = 0;
        if (this.position.x > Global.screenW)
            this.position.x = Global.screenW;
        if (this.position.y < 0)
            this.position.y = 0;
        if (this.position.y > Global.screenH)
            this.position.y = Global.screenH;
    }
}
