package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rebecca on 20/01/2016.
 */
public class Dog extends MovingBody {
	Array<Array<Texture>> sprites;
	int stepCounter;
	private Obstacle targetObstacle;
	private Vector2 target;

	public Dog(float x, float y, float width, float height) {
		this(x, y, width, height, 0, 0);
	}

	public Dog(float x, float y, float width, float height, float vx, float vy) {
		super(x, y, width, height, vx, vy);
		this.sprites = new Array<>();
		this.stepCounter = 0;
		this.target = new Vector2();
	}

	public Obstacle getTargetObstacle() {
		return targetObstacle;
	}

	public void setTargetObstacle(Obstacle targetObstacle) {
		this.targetObstacle = targetObstacle;
		this.velocity.setZero();
	}

	public void addSpriteSet(String[] list) {
		Array<Texture> spriteList = new Array<>();
		for (String str : list) {
			spriteList.add(new Texture(Gdx.files.internal(str)));
		}
		sprites.add(spriteList);
	}

	public Texture getSprite(boolean nextStep) {
		int d = 0;

		Vector2 relativeVelocity = velocity.cpy().sub(0f, Global.backgroundScrollSpeed);
		float angle = relativeVelocity.angleRad();

		if (angle > 7 * Math.PI / 4 || angle < Math.PI / 4) {
			// NORTH
			d = 0;
		} else if (angle > Math.PI / 4 && angle < 3 * Math.PI / 4) {
			// EAST
			d = 1;
		} else if (angle > 3 * Math.PI / 4 && angle < 5 * Math.PI / 4) {
			// SOUTH
			d = 2;
		} else if (angle > 5 * Math.PI / 4 && angle < 7 * Math.PI / 4) {
			// WEST
			d = 3;
		}

		if (nextStep) {
			stepCounter++;
			if (stepCounter > sprites.get(d).size - 1) {
				stepCounter = 0;
			}
		}
		return sprites.get(d).get(stepCounter);
	}

	public void update(float dt, Player player) {

        TargettingAlgorithm();

        // Calculate vector to the target obstacle
        Vector2 targetVector = this.target.cpy().sub(this.position);

        // Calculate distance to target
        float distanceToTarget = targetVector.len();

        // if the distance to target is significant, do the calculations
        if (distanceToTarget > 5) {
            // calculate force of dog towards target
            Vector2 fDog = targetVector.cpy().scl(5);

            // calculate lead vector
            Vector2 leadVec = player.position.cpy().sub(this.position);

            // calcualte lead 'distance'
            float leadDist = leadVec.len();

            // calculate 'stretch' factor
            float dL = leadDist / Global.leadLength;

            Vector2 fLead;
            if (dL > 1) {
                // lead is stretched, apply force proportional to lead extension
                fLead = leadVec.nor().scl(dL * Global.leadStiffness);
            } else {
                // lead is slack so no force applied
                fLead = new Vector2(0, 0);
            }

            // calculate resultant force on dog
            Vector2 fTotal = fDog.add(fLead);

            // calculate acceleration
            this.acceleration = fTotal.scl(1 / this.mass); // a = F / m

            // calculate velocity
            this.velocity.add(this.acceleration.cpy().scl(dt));

        } else {
            // Dog is at target obstacle so match the velocity
            this.velocity.setZero();// = targetObstacle.velocity.cpy();
        }

        // adjust the position based on the velocity * elapsed time
        this.position.add(velocity.cpy().scl(dt));

        // make sure the dog remains on screen
        clamp();

		/*

		// Dog model
		dogPlayer.directionSpeed += 0.5 * (Math.random() - 0.5);
		dogPlayer.directionSpeed = Math.min(dogPlayer.directionSpeed, 3);
		dogPlayer.directionSpeed = Math.max(dogPlayer.directionSpeed, -3);
		dogPlayer.direction += dt * dogPlayer.directionSpeed;
		if (dogPlayer.direction > 2 * Math.PI) {
			dogPlayer.direction -= 2 * Math.PI;
		} else if (dogPlayer.direction < 0) {
			dogPlayer.direction += 2 * Math.PI;
		}

		// EoM Dog
		float dx = player.x - dogPlayer.x;
		float dy = player.y - dogPlayer.y;
		float leadDist = (float) Math.sqrt(dy * dy + dx * dx);
		float[] leadVec = new float[]{dx / leadDist, dy / leadDist};
		float dL = leadDist/leadLength;

		float[] fLead;
		if (leadDist>leadLength) {
			fLead = new float[]{dL*leadStiffness*leadVec[0],dL*leadStiffness*leadVec[1]};
		} else {
			fLead = new float[]{0,0};
		}
		float[] fDogDamp = new float[]{-dogPlayer.vx*dogPlayer.damping,-dogPlayer.vy*dogPlayer.damping};
		float[] fDog = new float[]{(float) (dogPlayer.force*Math.sin(dogPlayer.direction)), (float) (dogPlayer.force*Math.cos(dogPlayer.direction))};
		float[] fTot = new float[]{fLead[0]+fDogDamp[0]+fDog[0],fLead[1]+fDogDamp[1]+fDog[1]};

		float[] acceleration = new float[]{fTot[0]/dogPlayer.mass, fTot[1]/dogPlayer.mass};
		dogPlayer.vx = dogPlayer.vx + acceleration[0]*dt;
		dogPlayer.vy = dogPlayer.vy + acceleration[1]*dt;
		//Log.d("width", String.valueOf(acceleration[0]) + "," + String.valueOf(acceleration[1]));
		//Log.d("v", String.valueOf(dogPlayer.vx) + "," + String.valueOf(dogPlayer.vy));

		dogPlayer.x = dogPlayer.x + dogPlayer.vx*dt;
		dogPlayer.y = dogPlayer.y + dogPlayer.vy*dt;*/
	}

    private void TargettingAlgorithm() {
        if (this.targetObstacle != null) {
            this.target = this.targetObstacle.position.cpy();
        }
    }


}