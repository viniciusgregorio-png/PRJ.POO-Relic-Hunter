package io.github.relichunter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.relichunter.Main;

public class SplashScreen implements Screen {

    private final Main game;
    private SpriteBatch batch;
    private Texture logo;
    private BitmapFont font;
    private Music musica;

    public SplashScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        logo = new Texture("Png-Telas/logoRelic.png");
        font  = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2f);

        musica = Gdx.audio.newMusic(Gdx.files.internal("musics/audioInicialTopGear.mp3"));        musica.setLooping(true);
        musica.setVolume(1.0f);
        musica.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);

        int largura = Gdx.graphics.getWidth();
        int altura = Gdx.graphics.getHeight();

        Gdx.gl.glViewport(0, 0, largura, altura);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, largura, altura);

        batch.begin();
        batch.draw(logo, 0, 0, largura, altura);

        String mensagem = "Pressione qualquer tecla para continuar";
        float textoX = largura / 2f - (mensagem.length() * 5f);
        float textoY = 40f;
        font.draw(batch, mensagem, textoX, textoY);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new TelaTeste(game));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {

    }
    @Override public void pause() {

    }
    @Override public void resume() {

    }
    @Override public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        logo.dispose();
        font.dispose();
        musica.dispose();
    }
}
