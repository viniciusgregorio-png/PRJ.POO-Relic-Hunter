package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.relichunter.Main;
import io.github.relichunter.inimigos.InimigosSnake;

public class GameScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private InimigosSnake snake;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        snake = new InimigosSnake(10, 100, 200f, 300f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);

        // F11 → alterna tela cheia / janela
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1250, 768);
            } else {
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
                Gdx.graphics.setFullscreenMode(displayMode);
            }
        }
        snake.update(delta);

        batch.begin();
        // seu jogo vai aqui
        snake.render(batch);
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
    }
}
