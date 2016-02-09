package com.mygdx.game;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import javax.microedition.khronos.opengles.GL10;

public class GameScreen implements Screen {
    final Drop game;

    Texture dropImage;
    Texture grassBackGround;
    Texture dogSprite;
    Texture boneSprite;
    ArrayList<Texture> obstacleSprites;
    ArrayList<Texture> manUpImages;
    OrthographicCamera camera;
    Array<Obstacle> obstacles;
    long lastObsTime;
    long lastStepTime;
    int stepCounter = 0;
    int obstaclesHit;

    int screenH = 800;
    int screenW = 480;

    float leadLength;
    float leadStiffness;
    float leadDamping;

    ShapeRenderer shapeRenderer;

    Dog dogPlayer;
    Player player;

    float targetR;
    float targetT;
    Array<Rectangle> backgrounds;

    float gameScore;
    float barWidthRatio = 0.8f;
    private long lastDogTargetTime;

    float[] obstaclePenalty;

    public GameScreen(final Drop gam) {
        this.game = gam;
        shapeRenderer = new ShapeRenderer();

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        manUpImages = new ArrayList<>();
        manUpImages.add(new Texture(Gdx.files.internal("man_up_left.png")));
        manUpImages.add(new Texture(Gdx.files.internal("man_up_right.png")));
        grassBackGround = new Texture(Gdx.files.internal("grass_tile.png"));

        dogSprite = new Texture(Gdx.files.internal("dog_sprite_0_R.png"));

        // test change

        //Lead
        leadLength = 100;
        leadStiffness = 10000.0f;
        leadDamping = 100.0f;

        //target
        targetR = 20;
        targetT = 3;

        //background
        backgrounds = new Array<>();
        backgrounds.add(new Rectangle(0, 0, 789, 678));
        if (backgrounds.get(0).height + backgrounds.get(0).y < screenH) {
            backgrounds.add(new Rectangle(0, backgrounds.get(0).height, 789, 678));
        }

        // load the drop sound effect and the rain background "music"
        //dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        //rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        //rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenW, screenH);

        // create width Rectangle to logically represent the bucket
        player = new Player(screenW / 2, 20,manUpImages.get(0).getWidth(),manUpImages.get(0).getHeight());
        player.maxSpeed = 200;

        lastStepTime = TimeUtils.nanoTime();

        //Initialize Player's dog
        dogPlayer = new Dog(screenW / 2, 100, 48, 48);
        dogPlayer.addSpriteSet(new String[]{"dog_sprite_0_R.png", "dog_sprite_0_M.png", "dog_sprite_0_L.png"});
        dogPlayer.addSpriteSet(new String[]{"dog_sprite_2_R.png", "dog_sprite_2_M.png", "dog_sprite_2_L.png"});
        dogPlayer.addSpriteSet(new String[]{"dog_sprite_4_R.png", "dog_sprite_4_M.png", "dog_sprite_4_L.png"});
        dogPlayer.addSpriteSet(new String[]{"dog_sprite_6_R.png", "dog_sprite_6_M.png", "dog_sprite_6_L.png"});
        dogPlayer.mass = 1;
        dogPlayer.damping = 10;
        dogPlayer.force = 1000;
        dogPlayer.maxSpeed = 800;

        // create the obstacles array and spawn the first raindrop
        obstacles = new Array<>();

        // Score
        gameScore = 0.5f;

	}

	@Override
    public void render(float delta) {
        // pre calc
        //float dTx = targetX - player.x;
        //float dTy = targetY - player.y;
        //float targetDist = (float) Math.sqrt(dTy * dTy + dTx * dTx);
        boolean nextStepDog = false;


        if (TimeUtils.nanoTime() - lastStepTime > 100000000) { // 100ms intervals
            lastStepTime = TimeUtils.nanoTime();
            stepCounter++;
            if (stepCounter > 1) {
                stepCounter = 0;
            }
            nextStepDog = true;
        }


        // clear the screen with width dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(85.0f/255, 139.0f/255, 47.0f/255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // time
        float dt = Gdx.graphics.getDeltaTime();

        // tell the camera to update its matrices.
        camera.update();

        // background
        drawBackground();

        // shadow circle
        drawShadowCircle();

        // target
        drawTarget();

        // Obstacles
        drawObstacles();

        drawDog(nextStepDog);
        drawLead();
        drawPlayer();
        drawBoundingBoxes();
        drawScore();

        drawScoreIndicator();

        // process user input
        processUserInput();

        player.update(dt);

		spawnObstacles();

        // set dog target to random obstacle
        changeDogTarget();

        dogPlayer.update(dt, player);

        moveObstacles(dt);

        moveBackground(dt);

        updateGameScore(dt);
    }

