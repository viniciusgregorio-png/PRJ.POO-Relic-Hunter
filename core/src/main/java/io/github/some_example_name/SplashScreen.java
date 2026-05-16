package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

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
        logo  = new Texture("logoRelic.png");
        font  = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2f);

        musica = Gdx.audio.newMusic(Gdx.files.internal("audioInicialTopGear.mp3"));
        musica.setLooping(true);
        musica.setVolume(1.0f);
        musica.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1f);

        int largura = Gdx.graphics.getWidth();
        int altura  = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(logo, 0, 0, largura, altura);

        String mensagem = "Pressione qualquer tecla para continuar";
        float textoX = largura / 2f - (mensagem.length() * 5f);
        float textoY = 40f;
        font.draw(batch, mensagem, textoX, textoY);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new GameScreen(game));
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
