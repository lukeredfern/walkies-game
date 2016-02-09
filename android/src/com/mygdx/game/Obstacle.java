package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by BNAGY4 on 29/01/2016.
 */
public class Obstacle extends MovingBody{

    float penalty = 0;
    boolean state;
    boolean persist;
    Texture sprite = null;
    ObstacleType type;

    public Obstacle(float x, float y, float width, float height) {
        this(x, y, width, height, 0f, 0f);
    }

    public Obstacle(float x, float y, float width, float height, float vx, float vy) {
        super(x, y, width, height, vx, vy);
    }

    public void setObstacleType(ObstacleType otype) {
        this.type = otype;
        this.enabledState = true;
        switch (this.type) {
            case Dog:
                this.sprite = new Texture(Gdx.files.internal("dog_sprite_4_R.png"));
                this.penalty = -0.05f;
                this.persist = true;
                break;
            case Bone:
                this.sprite = null;//new Texture(Gdx.files.internal("bone_sprite.png"));
                this.penalty = 0.01f;
                this.persist = false;
                break;
        }
    }
}