    private void drawScoreIndicator() {
        float thickness = 40;
        float weight = 2;
        float offset = 20;
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(120.0f / 255, 144.0f / 255, 156.0f / 255, 0.8f);
        shapeRenderer.rect(screenW / 2 - 0.5f * screenW * barWidthRatio, offset, screenW * barWidthRatio, thickness);
        if (gameScore>0.5) {
            shapeRenderer.setColor(2.0f / 255, 136.0f / 255, 209.0f / 255, 1);
            shapeRenderer.rect(screenW/2, offset, (gameScore - 0.5f) * screenW * barWidthRatio, thickness);
        } else {
            shapeRenderer.setColor(229.0f / 255, 57.0f / 255, 53.0f / 255, 1);
            shapeRenderer.rect(screenW/2, offset, -(0.5f-gameScore) * screenW * barWidthRatio, thickness);
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);

        // Border
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(62.0f / 255, 39.0f / 255, 35.0f / 255, 1);
        //shapeRenderer.rect(dogPlayer.getRectangle().x, dogPlayer.getRectangle().y, dogPlayer.getRectangle().width, dogPlayer.getRectangle().height);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rectLine(screenW / 2 - 0.5f * screenW * barWidthRatio - weight / 2, offset, screenW / 2 + 0.5f * screenW * barWidthRatio + weight / 2, offset, weight * 2);
        shapeRenderer.rectLine(screenW / 2 - 0.5f * screenW * barWidthRatio - weight / 2, offset + thickness, screenW / 2 + 0.5f * screenW * barWidthRatio + weight / 2, offset + thickness, weight * 2);
        shapeRenderer.rectLine(screenW / 2 - 0.5f * screenW * barWidthRatio, offset-weight/2, screenW / 2 - 0.5f * screenW * barWidthRatio, offset+thickness+weight/2, weight*2);
        shapeRenderer.rectLine(screenW / 2 + 0.5f * screenW * barWidthRatio, offset - weight / 2, screenW / 2 + 0.5f * screenW * barWidthRatio, offset + thickness + weight / 2, weight*2);

        shapeRenderer.rectLine(screenW / 2 - 0.25f * screenW * barWidthRatio, offset - weight / 2, screenW / 2 - 0.25f * screenW * barWidthRatio, offset + thickness * 0.2f + weight / 2, weight * 2);
        shapeRenderer.rectLine(screenW / 2 - 0.25f * screenW * barWidthRatio, offset+thickness*0.8f-weight/2, screenW / 2 - 0.25f * screenW * barWidthRatio, offset+thickness+weight/2, weight*2);

        shapeRenderer.rectLine(screenW / 2 , offset - weight / 2, screenW / 2 , offset + thickness * 0.2f + weight / 2, weight * 2);
        shapeRenderer.rectLine(screenW / 2 , offset+thickness*0.8f-weight/2, screenW / 2, offset+thickness+weight/2, weight*2);

        shapeRenderer.rectLine(screenW / 2 + 0.25f * screenW * barWidthRatio, offset - weight / 2, screenW / 2 + 0.25f * screenW * barWidthRatio, offset + thickness * 0.2f + weight / 2, weight * 2);
        shapeRenderer.rectLine(screenW / 2 + 0.25f * screenW * barWidthRatio, offset + thickness * 0.8f - weight / 2, screenW / 2 + 0.25f * screenW * barWidthRatio, offset + thickness+weight/2, weight*2);


        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rectLine(screenW / 2 - 0.5f * screenW * barWidthRatio, offset, screenW / 2 + 0.5f * screenW * barWidthRatio, offset, weight);
        shapeRenderer.rectLine(screenW / 2 - 0.5f * screenW * barWidthRatio, offset+thickness, screenW / 2 + 0.5f * screenW * barWidthRatio, offset+thickness, weight);
        shapeRenderer.rectLine(screenW / 2 - 0.5f * screenW * barWidthRatio, offset, screenW / 2 - 0.5f * screenW * barWidthRatio, offset+thickness, weight);
        shapeRenderer.rectLine(screenW / 2 + 0.5f * screenW * barWidthRatio, offset, screenW / 2 + 0.5f * screenW * barWidthRatio, offset + thickness, weight);

        shapeRenderer.rectLine(screenW / 2 - 0.25f * screenW * barWidthRatio, offset, screenW / 2 - 0.25f * screenW * barWidthRatio, offset+thickness*0.2f, weight);
        shapeRenderer.rectLine(screenW / 2 - 0.25f * screenW * barWidthRatio, offset+thickness*0.8f, screenW / 2 - 0.25f * screenW * barWidthRatio, offset+thickness, weight);

        shapeRenderer.rectLine(screenW / 2 , offset, screenW / 2 , offset + thickness * 0.2f, weight);
        shapeRenderer.rectLine(screenW / 2 , offset+thickness*0.8f, screenW / 2, offset+thickness, weight);

        shapeRenderer.rectLine(screenW / 2 + 0.25f * screenW * barWidthRatio, offset, screenW / 2 + 0.25f * screenW * barWidthRatio, offset+thickness*0.2f, weight);
        shapeRenderer.rectLine(screenW / 2 + 0.25f * screenW * barWidthRatio, offset+thickness*0.8f, screenW / 2 + 0.25f * screenW * barWidthRatio, offset+thickness, weight);


        shapeRenderer.end();
    }

