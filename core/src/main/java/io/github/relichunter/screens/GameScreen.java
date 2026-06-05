package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.relichunter.Main;
import io.github.relichunter.inimigos.*;

public class GameScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private PrimeiroInimigo snake;
    private SegundoInimigo snakeS;
    private TerceiroInimigo snakeT;
    private QuartoInimigo snakeQuarto;
    private QuintoInimigo snakeQuinto;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show(){
        batch = new SpriteBatch();
        snake = new PrimeiroInimigo(10, 100, 200f, 300f, 480, 320, null);
        snakeS = new SegundoInimigo(10, 100, 200f, 300f, 480, 320, null);
        snakeT = new TerceiroInimigo(10, 100, 200f, 300f, 480, 320, null);
        snakeQuarto = new QuartoInimigo(10, 100, 200f, 300f, 480, 320, null);
        snakeQuinto = new QuintoInimigo(10, 100, 200f, 300f, 480, 320, null);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1250, 768);
            } else {
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
                Gdx.graphics.setFullscreenMode(displayMode);
            }
        }
        snake.update(delta);
        snakeS.update(delta);
        snakeT.update(delta);
        snakeQuarto.update(delta);
        snakeQuinto.update(delta);

        batch.begin();
        snake.render(batch);
        snakeS.render(batch);
        snakeT.render(batch);
        snakeQuarto.render(batch);
        snakeQuinto.render(batch);
        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        snake.dispose();
        snakeS.dispose();
        snakeT.dispose();
        snakeQuarto.dispose();
        snakeQuinto.dispose();
    }
}
