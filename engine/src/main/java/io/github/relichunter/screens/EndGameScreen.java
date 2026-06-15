package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.relichunter.Main;

public class EndGameScreen implements Screen {

    private final Main game;
    private final int totalRubisColetados;

    private SpriteBatch batch;

    private Texture background;
    private Texture star;

    private int earnedStars;

    public EndGameScreen(Main game, int totalRubisColetados) {
        this.game = game;
        this.totalRubisColetados = totalRubisColetados;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();

        background = new Texture("assets/tela/endGame.png");
        star = new Texture("assets/tela/star.png");

        earnedStars = calculateStars(totalRubisColetados);
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

        drawStars(largura, altura);

        batch.end();
    }

    private void drawStars(int largura, int altura) {

        float scaleX = largura / 1312f;
        float scaleY = altura / 856f;

        float starSize = 134f * scaleX;

        float[][] positions = {
            {487f, 316f},
            {608, 316f},
            {732f, 316f}
        };

        for (int i = 0; i < earnedStars; i++) {

            float x = positions[i][0] * scaleX;
            float y = (856f - positions[i][1] - 95f) * scaleY;

            batch.draw(
                star,
                x,
                y,
                starSize,
                starSize
            );
        }
    }

    private int calculateStars(int rubies) {

        if (rubies == 0) {
            return 0;
        }

        if (rubies <= 4) {
            return 1;
        }

        if (rubies <= 7) {
            return 2;
        }

        return 3;
    }

    private void handleInput() {

        int largura = Gdx.graphics.getWidth();
        int altura = Gdx.graphics.getHeight();

        if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {

            float mouseX = Gdx.input.getX();
            float mouseY = altura - Gdx.input.getY();

            float retryX = largura * 0.31f;
            float retryY = altura * 0.03f;
            float retryW = largura * 0.17f;
            float retryH = altura * 0.10f;

            float exitX = largura * 0.52f;
            float exitY = altura * 0.03f;
            float exitW = largura * 0.17f;
            float exitH = altura * 0.10f;

            if (mouseX >= retryX
                && mouseX <= retryX + retryW
                && mouseY >= retryY
                && mouseY <= retryY + retryH) {

                restartGame();
                return;
            }

            if (mouseX >= exitX
                && mouseX <= exitX + exitW
                && mouseY >= exitY
                && mouseY <= exitY + exitH) {

                exitGame();
            }
        }
    }

    private void restartGame() {

        game.setScreen(new TelaTeste(game));
    }

    private void exitGame() {

        Gdx.app.exit();
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

        dispose();
    }

    @Override
    public void dispose() {

        if (batch != null)
            batch.dispose();

        if (background != null)
            background.dispose();

        if (star != null)
            star.dispose();
    }
}