    private void changeDogTarget() {
        if (TimeUtils.nanoTime() - lastDogTargetTime > 1e9 * Global.dogTargetChangeTime // every 3 seconds
                && obstacles.size > 0) {
            dogPlayer.setTargetObstacle(obstacles.get(MathUtils.random(0, obstacles.size - 1)));
            lastDogTargetTime = TimeUtils.nanoTime();
        }
    }

    private void spawnObstacles() {
		if (TimeUtils.nanoTime() - lastObsTime > 1e9 * 100 / Global.backgroundScrollSpeed) {
			if (MathUtils.random(1, 100) > Global.obstacleSpawnRate) {
				for (int i = 0; i< MathUtils.random(1, Global.maxObstacleSpawnAtOnce); i++) {
					Obstacle obstacle = new Obstacle(MathUtils.random(0, screenW - 64), screenH, 48, 48, 0f, -Global.backgroundScrollSpeed);
                    obstacle.setObstacleType(ObstacleType.values()[MathUtils.random(0, ObstacleType.values().length - 1)]);
					obstacles.add(obstacle);
				}
			}
            lastObsTime = TimeUtils.nanoTime();
		}
	}

	private void drawScore() {
        game.batch.begin();
        game.font.draw(game.batch, "Dog Interactions: " + obstaclesHit, 0, screenH);
        game.font.draw(game.batch, "Dog velocity: " + dogPlayer.velocity.toString(), 0, screenH - 20);
		game.font.draw(game.batch, "Dog position: " + dogPlayer.position.toString(), 0, screenH - 40);
		game.font.draw(game.batch, "Target position: " + player.getTarget().toString(), 0, screenH - 60);
        game.batch.end();
    }

