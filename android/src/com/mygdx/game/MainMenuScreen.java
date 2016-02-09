package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class MainMenuScreen implements Screen {

    final Drop game;

    OrthographicCamera camera;

    ShapeRenderer shapeRenderer;

    int screenH = 800;
    int screenW = 480;

    ArrayList<Button> buttons;

    public MainMenuScreen(final Drop gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenW, screenH);

        shapeRenderer = new ShapeRenderer();

        buttons = new ArrayList<>();
        buttons.add(new Button(screenW/2,screenH/2,200,50,"Endless Mode",0));
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Walkies!!! ", 100, screenH-150);
        game.batch.end();

        processUserInput();
        drawButtons();
    }

    private void processUserInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if (buttons.get(0).getRectangle().contains(touchPos.x,touchPos.y)) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
    }

    private void drawButtons(){

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f / 255, 145.0f / 255, 234.0f / 255, 1);
        for (Button button: buttons) {
            shapeRenderer.rect(button.getRectangle().x, button.getRectangle().y, button.a, button.b);
        }
        shapeRenderer.end();

        game.batch.begin();
        for (Button button: buttons) {
            game.font.draw(game.batch, button.getText(), button.getRectangle().x + 10, button.y);

        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}