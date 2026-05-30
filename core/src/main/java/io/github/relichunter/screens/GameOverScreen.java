package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.relichunter.Main;

public class GameOverScreen implements Screen {

    private final Main game;

    private SpriteBatch batch;
    private Texture background;

    private int selectedOption = 0;

    public GameOverScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();

        background = new Texture("gameOver.png");
    }

    @Override
    public void render(float delta) {
        handleInput();

        ScreenUtils.clear(0, 0, 0, 1);

        int largura = Gdx.graphics.getWidth();
        int altura = Gdx.graphics.getHeight();

        Gdx.gl.glViewport(0, 0, largura, altura);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, largura, altura);

        batch.begin();
        batch.draw(background, 0, 0, largura, altura);
        batch.end();
    }

    private void handleInput() {

        int largura = Gdx.graphics.getWidth();
        int altura = Gdx.graphics.getHeight();

        // Navegação por teclado (opcional)
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyJustPressed(Input.Keys.A)
            || Gdx.input.isKeyJustPressed(Input.Keys.UP)
            || Gdx.input.isKeyJustPressed(Input.Keys.W)) {

            selectedOption = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyJustPressed(Input.Keys.D)
            || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyJustPressed(Input.Keys.S)) {

            selectedOption = 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            if (selectedOption == 0) {
                restartGame();
            } else {
                backToMenu();
            }
        }

        if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {

            float mouseX = Gdx.input.getX();
            float mouseY = altura - Gdx.input.getY();

            float retryX = largura * 0.31f;
            float retryY = altura * 0.03f;
            float retryW = largura * 0.17f;
            float retryH = altura * 0.10f;

            float menuX = largura * 0.52f;
            float menuY = altura * 0.03f;
            float menuW = largura * 0.17f;
            float menuH = altura * 0.10f;

            if (mouseX >= retryX
                && mouseX <= retryX + retryW
                && mouseY >= retryY
                && mouseY <= retryY + retryH) {

                restartGame();
                return;
            }

            if (mouseX >= menuX
                && mouseX <= menuX + menuW
                && mouseY >= menuY
                && mouseY <= menuY + menuH) {

                backToMenu();
            }
        }
    }

    private void restartGame() {

        dispose();
        game.setScreen(new TelaTeste(game));
    }

    private void backToMenu() {

        dispose();
        game.setScreen(new SplashScreen(game));
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

        if (batch != null)
            batch.dispose();

        if (background != null)
            background.dispose();
    }
}