    private void drawPlayer() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(manUpImages.get(stepCounter), player.getRectangle().x, player.getRectangle().y);
        game.batch.end();
    }

    private void drawLead() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(62.0f / 255, 39.0f / 255, 35.0f / 255, 1);
        //shapeRenderer.rect(dogPlayer.getRectangle().x, dogPlayer.getRectangle().y, dogPlayer.getRectangle().width, dogPlayer.getRectangle().height);
        shapeRenderer.setColor(0.0f / 255, 145.0f / 255, 234.0f / 255, 1);
        shapeRenderer.line(player.position.x, player.position.y, dogPlayer.position.x, dogPlayer.position.y);
        shapeRenderer.end();
    }

    private void drawDog(boolean nextStepDog) {
        game.batch.begin();
        game.batch.draw(dogPlayer.getSprite(nextStepDog), dogPlayer.getRectangle().x, dogPlayer.getRectangle().y,48,48);
        game.batch.end();
    }

    private void drawObstacles() {
        game.batch.begin();


        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(62.0f / 255, 39.0f / 255, 35.0f / 255, 1);
        for (Obstacle obs : obstacles) {
            if (obs.sprite == null) {
                shapeRenderer.rect(obs.getRectangle().x, obs.getRectangle().y, obs.getRectangle().width, obs.getRectangle().height);
            }   else {
                game.batch.draw(obs.sprite, obs.getRectangle().x, obs.getRectangle().y, obs.getRectangle().width, obs.getRectangle().height);
            }
        }

        shapeRenderer.end();
        game.batch.end();
    }

    private void drawBackground() {
        int bgNum = backgrounds.size-1;
        if (backgrounds.get(bgNum).height+backgrounds.get(bgNum).y<screenH){
            backgrounds.add(new Rectangle(0,backgrounds.get(bgNum).height+backgrounds.get(bgNum).y,789,678));
        }
        if (backgrounds.get(0).height+backgrounds.get(0).y<0){
            backgrounds.removeIndex(0);
        }
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        for (Rectangle bg : backgrounds) {
            game.batch.draw(grassBackGround, bg.x, bg.y);
        }
        game.batch.end();
    }

    private void drawShadowCircle() {
    /*shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(174.0f / 255, 213.0f / 255, 129.0f / 255, 1.0f);
    for (int i = 0; i < 18;i++){
        shapeRenderer.arc(player.x, player.y, leadLength+3,i*20,10,18);
    }
    shapeRenderer.end();*/
        Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1, 1, 1, 0.5f));
        shapeRenderer.circle(player.position.x, player.position.y, leadLength, 180);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL10.GL_BLEND);
    }

    private void drawTarget() {
        if (player.getDistanceToTarget() > 5f) {
            Gdx.gl.glEnable(GL10.GL_BLEND);
            Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, 0.5f);
            shapeRenderer.rect(player.getTarget().x - targetT, player.getTarget().y - targetR, 2 * targetT, targetR - targetT);
            shapeRenderer.rect(player.getTarget().x - targetT, player.getTarget().y + targetT, 2 * targetT, targetR-targetT);
            shapeRenderer.rect(player.getTarget().x - targetR, player.getTarget().y - targetT, 2 * targetR, 2 * targetT);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL10.GL_BLEND);
        }
    }

    private void drawBoundingBoxes() {
        // dog

        Rectangle dogBoundingBox = dogPlayer.getRectangle();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);	// 0-1; r, g, height, alpha
        shapeRenderer.rect(dogBoundingBox.x, dogBoundingBox.y, dogBoundingBox.width, dogBoundingBox.height);
        shapeRenderer.end();

        // player
        Rectangle playerBoundingBox = player.getRectangle();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(playerBoundingBox.x, playerBoundingBox.y, playerBoundingBox.width, playerBoundingBox.height);
        shapeRenderer.end();
    }

    private void moveObstacles(float dt) {
        // move the obstacles, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add width sound effect.
        Iterator<Obstacle> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Obstacle obstacle = iter.next();
            obstacle.update(dt);
            if (obstacle.position.y + 64 < 0)
                iter.remove();
            if (obstacle.enabledState && obstacle.getRectangle().overlaps(dogPlayer.getRectangle())) {
                obstaclesHit++;
                gameScore += obstacle.penalty;
                //dropSound.play();
                //iter.remove();
                obstacle.enabledState = false;
                if (!obstacle.persist){
                    iter.remove();
                }
            }
        }
    }

    private void updateGameScore(float dt){
        gameScore += 0.005*dt;
        gameScore = Math.max(gameScore,0);
        gameScore = Math.min(gameScore,1);
    }

    private void moveBackground(float dt) {
        for (Rectangle bg : backgrounds) {
            bg.y -= Global.backgroundScrollSpeed * dt;
        }
    }

    private void processUserInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            player.setTarget(touchPos.x, touchPos.y);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        //rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
        manUpImages.get(0).dispose();
        manUpImages.get(1).dispose();
        //dropSound.dispose();
        //rainMusic.dispose();
    }

}