package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by BNAGY4 on 29/01/2016.
 */
public class Player extends MovingBody {
    private Vector2 target;
    Array<Texture> sprites;

    public Player(float x, float y, float width, float height) {
        this(x, y, width, height, 0, 0);
    }

    public Player(float x, float y, float width, float height, float vx, float vy) {
        super(x, y, width, height, vx, vy);
        target = new Vector2();
        sprites = new Array<>();
        maxSpeed = 0f;
    }

    public void setTarget(float x, float y) {
        setTarget(new Vector2(x, y));
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public Vector2 getTarget() {
        return this.target;
    }

    public Texture getSprite(boolean nextStep){
        // TODO: implement method
        throw new RuntimeException();
    }

    public void update(float dt) {
        if (getDistanceToTarget() > 5) {
            velocity = target.cpy().sub(position).nor().scl(maxSpeed); // normalized velocity vector
            Vector2 relVelocity = velocity.cpy().sub(new Vector2(0, Global.backgroundScrollSpeed));

            // new position = old position + velocity * time
            position.add(relVelocity.scl(dt));

            clamp(); // clamp position
            //player.x = player.x + Math.min(0.1f * targetDist * targetVec[0], player.maxSpeed);
            //player.y = player.y + Math.min(0.1f * targetDist * targetVec[1], player.maxSpeed);
        }
    }

    public float getDistanceToTarget() {
        return position.cpy().sub(target).len();
    }
}
